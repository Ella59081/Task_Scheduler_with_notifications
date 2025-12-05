package Task_scheduler_with_noifications.service;

import Task_scheduler_with_noifications.models.Task;

import java.util.List;

public interface TaskService {
    void addTask(Task task);
    void updateTask(Task t);
    void deleteTask(String id);
    List<Task> getAllTasks();
    Task getSingleTask(String id);
}
