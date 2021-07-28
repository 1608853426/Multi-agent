package core.message;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SoonMachine
 */
public class MessageStack {
    private List<Message> list = new ArrayList();
    private static final int N = 20;

    public synchronized void push(Message message){
        try {
            while (list.size() == N){
                System.out.println("队列已满，智能体 "
                        + Thread.currentThread().getName() + " 呈wait状态...");
                this.wait();

            }
            list.add(message);
            System.out.println("智能体 " + Thread.currentThread().getName() + "生产");
            this.notifyAll();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public synchronized Message get(String description){
        if (this.list.size() == 0){
            return new Message("队列为空");
        }
        for (Message message : list){
            if (description.equals(message.getDescription())){
                return message;
            }
        }
        return new Message("不存在该消息");
    }



}
