package dbmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;

public class ConnectToDatabase {

	private String dburl = "jdbc:postgresql://localhost:5433/postgres";
	String user = "postgres";
	String password = "demo123";

	private Connection conn;

	public Connection ConnectToDb() {
		try {
			conn = DriverManager.getConnection(dburl, user, password);
			if (conn != null) {

				System.out.println("Connected to the PostgreSQL server successfully.");

			} else {

				System.out.println("Failed to connect to the PostgreSQL server.");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
		return conn;
	}

	public static void main(String args[]) {

		ConnectToDatabase connectdb = new ConnectToDatabase();
		connectdb.ConnectToDb();

	}

}
