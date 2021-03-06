package core.tasks;

import core.agents.Agent;
import core.role.NullRole;
import core.role.Role;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author SoonMachine
 * @description 任务相关接口, 屎山代码
 */
public abstract class Task implements Serializable, Runnable{


    private static final long serialVersionUID = -8947497106385186426L;

    protected static final int NOTIFY_UP = -1;
    protected static final int NOTIFY_DOWN = 1;
    public static final String STATE_READY = "READY";
    public static final String STATE_BLOCKED = "BLOCKED";
    public static final String STATE_RUNNING = "RUNNING";
    private String taskName;
    private boolean startFlag;
    protected Agent myAgent;
    private volatile boolean runnableState;
    private volatile long restartCounter;
    private volatile String executionState;
    protected Task.RunnableChangedEvent myEvent;
    private CompositeTask wrappedParent;
    protected CompositeTask parent;
    private ArrayList<TaskRelationship> relationshipList = new ArrayList<>();

    private HashMap<String,Integer> MQPS = new HashMap<>();

    private Integer taskId;
    private Integer priority;
    private String status;
    private String target;

    public Role role = new NullRole();

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    void setParent(CompositeTask cb) {
        this.parent = cb;
        if (this.parent != null) {
            this.myAgent = this.parent.myAgent;
        }

        this.wrappedParent = null;
    }

    void setWrappedParent(CompositeTask cb) {
        this.wrappedParent = cb;
    }

    protected CompositeTask getParent() {
        return this.wrappedParent != null ? this.wrappedParent : this.parent;
    }

    public Task(Integer taskId, Integer priority, String status, String target) {
        this();
        this.taskId = taskId;
        this.priority = priority;
        this.status = status;
        this.target = target;
    }


    public Task() {
        this.startFlag = true;
        this.runnableState = true;
        this.restartCounter = 0L;
        this.executionState = "READY";
        this.myEvent = new Task.RunnableChangedEvent();
    }
    public final void setExecutionState(String s) {
        this.executionState = s;
    }

    public final String getExecutionState() {
        return this.executionState;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }


    public Agent getMyAgent() {
        return myAgent;
    }

    public void setMyAgent(Agent myAgent) {
        this.myAgent = myAgent;
    }

    public ArrayList<TaskRelationship> getRelationshipList() {
        return relationshipList;
    }

    public void setRelationshipList(ArrayList<TaskRelationship> relationshipList) {
        this.relationshipList = relationshipList;
    }

    /**
     * 用来定义任务需要完成的动作
     */
    @Override
    public abstract void run();

    /**
     * 判断任务是否完成的依据
     * @return 是否完成
     */
    public abstract boolean done();

    public int onEnd() {
        return 0;
    }

    public void onStart() {
    }

    void setRunnable(boolean runnable) {
        this.runnableState = runnable;
        if (this.runnableState) {
            ++this.restartCounter;
        }

    }

    public boolean isRunnable() {
        return this.runnableState;
    }

    public Task root() {
        Task p = this.getParent();
        return p != null ? p.root() : this;
    }

    public final long getRestartCounter() {
        return this.restartCounter;
    }

    public void block() {
        this.handleBlockEvent();
    }

    public void restart() {
/*        if (this.myAgent != null) {
            this.myAgent.removeTimer(this);
        }*/

        this.handleRestartEvent();
/*        if (this.myAgent != null) {
            this.myAgent.notifyRestarted(this);
        }*/

    }
    protected void handleBlockEvent() {
        this.myEvent.init(false, -1);
        this.handle(this.myEvent);
    }

    public void handleRestartEvent() {
        this.myEvent.init(true, -1);
        this.handle(this.myEvent);
    }

    protected void handle(Task.RunnableChangedEvent rce) {
        this.setRunnable(rce.isRunnable());
        if (this.parent != null && rce.isUpwards()) {
            this.parent.handle(rce);
        }

    }

    public HashMap<String, Integer> getMQPS() {
        return MQPS;
    }

    public void setMQPS(HashMap<String, Integer> MQPS) {
        this.MQPS = MQPS;
    }

    protected class RunnableChangedEvent implements Serializable{

        private static final long serialVersionUID = -4573919955157069317L;
        private boolean runnable;
        private int direction;

        protected RunnableChangedEvent() {
        }

        public void init(boolean b, int d) {
            this.runnable = b;
            this.direction = d;
        }

        public Task getSource() {
            return Task.this;
        }

        public boolean isRunnable() {
            return this.runnable;
        }

        public boolean isUpwards() {
            return this.direction == -1;
        }

    }
    @Override
    public int hashCode() {
        return this.getTaskId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Task)){
            return false;
        }

        Task obj1 = (Task) obj;
        if (this == obj1){
            return true;
        }

        return this.getTaskId().equals(obj1.getTaskId());
    }
}
