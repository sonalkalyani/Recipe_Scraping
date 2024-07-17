package dbmanager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import recipeScraping.LFV_OptionalRecipes;
import recipeScraping.recipeObjDrinks;

public class InsertRecordsInDB_LfvOptional {

    // Example method to insert data into database
    public void InsertData(ArrayList<recipeObjDrinks> obj, String dburl, String user, String password) {
        String sql = "INSERT INTO lfv_optionalrecipes (recipe_id, recipe_name, ingredients, prep_method, URL) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement statement = null;

        try {
            conn = DriverManager.getConnection(dburl, user, password);
            conn.setAutoCommit(false); // Disable auto-commit for batch processing

            statement = conn.prepareStatement(sql);

            for (recipeObjDrinks optrec : obj) {
                try {
                    statement.setString(1, optrec.getID()); // Assuming ID is a String
                    statement.setString(2, optrec.getName());
                    statement.setString(3, optrec.getIngredients());
                    statement.setString(4, optrec.getPrepMethod());
                    statement.setString(5, optrec.getlink());

                    statement.executeUpdate();
                } catch (SQLException e) {
                    // Handle specific SQL exceptions as needed
                    e.printStackTrace();
                }
            }

            conn.commit(); // Commit the transaction after all insertions are done
            System.out.println("Data inserted completed successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback in case of exception
                    System.out.println("Transaction rolled back.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ArrayList<recipeObjDrinks> obj = LFV_OptionalRecipes.optionalRecipesList();
        String dburl = "jdbc:postgresql://localhost:5433/postgres";
        String user = "postgres";
        String password = "demo123";

        InsertRecordsInDB_LfvOptional dbTest = new InsertRecordsInDB_LfvOptional();
        dbTest.InsertData(obj, dburl, user, password);
    }
}