package Task_scheduler_with_noifications.service.impl;
import Task_scheduler_with_noifications.config.DatabaseConnection;
import Task_scheduler_with_noifications.models.User;
import Task_scheduler_with_noifications.notifications.NotificationsManager;
import Task_scheduler_with_noifications.service.UserService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserServiceImpl implements UserService {

    public NotificationsManager ns = new NotificationsManager();

    @Override
    public void addUser(User user) {
        String sql = "INSERT INTO UserProfile (UserName, Email, ProfilePic," +
                " PhoneNumber, Gender) VALUES(?,?,?,?,?)";
        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getProfilePic());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getGender());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateUserProfile(User user) {
        String sql = "UPDATE UserProfile SET UserName = ?, Email = ?, ProfilePic = ?, " +
                " PhoneNumber = ?, Gender = ? WHERE UserName = ?";
        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getProfilePic());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getGender());
            ps.executeUpdate();
            ns.sendUserProfileNot("User Profile Updated");
        } catch (SQLException e) {
            String message = "Couldn't update user profile " + e.getMessage();
            ns.sendErrorNotification(message);
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUserProfile() {
        String sql = "SELECT UserName, Email, ProfilePic, " +
                " PhoneNumber, Gender FROM UserProfile ";
        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString("UserName"),
                            rs.getString("Email"),
                            rs.getString("ProfilePic"),
                            rs.getString("PhoneNumber"),
                            rs.getString("Gender")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
