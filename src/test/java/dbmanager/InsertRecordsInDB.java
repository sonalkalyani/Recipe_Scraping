package dbmanager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import recipeScraping.ConnectToDatabase;
import recipeScraping.LFV_PartialVegan;
import recipeScraping.recipeObj1;

public class InsertRecordsInDB {

	private String dburl = "jdbc:postgresql://localhost:5433/postgres";
	String user = "postgres";
	String password = "demo123";
	Connection connection;
	private static ArrayList<recipeObj1> recipes = new ArrayList<>();
    public void InsertData() throws IOException {
        
        this.recipes = recipes;
        
        PreparedStatement preparedStatement = null;
        Document doc =Jsoup.connect("https://www.tarladalal.com/RecipeAtoZ.aspx").get();
		Elements body = doc.select(".rcc_recipecard");
		try {
			connection = DriverManager.getConnection(dburl, user, password);
		if (connection != null) {
        	
        	
            connection.setAutoCommit(false); // Disable auto-commit for batch processing
            System.out.println("Connected to the PostgreSQL server successfully.");

		} else {

			System.out.println("Failed to connect to the PostgreSQL server.");
		}
            // Create a prepared statement for inserting records
            String sql = "INSERT INTO demo_recipes (recipe_id) VALUES (?)";
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
    
    public static void main(String args[]) throws IOException {
    	
    	InsertRecordsInDB datainsert = new InsertRecordsInDB();
    	datainsert.InsertData();
    }
}