package dbtests;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import dbmanager.ConnectToDatabase;
import recipeScraping.LFV_OptionalRecipes;
import recipeScraping.recipeObjDrinks;

public class InsertRecordsInDB_LfvOptional {

	LFV_OptionalRecipes optrecipes = new LFV_OptionalRecipes();
	ConnectToDatabase dbconnection = new ConnectToDatabase();
	Connection conn;

	public void InsertData() throws IOException {

		conn = dbconnection.ConnectToDb();
		String sql = "INSERT INTO lfv_optionalrecipes (recipe_id, recipe_name, ingredients, prep_method, URL) VALUES (?, ?, ?, ?, ?)";

		try {

			PreparedStatement statement = null;
			ArrayList<recipeObjDrinks> obj = LFV_OptionalRecipes.optionalRecipesList();
			conn.setAutoCommit(false);
			statement = conn.prepareStatement(sql);

			for (recipeObjDrinks optrec : obj) {
				try {
					statement.setString(1, optrec.getID());
					statement.setString(2, optrec.getName());
					statement.setString(3, optrec.getIngredients());
					statement.setString(4, optrec.getPrepMethod());
					statement.setString(5, optrec.getlink());

					statement.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			conn.commit();
			System.out.println("Data inserted completed successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (conn != null) {
					// Rollback in case of exception
					conn.rollback();
					System.out.println("Transaction rolled back.");
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {

		InsertRecordsInDB_LfvOptional dbTest = new InsertRecordsInDB_LfvOptional();
		dbTest.InsertData();
	}
}