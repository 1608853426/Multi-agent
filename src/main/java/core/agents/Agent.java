package core.agents;


import core.role.Role;
import core.tasks.Task;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * @author SoonMachine
 */
public class Agent implements Serializable{

    private Task task;
    private Integer AgentId;
    private String roleName;
    private ArrayList<Task> taskArrayList = new ArrayList<>();
    private static final long serialVersionUID = -1555019280735961301L;

    private String State;

    private static final String WAIT = "Wait";
    private static final String DEAD = "Dead";
    private static final String READY = "Ready";
    private static final String ACTIVE = "Active";
    private static final String EXECUTE = "Execute";
    public Agent() {
    }

    public Agent(Integer agentId) {
        this.AgentId = agentId;
    }

    public Integer getAgentId() {
        return AgentId;
    }

    public void setAgentId(Integer agentId) {
        AgentId = agentId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }


    public ArrayList<Task> getTaskArrayList() {
        return taskArrayList;
    }

    public void setTaskArrayList(ArrayList<Task> taskArrayList) {
        this.taskArrayList = taskArrayList;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}
