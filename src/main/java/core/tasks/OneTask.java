package core.tasks;

/**
 * @author SoonMachine
 */
public abstract class OneTask extends NormalTask{

    public OneTask(Integer taskId, Integer priority, String status, String target) {
        super(taskId, priority, status, target);
    }

    public OneTask() {
    }

    @Override
    public boolean done() {
        return true;
    }
}
