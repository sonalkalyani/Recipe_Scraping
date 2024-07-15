//package recipeScraping;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class ConnectToDatabase {
//
//	public static void main(String[] args) {
//        // JDBC URL, username and password of PostgreSQL server
//        String url = "jdbc:postgresql://localhost:5433/PostgreSQL 16";
//        String user = "postgres";
//        String password = "demo123";
//
//        // SQL query to execute
//        String query = "SELECT * FROM mytable";
//
//        try (Connection conn = DriverManager.getConnection(url, user, password)) {
//            if (conn != null) {
//                System.out.println("Connected to the PostgreSQL server successfully.");
//                
//                // Execute SQL query
//                // Example: Statement stmt = conn.createStatement();
//                // ResultSet rs = stmt.executeQuery(query);
//                
//                // Perform database operations here
//                // For example, fetch data, update data, etc.
//                
//            } else {
//                System.out.println("Failed to connect to the PostgreSQL server.");
//            }
//        } catch (SQLException e) {
//            System.out.println("SQLException: " + e.getMessage());
//            System.out.println("SQLState: " + e.getSQLState());
//            System.out.println("VendorError: " + e.getErrorCode());
//        }
//    }
//	
//}
