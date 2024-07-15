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
import recipeScraping.LFV_PartialVeganPagination;
import recipeScraping.recipeObj1;

public class InsertRecordsInDB_test {

	private String dburl = "jdbc:postgresql://localhost:5433/postgres";
	private String user = "postgres";
	private String password = "demo123";
	LFV_PartialVeganPagination getrecipes = new LFV_PartialVeganPagination();
	
    public void InsertData() throws IOException {
    	
    	ArrayList<recipeObj1> recipes = new ArrayList();
        
    	String sql = "INSERT INTO recipes (recipe_id, recipe_name, recipe_category, food_category, ingredients, " +
                "prep_time, cook_time, tags, num_servings, cuisine_category, description, prep_method, nutrients, URL) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
       // this.dbconnection = dbconnection;
        
//       PreparedStatement preparedStatement = null;
        Document doc =Jsoup.connect("https://www.tarladalal.com/RecipeAtoZ.aspx").get();
//		Elements body = doc.select(".rcc_recipecard");
		try (Connection conn = DriverManager.getConnection(dburl, user, password);
				
			 PreparedStatement statement = conn.prepareStatement(sql))
		{
			
			if (conn != null) {
	        	
	        	
	            conn.setAutoCommit(false); // Disable auto-commit for batch processing
	            System.out.println("Connected to the PostgreSQL server successfully.");

			} else 

				System.out.println("Failed to connect to the PostgreSQL server.");
			
			for (recipeObj1 recipe : recipes) 
		{
				ArrayList<recipeObj1> rec = getrecipes.getRecipes();
				
			statement.setString(1, recipe.getID());
			statement.setString(2, recipe.getName());
            statement.setString(3, recipe.getRecipeCategory());
            statement.setString(4, recipe.getFoodCategory());
            statement.setString(5, recipe.getIngredients());
            statement.setInt(6, recipe.getPrepTime());
            statement.setInt(7, recipe.getCookTime());
            statement.setString(8, recipe.getTags());
            statement.setString(9, recipe.getNumServings());
            statement.setString(10, recipe.getCuisineCategory());
            statement.setString(11, recipe.getDescription());
            statement.setString(12, recipe.getPrepMethod());
            statement.setString(13, recipe.getNutrients());
            statement.setString(14, recipe.getURL());
					
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new recipe was inserted successfully.");
            }
		}
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   public static void main(String[] args) throws IOException {

	   InsertRecordsInDB_test recipeinsert = new InsertRecordsInDB_test();
	   
	   recipeinsert.InsertData();
}
}