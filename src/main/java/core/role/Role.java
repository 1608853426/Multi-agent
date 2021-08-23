package core.role;

import java.util.*;

/**
 * @author SoonMachine
 */
public  class Role{

    private static final long serialVersionUID = -6779609408858855669L;

    private HashMap<String,Integer> capabilities = new HashMap<>();

    private Integer roleId;

    private String roleName;

    private boolean beTaken;

    private Integer permissions;

    private List<Role> relatedRoles = new ArrayList<>();

    private Object msgTemplate;

    private String protocols;


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

    public HashMap<String, Integer> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(HashMap<String, Integer> capabilities) {
        this.capabilities = capabilities;
    }

    public boolean isBeTaken() {
        return beTaken;
    }

    public void setBeTaken(boolean beTaken) {
        this.beTaken = beTaken;
    }

    public Integer getPermissions() {
        return permissions;
    }

    public void setPermissions(Integer permissions) {
        this.permissions = permissions;
    }

    public List<Role> getRelatedRoles() {
        return relatedRoles;
    }

    public void setRelatedRoles(List<Role> relatedRoles) {
        this.relatedRoles = relatedRoles;
    }

    public Object getMsgTemplate() {
        return msgTemplate;
    }

    public void setMsgTemplate(Object msgTemplate) {
        this.msgTemplate = msgTemplate;
    }

    public String getProtocols() {
        return protocols;
    }

    public void setProtocols(String protocols) {
        this.protocols = protocols;
    }
}
