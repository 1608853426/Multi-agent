package core.agents;



import core.tasks.TaskContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author SoonMachine
 * @description 智能体容器
 * @date 2021-8-13 15:35:14
 */
public class AgentContainer {

    private static final int CORE_POOL_SIZE = 7;
    private static final int MAX_POOL_SIZE = 10 ;
    private static final int QUEUE_CAPACITY = 100;
    private static final Long KEEP_ALIVE_TIME = 1L;
    ThreadPoolExecutor executor = new ThreadPoolExecutor(CORE_POOL_SIZE,
            MAX_POOL_SIZE,KEEP_ALIVE_TIME, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(QUEUE_CAPACITY),
            new ThreadPoolExecutor.CallerRunsPolicy());


    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    private ArrayList<Agent> agentInstance = new ArrayList<>();

    private static final AgentContainer INSTANCE = new AgentContainer();

    public AgentContainer(){};

    public static AgentContainer getInstance(){return INSTANCE;}

    public void createAgent(Agent agent){
        this.agentInstance.add(agent);
    }

    public Optional<Agent> findAgent(int agentId){
        return agentInstance.stream().filter(agent -> agent.getAgentId().equals(agentId)).findFirst();
    }

    public boolean deleteAgent(int agentId){
        return this.agentInstance.removeIf(agent -> agent.getAgentId().equals(agentId));
    }

}
