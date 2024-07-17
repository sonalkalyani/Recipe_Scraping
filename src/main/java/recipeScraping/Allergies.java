package recipeScraping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class Allergies {
	// Read partial vegan list
	// A to A, 1 to last page
	// Checking ingd against Partial vegan list - if found eliminate it
	// checking Avoid recipes
	// checking Add list
	private static ArrayList<recipeObjDrinks> recipesAllergens = new ArrayList<>();
	static String description;
	// MAIN METHOD
	public static void main(String[] args) throws Exception {
		ArrayList<String> allergens = new ArrayList<>();
		allergens.add("pecans");
//		allergens.add("walnut");
//		allergens.add("cashew");
//		allergens.add("milk");
	//	allergens.add("cashewnuts");
		System.out.println(recipesList(allergens)); // Result set of recipes with elimiated criteria met, avoid recipes
													// met, Add recipes met
	}
	public static ArrayList<recipeObjDrinks> recipesList(ArrayList<String> allergens) throws IOException {
		ArrayList<String> links = new ArrayList<>();
		ArrayList<String> ids = new ArrayList<>();
		String sampleUrl = "https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=B&pageindex=";
		String baseUrl = sampleUrl + "1";
		Document document = Jsoup.connect(baseUrl).timeout(10 * 1000).get();
		int pageCount = 0;
		int ExtractedRecipesCount = 0;
		// for(char alphabet = 'A'; alphabet<='Z'; alphabet++) {
		for (char alphabet = 'B'; alphabet <= 'B'; alphabet++) { // REMOVE IT
			System.out.println("At page ####  :  " + alphabet);
			String url_part1 = "https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=";
			String url_part2 = "&pageindex=";
			String url_AZ = url_part1 + alphabet + url_part2 + "1";
			Document AZ_Doc = Jsoup.connect(url_AZ).get();
			Elements pageList = AZ_Doc.select("#maincontent > div:nth-child(1) > div:nth-child(2) a");
		//	pageCount = Integer.parseInt(pageList.last().text());
			System.out.println("number of pages: " + pageList.last().text());
			String rc_name = "";
			for (int page = 0; page <= 3; page++) {
				System.out.println("Getting inside the loop for page number:  " + page);
				String url = url_part1 + alphabet + url_part2 + page;
				Document currentDoc = Jsoup.connect(url).get();
				Elements recipesList = currentDoc.select(".rcc_recipecard");
				// STORING RECIPE IDS AND NAMES
				ids.clear();
				links.clear();
				for (Element rc : recipesList) {
					rc_name = (rc.select("div.rcc_recipecard span.rcc_recipename a").text());
					System.out.println("Name is                           --------->" + rc_name);
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
				for (i = 0; i <= links.size() - 1; i++) {
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
					if (ingredients_div != null) {
						Elements ingredients_qty = ingredients_div.select("[itemprop=recipeIngredient]");
						Elements ingredients_links = ingredients_div.select("a");
						for (Element e : ingredients_links) {
							ingredients.add(e.text());
						}
					} else
						System.out.println("This recipe has no ingredients");
					System.out.println(ingredients);
					try {
						if (hasAllergens(ingredients, allergens)) {
							{
								continue;
							}
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					// PREP METHOD
					String prepMethodStr = "";
					ArrayList<String> prepMethod = new ArrayList<>();
					prepMethod.clear();
					Element prepMethod_div = null;
					Elements prepMethod_links = null;
					prepMethod_div = r_page.select("#recipe_small_steps").first();
					if (prepMethod_div != null) {
						prepMethod_links = prepMethod_div.select("[itemprop = text]");
						for (Element e : prepMethod_links) {
							prepMethod.add(e.text());
						}
					} else
						System.out.println("This recipe has no prep method");
					String ingredientsFinal = String.join(",", ingredients);
					ExtractedRecipesCount++;
					System.out.println("Total number of ExtractedRecipesCount ------>>" + ExtractedRecipesCount);
					// NAME
					String name = "";
					name = Objects.requireNonNull(r_page.select("#ctl00_cntrightpanel_lblRecipeName")).text();
					recipesAllergens.add(new recipeObjDrinks(ids.get(i), name, ingredientsFinal, prepMethodStr, link));
				}
			}
		}
		return recipesAllergens;
	}
	
	// HAS ELIMINATED ALLERGENS FUNCTION
	public static boolean hasAllergens(ArrayList<String> Ingredients, ArrayList<String> allergen) throws IOException {
		for (String str : Ingredients) {
			for (String str2 : allergen) {
				if (str.toLowerCase().trim().contains(str2.toLowerCase().trim())) {
					System.out.println("Found allergens");
					return true;
				}
			}
		}
		return false;
	}
}
