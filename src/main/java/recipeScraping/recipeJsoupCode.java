package recipeScraping;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class recipeJsoupCode {
	
//	private ArrayList<recipeObj> recipes;
//	private ArrayList<String> eliminateList;

		public static void main(String[] args) {
			// TODO Auto-generated method stub

			//Remove the eliminate list
			//Insert into the table

			String url = "https://www.tarladalal.com/RecipeAtoZ.aspx";
			try {
				Document document = Jsoup.connect(url).get();
				Elements recipesList = document.select( ".rcc_recipecard");
				System.out.println("{{{{{{{{{{{{{{{{");
				int prepTime;
				int cookTime;
				
				ArrayList<String> links = new ArrayList<>(14)
				
				for(Element rc:recipesList) {
					System.out.println("*****************");
					String rc_name = rc.select("div > span > a").text();
					System.out.println("rc_name " + rc_name);
					
					
					
					try 
					{
						System.out.println("Try block");
						//prepTime = Integer.parseInt(rc.select("[itemprop=prepTime]").first().text().replaceAll("[^\\d]", "").strip());
						prepTime = Integer.parseInt(rc.select("[itemprop=prepTime]").first().text());
						System.out.println(prepTime);
						
						//cookTime = Integer.parseInt(rc.select("[itemprop=cookTime]").first().text().replaceAll("[^\\d]", "").strip());
						//System.out.println("Cook time " + cookTime);
						
					} 
					catch (Exception e) 
					{
						System.out.println("Catch exception "+ e);
						e.printStackTrace();
						prepTime = 0;
						cookTime = 0;
					}
				}
					
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
