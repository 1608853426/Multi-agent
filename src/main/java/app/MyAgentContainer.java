package app;

import core.agents.Agent;
import core.agents.AgentContainer;
import core.agents.AssignAgentInstance;
import core.role.Role;
import core.tasks.Task;
import core.tasks.TaskContainer;

import java.util.Optional;

public class MyAgentContainer extends AgentContainer implements AssignAgentInstance {
    public MyAgentContainer() {
        super();
    }

    public void assignAgent(TaskContainer taskContainer){
        for (int i = 0; i < 6; i++) {
            Optional<Agent> agent = this.findAgent(i);
            Optional<Task> task = taskContainer.findTask(i);
            if (task.isPresent() && agent.isPresent()){
                EnemyPlaneAgent agent1 =(EnemyPlaneAgent) agent.get();
                if (agent1.getAvailable().equals(false)){
                    agent1 = new EnemyPlaneAgent(i);
                }
                Task task1 = task.get();
                PlaneRole role = (PlaneRole)task1.getRole();
                agent1.setRole(role);
                this.getExecutor().execute(agent1);
            }
        }
        Optional<Agent> agent = this.findAgent(6);
        Optional<Task> task = taskContainer.findTask(100);
        if (agent.isPresent() && task.isPresent()){
            Agent agent1
                    = agent.get();
            agent1.setTask(task.get());
            this.getExecutor().execute(agent1.getTask());
        }
    }

    @Override
    public void assignAgentInstance(AgentContainer agentContainer, TaskContainer taskContainer) {
        for (int i = 0; i < 6; i++) {
            Optional<Agent> agent = agentContainer.findAgent(i);
            Optional<Task> task = taskContainer.findTask(i);
            if (task.isPresent() && agent.isPresent()){
                EnemyPlaneAgent agent1 =(EnemyPlaneAgent) agent.get();
                Task task1 = task.get();
                PlaneRole role = (PlaneRole)task1.getRole();
                agent1.setRole(role);
                agentContainer.getExecutor().execute(agent1);
            }
        }
        Optional<Agent> agent = agentContainer.findAgent(6);
        Optional<Task> task = taskContainer.findTask(100);
        if (agent.isPresent() && task.isPresent()){
            Agent agent1
                    = agent.get();
            agent1.setTask(task.get());
            agentContainer.getExecutor().execute(agent1.getTask());
        }
    }
}
