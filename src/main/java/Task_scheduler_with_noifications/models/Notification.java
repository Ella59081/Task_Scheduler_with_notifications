package Task_scheduler_with_noifications.models;

import Task_scheduler_with_noifications.enums.ActivityType;
import Task_scheduler_with_noifications.enums.Method;

import java.time.LocalDateTime;

public class Notification {
    private String id;
    private String activity;
    private String method;
    private String taskId;
    private String projectId;
    private String timeStamp;
    private String message;

    public Notification(String id, String activity, String method,
                         String taskId, String projectId, String timeStamp, String message) {
        this.id = id;
        this.activity = activity;
        this.method = method;
        this.taskId = taskId;
        this.projectId = projectId;
        this.timeStamp = timeStamp;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
