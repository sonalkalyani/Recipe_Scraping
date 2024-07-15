package tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import base.BaseTest;
import recipeScraping.recipeObj;
import utils.CommonUtils;

public class LFV_RecipesToAvoid extends BaseTest {

	CommonUtils commonUtils;
	ArrayList<String> tags = new ArrayList<>();
	ArrayList<recipeObj> recipes = new ArrayList<>();
	static String foodCategory;
	static String recipeCategory;
	static ArrayList<String> recipeCategoryXL = new ArrayList<>();
	static ArrayList<String> foodCategoryXL = new ArrayList<>();
	static ArrayList<String> cuisineCategoryXL = new ArrayList<>();
	int numServings;
	String cuisineCategory;
	String description;
	ArrayList<String> nutrients = new ArrayList<>();

	@Test
	public void extractOptionalRecipes() throws InterruptedException, IOException {

		commonUtils = new CommonUtils(driver);
		commonUtils.doClickOnLink();
		ArrayList<String> recipeIDsList = new ArrayList<>();

		driver.navigate().to("https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=S&pageindex=62");
//		https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=S&pageindex=62

		// list of WebElements that store all the links
		List<WebElement> raw_recipes = driver.findElements(By.xpath("//div[@class='rcc_recipecard']"));

		// arraylist to store all the links in string
		List<String> links = new ArrayList<>();

		for (WebElement e : raw_recipes) {
			// .findElement -----> finds the tag <a> inside the current WebElement
			// .getAttribute ----> returns the href attribute of the <a> tag in the current
			// WebElement
			links.add(e.findElement(By.tagName("a")).getAttribute("href"));
		}
		// System.out.println(links);
		List<WebElement> recipeIDs = driver.findElements((By.xpath("//span[contains(text(),'Recipe#')]")));
		

		for (WebElement e : recipeIDs) {
			String recipeIDText = e.getText().substring(0);
			String recipeIDTextFinal = recipeIDText.substring(0, recipeIDText.indexOf("\n"));
			System.out.println(recipeIDTextFinal);
			recipeIDsList.add(recipeIDTextFinal);
		}

		int i = 0;
		ArrayList<String> recipesFinal = new ArrayList<>();
		ArrayList<String> recipesAvoid = new ArrayList<>();
		// loop through the links to access the data for each recipe in a single page
		for (i = 0; i < links.size(); i++) {
			String link = links.get(i);
		//	System.out.println("The link is:" +link);
			// System.out.println(link);
			// get the DOM in HTML format of the recipe from the link
			Document document = null;
			try {
				document = Jsoup.connect(link).get();
			} catch (HttpStatusException e) {
				if (e.getStatusCode() == 404)
					continue;
			}

			// RECIPE PREP METHOD
			ArrayList<String> prepMethod = new ArrayList<>();
			Element prepMethod_div = document.select("#recipe_small_steps").first();
			Elements prepMethod_text = prepMethod_div.select("[itemprop = text]");

			for (Element e : prepMethod_text) {
				prepMethod.add(e.text());
			}

		//	System.out.println(prepMethod);

			if (commonUtils.recipesToBeAvoided(prepMethod)) {
				recipesAvoid.add(link);
				System.out.println("=====================================================");
				continue;
			} else {
               
				recipesFinal.add(link);
			}
			// Getting ingredients first extra computation is not done on stuff that'll be
			// filtered out

			// ArrayList to store each ingredient in string format
			ArrayList<String> ingredients = new ArrayList<>();

			// get the specific area that deals with the ingredients
			Element ingredients_div = document.select("div#rcpinglist").first();
			// get the list of ingredients which are blue (links), made an executive
			// decision to screw the rest of them >:(
			assert ingredients_div != null;
			Elements ingredients_links = ingredients_div.select("a");

			// loop to add the ingredients received into the arraylist in string format
			for (Element e : ingredients_links) {
				ingredients.add(e.text());
			}
			if (commonUtils.eliminatedItemsList(ingredients)) {
				continue;
			}

			// NAME
			String name = Objects.requireNonNull(document.select("#ctl00_cntrightpanel_lblRecipeName")).text();
			System.out.println("Recipe Name ----------->      " + name);

			// PREP AND COOK TIME
			int prepTime;
			int cookTime;
			try {
				prepTime = Integer.parseInt(
						document.select("[itemprop=prepTime]").first().text().replaceAll("[^\\d]", "").strip());
				System.out.println("Prep time " + prepTime);
				cookTime = Integer.parseInt(
						document.select("[itemprop=cookTime]").first().text().replaceAll("[^\\d]", "").strip());
				System.out.println("Cook time " + cookTime);

			} catch (Exception e) {
				prepTime = 0;
				cookTime = 0;
			}

			// TAGS
			Elements tagsList = document.select("div.tags a span");
			for (Element ele : tagsList) {
				System.out.println(ele.text());
				tags.add(ele.text());
			}
		
	//		  recipes.add(new recipeObj(recipeIDs.get(i), name, ingredients, prepTime,cookTime, prepMethod, link));

			}
		System.out.println("Size of the final list of the recipes is:" + recipesFinal.size());
		System.out.println(recipesFinal);

		System.out.println("Size of the recipes that are avoided is:" + recipesAvoid.size());
		System.out.println(recipesAvoid);
           		}
	 
    


	}
