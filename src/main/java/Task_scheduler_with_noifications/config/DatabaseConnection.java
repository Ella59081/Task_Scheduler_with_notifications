package Task_scheduler_with_noifications.config;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection connect() {
        Connection conn = null;
        try {
            // SQLite connection string
            String home = System.getProperty("user.home");
            String folderPath = home + "/Documents/TaskScheduler";
            File folder = new File(folderPath);
            if(!folder.exists()){
                folder.mkdirs();
            }
            String url = "jdbc:sqlite:" + folderPath + "/app.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Connected to SQLite at :" + url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}

