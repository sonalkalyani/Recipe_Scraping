package tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import base.BaseTest;
import utils.CommonUtils;

public class LFV_RecipesToAvoid extends BaseTest {

	CommonUtils commonUtils;

    @Test
	public void extractOptionalRecipes() throws InterruptedException, IOException {
		
		commonUtils = new CommonUtils(driver);
		commonUtils.doClickOnLink();
		

		driver.navigate().to("https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=S&pageindex=62");
		// list of WebElements that store all the links
		List<WebElement> raw_recipes = driver.findElements(By.xpath("//div[@class='rcc_recipecard']"));

		// arraylist to store all the links in string 
		List<String> links = new ArrayList<>();
		
		for (WebElement e : raw_recipes)
		{
			// .findElement -----> finds the tag <a> inside the current WebElement
			// .getAttribute ----> returns the href attribute of the <a> tag in the current WebElement
			 links.add(e.findElement(By.tagName("a")).getAttribute("href"));
		}
//		System.out.println(links);
//		List<WebElement> recipeIDs = driver.findElements((By.xpath("//span[contains(text(),'Recipe#')]")));
//		ArrayList<String> recipeIDsList = new ArrayList<>();
//
//		for (WebElement e : recipeIDs)
//		{
//			String recipeIDText = e.getText().substring(0);
//			String recipeIDTextFinal = recipeIDText.substring(0, recipeIDText.indexOf("\n"));
//			System.out.println(recipeIDTextFinal);
//			recipeIDsList.add(recipeIDTextFinal);
//		}
//		
		int i = 0;
		ArrayList<String> recipesFinal = new ArrayList<>();
		ArrayList<String> recipesAvoid = new ArrayList<>();
		// loop through the links to access the data for each recipe in a single page
		for (i = 0; i < links.size(); i++) {
			String link = links.get(i);
		//	System.out.println(link);
			// get the DOM in HTML format of the recipe from the link
			Document document = null;
			try {
				document = Jsoup.connect(link).get();
			} catch (HttpStatusException e) {
				if (e.getStatusCode() == 404) 
					continue;
			}

			//to get the prep method


				ArrayList<String> prepMethod = new ArrayList<>();
				Element prepMethod_div = document.select("#recipe_small_steps").first();
				Elements prepMethod_links = prepMethod_div.select("[itemprop = text]");

				for (Element e : prepMethod_links) {
					String text = e.text();
				    prepMethod.add(e.text());
				}
		
			//	System.out.println(prepMethod);
				
			//	commonUtils.recipesToBeAvoided(prepMethod);
				if (commonUtils.recipesToBeAvoided(prepMethod))
				{
					recipesAvoid.add(link);
					System.out.println("=====================================================");
					continue;
				}
				else {
					
					recipesFinal.add(link);
				}


		}
		System.out.println("Size of the list is"+recipesFinal.size());
		System.out.println(recipesFinal);	
					
	}
	}


