package recipeScraping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LFV_OptionalRecipesNoSugar {
	private static ArrayList<String> eliminateList = new ArrayList<>();
	private static ArrayList<recipeObjDrinks> recipesDrink = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		ArrayList<recipeObjDrinks> obj = optionalRecipesList();
		System.out.println(obj);
	}

	public static ArrayList<recipeObjDrinks> optionalRecipesList() throws IOException {
		ArrayList<String> links = new ArrayList<>();
		ArrayList<String> ids = new ArrayList<>();

		String fileName = System.getProperty("user.dir")+"\\Ingredients.xlsx";
		eliminateList = Get_IngredientsList.get_EliminateList(fileName, 1);

		String sampleUrl = "https://www.tarladalal.com/recipes-for-indian-beverages-indian-drinks-141";
		String url_part2 = "?pageindex=";
		Document document = Jsoup.connect(sampleUrl + url_part2 + 1).timeout(10 * 1000).get();
		int pageCount = 0;
		int ExtractedRecipesCount = 0;

		Elements pageList = document.select("#maincontent > div:nth-child(1) > div:nth-child(2) a");
		pageCount = Integer.parseInt(pageList.last().text());
		System.out.println("number of pages: " + pageList.last().text());

		for (int page = 1; page <= pageCount; page++) {
			System.out.println("Getting inside the loop for page number:  " + page);

			String url = sampleUrl + url_part2 + page;
			Document currentDoc = Jsoup.connect(url).get();
			Elements linksList = null;

			try {
				currentDoc = Jsoup.connect(url).timeout(20 * 1000).get();
				Elements recipesList = currentDoc.select("article[itemprop='itemListElement']"); // 24
				// RECIPE LINKS
				linksList = currentDoc.select("article[itemprop='itemListElement'] span[itemprop='name'] a");
			
				links.clear();
				for (Element link : linksList) {
					links.add(link.attr("abs:href"));
			
				}

			

				// RECIPE IDS
				for (Element rc : recipesList) {
					// System.out.println("*****************");
					String rc_name = (rc.select("article[itemprop='itemListElement'] div > span > a").text());
					// System.out.println("------"+rc_name);
					// String recipeId = rc.select("div.rcc_rcpno").attr("id");

					String recipeId = rc.select("article[itemprop='itemListElement']").attr("id");

					ids.add(recipeId.substring(3));
				}

				int i = 0;
				int k = 0;
				ArrayList<String> recipesFinal = new ArrayList<>();
				ArrayList<String> recipesWithoutSugar = new ArrayList<>();
				// loop through the links to access the data for each recipe in a single page
				for (i = 0; i < links.size(); i++) {
					String link = links.get(i);
					Document r_page = null;
					try {
						r_page = Jsoup.connect(link).get();
					} catch (HttpStatusException e) {
						if (e.getStatusCode() == 404)
							continue;
					}
					// RECIPE PREP METHOD
					ArrayList<String> prepMethod = new ArrayList<>();

					Element prepMethod_div = r_page.select("#recipe_small_steps").first();
					String prepMethodStr = "";
					Elements prepMethod_text = prepMethod_div.select("[itemprop = text]");

					for (Element e : prepMethod_text) {
						prepMethod.add(e.text());
					}

					// INGREDIENTS
					ArrayList<String> ingredients = new ArrayList<>();
					Element ingredients_div = r_page.select("div#rcpinglist").first();
					// get the list of ingredients which are in bold
					assert ingredients_div != null;

					Elements ingredients_links = ingredients_div.select("a");

					for (Element e : ingredients_links) {
						ingredients.add(e.text());

					}

					// System.out.println(ingredients);

					if (optionalRecipes(ingredients)) {
						// recipesWithoutSugar.add(link);
						// System.out.println("Sugar is
						// true----------------------------------------------------");
						continue;

					} else {
						k++;
					}

					// CHECK IF RECIPE HAS ITEMS FROM ELIMINATED LIST
					if (hasEliminatedItems(ingredients)) {
						System.out.println("Inside hasEliminatedItems method-----------");
						continue;
					}

					// NAME
					String name = "";
					name = Objects.requireNonNull(r_page.select("#ctl00_cntrightpanel_lblRecipeName")).text();
					// System.out.println("Recipe Name -----------> " + name);
					prepMethodStr = String.join(",", prepMethod);
					String ingredientsFinal = String.join(",", ingredients);
					recipesDrink.add(new recipeObjDrinks(ids.get(i), name, ingredientsFinal, prepMethodStr, link));

				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		System.out.println("Extracted Recipes Count" +ExtractedRecipesCount);
		return recipesDrink;
	}

	// HAS ELIMINATED ITEMS FUNCTION
	public static boolean hasEliminatedItems(ArrayList<String> ingredients) throws IOException {

		for (String str : ingredients) {
			for (String str2 : eliminateList) {
				if (str2.contains(str)) {
					return true;
				}
			}
		}

		return false;
	}

	// HAS ELIMINATED ITEMS FUNCTION
	public static boolean optionalRecipes(ArrayList<String> ingredients) throws IOException {

		for (String str : ingredients) {
			if (str.contains("sugar")) {
				return true;
			}
		}

		return false;
	}

}
