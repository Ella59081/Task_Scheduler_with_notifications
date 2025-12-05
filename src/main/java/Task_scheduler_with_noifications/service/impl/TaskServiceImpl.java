package Task_scheduler_with_noifications.service.impl;

import Task_scheduler_with_noifications.config.DatabaseConnection;
import Task_scheduler_with_noifications.enums.ActivityType;
import Task_scheduler_with_noifications.enums.Method;
import Task_scheduler_with_noifications.models.Task;
import Task_scheduler_with_noifications.notifications.NotificationsManager;
import Task_scheduler_with_noifications.service.TaskService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskServiceImpl implements TaskService {
//    static final Connection c = DatabaseConnection.connect();
    public NotificationsManager ns = new NotificationsManager(this);
    @Override
    public void addTask(Task task) {
        String sql = "INSERT INTO Tasks(ID, Name, Description, ProjectId, " +
                "dueDate, Status, createdAt, updatedAt, completedAt) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, task.getId());
            ps.setString(2, task.getName());
            ps.setString(3, task.getDescription());
            ps.setString(4, task.getProjectId());
            ps.setString(5, task.getDueDate());
            ps.setString(6, task.getStatus());
            ps.setString(7, task.getCreatedAt());
            ps.setString(8, task.getUpdatedAt());
            ps.setString(9, task.getCompletedAt());
            ps.executeUpdate();
            ns.sendNotifications(task, Method.in_app , ActivityType.New_task_created);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTask(Task t) {
        String sql = "UPDATE Tasks SET Name = ?, Description = ?, ProjectId = ?, dueDate = ?, Status = ?, "+
                " createdAt = ?, updatedAt = ?, completedAt = ? WHERE ID = ?";
        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getId());
            ps.setString(2, t.getName());
            ps.setString(3, t.getDescription());
            ps.setString(4, t.getProjectId());
            ps.setString(5, t.getDueDate());
            ps.setString(6, t.getStatus());
            ps.setString(7, t.getCreatedAt());
            ps.setString(8, t.getUpdatedAt());
            ps.setString(9, t.getCompletedAt());
            ps.executeUpdate();
            ns.sendNotifications(t, Method.in_app, ActivityType.Task_updated);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTask(String id) {
        String sql = "DELETE FROM Tasks WHERE ID = ?";
        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Task> getAllTasks() {
        String sql = "SELECT * FROM Tasks ORDER BY ID DESC";
        List<Task> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.connect();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Task(
                        rs.getString("ID"),
                        rs.getString("Name"),
                        rs.getString("Description"),
                        rs.getString("ProjectId"),
                        rs.getString("dueDate"),
                        rs.getString("Status"),
                        rs.getString("createdAt"),
                        rs.getString("updatedAt"),
                        rs.getString("completedAt")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Task getSingleTask(String id) {
        String sql = "SELECT ID, Name, Description, ProjectId, dueDate, Status, " +
                " createdAt, updatedAt, completedAt FROM Tasks WHERE ID = ?";
        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Task(
                            rs.getString("ID"),
                            rs.getString("Name"),
                            rs.getString("Description"),
                            rs.getString("ProjectId"),
                            rs.getString("dueDate"),
                            rs.getString("Status"),
                            rs.getString("createdAt"),
                            rs.getString("updatedAt"),
                            rs.getString("completedAt")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;

    }
}
