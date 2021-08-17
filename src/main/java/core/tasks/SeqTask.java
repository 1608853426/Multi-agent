package core.tasks;

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
    public void run() {
        for (Object subBehaviour : this.subBehaviours) {
            Task o = (Task) subBehaviour;
            o.run();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
