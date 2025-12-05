package Task_scheduler_with_noifications.models;

import Task_scheduler_with_noifications.enums.ActivityType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Activity {
    private String activity;
    private String dateTime;
    private String projectId;
    private String taskId;

    public Activity(String activity, String dateTime, String projectId, String taskId) {
        this.activity = activity;
        this.dateTime = dateTime;
        this.projectId = projectId;
        this.taskId = taskId;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
