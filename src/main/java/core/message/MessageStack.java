package core.message;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SoonMachine
 */
public class MessageStack {
    private List<AgentMessage> list = new ArrayList();
    private static final int N = 20;

    public synchronized void push(AgentMessage agentMessage){
        try {
            while (list.size() == N){
                System.out.println("队列已满，智能体 "
                        + Thread.currentThread().getName() + " 呈wait状态...");
                this.wait();

            }
            list.add(agentMessage);
            System.out.println("智能体 " + Thread.currentThread().getName() + "生产");
            this.notifyAll();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public synchronized AgentMessage get(String description){
        if (this.list.size() == 0){
            return new AgentMessage("队列为空");
        }
        for (AgentMessage agentMessage : list){
            if (description.equals(agentMessage.getDescription())){
                return agentMessage;
            }
        }
        return new AgentMessage("不存在该消息");
    }



}
