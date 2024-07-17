package dbmanager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import recipeScraping.ConnectToDatabase;
import recipeScraping.LFV_PartialVeganPagination;
import recipeScraping.recipeObj1;

public class Test{
	
	// Method to extract recipes
	public ArrayList<recipeObj1> extractRecipes() throws InterruptedException, IOException {
	
	LFV_PartialVeganPagination lfvpartialvegan = new LFV_PartialVeganPagination();
    
    ArrayList<recipeObj1> obj1 = new ArrayList<>();
     obj1 = lfvpartialvegan.recipesList();
   
   throw new InterruptedException("Read timeout occurred during extraction");
	}
    
    //Method to insert recipes into database
    public void InsertToLFVPartialVegan(ArrayList<recipeObj1> recipes) throws IOException {
    	ConnectToDatabase dbconnection = new ConnectToDatabase();
    	Connection conn;
    	String sql = "INSERT INTO lfv_partialvegan (recipe_id, recipe_name, recipe_category, food_category, ingredients, prep_method, "
                + "prep_time, cook_time, tags, num_servings, cuisine_category, description, nutrients, URL) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
        	conn = dbconnection.ConnectToDb();	
        	PreparedStatement statement = conn.prepareStatement(sql);
        	conn.setAutoCommit(false);	

        	for (recipeObj1 lfvpartialvegan : recipes) {
        		try {	
        			statement.setString(1, lfvpartialvegan.getID());
        			statement.setString(2, lfvpartialvegan.getName());
        			statement.setString(3, lfvpartialvegan.getRecipeCategory());
        			statement.setString(4, lfvpartialvegan.getFoodCategory());
        			statement.setString(5, lfvpartialvegan.getIngredients());
        			statement.setString(6, lfvpartialvegan.getPrepMethod());
        			statement.setInt(7, lfvpartialvegan.getPrepTime());
        			statement.setInt(8, lfvpartialvegan.getCookTime());
        			statement.setString(9, lfvpartialvegan.getTags());
        			statement.setString(10, lfvpartialvegan.getNumServings());
        			statement.setString(11, lfvpartialvegan.getCuisineCategory());
        			statement.setString(12, lfvpartialvegan.getDescription());
        			statement.setString(13, lfvpartialvegan.getNutrients());
        			statement.setString(14, lfvpartialvegan.getURL());
        			
        			statement.executeUpdate();
        			conn.commit();
        			
        		}
        		
        			catch (SQLException e) {
        			
        		
        				if (e.getSQLState().equals(lfvpartialvegan.getID())) { // Check for duplicate key violation

        					System.out.println("Duplicate key error for recipe_id: " + lfvpartialvegan.getID());
        				}
        				else {
        					throw e;
        				}
        			}
        		}
        		System.out.println("Data Inserted Successfully");
        		
        	}
        	catch (Exception e) {
        		e.printStackTrace();
        	}
    }


    public static void main(String[] args) throws IOException, SQLException {
    	

    	Test recipeinsert = new Test();
    	try {
    		ArrayList<recipeObj1> recipes = recipeinsert.extractRecipes();
    		recipeinsert.InsertToLFVPartialVegan(recipes);
    		
    	}
    	catch (InterruptedException e) {
    		System.out.println("Read timeout occurred during recipe extraction: " + e.getMessage());
            // Proceed with inserting whatever recipes were extracted before the timeout
    		ArrayList<recipeObj1> recipes = new ArrayList<>();
    		recipeinsert.InsertToLFVPartialVegan(recipes);
    		
		}
    }
}
