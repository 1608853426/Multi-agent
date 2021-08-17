package core.tasks;

/**
 * @author SoonMachine
 */
public  class CircleTask extends NormalTask{
    public CircleTask(Integer taskId, Integer priority, String status, String target) {
        super(taskId, priority, status, target);
    }


    public CircleTask() {
    }

    @Override
    public void run() {
        this.role.doAssignTask();
    }

    @Override
    public boolean done() {
        return false;
    }


}
