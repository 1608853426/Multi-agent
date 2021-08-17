package core.role;

import core.agents.Agent;

import java.util.*;

/**
 * @author SoonMachine
 */
public  class Role{

    private static final long serialVersionUID = -6779609408858855669L;

    private HashMap<String,Integer> MQPS = new HashMap<>();

    private Integer roleId;

    private String roleName;

    /**
     * 公共的执行任务的方法
     */
    public  void doAssignTask(){};

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public HashMap<String, Integer> getMQPS() {
        return MQPS;
    }

    public void setMQPS(HashMap<String, Integer> MQPS) {
        this.MQPS = MQPS;
    }
}
