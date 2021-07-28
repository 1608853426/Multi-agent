package core.agents;


import app.Plane;

import java.io.Serializable;

/**
 * @author SoonMachine
 */
public class Agent extends Thread implements Serializable{


    private String roleName;

    private static final long serialVersionUID = -1555019280735961301L;

    public Agent(Runnable target) {
        super(target);
    }

    public Agent(ThreadGroup group, Runnable target) {
        super(group, target);
    }

    public Agent(String name) {
        super(name);
    }

    public Agent(ThreadGroup group, String name) {
        super(group, name);
    }

    public Agent(Runnable target, String name) {
        super(target, name);
    }

    public Agent(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
    }

    public Agent(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, target, name, stackSize);
    }

    public Agent(ThreadGroup group, Runnable target, String name, long stackSize, boolean inheritThreadLocals) {
        super(group, target, name, stackSize, inheritThreadLocals);
    }

    @Override
    public void run() {
        super.run();
    }

    @Override
    public synchronized void start() {
        super.start();
    }


    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
