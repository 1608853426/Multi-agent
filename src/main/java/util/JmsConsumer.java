package util;

import core.message.AgentMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SoonMachine
 * @description 消费者
 * @date 2021-8-16 00:56:13
 */
public class JmsConsumer {
    public static final String ACTIVEMQ_URL = "tcp://121.4.191.125:61617";
    public static final String QUEUE_NAME = "MessageQueue";


    public static List<AgentMessage> receiveMessage() throws JMSException {
        //创建连接工厂,按照给定的url地址,采用默认用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        //通过连接工厂,获得连接connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();

        //启动连接
        connection.start();

        //创建会话 false指是否在事务上处理，Session.AUTO_ACKNOWLEDGE为应答模式，这里是自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //创建一个目标
        Queue queue = session.createQueue(QUEUE_NAME);

        MessageConsumer consumer = session.createConsumer(queue);

        List<AgentMessage> list = new ArrayList<>();

        while (true) {
            AgentMessage receive = (AgentMessage) consumer.receive();
            if (null != receive) {
                list.add(receive);
            } else {
                break;
            }
        }

        //关闭连接
        consumer.close();
        session.close();
        connection.close();
        return list;

    }
}
