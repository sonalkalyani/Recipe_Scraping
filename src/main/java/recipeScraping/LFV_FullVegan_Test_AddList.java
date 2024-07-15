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

public class LFV_FullVegan_Test_AddList {
	
	
	private static ArrayList<recipeObj> recipes = new ArrayList<>();
	private static ArrayList<String> eliminateList = new ArrayList<>();

	static String foodCategory;
	static String recipeCategory;
	static ArrayList<String> recipeCategoryXL = new ArrayList<>();
	static ArrayList<String> foodCategoryXL = new ArrayList<>();
	static ArrayList<String> cuisineCategoryXL = new ArrayList<>();
	
	static ArrayList<String> tags = new ArrayList<>();	
	static int numServings;
	static Set<String> cuisineCategory = new HashSet<String>();
	static String description;
	static ArrayList<String> nutrients = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		
		String fileName = "C:\\Users\\vmman\\git\\Recipe_Scraping\\Ingredients.xlsx";
		eliminateList=Get_IngredientsList.get_EliminateList(fileName, 0);
		
		String url = "https://www.tarladalal.com/RecipeAtoZ.aspx";
		Document document = Jsoup.connect(url).timeout(10 * 1000).get();
		Elements recipesList = document.select( ".rcc_recipecard");
		
		Elements linksList = document.select("div span a");
		
		for(Element rc:recipesList) {
			//System.out.println("*****************");
			String rc_name = (rc.select("div > span > a").text());
			System.out.println("Recipe Name " + rc_name);
			String link = (rc.select("div > span > a").attr("abs:href"));
			//System.out.println("link initially " + link);
			Document r_page = null;
			if(link!="") {
			
			try 
			{
			 r_page = Jsoup.connect(link).get();
			}
			catch (HttpStatusException e) 
			{
				if (e.getStatusCode() == 404) continue;
			}
			
			Element ingredients_div = r_page.select("div#rcpinglist").first();
			assert ingredients_div != null;
			Elements ingredients_links = ingredients_div.select("a");
			ArrayList<String> ingredients = new ArrayList<>();
			for (Element e : ingredients_links) {
				ingredients.add(e.text());
				if (hasEliminatedItems(ingredients)) 
				{ //System.out.println(" has elminated is TRUE");
				//continue;
				}
				else {
					
				    System.out.println("Ingre list ------------->    " + e.text());
				}
			}
		}
			
		}

	}
	public static boolean hasEliminatedItems(ArrayList<String> Ingredients) throws IOException {
		for (String ingredient : Ingredients) {
			for (String restrictedItem : eliminateList) {
				if (restrictedItem.contains(ingredient)) {
					System.out.println("Eliminated item ----->    " + restrictedItem);
					return true;
				}
			}
		}
		return  false;
	}

}
