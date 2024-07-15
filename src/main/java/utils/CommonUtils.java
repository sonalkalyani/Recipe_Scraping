package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CommonUtils {


	WebDriver driver;
	Document document;

	// ***********************************Constructor**************************************************/
	public CommonUtils(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	// ***********************************Locators*****************************************************/

	@FindBy(xpath = "//a[@title='Recipea A to Z']")
	private WebElement recipeAToZLink;

	// ***********************************Page
	// Actions/Methods*****************************************/

	public void doClickOnLink() {
		recipeAToZLink.click();
	}

	public boolean recipesToBeAvoided(ArrayList<String> prepMethod) {

		List<String> recipesToAvoidList = Arrays
				.asList(new String[] { "fried food", "microwave", "ready meals", "chips", "crackers" });

		for (String str : prepMethod) {
			for (String str2 : recipesToAvoidList) {
				if (str.contains(str2)) {
					System.out.println("String matches" + str);
					return true;
				}
			}
		}

		return false;

	}

//	public void hasRecipeCategory(ArrayList<String> tags) {
//		ArrayList<String> recipeCategoryXL = new ArrayList<>();
//	    String recipeCategory = "";
//
//		// EXTRACT FROM TAG OR RECIPE NAME
//		// HARD CODED
//		recipeCategoryXL.add("Breakfast");
//		recipeCategoryXL.add("Lunch");
//		recipeCategoryXL.add("Snack");
//		recipeCategoryXL.add("Dinner");
//
//		for (String tag : tags) {
//			for (String tag2 : recipeCategoryXL) {
//				if (tag.contains(tag2)) {
//					recipeCategory = recipeCategory + tag;
//					System.out.println("Recipe category ----------> " + tag);
//					break;
//				}
//			}
//		}
//
//	
//
//	}

	public boolean eliminatedItemsList(ArrayList<String> ingredients) throws IOException {
		// HARD CODED
		ArrayList<String> eliminateList = new ArrayList<>();
		eliminateList.add("bacon");
		eliminateList.add("Eggs");
		eliminateList.add("Pork");
		eliminateList.add("ham");
		eliminateList.add("butter");
		eliminateList.add("ghee");

		for (String str : ingredients) {
			for (String str2 : eliminateList) {
				if (str2.contains(str)) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean hasFoodCategory(ArrayList<String> tags) {
		String foodCategory;
		ArrayList<String> foodCategoryXL = new ArrayList<>();
		// FOOD CATEGORY
		// HARD CODED
		foodCategoryXL.add("Vegan");
		foodCategoryXL.add("Vegetarian");
		foodCategoryXL.add("Jain");
		foodCategoryXL.add("Eggitarian");
		foodCategoryXL.add("Non-veg");
		foodCategory = "";

		for (String str : tags) {
			for (String str2 : foodCategoryXL) {
				if (str2.contains(str)) {
					foodCategory = foodCategory + str;
					System.out.println("food category ------------>  " + foodCategory);
					break;
				}
			}
		}

			
return  false;
			
}
}
