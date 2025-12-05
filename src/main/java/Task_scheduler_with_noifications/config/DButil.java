package Task_scheduler_with_noifications.config;
import java.sql.*;


public class DButil {
    static final Connection connection = DatabaseConnection.connect();
    public static void initDatabase() {
        String createTasks = "CREATE TABLE IF NOT EXISTS Tasks " +
                "(ID TEXT PRIMARY KEY," +
                "Name TEXT NOT NULL," +
                "Description TEXT," +
                "ProjectId TEXT," +
                "dueDate TEXT NOT NULL," +
                "Status TEXT," +
                "createdAt TEXT," +
                "updatedAt TEXT," +
                "completedAt TEXT);" ;
        String createProjects =
                "CREATE TABLE IF NOT EXISTS Projects " +
                "(ID TEXT PRIMARY KEY," +
                "Name TEXT NOT NULL," +
                "Description TEXT," +
                "dueDate TEXT NOT NULL," +
                "Status TEXT," +
                "createdAt TEXT," +
                "updatedAt TEXT," +
                "completedAt TEXT);";
        String createNot =
                "CREATE TABLE IF NOT EXISTS Notifications " +
                "(ID TEXT PRIMARY KEY," +
                "TaskID TEXT," +
                "ProjectID TEXT," +
                "Activity TEXT," +
                "TimeStamp TEXT," +
                "Message TEXT," +
                "Method TEXT); ";
        String createUserTable =
                "CREATE TABLE IF NOT EXISTS UserProfile " +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "UserName TEXT," +
                "Email TEXT," +
                "ProfilePic TEXT," +
                "PhoneNumber TEXT," +
                "Gender TEXT); " ;
        String createActivityLog =
                "CREATE TABLE IF NOT EXISTS ActivityLog " +
                "(Activity TEXT ," +
                "DateTime TEXT," +
                "TaskId TEXT," +
                "ProjectId TEXT);";
        try (connection;
             Statement st = connection.createStatement()) {
            connection.setAutoCommit(false);
            try{
                st.execute(createTasks);
                st.execute(createProjects);
                st.execute(createNot);
                st.execute(createUserTable);
                st.execute(createActivityLog);
                connection.commit();
                System.out.println("Tables successfully created");
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize DB", e);
        }
    }
}
