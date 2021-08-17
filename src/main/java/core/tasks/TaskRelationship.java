package core.tasks;

import java.util.HashMap;

/**
 * @author SoonMachine
 * @description 任务之间关系
 * @date 2021-8-12 13:47:00
 */
public class TaskRelationship {

    /**
     * 先导关系
     */
    private static final String FORERUNNER_RELATIONSHIP = "forerunnerRelationship";

    /**
     * 后置关系
     */
    private static final String REAR_RELATIONSHIP = "rearRelationship";


    /**
     * 并行关系
     */
    private static final String PARALLEL_RELATIONSHIP = "parallelRelationship";

    /**
     * 关系名称
     */
    private String relationshipName;

    /**
     * 关系类型
     */
    private HashMap<Task,String> relationshipType;

    public String getRelationshipName() {
        return relationshipName;
    }

    public void setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName;
    }

    public HashMap<Task, String> getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(HashMap<Task, String> relationshipType) {
        this.relationshipType = relationshipType;
    }

    public TaskRelationship(String relationshipName) {
        this.relationshipName = relationshipName;
    }
}
