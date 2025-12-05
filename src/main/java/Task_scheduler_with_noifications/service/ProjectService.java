package Task_scheduler_with_noifications.service;

import Task_scheduler_with_noifications.models.Project;

import java.util.List;

public interface ProjectService {
    void addProject(Project project);
    void updateProject(Project p);
    void deleteProject(String id);
    List<Project> getAllProjects();
    Project getSingleProject(String id);
}
