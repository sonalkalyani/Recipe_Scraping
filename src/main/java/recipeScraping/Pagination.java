package recipeScraping;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Pagination 
{
	static	int count=0;
	public static void main (String arg[]) throws InterruptedException
	{
		WebDriver driver=new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.tarladalal.com/");
		
		ArrayList<String> receipename = new ArrayList<>();
		ArrayList<Integer> receipID = new ArrayList<>();
		ArrayList<String> indredient = new ArrayList<>();
		ArrayList<String> method_ = new ArrayList<>();
		
		
		WebElement AtoZ=driver.findElement(By.xpath("//a[@title='Recipea A to Z']"));
		AtoZ.click();
		
		
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//		
//		
//		 // Find the table element
//	      WebElement tableElement = driver.findElement(By.tagName("table"));
//	      List<WebElement> rows = tableElement.findElements(By.tagName("tr"));
//
//	      // Iterate through each row
//	      for (WebElement rowElement : rows) {
//	         List<WebElement> cells = rowElement.findElements(By.tagName("td"));
//
//	         // Iterate through each cell in the row
//	         for (WebElement cellElement : cells) {
//	            String cellData = cellElement.getText();
//	            // Process the cell data as needed
//	            System.out.print(cellElement.getText() + "\t");
//	         }
		char letter = 0 ;
		char[]  arr= {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
//		
		for(int i=0;i<=arr.length-1;i++)
		{
			letter=arr[i];
			
		//click on letter P
		//Later on write a logic to iterate through all the letters by itself without hard-coding it
		
		
		//find last page for any letter
		List<WebElement> pagesElements=driver.findElements(By.xpath("//*[@id='maincontent']/div[1]/div[2]/a"));
		int numberOfPageElementsDisplayed = pagesElements.size(); //15
		WebElement lastPageElement = pagesElements.get(numberOfPageElementsDisplayed-1);
		int lastPage = Integer.parseInt(lastPageElement.getText());
		

		
		//Looping through Pages
		//for(int pageNum=1;pageNum<=lastPage;pageNum++)
		for(int pageNum=1;pageNum<=2;pageNum++)
		{
			//https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=A&pageindex=1
			String pageURL = "https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith="+letter+"&pageindex="+pageNum;
			driver.get(pageURL);
			
			//take count of your recipes
			List<WebElement> recipes = driver.findElements(By.xpath("//*[@class='rcc_recipename']/a"));
			int numberOfRecipes = recipes.size(); //14 (not always)
			
			//Loop through recipes in your current page
			//for(int recipeNum=0; recipeNum<numberOfRecipes; recipeNum++) {
			for(int recipeNum=0; recipeNum<2; recipeNum++) {	
				//click on a recipeURL from the list
				recipes.get(recipeNum).click();
				
				//NOW YOU ARE INSIDE AN INDIVIDUAL RECIPE. bEGIN SCRAPPING:
				//=====================================================
				//collect the recipe name
			WebElement recipeName = driver.findElement(By.cssSelector("#ctl00_cntrightpanel_lblRecipeName"));
			count++;
				System.out.println("Recipe Name = " + recipeName.getText());
				
				receipename.add(recipeName.getText());
				
				System.out.println("total scraped recipe count  = " + count);
				
				
				WebElement ingredient=	driver.findElement(By.xpath("//*[@id=\"rcpinglist\"]"));
				WebElement method=	driver.findElement(By.xpath("//*[@id=\"recipe_small_steps\"]"));
				 String rec_id=driver.getCurrentUrl();
					int id=   Integer.parseInt(rec_id.replaceAll("[\\D]", ""));
				
				indredient.add(ingredient.getText());
				method_.add(method.getText());
				receipID.add(id);
			  
			
			System.out.println("id2 ------> "+id);
				
				System.out.println("ingredient ------>"+ingredient.getText());
				System.out.println("method ------>"+method.getText());
				
				//collect recipe url --> driver.getURL()
				
				
				
				//=====================================================
				
				//after collecting all recipe data --> you insert into some table
				
				
				//DONT TOUCH THESE TWO LINES BELOW
				driver.get(pageURL);
				//Re-initializing
				recipes = driver.findElements(By.xpath("//*[@class='rcc_recipename']/a"));
				System.out.println("pageNum = " + pageNum);
			}
			
			
			
			
			
			
			/*
			
			
			System.out.println("==============================================================");
			//WebElement activepage1=driver.findElement(By.xpath("//*[@href="/RecipeAtoZ.aspx?pageindex=[" + p + "]"));
			String pageindex="//*[@href=\"/RecipeAtoZ.aspx?pageindex=[" + p + "]";
			
	WebElement activepage=driver.findElement(By.xpath("//*[@id=\"maincontent\"]/div[1]/div[2]/a[" + p + "]"));
			
			//WebElement activepage=driver.findElement(By.xpath();

	activepage.click();
			
		List<WebElement>recipelinks=driver.findElements(By.xpath("//*[@class=\"rcc_recipename\"]"))	;
		System.out.println("recipelinks size------>"+recipelinks.size());
		
		
		for(int j=1;j<8;j++)
			{
			//WebElement str=	driver.findElement(By.id("rcp37154"));
				//System.out.println("receipe id ------>"+str.getText().toString());
				//System.out.println("receipe name ------>"+recipelinks.get(j).getText());
				
				recipelinks.get(j).click();
			WebElement ingredient=	driver.findElement(By.xpath("//*[@id=\"rcpinglist\"]"));
			WebElement method=	driver.findElement(By.xpath("//*[@id=\"recipe_small_steps\"]"));
			
			
			System.out.println("ingredient ------>"+ingredient.getText());
			System.out.println("method ------>"+method.getText());
			//driver.close();
			
				//Thread.sleep(5000);
				//WebElement ingredient=	driver.findElement(By.xpath("//*[@id=\"rcpinglist\"]"));
				//System.out.println("ingredient ------>"+ingredient.getText().toString());
				
				//WebElement method=	driver.findElement(By.xpath("//*[@itemprop=\"recipeIngredient\"]"));
				//System.out.println("ingredient ------>"+method.getText().toString());
				
			}*/
			
		}
		
		
		
		
		
		
	}
	}
	}
