package app;

import core.role.Role;
import core.role.RoleContainer;
import core.tasks.AssignTaskInstance;
import core.tasks.Task;
import core.tasks.TaskContainer;

import java.util.Optional;

public class MyTaskContainer extends TaskContainer implements AssignTaskInstance {
    private MyTaskContainer() {
        super();
    }

    @Override
    public void assignTaskInstance(TaskContainer taskContainer, RoleContainer roleContainer) {
        for (int i = 0; i < 6; i++) {
            Optional<Task> task = taskContainer.findTask(i);
            Role roleInstance = roleContainer.findRoleInstance(i);
            if (task.isPresent()) {
                Task task1 = task.get();
                if (roleInstance.getMQPS().get("attack") > task1.getMQPS().get("attack"))
                {task1.setRole(roleInstance);}
            }
        }

        Optional<Task> task = taskContainer.findTask(100);
        task.ifPresent(value -> value.setRole(roleContainer.findRoleInstance(100)));
    }
}
