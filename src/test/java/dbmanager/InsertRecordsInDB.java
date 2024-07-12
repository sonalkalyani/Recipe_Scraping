package dbmanager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class InsertRecordsInDB {

    private static final String URL = "jdbc:postgresql://localhost:5433/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "demo123";

    public static void main(String[] args) throws IOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Document doc =Jsoup.connect("https://www.tarladalal.com/RecipeAtoZ.aspx").timeout(5000).get();
		Elements body = doc.select(".rcc_recipecard");

        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false); // Disable auto-commit for batch processing

            // Create a prepared statement for inserting records
            String sql = "INSERT INTO demo_recipes (id) VALUES (?)";
            preparedStatement = connection.prepareStatement(sql);
            	
            	for (Element recipeid : body) {  			
        			String recid = recipeid.select("div.rcc_recipecard").attr("id");
        			String modifiedRecid = recid.replaceAll("^[a-zA-Z]+", "");
        		     System.out.println(modifiedRecid);
        			preparedStatement.setString(1, modifiedRecid); // Set the value for column1
        			preparedStatement.execute();
        			
                 }

            // Commit the transaction
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback(); // Rollback the transaction on error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // Clean up resources
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}