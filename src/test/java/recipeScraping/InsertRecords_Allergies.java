package dbtests;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import dbmanager.ConnectToDatabase;
import recipeScraping.Allergies;
import recipeScraping.LFV_PartialVeganPagination;
import recipeScraping.recipeObj1;
import recipeScraping.recipeObjDrinks;

public class InsertRecords_Allergies {

	Allergies allergiesobj = new Allergies();
	ConnectToDatabase dbconnection = new ConnectToDatabase();
	Connection conn;

	public void InsertToAllergies() throws IOException {

		conn = dbconnection.ConnectToDb();

		String sql = "INSERT INTO allergies_nuts (recipe_id, recipe_name, ingredients, prep_method, URL) "
				+ "VALUES (?, ?, ?, ?, ?)";
		try {
			ArrayList<String> allergens = new ArrayList<>();
			allergens.add("pecans");
			ArrayList<recipeObjDrinks> obj1 = Allergies.recipesList(allergens);

			PreparedStatement statement = conn.prepareStatement(sql);
			for (recipeObjDrinks allergiesobj : obj1) {
				try {
					statement.setString(1, allergiesobj.getID());
					statement.setString(2, allergiesobj.getName());
					statement.setString(3, allergiesobj.getIngredients());
					statement.setString(4, allergiesobj.getPrepMethod());
					statement.setString(5, allergiesobj.getlink());

					statement.executeUpdate();

				}

				catch (SQLException e) {

					if (e.getSQLState().equals(allergiesobj.getID())) { // Check for duplicate key violation

						System.out.println("Duplicate key error for recipe_id: " + allergiesobj.getID());
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

		InsertRecords_Allergies allergyrecipes = new InsertRecords_Allergies();

		allergyrecipes.InsertToAllergies();
	}
}
