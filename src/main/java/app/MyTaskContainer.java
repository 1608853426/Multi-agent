package app;

import core.role.Role;
import core.role.RoleContainer;
import core.tasks.AssignTaskInstance;
import core.tasks.Task;
import core.tasks.TaskContainer;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author SoonMachine
 */
public class MyTaskContainer extends TaskContainer implements AssignTaskInstance {
    private static final MyTaskContainer TASK_CONTAINER = new MyTaskContainer();

    private MyTaskContainer() {
        super();
    }

    public static MyTaskContainer getInstance(){
        return TASK_CONTAINER;
    }

    @Override
    public void assignTaskInstance(TaskContainer taskContainer, RoleContainer roleContainer) {
        ArrayList<Task> taskArrayList = new ArrayList<>();
        ArrayList<Role> roleArrayList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Optional<Task> task = taskContainer.findTask(i);
            Role roleInstance = roleContainer.findRoleInstance(i);
            assert task.isPresent();
            taskArrayList.add(task.get());
            roleArrayList.add(roleInstance);
        }
        GenerationAlgorithm generationAlgorithm = new GenerationAlgorithm();
        ArrayList<PII> ga = generationAlgorithm.ga(taskArrayList, roleArrayList);

        for (int i = 0; i < ga.size(); i++) {
            PII pii = ga.get(i);
            int first = pii.first;
            int second = pii.second;
            Task task = taskArrayList.get(first);
            Role role = roleArrayList.get(second);
            task.setRole(role);
        }
        Optional<Task> task = taskContainer.findTask(100);
        task.ifPresent(value -> value.setRole(roleContainer.findRoleInstance(100)));
    }
}
