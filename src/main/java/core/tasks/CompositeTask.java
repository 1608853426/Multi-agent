package core.tasks;

/**
 * @author SoonMachine
 */
public abstract class CompositeTask extends Task{
    private boolean starting = true;
    private boolean finished = false;
    private boolean currentDone;
    private int currentResult;
    protected boolean currentExecuted = false;

    public CompositeTask(Integer taskId, Integer priority, String status, String target) {
        super(taskId, priority, status, target);
    }

    public CompositeTask() {
    }

    protected abstract void scheduleNext(boolean currentDone, int currentResult);

    protected abstract void scheduleFirst();

    protected abstract Task getCurrent();
}
