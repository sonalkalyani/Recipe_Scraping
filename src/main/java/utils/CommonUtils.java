package utils;

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
	
	// ***********************************Page Actions/Methods*****************************************/
	
	public void doClickOnLink() {
		recipeAToZLink.click();
	}

	public boolean recipesToBeAvoided(ArrayList<String> prepMethod){

	List<String> recipesToAvoidList = Arrays.asList(new String[] { "fried food", "microwave","ready meals", "chips", "crackers"});

			for (String str : prepMethod) {
			for (String str2 : recipesToAvoidList) {
				if (str.contains(str2)) {   //milk==milk
					System.out.println("-----------String matches-------");
					return true;
				}
				
				
			}
//	 for (String str : prepMethod) {
//	for(String str2 : recipesToAvoidList)
//	{
//		System.out.println(str);
//		System.out.println(str2);
//		 if (str.contains(str2)) {
//	            System.out.println("matches");
//	        } else {
//	            System.out.println("do nothing");
//	        }
//		 
//	 }
//	 }
	
			

	}
			
return  false;
			
}

}
