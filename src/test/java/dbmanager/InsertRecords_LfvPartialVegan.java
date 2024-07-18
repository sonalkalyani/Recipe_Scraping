package dbmanager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import recipeScraping.ConnectToDatabase;
import recipeScraping.LFV_PartialVeganPagination;
import recipeScraping.recipeObj1;

public class InsertRecords_LfvPartialVegan {

	LFV_PartialVeganPagination lfvpartialvegan = new LFV_PartialVeganPagination();
	ConnectToDatabase dbconnection = new ConnectToDatabase();
	Connection conn;

	public void InsertToLFVPartialVegan() throws IOException {

		conn = dbconnection.ConnectToDb();
		ArrayList<recipeObj1> obj1 = LFV_PartialVeganPagination.recipesList();
		String sql = "INSERT INTO lfv_partialvegan (recipe_id, recipe_name, recipe_category, food_category, ingredients, prep_method, "
				+ "prep_time, cook_time, tags, num_servings, cuisine_category, description, nutrients, URL) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			for (recipeObj1 lfvpartialvegan : obj1) {
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

				}

				catch (SQLException e) {

					// Check for duplicate key violation
					if (e.getSQLState().equals(lfvpartialvegan.getID())) {

						System.out.println("Duplicate key error for recipe_id: " + lfvpartialvegan.getID());
					} else {
						throw e;
					}
				}
			}
			System.out.println("Data Inserted Successfully");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws IOException, SQLException {

		InsertRecords_LfvPartialVegan recipeinsert = new InsertRecords_LfvPartialVegan();

		recipeinsert.InsertToLFVPartialVegan();
	}
}
