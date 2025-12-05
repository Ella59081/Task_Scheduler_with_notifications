package Task_scheduler_with_noifications.notifications;
import Task_scheduler_with_noifications.config.DatabaseConnection;
import Task_scheduler_with_noifications.enums.ActivityType;
import Task_scheduler_with_noifications.enums.Method;
import Task_scheduler_with_noifications.models.Activity;
import Task_scheduler_with_noifications.models.Project;
import Task_scheduler_with_noifications.models.Task;
import Task_scheduler_with_noifications.models.Notification;
import Task_scheduler_with_noifications.service.impl.ProjectServiceImpl;
//import Task_scheduler_with_noifications.service.impl.TaskServiceImpl;
import Task_scheduler_with_noifications.service.impl.TaskServiceImpl;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
//import java.time.Duration;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import static Task_scheduler_with_noifications.config.generateTableId.generateId;

public class NotificationsManager {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final Map<String, LocalDateTime> notified = new ConcurrentHashMap<>(); // track last notified time per task

    // how early to notify (e.g. when due exactly or X minutes before)
    private final java.time.Duration notifyBefore = java.time.Duration.ofMinutes(0);
    private List<Task> tasks;
    private List<Project> projects;

    public NotificationsManager(TaskServiceImpl ts) {
        this.tasks = ts.getAllTasks();

    }
    public NotificationsManager(ProjectServiceImpl ps) {
        this.projects = ps.getAllProjects();
    }

    public NotificationsManager() {

    }

    public void start() {
        // check every 30 seconds
        scheduler.scheduleAtFixedRate(this::checkTasks, 0, 30, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::checkProjects, 0, 30, TimeUnit.SECONDS);

    }

    public void stop() {
        scheduler.shutdownNow();
    }

    private void checkTasks() {
        try {
            LocalDateTime now = LocalDateTime.now();
            for (Task t : tasks) {
                LocalDateTime due = LocalDateTime.parse(t.getDueDate()); // change Task model to expose LocalDateTime
                if (due == null) continue;

                // when to notify (you can adjust logic for reminders, multiple reminders etc.)
                LocalDateTime notifyTime = due.minus(notifyBefore);
                // only notify if we are within the check window and not already notified for this due time
                if (!now.isBefore(notifyTime) && !now.isAfter(due.plusMinutes(1))) {
                    LocalDateTime last = notified.get(t.getId());
                    // avoid duplicate notifications for same due occurrence
                    if (last == null || last.isBefore(due)) {
                        notified.put(t.getId(), due);
                        sendNotifications(t, Method.in_app, ActivityType.Task_due);
                        sendNotifications(t, Method.push, ActivityType.Task_due);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkProjects() {
        try {
            LocalDateTime now = LocalDateTime.now();
            for (Project p : projects) {
                LocalDateTime due = LocalDateTime.parse(p.getDueDate()); // change Task model to expose LocalDateTime
                if (due == null) continue;

                // when to notify (you can adjust logic for reminders, multiple reminders etc.)
                LocalDateTime notifyTime = due.minus(notifyBefore);
                // only notify if we are within the check window and not already notified for this due time
                if (!now.isBefore(notifyTime) && !now.isAfter(due.plusMinutes(1))) {
                    LocalDateTime last = notified.get(p.getId());
                    // avoid duplicate notifications for same due occurrence
                    if (last == null || last.isBefore(due)) {
                        notified.put(p.getId(), due);
                        sendProjectNot(p, Method.in_app, ActivityType.Project_due);
                        sendProjectNot(p, Method.push, ActivityType.Project_due);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendNotifications(Task task, Method method, ActivityType activity) {
        LocalDateTime dt = LocalDateTime.now();
        if(method == Method.in_app){
            // 1) In-app (ControlsFX)

            switch(activity){
                case New_task_created -> {
                    Platform.runLater(() -> {
                        Notifications.create()
                                .title("New task, " + task.getName() + "created")
                                .text("Task " + task.getName() + "created at" + dt )
                                .hideAfter(Duration.seconds(6))
                                .position(Pos.TOP_RIGHT)
                                .showInformation();
                    });
                    String message = "New task, " + task.getName() + "created" +
                            "at " + dt;
                    Notification n = new Notification(generateId("Notifications"), activity.toString(),
                            "in-app", task.getId(), null, dt.toString(), message);
                    updateNotifications(n);
                    Activity a = new Activity(message, dt.toString(), null, task.getId());
                    updateActivityLog(a);
                }
                case Task_updated -> {
                    Platform.runLater(() -> {
                        Notifications.create()
                                .title("Task, " + task.getName() + "updated")
                                .text("Task" + task.getName() + "updated at" + dt )
                                .hideAfter(Duration.seconds(6))
                                .position(Pos.TOP_RIGHT)
                                .showInformation();
                    });
                    String message = "Task, " + task.getName() + "updated" +
                            "at " + dt;
                    Notification n = new Notification(generateId("Notifications"), activity.toString(),
                            "in-app", task.getId(), null, dt.toString(), message);
                    updateNotifications(n);
                    Activity a = new Activity(message, dt.toString(), null, task.getId());
                    updateActivityLog(a);
                }
                case Task_deleted -> {
                    Platform.runLater(() -> {
                        Notifications.create()
                                .title("Task, " + task.getName() + "deleted")
                                .text("Task " + task.getName() + "deleted at" + dt )
                                .hideAfter(Duration.seconds(6))
                                .position(Pos.TOP_RIGHT)
                                .showInformation();
                    });
                    String message = "Task, " + task.getName() + "deleted" +
                            "at " + dt;
                    Notification n = new Notification(generateId("Notifications"), activity.toString(),
                            "in-app", task.getId(), null, dt.toString(), message);
                    updateNotifications(n);
                    Activity a = new Activity(message, dt.toString(), null, task.getId());
                    updateActivityLog(a);
                }
                case Task_due -> {
                    Platform.runLater(() -> {
                        Notifications.create()
                                .title("Task Due: " + task.getName())
                                .text("Task due at " + formatLocalDateTime(LocalDateTime.parse(task.getDueDate())))
                                .hideAfter(Duration.seconds(6))
                                .position(Pos.TOP_RIGHT)
                                .showWarning();
                    });
                    String message = "Task Due: " + task.getName() +
                            "Due at " + formatLocalDateTime(LocalDateTime.parse(task.getDueDate()));
                    Notification n = new Notification(generateId("Notifications"), activity.toString(),
                            "in-app", task.getId(), null, dt.toString(), message);
                    updateNotifications(n);
                    Activity a = new Activity(message, dt.toString(), null, task.getId());
                    updateActivityLog(a);
                }
            }
        }else{
            // 2) Native Windows toast (runs Powershell - works offline)
            // Fire-and-forget on another thread so we don't block the scheduler
            switch(activity){
                case New_task_created -> {
                    CompletableFuture.runAsync(() -> {
                        WindowsToastUtil.showToast("New task, " + task.getName(),
                                "created at " + dt);
                    });
                    String message = "New task, " + task.getName() + "created" +
                            "at " + dt;
                    Notification n = new Notification(generateId("Notifications"), activity.toString(),
                            method.toString(), task.getId(), null, dt.toString(), message);
                    updateNotifications(n);
                    Activity a = new Activity(message, dt.toString(), null, task.getId());
                    updateActivityLog(a);
                }
                case Task_updated -> {
                    CompletableFuture.runAsync(() -> {
                        WindowsToastUtil.showToast("Task, " + task.getName(),
                                "updated at " + dt);
                    });
                    String message = "Task, " + task.getName() + "updated" +
                            "at " + dt;
                    Notification n = new Notification(generateId("Notifications"), activity.toString(),
                            method.toString(), task.getId(), null, dt.toString(), message);
                    updateNotifications(n);
                    Activity a = new Activity(message, dt.toString(), null, task.getId());
                    updateActivityLog(a);
                }
                case Task_deleted -> {
                    CompletableFuture.runAsync(() -> {
                        WindowsToastUtil.showToast("Task, " + task.getName(),
                                "deleted at " + dt);
                    });
                    String message = "Task, " + task.getName() + "deleted" +
                            "at " + dt;
                    Notification n = new Notification(generateId("Notifications"), activity.toString(),
                            method.toString(), task.getId(), null, dt.toString(), message);
                    updateNotifications(n);
                    Activity a = new Activity(message, dt.toString(), null, task.getId());
                    updateActivityLog(a);
                }
                case Task_due -> {
                    CompletableFuture.runAsync(() -> {
                        WindowsToastUtil.showToast("Task Due: " + task.getName(),
                                "Due at " + formatLocalDateTime(LocalDateTime.parse(task.getDueDate())));
                    });
                    String message = "Task Due: " + task.getName() +
                            "Due at " + formatLocalDateTime(LocalDateTime.parse(task.getDueDate()));
                    Notification n = new Notification(generateId("Notifications"), activity.toString(),
                            method.toString(), task.getId(), null, dt.toString(), message);
                    updateNotifications(n);
                    Activity a = new Activity(message, dt.toString(), null, task.getId());
                    updateActivityLog(a);
                }
            }
        }

    }

    public void sendProjectNot(Project project, Method method, ActivityType activity) {
        LocalDateTime dt = LocalDateTime.now();
        if(method == Method.in_app){
            // 1) In-app (ControlsFX)

            switch(activity){
                case New_project_created -> {
                    Platform.runLater(() -> {
                        Notifications.create()
                                .title("New project, " + project.getName() + "created")
                                .text("Project " + project.getName() + "created at" + dt )
                                .hideAfter(Duration.seconds(6))
                                .position(Pos.TOP_RIGHT)
                                .showInformation();
                    });
                    String message = "New project, " + project.getName() + "created" +
                            "at " + dt;
                    Notification n = new Notification(generateId("Notifications"), activity.toString(),
                            method.toString(), null, project.getId(), dt.toString(), message);
                    updateNotifications(n);
                    Activity a = new Activity(message, dt.toString(), project.getId(), null);
                    updateActivityLog(a);
                }
                case Project_updated -> {
                    Platform.runLater(() -> {
                        Notifications.create()
                                .title("Project, " + project.getName() + "updated")
                                .text("Project " + project.getName() + "updated at" + dt )
                                .hideAfter(Duration.seconds(6))
                                .position(Pos.TOP_RIGHT)
                                .showInformation();
                    });
                    String message = "Project, " + project.getName() + "updated" +
                            "at " + dt;
                    Notification n = new Notification(generateId("Notifications"), activity.toString(),
                            method.toString(), null, project.getId(), dt.toString(), message);
                    updateNotifications(n);
                    Activity a = new Activity(message, dt.toString(), project.getId(), null);
                    updateActivityLog(a);
                }
                case Project_deleted -> {
                    Platform.runLater(() -> {
                        Notifications.create()
                                .title("Project, " + project.getName() + "deleted")
                                .text("Project " + project.getName() + "deleted at" + dt )
                                .hideAfter(Duration.seconds(6))
                                .position(Pos.TOP_RIGHT)
                                .showInformation();
                    });
                    String message = "Project, " + project.getName() + "deleted" +
                            "at " + dt;
                    Notification n = new Notification(generateId("Notifications"), activity.toString(),
                            method.toString(), null, project.getId(), dt.toString(), message);
                    updateNotifications(n);Activity a = new Activity(message, dt.toString(), project.getId(), null);
                    updateActivityLog(a);

                }
                case Project_due -> {
                    Platform.runLater(() -> {
                        Notifications.create()
                                .title("Project Due: " + project.getName())
                                .text("Project due at " + formatLocalDateTime(LocalDateTime.parse(project.getDueDate())))
                                .hideAfter(Duration.seconds(6))
                                .position(Pos.TOP_RIGHT)
                                .showWarning();
                    });
                    String message = "Project Due: " + project.getName() +
                            "Due at " + formatLocalDateTime(LocalDateTime.parse(project.getDueDate()));
                    Notification n = new Notification(generateId("Notifications"), activity.toString(),
                            method.toString(), null, project.getId(), dt.toString(), message);
                    updateNotifications(n);
                    Activity a = new Activity(message, dt.toString(), project.getId(), null);
                    updateActivityLog(a);
                }
            }
        }else{
            // 2) Native Windows toast (runs Powershell - works offline)
            // Fire-and-forget on another thread so we don't block the scheduler
            switch(activity){
                case New_project_created -> {
                    CompletableFuture.runAsync(() -> {
                        WindowsToastUtil.showToast("New project, " + project.getName(),
                                "created at " + dt);
                    });
                    String message = "New project, " + project.getName() + "created" +
                            "at " + dt;
                    Notification n = new Notification(generateId("Notifications"), activity.toString(),
                            method.toString(), null, project.getId(), dt.toString(), message);
                    updateNotifications(n);
                    Activity a = new Activity(message, dt.toString(), project.getId(), null);
                    updateActivityLog(a);
                }
                case Project_updated -> {
                    CompletableFuture.runAsync(() -> {
                        WindowsToastUtil.showToast("Project, " + project.getName(),
                                "updated at " + dt);
                    });
                    String message = "Project, " + project.getName() + "updated" +
                            "at " + dt;
                    Notification n = new Notification(generateId("Notifications"), activity.toString(),
                            method.toString(), null, project.getId(), dt.toString(), message);
                    updateNotifications(n);
                    Activity a = new Activity(message, dt.toString(), project.getId(), null);
                    updateActivityLog(a);
                }
                case Project_deleted -> {
                    CompletableFuture.runAsync(() -> {
                        WindowsToastUtil.showToast("Project, " + project.getName(),
                                "deleted at " + dt);
                    });
                    String message = "Project, " + project.getName() + "deleted" +
                            "at " + dt;
                    Notification n = new Notification(generateId("Notifications"), activity.toString(),
                            method.toString(), null, project.getId(), dt.toString(), message);
                    updateNotifications(n);
                    Activity a = new Activity(message, dt.toString(), project.getId(), null);
                    updateActivityLog(a);
                }
                case Project_due -> {
                    CompletableFuture.runAsync(() -> {
                        WindowsToastUtil.showToast("Project Due: " + project.getName(),
                                "Due at " + formatLocalDateTime(LocalDateTime.parse(project.getDueDate())));
                    });
                    String message = "Project Due: " + project.getName() +
                            "Due at " + formatLocalDateTime(LocalDateTime.parse(project.getDueDate()));
                    Notification n = new Notification(generateId("Notifications"), activity.toString(),
                            "in-app", null, project.getId(), dt.toString(), message);
                    updateNotifications(n);
                    Activity a = new Activity(message, dt.toString(), project.getId(), null);
                    updateActivityLog(a);
                }
            }
        }

    }

    public void sendErrorNotification(String message){
        LocalDateTime dt = LocalDateTime.now();
        Platform.runLater(() -> {
            Notifications.create()
                    .title("An error occurred")
                    .text(message)
                    .hideAfter(Duration.seconds(6))
                    .position(Pos.TOP_RIGHT)
                    .showError();
        });
        String log = ActivityType.An_error_occurred.toString() + ", " + message;
        Activity a = new Activity(log, dt.toString(), null, null);
        updateActivityLog(a);
    }

    public void sendUserProfileNot(String message){
        LocalDateTime dt = LocalDateTime.now();
        Platform.runLater(() -> {
            Notifications.create()
                    .title("User profile updated")
                    .text(message)
                    .hideAfter(Duration.seconds(6))
                    .position(Pos.TOP_RIGHT)
                    .showInformation();
        });
        String log = ActivityType.UserProfile_updated.toString() + ", " + message;
        Activity a = new Activity(log, dt.toString(), null, null);
        updateActivityLog(a);
    }

    public List<Notification> getNotifications(){
        String sql = "SELECT * FROM Notifications ORDER BY ID DESC";
        List<Notification> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.connect();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Notification(
                        rs.getString("ID"),
                        rs.getString("TaskID"),
                        rs.getString("Message"),
                        rs.getString("ProjectID"),
                        rs.getString("Activity"),
                        rs.getString("TimeStamp"),
                        rs.getString("Method")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    private void updateNotifications(Notification n){
        final Connection c = DatabaseConnection.connect();
        String sql = "INSERT INTO Notifications (ID, TaskID, ProjectID, Activity, TimeStamp," +
                "Message, Method) VALUES(?,?,?,?,?,?,?)";
        try (c;
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, n.getId());
            ps.setString(2, n.getTaskId());
            ps.setString(3, n.getProjectId());
            ps.setString(4, n.getActivity());
            ps.setString(5, n.getTimeStamp());
            ps.setString(6, n.getMessage());
            ps.setString(7, n.getMethod());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Activity> getActivityLog (){
        String sql = "SELECT * FROM ActivityLog";
        List<Activity> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.connect();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Activity(
                        rs.getString("Activity"),
                        rs.getString("DateTime"),
                        rs.getString("ProjectID"),
                        rs.getString("TaskId")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    private void updateActivityLog(Activity a){
        final Connection c = DatabaseConnection.connect();
        String sql = "INSERT INTO ActivityLog (Activity, DateTime, TaskId, ProjectId) VALUES(?,?,?,?)";
        try (c;
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getActivity());
            ps.setString(2, a.getDateTime());
            ps.setString(3, a.getTaskId());
            ps.setString(4, a.getProjectId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String formatLocalDateTime(LocalDateTime dt) {
        return dt.toString().replace('T', ' '); // simple formatting; replace with DateTimeFormatter if desired
    }

}