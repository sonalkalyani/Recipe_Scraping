package recipeScraping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LFV_OptionalRecipes {

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
		eliminateList=Get_IngredientsList.get_EliminateList(fileName, 1);
		
		String url = "https://www.tarladalal.com/recipes-for-indian-beverages-indian-drinks-141";
		Elements linksList = null;
		try {
			Document document = Jsoup.connect(url).timeout(20 * 1000).get();
			Elements recipesList = document.select("article[itemprop='itemListElement']"); // 24
			// RECIPE LINKS
			linksList = document.select("article[itemprop='itemListElement'] span[itemprop='name'] a");
			// div.rcc_recipecard span.rcc_recipename a").text());
			links.clear();
			for (Element link : linksList) {
				links.add(link.attr("abs:href"));
				System.out.println(link.attr("abs:href"));
			}

			// System.out.println(links);

			// RECIPE IDS
			for (Element rc : recipesList) {
				// System.out.println("*****************");
				String rc_name = (rc.select("article[itemprop='itemListElement'] div > span > a").text());
				// System.out.println("------"+rc_name);
				// String recipeId = rc.select("div.rcc_rcpno").attr("id");

				String recipeId = rc.select("article[itemprop='itemListElement']").attr("id");

				ids.add(recipeId.substring(3));
			}

			// System.out.println(ids);

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

				// Elements prepMethod_text = prepMethod_div.select("[itemprop = text]");

				// INGREDIENTS
				ArrayList<String> ingredients = new ArrayList<>();
				Element ingredients_div = r_page.select("div#rcpinglist").first();
				// get the list of ingredients which are in bold
				assert ingredients_div != null;
				// Elements ingredients_qty =
				// ingredients_div.select("[itemprop=recipeIngredient]");
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
					// recipesFinal.add(link);
					// System.out.println("No sugar recipe" +link);
				}

				// System.out.println("k is "+k);

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
				// System.out.println("String from prep method ---"+str);
				// System.out.println("String from optionalRecipeList method ---"+str2);
				return true;
			}
		}

		return false;
	}

}
