package app;

import java.awt.*;


/**
 * @author SoonMachine
 */
public class PlaneRole extends GameRole implements Runnable {
    private double degree = (Math.random() * (Math.PI / 2) + Math.PI / 4);
    private boolean live = true;
    private boolean collide = false;
    /**
     * ex1为飞机第一次炸毁后的动画
     * ex2用于解决飞机第一次炸毁，第二出现也炸毁，
     * 而第一次的动画没有播放完的情况
     */
    Explode explode1 = new Explode();
    Explode explode2 = new Explode();
    boolean ex1 = false;
    boolean ex2 = false;
    Bullet[] bullets;
    private int bulletCount = Constant.DEFAULT_ARMY_BC;

    public PlaneRole(Image img) {
        setImg(img);
        bullets = new Bullet[bulletCount];
        for (int i = 0; i < bullets.length; i++) {
            bullets[i] = new Bullet();
        }
        setPosition((Math.random() * 6 * 64 + 64), -34);
        setHeight(img.getHeight(null));
        setWidth(img.getWidth(null));
        this.getCapabilities().put("attack",10);
    }

    public PlaneRole(Image img, double x, double y, int bulletins) {
        setImg(img);
        this.bulletCount = bulletins;
        bullets = new Bullet[bulletins];
        for (int i = 0; i < bullets.length; i++) {
            bullets[i] = new Bullet();
        }
        setPosition((Math.random() * 6 * 64 + 64), -34);
        setWidth(getImg().getWidth(null));
        setHeight(getImg().getHeight(null));
    }

    @Override
    public void drawSelf(Graphics g) {
        if (live) {
            g.drawImage(getImg(), (int) getX(), (int) getY(), null);
            move();
        }
        if (collide) {
            exp_anim(g);
        }
        checkLocation();
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public void setCollide(boolean collide) {
        this.collide = collide;
    }

    public void setBulletCount(int bulletcount) {
        this.bulletCount = bulletcount;
    }

    public void exp_anim(Graphics g) {
        if (collide && ex1) {
            explode1.draw(g);
        }
        if (collide && ex2) {
            explode2.draw(g);
        }
        if (explode1.count >= 15) {
            ex1 = false;
            explode1.count = 0;
        }
        if (explode2.count >= 15) {
            ex2 = false;
            explode2.count = 0;
        }
        if (!ex1 && !ex2) {
            collide = false;
        }
    }

    /**
     * 检查位置,每次死后重新设置位置和角度
     */
    @Override
    public void checkLocation() {
        if (!live || getY() > Constant.GAME_HEIGHT) {
            setPosition(Math.random() * 8 * 64, -34);
            degree = (Math.random() * (Math.PI / 2) + Math.PI / 4);
            live = true;
        }
        if (getX() < 0 || getX() > Constant.GAME_WIDTH - 64) {
            degree = Math.PI - degree;
        }
    }

    /**
     * 移动位置
     */
    @Override
    public void move() {
        move(getX() + Constant.ARMY_STEP * Math.cos(degree),
                getY() + Constant.ARMY_STEP * Math.sin(degree));
    }

    /**
     * 发射子弹
     */
    public void fire() {
        /*
         * 寻找一个子弹，再发射
         */

        if (live && (Math.random() < 0.5)) {
            for (Bullet bullet : bullets) {
                if (!bullet.isLive()) {
                    bullet.setPosition(getX() + 25, getY() + 60);
                    bullet.setLive(true);
                    break;
                }
            }
        }

    }


    @Override
    public void run() {
        this.fire();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doAssignTask() {
        while (true) {
            this.fire();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
