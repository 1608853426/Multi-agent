package core.tasks;

import core.role.RoleContainer;

import java.util.ArrayList;

/**
 * @author SoonMachine
 * @description 分配任务的接口
 * @date 2021-8-12 15:49:18
 */
public interface AssignTaskInstance {
    /**
     * 任务分配接口
     * @param container 任务容器
     * @param roleContainer 角色容器
     */
    void assignTaskInstance(TaskContainer container, RoleContainer roleContainer);
}
