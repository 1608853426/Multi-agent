package core.tasks;

/**
 * @author SoonMachine
 */
public abstract class CircleTask extends NormalTask{
    public CircleTask(Integer taskId, Integer priority, String status, String target) {
        super(taskId, priority, status, target);
    }

    public CircleTask() {
    }

    @Override
    public boolean done() {
        return false;
    }
}
