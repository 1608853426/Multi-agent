/*
 *
 * Create by_xiaoqing on 2018-04-05
 *
 */
package app;

import core.agents.Agent;
import core.role.Role;
import core.role.RoleAssignment;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;


/**
 * 游戏主窗口
 * 游戏开始前，关闭输入法
 * w前，s后，a左，d右+shift（可以加速）
 * @author SoonMachine
 */
public class MyGameFrame extends Frame implements RoleAssignment {

    /**
     * 飞机图片
     */
    private Image planeImg;


    /**
     * 背景图片
     */
    private Image bg;

    /**
     * 血包
     */
    private Image supplyImg;

    /**
     * 操作的飞机
     */
    private MyPlane plane;

    /**
     * 敌机图片
     */
    private Image[] armyImages;

    /**
     * 敌机
     */
    private Plane[] enemies;

    /**
     * 用于双缓冲
     */
    private Image offScreenImage;

    /**
     * 所获分数
     */
    private int score;

    /**
     * 医疗包
     */
    private SupplyPacket supply;

    /**
     * 用于画窗口的线程
     */
    private Thread paintThread;

    /**
     * 补给出现的线程
     */
    private Thread supplyThread;


    private Agent[] agents;
    private static int agentNums;
    /**
     * 用于加载图片
     */
    public void load() {
        /*加载飞机图片*/
        planeImg = GameUtil.getImage("images/plane.png");
        /*加载背景图片*/
        bg = GameUtil.getImage("images/bg.jpg");
        /*加载敌机图片*/
        armyImages = new Image[6];
        for (int i = 0; i < armyImages.length; i++) {
            armyImages[i] = GameUtil.getImage("images/army/army" + (i + 1) + ".png");
            armyImages[i].getWidth(null);
        }
        /*加载补给包图片*/
        supplyImg = GameUtil.getImage("images/supply.png");

    }

    /**
     * 用于初始化窗口
     */
    public void init() {
        this.setTitle("PlaneGame");
        setVisible(true);
        setSize(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);
        setLocation(400, 100);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        addKeyListener(new KeyMonitors());
        plane = new MyPlane(planeImg, 288, 460);
        supply = new SupplyPacket(supplyImg);
        enemies = new Plane[6];
        Role enemyRole = new Role();
        enemyRole.setRoleName("enemyPlane");
        List<Object> instance = enemyRole.getInstance();
        for (int i = 0; i < enemies.length; i++) {
            enemies[i] = new Plane(armyImages[i]);
            instance.add(enemies[i]);
            agentNums ++;
        }

        Role myRole = new Role();
        myRole.setRoleName("myPlane");
        List<Object> instance1 = myRole.getInstance();
        instance1.add(plane);
        agentNums ++;

        List<Role> roleList = new ArrayList<>();
        roleList.add(enemyRole);
        roleList.add(myRole);

        agents = new Agent[agentNums];

        this.roleAssignment(agents,roleList);


        paintThread = new PaintThread();
        supplyThread = new Thread(supply);
    }

    /**
     * 用于开启线程
     */
    public void start() {
        paintThread.start();

        for (Agent agent : agents){
            agent.start();
        }
        supplyThread.start();
    }

    @Override
    public void roleAssignment(Agent[] agents, List<Role> roles) {
            int temp = 0;
            for (Role role : roles){
                List<Object> instance = role.getInstance();
                String roleName = role.getRoleName();
                for (Object o : instance){

                    agents[temp] = new Agent((Runnable) o);
                    agents[temp].setRoleName(roleName);
                    temp ++;
                }
            }
    }

    /**
     * 内部的一个线程类，用于重画窗口
     */
    class PaintThread extends Thread {
        @Override
        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(40);//1秒画25次
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 内部类，监听键盘
     */
    class KeyMonitors extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            plane.addDirection(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            plane.minusDirection(e);
        }

    }

    @Override
    /**
     * 用于画窗口
     */
    public void paint(Graphics g) {
        g.drawImage(bg, 0, 0, null);
        drawScore(g);
        supply.drawSelf(g);
        plane.drawSelf(g);
        for (int i = 0; i < plane.bullets.length; i++) {
            plane.bullets[i].drawSelf(g);
        }

        for (Plane value : enemies) {
            value.drawSelf(g);
        }
        for (Plane enemy : enemies) {
            for (int j = 0; j < enemy.bullets.length; j++) {
                enemy.bullets[j].drawSelf(g);
            }
        }
        plane.blood.draw(g);
        isCollide();//判断是否发现碰撞

    }

    /**
     * 用于双缓冲
     */
    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            /*游戏窗口的宽度和高度*/
            offScreenImage = this.createImage(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);
        }

        Graphics gOff = offScreenImage.getGraphics();
        paint(gOff);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    /**
     * 用于判断是否发生碰撞
     */
    public void isCollide() {
        for (Plane enemy : enemies) {
            boolean peng = false;
            for (int j = 0; j < plane.bullets.length; j++) {
                if (plane.bullets[j].isLive() &&
                        (enemy.getRect().intersects
                                (plane.bullets[j].getRect()))) {
                    peng = true;
                    plane.bullets[j].setLive(false);
                    score += 10;
                    break;
                }
            }
            if (peng) {
                enemy.setCollide(true);
                if (enemy.explode1.count == 0) {
                    enemy.explode1.setLocation(enemy.getX(), enemy.getY());
                    enemy.ex1 = true;
                } else {
                    enemy.explode2.setLocation(enemy.getX(), enemy.getY());
                    enemy.ex2 = true;
                }
                enemy.setLive(false);
            }//判断敌方飞机是否被击中
            for (int j = 0; j < enemy.bullets.length; j++) {
                if (enemy.bullets[j].isLive() && (enemy.bullets[j].getRect().intersects
                        (plane.getRect()))) {
                    plane.blood.minusBlood();
                    enemy.bullets[j].setLive(false);
                }
            }//判断我方飞机是否被击中
        }
        if (supply.isLive() && plane.isLive()
                && plane.getRect()
                .intersects(supply.getRect())) {
            plane.blood.addBlood();
            supply.setLive(false);
        }//判断我方飞机是否获得补给
    }

    /**
     * 用于画分数
     */
    public void drawScore(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.setFont(new Font("宋体", Font.BOLD, 20));
        g.drawString("得分为：" + score, 20, 50);
        g.setColor(c);
    }

    public static void main(String[] args) {
        MyGameFrame f = new MyGameFrame();
        f.load();
        f.init();
        f.start();
    }

}
