package core.role;

import core.agents.Agent;

import java.util.List;

/**
 * @author SoonMachine
 */
public interface RoleAssignment{
    void roleAssignment(Agent[] agents, List<Role> roles);
}
