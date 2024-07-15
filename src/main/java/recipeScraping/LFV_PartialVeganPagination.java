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
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;

public class LFV_PartialVeganPagination {

	// Get a list of recipes which do not incude

	private static ArrayList<recipeObj1> recipes = new ArrayList<>();
	private static ArrayList<String> eliminateList = new ArrayList<>();

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
	public static void main(String[] args) throws IOException 
	{
		ArrayList<String> links = new ArrayList<>();
		ArrayList<String> ids = new ArrayList<>();

		// removed eliminates + allowed partial

		// Allowed in partial vegan
		/* Butter ghee salmon mackerel sardines */
		// reading eliminateList from excel
		String fileName = "/Users/saumdas/git/Recipe_Scraping/Ingredients.xlsx";
		// String fileName = "C:\\Users\\vmman\\git\\Recipe_Scraping\\Ingredients.xlsx";
		eliminateList = Get_IngredientsList.get_EliminateList(fileName, 0);
		System.out.println("Eliminate List      ------->   " + eliminateList );
		
		
		char letter = 0;
		char[] arr = { 'A'};
		//, 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			//	'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		for (int p = 0; p <= arr.length - 1; p++) {
			letter = arr[p];
			String urlMain = "https://www.tarladalal.com/RecipeAtoZ.aspx";
			Document doc = null;
			doc = Jsoup.connect(urlMain).timeout(20 * 1000).get();
			//doc = Jsoup.connect(urlMain).get();
			// Elements pagesElements = doc.select("#maincontent div[1] div[2] a");
			Elements pagesElements = doc.select("#maincontent > div:nth-child(1) > div:nth-child(2) a");
			int numberOfPageElementsDisplayed = pagesElements.size();
			
			System.out.println("numberOfPageElementsDisplayed  -------->   " + numberOfPageElementsDisplayed);
			
			System.out.println("Number of pages in K   ---------" + pagesElements.get(numberOfPageElementsDisplayed-1).text());
			
			
			
			
			Element lastPageElement = pagesElements.get(numberOfPageElementsDisplayed - 1);
			int lastPage = Integer.parseInt(lastPageElement.text());
			
			System.out.println("lastPage    ---------->   " + lastPage);
			// Looping through Pages
			//for (int pageNum = 1; pageNum <= lastPage; pageNum++) {
			for (int pageNum = 1; pageNum <= 2; pageNum++) {
				
				String url = "https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=" + letter + "&pageindex="
						+ pageNum;
				System.out.println("Current page number  ------->     " + pageNum);
				//System.out.println("URL ----> " + url);
				
				// WEBSITE LAUNCH
				try {
					Document document = null;
					document = Jsoup.connect(url).timeout(5 * 1000).get();
					//document = Jsoup.connect(url).get();
					Elements recipesList = null;
					recipesList = document.select(".rcc_recipecard");
					
					// RECIPE LINKS
					System.out.println("Number of recipes on this page --------------" + recipesList.size());
					
					Elements linksList = null;
					links.clear();
					
					ids.clear();
					
					linksList = document.select("div.rcc_recipecard span.rcc_recipename a");
					for (Element link : linksList) {
						links.add(link.attr("abs:href"));
					}
					
					
					// RECIPE IDS
					for (Element rc : recipesList) {
						// System.out.println("*****************");
						//String rc_name = (rc.select("div > span > a").text());
						String recipeId = rc.select("div.rcc_recipecard").attr("id");
						ids.add(recipeId.substring(3));
					}
					int i = 0;
					// loop through the links to access the data for each recipe in a single page
					for (i = 0; i < links.size() - 1; i++) {
						//System.out.println("Value of i ---------- "+ (links.size()-1));
						String link = links.get(i);
						
						//System.out.println("link for r_page   ------>   " + link);
						Document r_page = null;
						try {
							r_page = Jsoup.connect(link).get();
						} catch (HttpStatusException e) {
							if (e.getStatusCode() == 404)
								continue;
						}

						// INGREDIENTS
						ArrayList<String> ingredients = new ArrayList<>();
						Element ingredients_div = r_page.select("div#rcpinglist").first();
						// get the list of ingredients which are in bold
						assert ingredients_div != null;
						Elements ingredients_qty = ingredients_div.select("[itemprop=recipeIngredient]");
						for (Element e : ingredients_qty) {
							ingredients.add(e.text());

						}

						String ingredientsFinal = String.join(",", ingredients);

						// CHECK IF RECIPE HAS ITEMS FROM ELIMINATED LIST
						if (hasEliminatedItems(ingredients)) { // System.out.println(" has elminated is TRUE");
							{
								
							System.out.println("Inside the hasEliminated looooooooooooooooooooooop");
							continue;
							}
						}
						
						
						String name= "";
						
						// RECIPE NAME
						name = Objects.requireNonNull(r_page.select("#ctl00_cntrightpanel_lblRecipeName"))
								.text();
						//System.out.println("Recipe Name ----------->      " + name);

						// PREP AND COOK TIME
						int prepTime;
						int cookTime;
						try {
							prepTime = Integer.parseInt(r_page.select("[itemprop=prepTime]").first().text()
									.replaceAll("[^\\d]", "").strip());
							cookTime = Integer.parseInt(r_page.select("[itemprop=cookTime]").first().text()
									.replaceAll("[^\\d]", "").strip());

						} catch (Exception e) {
							prepTime = 0;
							cookTime = 0;
						}

						// PREP METHOD
						String prepMethodStr = "";
						ArrayList<String> prepMethod = new ArrayList<>();
						Element prepMethod_div = r_page.select("#recipe_small_steps").first();
						Elements prepMethod_links = prepMethod_div.select("[itemprop = text]");
						for (Element e : prepMethod_links) {
							prepMethod.add(e.text());
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
						// EXTRACT FROM TAG OR RECIPE NAME
						recipeCategoryXL = Get_IngredientsList.get_FoodCategory(fileName, 2);
						for (String tag : tags) {
							if (recipeCategoryXL.contains(tag)) {
								recipeCategory = recipeCategory + tag;
								break;
							} else {
								// System.out.println("Tag NOT FOUND " + tag);
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
							} else {
								// System.out.println("Tag NOT FOUND " + tag);
							}
						}
						// NUMBER OF SERVINGS
						numServings="";
						String str = r_page.select("#ctl00_cntrightpanel_lblServes").text();
						// System.out.println(str.substring(str.indexOf("M")));
						numServings = str;

						cuisineCategoryXL = Get_IngredientsList.get_FoodCategory(fileName, 1);
						// System.out.println("cuisineCategory : " +cuisineCategoryXL);

						cuisineCategory.clear();
						// call the function checkTags
						for (String tag : tags) {
							String tag_found = checkTags(tag, cuisineCategoryXL);
							if (tag_found != "") {

								cuisineCategory.add(tag_found);
								continue;
							}
						}

						// System.out.println("Cuisine category -------------> " + cuisineCategory);
						String cuisineCategoryFinal = "";
						cuisineCategoryFinal = String.join(",", cuisineCategory);

						// DESCRIPTION
						Element descriptionEle = r_page.select("[name=description]").first();
						description = descriptionEle.attr("content");

						Element nutrientsList = r_page.selectFirst("table#rcpnutrients");
						
						String nutrientsString="";
						
						if (nutrientsList != null) {
							Elements rows = nutrientsList.select("tr");
							for (Element row : rows) {
								// Select all cells in the row
								Elements cells = row.select("td");

								// Print cell text
								for (Element cell : cells) {
									// System.out.print(cell.text().trim() + "\t");
									nutrientsString = nutrientsString + " " + cell.text().trim() + " ";
											
								}
								// System.out.println(); // Move to the next line after each row
							}
						} else {
							// System.out.println("Table not found on the page.");
						}
						
						
						
						String tagsFinal = String.join(",", tags);
						// How to store it in DB - convert into a string
						// new Recipe -------> calls the *constructor*

						//System.out.println(ids.get(i));
								
								/* + " " + name + " " + recipeCategory + " " + 
								foodCategory + " " + ingredientsFinal + " " + prepTime + " " + 
								cookTime + " " + tagsFinal + " " + numServings + " " + 
								cuisineCategoryFinal + " " + description + " " + prepMethodStr + " " + 
								nutrientsString + " " + link);*/
						
						recipes.add(
								new recipeObj1(
								ids.get(i), 
								name, 
								recipeCategory, 
								foodCategory, 
								ingredientsFinal,
								prepTime, 
								cookTime, 
								tagsFinal, 
								numServings, 
								cuisineCategoryFinal, 
								description,
								prepMethodStr, 
								nutrientsString, 
								link)
								);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// HAS ELIMINATED ITEMS FUNCTION
	public static boolean hasEliminatedItems(ArrayList<String> Ingredients) throws IOException {
		for (String ingredient : Ingredients) {
			for (String restrictedItem : eliminateList) {
				if (restrictedItem.contains(ingredient)) {
					System.out.println("Eliminated item ----->    " + restrictedItem);
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
	public static ArrayList<recipeObj1> getRecipes() {
		return recipes;
	}

	// Print recipes
	public void printRecipes() {
		for (recipeObj1 r : recipes) {
			System.out.println(r);
		}
	}
}
