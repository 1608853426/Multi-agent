package app;

import core.agents.Agent;

/**
 * @author SoonMachine
 */
public class EnemyPlaneAgent extends Agent implements Runnable {
    PlaneRole role;
    Bullet[] bullets = new Bullet[Constant.DEFAULT_ARMY_BC];
    Integer bulletsNums = Constant.DEFAULT_PLANE_BC;
    private Boolean isAvailable = true;
    public MyAgentContainer agentContainer;

    public EnemyPlaneAgent(int id) {
        super(id);
        for (int i = 0; i < bullets.length; i++) {
            bullets[i] = new Bullet();
        }
    }

    public PlaneRole getRole() {
        return role;
    }

    public void setRole(PlaneRole role) {
        this.role = role;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public void fire() {
        if (role.isLive() && (Math.random() < 0.5)) {
            for (Bullet bullet : bullets) {
                if (!bullet.isLive()) {
                    bullet.setPosition(role.getX() + 25, role.getY() + 60);
                    bullet.setLive(true);
                    this.bulletsNums --;
                    if (bulletsNums <= 0){
                        this.setAvailable(false);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            this.fire();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
