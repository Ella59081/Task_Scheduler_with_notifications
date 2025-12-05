package Task_scheduler_with_noifications.service.impl;

import Task_scheduler_with_noifications.config.DatabaseConnection;
import Task_scheduler_with_noifications.enums.ActivityType;
import Task_scheduler_with_noifications.enums.Method;
import Task_scheduler_with_noifications.models.Project;
import Task_scheduler_with_noifications.notifications.NotificationsManager;
import Task_scheduler_with_noifications.service.ProjectService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectServiceImpl implements ProjectService {
//    static final Connection c = DatabaseConnection.connect();
    public NotificationsManager ns = new NotificationsManager(this);
    @Override
    public void addProject(Project project) {
        String sql = "INSERT INTO Projects (ID, Name, Description, " +
                "dueDate, Status, createdAt, updatedAt, completedAt) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, project.getId());
            ps.setString(2, project.getName());
            ps.setString(3, project.getDescription());
            ps.setString(4, project.getDueDate());
            ps.setString(5, project.getStatus());
            ps.setString(6, project.getCreatedAt());
            ps.setString(7, project.getUpdatedAt());
            ps.setString(8, project.getCompletedAt());
            ps.executeUpdate();
            ns.sendProjectNot(project, Method.in_app , ActivityType.New_task_created);
        } catch (SQLException e) {
            String message = "Couldn't create project " + project.getName() + " " + e.getMessage();
            ns.sendErrorNotification(message);
            throw new RuntimeException(e);

        }
    }

    @Override
    public void updateProject(Project p) {
        String sql = "UPDATE Projects SET Name = ?, Description = ?, dueDate = ?, Status = ?,"+
                " createdAt = ?, updatedAt = ?, completedAt = ? WHERE ID = ?";
        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getDescription());
            ps.setString(4, p.getDueDate());
            ps.setString(5, p.getStatus());
            ps.setString(6, p.getCreatedAt());
            ps.setString(7, p.getUpdatedAt());
            ps.setString(8, p.getCompletedAt());
            ps.executeUpdate();
            ns.sendProjectNot(p, Method.in_app , ActivityType.Project_updated);
        } catch (SQLException e) {
            String message = "Couldn't update project " + p.getName() + " " + e.getMessage();
            ns.sendErrorNotification(message);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteProject(String id) {
        String sql = "DELETE FROM Projects WHERE ID = ?";
        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Project> getAllProjects() {
        String sql = "SELECT * FROM Projects ORDER BY ID DESC";
        List<Project> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.connect();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Project(
                        rs.getString("ID"),
                        rs.getString("Name"),
                        rs.getString("Description"),
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
    public Project getSingleProject(String id) {
        String sql = "SELECT ID, Name, Description, dueDate, Status, " +
                " createdAt, updatedAt, completedAt FROM Projects WHERE ID = ?";
        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Project(
                            rs.getString("ID"),
                            rs.getString("Name"),
                            rs.getString("Description"),
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
