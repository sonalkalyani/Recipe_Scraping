package dbmanager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import recipeScraping.ConnectToDatabase;
import recipeScraping.LFV_OptionalRecipes;
import recipeScraping.LFV_PartialVegan;
import recipeScraping.LFV_PartialVeganPagination;
import recipeScraping.recipeObj1;
import recipeScraping.recipeObjDrinks;

public class Test {

//	private String dburl = "jdbc:postgresql://localhost:5433/postgres";
//	private String user = "postgres";
//	private String password = "demo123";
	//LFV_OptionalRecipes optrec = new LFV_OptionalRecipes();
	ConnectToDatabase dbconnection = new ConnectToDatabase();
	Connection conn;

	public void InsertData() throws IOException, SQLException {

		
		ArrayList<recipeObjDrinks> obj = LFV_OptionalRecipes.optionalRecipesList();

		String sql = "INSERT INTO lfv_optionalrecipes (recipe_id, recipe_name, ingredients,prep_method, URL) VALUES (?, ?, ?, ?, ?)";
		try {
				dbconnection.ConnectToDb();
				PreparedStatement statement = conn.prepareStatement(sql); 


			for (recipeObjDrinks optrec : obj) {

				try {

					statement.setString(1, optrec.getID());
					statement.setString(2, optrec.getName());
					statement.setString(3, optrec.getIngredients());
					statement.setString(4, optrec.getPrepMethod());
					statement.setString(5, optrec.getlink());

//            statement.setInt(6, recipe.getPrepTime());
//            statement.setInt(7, recipe.getCookTime());
//            statement.setString(8, recipe.getTags());
//            statement.setString(9, recipe.getNumServings());
//            statement.setString(10, recipe.getCuisineCategory());
//            statement.setString(11, recipe.getDescription());
//            statement.setString(12, recipe.getPrepMethod());
//            statement.setString(13, recipe.getNutrients());
//            statement.setString(14, recipe.getURL());
					statement.executeUpdate();;
					System.out.println("Data Inserted Successfully");
					conn.commit();
					

				}

				catch (SQLException e) {

					if (e.getSQLState().equals(optrec.getID())) { // Check for duplicate key violation

						System.out.println("Duplicate key error for recipe_id: " + optrec.getID());

					} else {
						throw e;
					}
				}
				
			}
			
		}
		
		finally {
			
		}
	}

	public static void main(String[] args) throws IOException, SQLException {

		InsertRecordsInDB_test recipeinsert = new InsertRecordsInDB_test();

		recipeinsert.InsertData();
	}
}