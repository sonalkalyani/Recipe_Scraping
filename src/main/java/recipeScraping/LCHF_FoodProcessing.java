package recipeScraping;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class LCHF_FoodProcessing {

	private static ArrayList<recipeObjLCHF> recipes = new ArrayList<>();
	private static ArrayList<String> CategoriesToKeep = new ArrayList<>();
	static ArrayList<String> recipeCategoryXL = new ArrayList<>();
	static ArrayList<String> foodCategoryXL = new ArrayList<>();
	static ArrayList<String> tags = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		System.out.println(recipes_LCHF_FoodCategories());
	}

	public static ArrayList<recipeObjLCHF> recipes_LCHF_FoodCategories() throws IOException {

		ArrayList<recipeObjLCHF> LCHF_List = LCHF_Recipes.recipesList_LCHF();
		ArrayList<recipeObjLCHF> LCHF_Food_Cat_List = LCHF_Recipes.recipesList_LCHF();

		int RecipesCount = 0;

		String fileName = "C:\\Users\\balbi\\git\\Recipe_Scraping\\Ingredients.xlsx";
		CategoriesToKeep = Get_IngredientsList.get_EliminateList_LCHF(fileName, 3);
		for (int i = 1; i <= LCHF_List.size() - 1; i++) {
			System.out.println("PrepMethod is              " + LCHF_List.get(i).getPrepMethod());
			System.out.println("Getting inside the loop for page number:  " + i);
			// CHECK IF RECIPE HAS prep method keywords FROM Selected food category
			// otherwise skip it
			try {
				if (hasFoodCategories(LCHF_List.get(i).getPrepMethod()) == false) {
					{
						System.out.println("Ignoring this recipe             --------------");
						continue;
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			RecipesCount++;
			System.out.println("RecipesCount          ->           "  + RecipesCount);
			LCHF_Food_Cat_List.add(LCHF_List.get(i));
		}
		return LCHF_Food_Cat_List;
	}

	// Make sure the final recipes contain atleast one item from this list
	public static boolean hasFoodCategories(String prepMethodStr) {
		for (String foodCat : CategoriesToKeep) {
			if (prepMethodStr.contains(foodCat)) {
				System.out.println("Category ----->             " + foodCat);
				return true;
			}
		}
		return false;
	}
}
