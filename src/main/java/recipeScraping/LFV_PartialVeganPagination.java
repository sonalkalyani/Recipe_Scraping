package recipeScraping;

import java.io.IOException;
import java.io.PrintStream;
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
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;

public class LFV_PartialVeganPagination {

	// Read partial vegan list
	// A to Z, 1 to last page
	// Checking ingd against Partial vegan list - if found eliminate it
	// checking Avoid recipes
	// checking Add list

	private static ArrayList<recipeObj1> recipes = new ArrayList<>();
	private static ArrayList<String> eliminateList = new ArrayList<>();
	private static ArrayList<String> AddToList = new ArrayList<>();
	static String foodCategory;
	static String recipeCategory;
	static ArrayList<String> recipeCategoryXL = new ArrayList<>();
	static ArrayList<String> foodCategoryXL = new ArrayList<>();
	static ArrayList<String> cuisineCategoryXL = new ArrayList<>();
	static ArrayList<String> tags = new ArrayList<>();
	static String numServings;
	static Set<String> cuisineCategory = new HashSet<String>();
	static String description;
	static ArrayList<String> nutrients = new ArrayList<>();

	// MAIN METHOD
	public static void main(String[] args) throws Exception {
		System.out.println(recipesList());
	}

	public static ArrayList<recipeObj1> recipesList() throws IOException {
		int totalRecipes = 0;
		ArrayList<String> links = new ArrayList<>();
		ArrayList<String> ids = new ArrayList<>();

		// remove eliminates + allowed partial - TO BE DONE

		// Read partial vegan
		String fileName = "C:\\Users\\balbi\\git\\Recipe_Scraping\\Ingredients.xlsx";
		eliminateList = Get_IngredientsList.get_EliminateList(fileName, 1);

		String sampleUrl = "https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=A&pageindex=";
		String baseUrl = sampleUrl + "1";
		Document document = Jsoup.connect(baseUrl).timeout(10 * 1000).get();
		int pageCount = 0;
		// for(char alphabet = 'A'; alphabet<='Z'; alphabet++) {
		for (char alphabet = 'A'; alphabet <= 'A'; alphabet++) { // REMOVE IT
			System.out.println("At page ####  :  " + alphabet);
			String url_part1 = "https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=";
			String url_part2 = "&pageindex=";
			String url_AZ = url_part1 + alphabet + url_part2 + "1";
			Document AZ_Doc = Jsoup.connect(url_AZ).get();
			Elements pageList = AZ_Doc.select("#maincontent > div:nth-child(1) > div:nth-child(2) a");
			pageCount = Integer.parseInt(pageList.last().text());
			System.out.println("number of pages: " + pageList.last().text());

			String rc_name ="";
			pageCount = 5; // REMOVE IT
			
			for (int page = 1; page <= pageCount; page++) {
				System.out.println("Getting inside the loop for page number:  " + page);
				String url = url_part1 + alphabet + url_part2 + page;
				Document currentDoc = Jsoup.connect(url).get();
				Elements recipesList = currentDoc.select(".rcc_recipecard");
				// STORING RECIPE IDS AND NAMES

				ids.clear();
				links.clear();
				
				for (Element rc : recipesList) {
					rc_name = (rc.select("div.rcc_recipecard span.rcc_recipename a").text());
					String recipeId = rc.select("div.rcc_recipecard").attr("id");
					ids.add(recipeId.substring(3));
					
				}
				// RECIPE LINKS
				Elements linksList = null;
				linksList = currentDoc.select("div.rcc_recipecard span.rcc_recipename a");
				for (Element link : linksList) {
					links.add(link.attr("abs:href"));
				}

				int i = 0;
				// loop through the links to access the data for each recipe in a single page
				for (i = 0; i < links.size() - 1; i++) {
					String link = links.get(i);
					Document r_page = null;
					try {
						r_page = Jsoup.connect(link).get();
					} catch (HttpStatusException e) {
						if (e.getStatusCode() == 404)
							continue;
					}

					// INGREDIENTS
					ArrayList<String> ingredients = new ArrayList<>();
					ingredients.clear();

					Element ingredients_div = r_page.select("div#rcpinglist").first();
					assert ingredients_div != null;
					Elements ingredients_qty = ingredients_div.select("[itemprop=recipeIngredient]");
					for (Element e : ingredients_qty) {
						ingredients.add(e.text());
					}

					// CHECK IF RECIPE HAS ITEMS FROM ELIMINATED LIST
					
				
					try {

						if (hasEliminatedItems(ingredients)) { // System.out.println(" has elminated is TRUE");
							{
								System.out.println("Inside the has Eliminated looooooooooooooooooooooop");
								continue;
							}
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					String name = "";
					// RECIPE NAME
					name = Objects.requireNonNull(r_page.select("#ctl00_cntrightpanel_lblRecipeName")).text();

					// PREP AND COOK TIME
					int prepTime, cookTime;
					try {
						prepTime = Integer.parseInt(
								r_page.select("[itemprop=prepTime]").first().text().replaceAll("[^\\d]", "").strip());
						cookTime = Integer.parseInt(
								r_page.select("[itemprop=cookTime]").first().text().replaceAll("[^\\d]", "").strip());

					} catch (Exception e) {
						System.out.println(e);
						prepTime = 0;
						cookTime = 0;
					}

					// PREP METHOD
					String prepMethodStr = "";
					ArrayList<String> prepMethod = new ArrayList<>();
					prepMethod.clear();
					
					Element prepMethod_div = null;
					Elements prepMethod_links = null;

					prepMethod_div = r_page.select("#recipe_small_steps").first();
					prepMethod_links = prepMethod_div.select("[itemprop = text]");
					for (Element e : prepMethod_links) {
						prepMethod.add(e.text());
					}

					// Checking if recipe is from the 'Avoid list'
					try {
						if (recipesToBeAvoided(prepMethod)) {
							System.out.println("Avoiding this recipe......................");
							System.out.println("Recipe Name " + rc_name);
							prepMethod.clear();
							continue;
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					prepMethodStr = String.join(",", prepMethod);

					// TAGS
					tags.clear();
					Elements tagsList = r_page.select("div.tags a span");
					for (Element ele : tagsList) {
						tags.add(ele.text());
					}

					// RECIPE CATEGORY
					recipeCategory = "";
					recipeCategoryXL = Get_IngredientsList.get_FoodCategory(fileName, 2);
					for (String tag : tags) {
						if (recipeCategoryXL.contains(tag)) {
							recipeCategory = recipeCategory + tag;
							break;
						}
					}

					// FOOD CATEGORY
					foodCategoryXL = Get_IngredientsList.get_FoodCategory(fileName, 0);
					foodCategory = "";
					for (String tag : tags) {
						if (foodCategoryXL.contains(tag)) {
							foodCategory = foodCategory + tag;
							// System.out.println("food category ------------> " + foodCategory);
							break;
						}
					}
					// NUMBER OF SERVINGS
					numServings = "";
					String str = r_page.select("#ctl00_cntrightpanel_lblServes").text();
					numServings = str;
					cuisineCategoryXL = Get_IngredientsList.get_FoodCategory(fileName, 1);
					cuisineCategory.clear();
					for (String tag : tags) {
						String tag_found = checkTags(tag, cuisineCategoryXL);
						if (tag_found != "") {
							cuisineCategory.add(tag_found);
							continue;
						}
					}

					String cuisineCategoryFinal = "";
					cuisineCategoryFinal = String.join(",", cuisineCategory);

					// DESCRIPTION
					Element descriptionEle = r_page.select("[name=description]").first();
					description = descriptionEle.attr("content");

					// NUTRIENTS
					Element nutrientsList = r_page.selectFirst("table#rcpnutrients");
					String nutrientsString = "";
					if (nutrientsList != null) {
						Elements rows = nutrientsList.select("tr");
						for (Element row : rows) {
							Elements cells = row.select("td");
							for (Element cell : cells) {
								nutrientsString = nutrientsString + " " + cell.text().trim() + " ";
							}
						}
					}

					// Check if the recipe contains at least one item from the ADD list, otherwise
					// ignore it
					AddToList = Get_IngredientsList.get_EliminateList(fileName, 2);
					
					try {

						if (recipesGoodToAdd(ingredients) == false) // None of the ingredients exist in the Add list
						{
							// System.out.println("Inside the Add list continue false ++++++++++++++ ");
							continue;
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					String ingredientsFinal = String.join(",", ingredients);
					String tagsFinal = String.join(",", tags);
					totalRecipes++;

					recipes.add(new recipeObj1(ids.get(i), name, recipeCategory, foodCategory, ingredientsFinal,
							prepTime, cookTime, tagsFinal, numServings, cuisineCategoryFinal, description,
							prepMethodStr, nutrientsString, link));
				}
			}
		}
		System.out.println("Total number of recipes    " + totalRecipes);
		return recipes;
	}

	// HAS ELIMINATED ITEMS FUNCTION
	public static boolean hasEliminatedItems(ArrayList<String> Ingredients) throws IOException {
		for (String str : Ingredients) {
			for (String str2 : eliminateList) {
				if (str2.contains(str)) {
					return true;
				}
			}
		}
		return false;
	}

	// Check if tag exists in the Cuisine Category Master List
	public static String checkTags(String tag, ArrayList<String> cuisineCategoryXL) {
		String tags_found = "";
		String excelVal = "";
		ArrayList<String> tagList = new ArrayList<>(Arrays.asList(tag.split(" ")));
		Iterator<String> it = cuisineCategoryXL.iterator();
		while (it.hasNext()) {
			excelVal = it.next();
			if (tagList.contains(excelVal)) {
				tags_found = excelVal;
			}
		}
		return tags_found;
	}

	// Return arraylist of extracted recipes
	public ArrayList<recipeObj1> getRecipes() {
		return recipes;
	}

	// Print recipes
	public void printRecipes() {
		for (recipeObj1 r : recipes) {
			System.out.println(r);
		}
	}

	// Recipes to avoid
	public static boolean recipesToBeAvoided(ArrayList<String> prepMethod) {
		List<String> recipesToAvoidList = Arrays.asList(new String[] { "fried food", "Fried Food", "deep-fry",
				"Deep-Fried", "Deep Fry", "microwave", "Microwave", "MICROWAVE", "ready meals", "Ready Meal",
				"readymade", "chips", "Chips", "crackers", "Crackers" });
		for (String str : prepMethod) {
			for (String str2 : recipesToAvoidList) {
				if (str.contains(str2)) {
					System.out.println("String matches from prepMethod           " + str);
					System.out.println("String matches from fried food list           " + str2);
					return true;
				}
			}
		}
		return false;
	}

	// Make sure the final recipes contain atleast one item from this list
	public static boolean recipesGoodToAdd(ArrayList<String> Ingredients) {
		for (String ingredient : Ingredients) {
			for (String AddItem : AddToList) {
				if (AddItem.contains(ingredient)) {
					System.out.println("AddItem ----->             " + AddItem);
					System.out.println("ingredient ----->             " + ingredient);
					return true;
				}
			}
		}
		return false;
	}
}
