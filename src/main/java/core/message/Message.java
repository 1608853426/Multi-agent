package core.message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SoonMachine
 */
public class Message {

    private String description;

    private Map objMap = new HashMap();

    public Message(String description) {
        this.description = description;
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
