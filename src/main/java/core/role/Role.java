package core.role;

import java.util.*;

/**
 * @author SoonMachine
 */
public class Role {
    private Integer roleId;
    private String roleName;
    private Map<String,Integer> claim;
    private List<Object> instance;

    public Role() {
        instance = new ArrayList<>();
    }

    public void addClaim(String name,Integer value){
        this.claim.put(name, value);
    }

    public void removeClaim(String name){
        this.claim.remove(name);
    }

    public List<Object> getInstance() {
        return instance;
    }

    public void setInstance(List<Object> instance) {
        this.instance = instance;
    }

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
}
