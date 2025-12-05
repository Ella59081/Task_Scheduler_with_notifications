package Task_scheduler_with_noifications.config;
import java.sql.*;

public class generateTableId {

    public static void main(String[] args) {
        generateId("Tasks");
    }
    public static String generateId(String table) {
        String result = "";
        String pf = "";
//        final Connection c = DatabaseConnection.connect();
        String sql = "SELECT ID FROM " + table + " ORDER BY CAST" +
                "(SUBSTRING(ID, 3) AS UNSIGNED) DESC LIMIT 1";

        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            {
                switch (table){
                    case "Tasks" -> pf = "TS";
                    case "Projects" -> pf = "PJ";
                    case "Notifications" -> pf = "NT";
                    default -> {
                        System.out.println("Invalid table name");
                        return null;
                    }
                }
            }
            if(rs.next()){
                String lastId = rs.getString("ID");
                int number = Integer.parseInt(lastId.substring(2));
                number++;
                int incId = Integer.parseInt(Integer.toString(number));
                result = pf + "0" + incId;

            }else{
                result = pf + "01";
            }
//            System.out.println(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;

    }
}
