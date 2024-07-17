
package recipeScraping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

	public class LFV_FullVegan {
	

		
		private static ArrayList<String> FV_Avoid_Ingredients = new ArrayList<>();
		
		
		public static void main(String[] args) throws IOException {
			System.out.println(recipes_FullVegan());
		}

		public static ArrayList<recipeObj1> recipes_FullVegan() throws IOException {
			
			ArrayList<recipeObj1> FullVegan_List = new ArrayList<>();
			ArrayList<recipeObj1> PartialVegan_List = LFV_PartialVeganPagination.recipesList();
			
			int RecipesCount = 0;
			
			FV_Avoid_Ingredients.add("butter");
			FV_Avoid_Ingredients.add("ghee");
			FV_Avoid_Ingredients.add("salmon");
			FV_Avoid_Ingredients.add("mackerel");
			FV_Avoid_Ingredients.add("sardines");
			
			for (int i = 1; i <= PartialVegan_List.size() - 1; i++) {
				System.out.println("PartialVegan Ingredients list:      " + PartialVegan_List.get(i).getIngredients());
				System.out.println("Getting inside the loop for page number:  " + i);
				// CHECK IF RECIPE HAS prep method keywords FROM Selected food category
				// otherwise skip it
				try {
					if (hasFV_Ingredients(PartialVegan_List.get(i).getIngredients()) == false) {
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
				FullVegan_List.add(PartialVegan_List.get(i));
				
		}
			return FullVegan_List;
		
		
		}
		
		//comparing partial eliminate list with full vegan eliminate list
		public static boolean hasFV_Ingredients(String Ingredients) {
			for (String FV_Avoid : FV_Avoid_Ingredients) {
				if (Ingredients.contains(FV_Avoid)) {
					System.out.println("Ingredient Found:     ----->             " + FV_Avoid);
					return true;
				}
			}
			return false;
		}
	}
			
				
					
