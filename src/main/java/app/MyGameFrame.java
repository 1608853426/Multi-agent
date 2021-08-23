/*
 *
 * Create by_xiaoqing on 2018-04-05
 *
 */
package app;

import core.agents.Agent;
import core.role.Role;
import core.role.RoleContainer;
import core.tasks.AssignTaskInstance;
import core.tasks.CircleTask;
import core.tasks.Task;
import core.tasks.TaskContainer;


import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Optional;
import java.util.Random;


/**
 * 游戏主窗口
 * 游戏开始前，关闭输入法
 * w前，s后，a左，d右+shift（可以加速）
 *
 * @author SoonMachine
 */
public class MyGameFrame extends Frame implements AssignTaskInstance {

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
    private MyPlaneRole plane;

    /**
     * 敌机图片
     */
    private Image[] armyImages;

    /**
     * 敌机
     */
    private PlaneRole[] enemies;

    private static TaskContainer taskContainer;
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



    private static int agentNums;

    private MyAgentContainer agentContainer;
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
        supply = new SupplyPacket(supplyImg);
        Random r = new Random();


        taskContainer = TaskContainer.getInstance();
        CircleTask myTask = new CircleTask(100, 0, Task.STATE_READY, "attack");
        taskContainer.createTask(myTask);
        CircleTask[] enemyTask = new CircleTask[6];
        for (int i = 0; i < 6; i++) {
            enemyTask[i] = new CircleTask(i, 0, Task.STATE_READY, "attack");
            enemyTask[i].getMQPS().put("attack",r.nextInt(91) + 10);
            taskContainer.createTask(enemyTask[i]);
        }


        RoleContainer roleContainer = RoleContainer.getInstance();
        plane = new MyPlaneRole(planeImg, 288, 460);
        plane.setRoleId(100);
        roleContainer.createRoleInstance(plane);
        agentNums++;

        enemies = new PlaneRole[6];
        for (int i = 0; i < enemies.length; i++) {
            enemies[i] = new PlaneRole(armyImages[i]);
            enemies[i].setRoleId(i);
            enemies[i].getCapabilities().put("attack",r.nextInt(91) + 10);
            roleContainer.createRoleInstance(enemies[i]);
            agentNums++;
        }


        MyTaskContainer.getInstance().assignTaskInstance(taskContainer,roleContainer);


        paintThread = new PaintThread();
        supplyThread = new Thread(supply);

        agentContainer = new MyAgentContainer();
        Agent[] agents = new Agent[7];
        for (int i = 0; i < 6; i++) {
            agents[i] = new EnemyPlaneAgent(i);
            agentContainer.createAgent(agents[i]);
        }
        agents[6] = new Agent(6);
        agentContainer.createAgent(agents[6]);
        paintThread.start();
        supplyThread.start();

        agentContainer.assignAgent(taskContainer);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    for (int i = 0; i < 6; i++) {
                        Optional<Agent> agent = agentContainer.findAgent(1);
                        if (agent.isPresent()){
                            EnemyPlaneAgent agent1 = (EnemyPlaneAgent) agent.get();
                            if (agent1.getAvailable().equals(false)){
                                System.out.println("Agent弹药耗尽,触发了角色重新分配");
                                agentContainer.assignAgent(taskContainer);
                            }
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.run();
    }


    @Override
    public void assignTaskInstance(TaskContainer taskContainer, RoleContainer roleContainer) {
        for (int i = 0; i < 6; i++) {
            Optional<Task> task = taskContainer.findTask(i);
            Role roleInstance = roleContainer.findRoleInstance(i);
            if (task.isPresent()) {
                Task task1 = task.get();
                task1.setRole(roleInstance);
            }
        }

        Optional<Task> task = taskContainer.findTask(100);
        task.ifPresent(value -> value.setRole(roleContainer.findRoleInstance(100)));
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
    /*
      用于画窗口
     */
    public void paint(Graphics g) {
        g.drawImage(bg, 0, 0, null);
        drawScore(g);
        supply.drawSelf(g);
        plane.drawSelf(g);
        for (int i = 0; i < plane.bullets.length; i++) {
            plane.bullets[i].drawSelf(g);
        }

        for (PlaneRole value : enemies) {
            value.drawSelf(g);
        }
        for (PlaneRole enemy : enemies) {
            for (int j = 0; j < enemy.bullets.length; j++) {
                enemy.bullets[j].drawSelf(g);
            }
        }
        for (int i = 0; i < 6; i++) {
            Optional<Agent> agent = agentContainer.findAgent(i);
            if (agent.isPresent()){
                EnemyPlaneAgent enemyPlaneAgent = (EnemyPlaneAgent) agent.get();
                for (Bullet bullet : enemyPlaneAgent.bullets){
                    bullet.drawSelf(g);
                }
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
        for (PlaneRole enemy : enemies) {
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
    }

}
