package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CommonUtils {

	WebDriver driver;

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
	
	
	
}
