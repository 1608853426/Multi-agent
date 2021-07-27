package core.role;

import java.util.*;

/**
 * @author SoonMachine
 */
public abstract class Role {
    private Integer roleId;
    private String roleName;
    private Map<String,Integer> claim;
    private List<Object> instance;

    public Role() {

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
}
