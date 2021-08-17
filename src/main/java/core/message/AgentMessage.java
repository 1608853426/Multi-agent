package core.message;

import org.apache.activemq.command.ActiveMQMessage;

import javax.jms.Message;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SoonMachine
 */
public class AgentMessage extends ActiveMQMessage {

    private String agentName;

    private String description;

    private Map objMap = new HashMap();

    public AgentMessage(String description) {
        this.description = description;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map getObjMap() {
        return objMap;
    }

    public void setObjMap(Map objMap) {
        this.objMap = objMap;
    }
}
