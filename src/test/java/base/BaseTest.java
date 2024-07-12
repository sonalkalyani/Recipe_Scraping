package base;

import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import factory.DriverFactory;
import logger.Log;
import utils.ConfigReader;

public class BaseTest {
	 public static WebDriver driver;
		Properties prop;
	   
	    @BeforeTest
	    public void setUpDriver() {

	    	
	    	prop = ConfigReader.initializeProperties();
			DriverFactory.initializeBrowser(prop.getProperty("browser"));
		    Log.info("Browser Launching");
			driver = DriverFactory.getDriver();
			driver.manage().deleteAllCookies();
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			driver.get(prop.getProperty("url"));
			Log.info("Launching the URL");
	    }
	    
//	    @AfterTest
//	    public void tearDown(){
//	        driver.close();
//	    }
}
