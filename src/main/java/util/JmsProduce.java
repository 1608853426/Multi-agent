package util;



import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMessage;

import javax.jms.*;

public class JmsProduce {
    public static final String ACTIVEMQ_URL = "tcp://121.4.191.125:61617";
    public static final String QUEUE_NAME = "MessageQueue";

    public static void sendMessage(ActiveMQMessage message) throws JMSException{
        //创建连接工厂,按照给定的url地址,采用默认用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        //通过连接工厂,获得连接connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();

        //启动连接
        connection.start();

        //创建会话 false指是否在事务上处理，Session.AUTO_ACKNOWLEDGE为应答模式，这里是自动应答
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        //创建一个目标
        Queue queue = session.createQueue(QUEUE_NAME);

        //创建生产者
        MessageProducer producer = session.createProducer(queue);

        //发送消息
        producer.send(message);

        //关闭连接
        producer.close();
        session.close();
        connection.close();
    }
}

