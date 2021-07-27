package core.tasks;

/**
 * @author SoonMachine
 */
public abstract class NormalTask extends Task{
    public NormalTask(Integer taskId, Integer priority, String status, String target) {
        super(taskId, priority, status, target);
    }

    public NormalTask() {
    }

}
