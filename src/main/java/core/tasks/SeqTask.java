package core.tasks;

import java.util.Hashtable;

public class SeqTask extends CompositeTask{

    private TaskList subBehaviours = new TaskList();
    int current = 0;

    public SeqTask(Integer taskId, Integer priority, String status, String target) {
        super(taskId, priority, status, target);
    }

    public SeqTask() {
    }

    @Override
    protected void scheduleNext(boolean currentDone, int currentResult) {
    if (currentDone){
        this.current ++;
    }
    }

    @Override
    protected void scheduleFirst() {
        this.current = 0;
    }

    @Override
    protected Task getCurrent() {
        Task task = null;
        if (this.subBehaviours.size() > this.current){
            task = (Task) this.subBehaviours.get(this.current);
        }
        return task;
    }

    @Override
    public void action() {
        for (int i = 0; i < this.subBehaviours.size(); i++) {
            Task o = (Task) this.subBehaviours.get(i);
            o.action();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
