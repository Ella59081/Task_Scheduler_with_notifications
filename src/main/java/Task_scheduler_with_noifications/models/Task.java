package Task_scheduler_with_noifications.models;

import Task_scheduler_with_noifications.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Task {
    private String id;
    private String name;
    private String description;
    private String projectId;
    private String dueDate;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String completedAt;

    public Task(String id, String name, String description, String projectId, String dueDate,
                String status, String createdAt, String updatedAt, String completedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.projectId = projectId;
        this.dueDate = dueDate;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.completedAt = completedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }
}
