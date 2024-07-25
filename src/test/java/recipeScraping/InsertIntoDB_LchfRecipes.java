package recipeScraping;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import dbmanager.ConnectToDatabase;
import recipeScraping.LCHF_Recipes;
import recipeScraping.LFV_PartialVeganPagination;
import recipeScraping.recipeObj1;
import recipeScraping.recipeObjLCHF;

public class InsertIntoDB_LchfRecipes {

	LCHF_Recipes lchfrecipes = new LCHF_Recipes();
	ConnectToDatabase dbconnection = new ConnectToDatabase();
	Connection conn;

	public void InsertToLchfRecipes() throws IOException {

		conn = dbconnection.ConnectToDb();

		ArrayList<recipeObjLCHF> obj1 = LCHF_Recipes.recipesList_LCHF();

		String sql = "INSERT INTO lchf_recipes (recipe_id, recipe_name, recipe_category, food_category, ingredients, prep_method, "
				+ "prep_time, cook_time, tags, num_servings, cuisine_category, description, nutrients, URL) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			for (recipeObjLCHF lchfrecipes : obj1) {
				try {
					statement.setString(1, lchfrecipes.getID());
					statement.setString(2, lchfrecipes.getName());
					statement.setString(3, lchfrecipes.getRecipeCategory());
					statement.setString(4, lchfrecipes.getFoodCategory());
					statement.setString(5, lchfrecipes.getIngredients());
					statement.setString(6, lchfrecipes.getPrepMethod());
					statement.setInt(7, lchfrecipes.getPrepTime());
					statement.setInt(8, lchfrecipes.getCookTime());
					statement.setString(9, lchfrecipes.getTags());
					statement.setString(10, lchfrecipes.getNumServings());
					statement.setString(11, lchfrecipes.getCuisineCategory());
					statement.setString(12, lchfrecipes.getDescription());
					statement.setString(13, lchfrecipes.getNutrients());
					statement.setString(14, lchfrecipes.getURL());

					statement.executeUpdate();

				}

				catch (SQLException e) {

					// Check for duplicate key violation
					if (e.getSQLState().equals(lchfrecipes.getID())) {

						System.out.println("Duplicate key error for recipe_id: " + lchfrecipes.getID());
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

		InsertIntoDB_LchfRecipes recipeinsert = new InsertIntoDB_LchfRecipes();

		recipeinsert.InsertToLchfRecipes();
	}
}
