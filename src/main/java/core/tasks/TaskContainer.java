package core.tasks;

import core.role.RoleContainer;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author SoonMachine
 * @description 单例模式 TaskContainer是多任务的管理模块，负责动态生成任务实例，建立任务实例之间的关系，并动态维护任务实例的状态变化。
 * @date 2021-8-13 01:03:25
 */
public class TaskContainer {

    /**
     * 任务列表
     */
    private  ArrayList<Task> taskList = new ArrayList<>();

    /**
     * 创建一个对象
     */
    private static final TaskContainer INSTANCE = new TaskContainer();

    /**
     * 构造方法私有化
     */
    public TaskContainer(){}


    /**
     * 获取唯一可用对象
     * @return 实例
     */
    public static TaskContainer getInstance(){
        return INSTANCE;
    }

    /**
     * 添加任务
     * @param task 创建的任务
     */
    public void createTask(Task task){
        taskList.add(task);
    }


    /**
     * 删除任务
     * @param taskId 任务id
     * @return 是否删除成功
     */
    public boolean deleteTask(int taskId){
        return taskList.removeIf(task -> task.getTaskId().equals(taskId));
    }

    /**
     * 查找任务
     * @param taskId 任务id
     * @return 避免空指针的返回类型
     */
    public Optional<Task> findTask(int taskId){
        return taskList.stream().filter(task -> task.getTaskId().equals(taskId)).findFirst();
    }

}
