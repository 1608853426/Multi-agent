package core.tasks;

import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author SoonMachine
 */
public class ParallelTask extends CompositeTask{

    private TaskList subBehaviours = new TaskList();
    private Hashtable blockedChildren  = new Hashtable();
    private TaskList terminatedChildren = new TaskList();
    public ParallelTask(Integer taskId, Integer priority, String status, String target) {
        super(taskId, priority, status, target);
    }

    public ParallelTask() {
    }

    @Override
    protected void scheduleNext(boolean currentDone, int currentResult) {
        Task b = this.subBehaviours.next();
        if (b != null && this.blockedChildren.size() < this.subBehaviours.size()) {
            while(!b.isRunnable()) {
                b = this.subBehaviours.next();
            }
        }
    }

    @Override
    protected void scheduleFirst() {
        this.subBehaviours.begin();
        Task b = this.subBehaviours.getCurrent();
        if (b != null && this.blockedChildren.size() < this.subBehaviours.size()) {
            while(!b.isRunnable()) {
                b = this.subBehaviours.next();
            }
        }
    }

    @Override
    protected Task getCurrent() {
        return this.subBehaviours.getCurrent();
    }

    @Override
    public void run() {
        ExecutorService ex = Executors.newFixedThreadPool(this.subBehaviours.size());
        for (int i = 0; i < this.subBehaviours.size(); i++) {
            Task o = (Task) subBehaviours.get(i);
            ex.submit(new Runnable() {
                @Override
                public void run() {
                    o.run();
                }
            });
        }
    }

    @Override
    public boolean done() {
        return false;
    }

    public void addSubBehaviour(Task b) {
        this.subBehaviours.addElement(b);
        b.setParent(this);
        b.setMyAgent(this.myAgent);
        if (b.isRunnable()) {
            if (!this.isRunnable()) {
                this.myEvent.init(true, -1);
                super.handle(this.myEvent);
                this.currentExecuted = true;
            }
        } else {
            this.blockedChildren.put(b, b);
        }

    }

    public void removeSubBehaviour(Task b) {
        this.terminatedChildren.removeElement(b);
        boolean rc = this.subBehaviours.removeElement(b);
        if (rc) {
            b.setParent((CompositeTask) null);
        }

        if (!b.isRunnable()) {
            this.blockedChildren.remove(b);
        } else if (!this.subBehaviours.isEmpty() && this.blockedChildren.size() == this.subBehaviours.size()) {
            this.myEvent.init(false, -1);
            super.handle(this.myEvent);
        }

    }
}
