package messagetest;

import core.message.AgentMessage;
import core.message.MessageStack;
import messagetest.test.DbUtil;

public class MessageTest {
    static MessageStack messageStack = new MessageStack();
    public static void main(String[] args) {


    }

    private static void record(AgentMessage agentMessage) {
        DbUtil.record(agentMessage.getAgentName(), agentMessage.getDescription());
    }


}
