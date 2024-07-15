package recipeScraping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LFV_FullVegan_Test_Pagination {
	
	private static ArrayList<recipeObj> recipes = new ArrayList<>();
	private static ArrayList<String> eliminateList = new ArrayList<>();

	static String foodCategory;
	static String recipeCategory;
	static ArrayList<String> recipeCategoryXL = new ArrayList<>();
	static ArrayList<String> foodCategoryXL = new ArrayList<>();
	static ArrayList<String> cuisineCategoryXL = new ArrayList<>();
	
	static ArrayList<String> tags = new ArrayList<>();	
	static int numServings;
	static Set<String> cuisineCategory = new HashSet<String>();
	static String description;
	static ArrayList<String> nutrients = new ArrayList<>();
	
	public static void main(String[] args) throws IOException {
		
		String sampleUrl = "https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=A&pageindex=";
		String baseUrl = sampleUrl+"1";
		//String baseUrl = "https://www.tarladalal.com/RecipeAtoZ.aspx";
		Document document = Jsoup.connect(baseUrl).timeout(10 * 1000).get();
		//Elements recipesList = document.select( ".rcc_recipecard");
		//Elements pageList = document.select( ".respglink");
		//int pageCount =pageList.size();
		//System.out.println("number of pages: " + pageCount);
		int pageCount=0;
		for(char alphabet = 'A'; alphabet<='Z'; alphabet++) {
			System.out.println("At page ####  :  " +alphabet );
			
			String url_part1 = "https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=";
			String url_part2 = "&pageindex=";
			String url_AZ = url_part1+alphabet+url_part2+"1";
			Document AZ_Doc = Jsoup.connect(url_AZ).get();
			//Elements pageList = AZ_Doc.select( ".respglink");
			Elements pageList = AZ_Doc.select("#maincontent > div:nth-child(1) > div:nth-child(2) a");
			//#maincontent > div > div:nth-child(2) >
			pageCount = Integer.parseInt(pageList.last().text()); 
			System.out.println("number of pages: "+pageList.last().text());   
			//System.out.println("number of pages: " + pageCount);
			
			for(int page =1; page <= pageCount; page++) {
				System.out.println("Getting inside the loop for page number:  " +page );
				
				String url = url_part1+alphabet+url_part2+page;
			
				//String url = sampleUrl+page;
				//String url = baseUrl+"?pageindex="+page;
				Document currentDoc = Jsoup.connect(url).get();
				Elements recipesList = currentDoc.select( ".rcc_recipecard");
				for(Element rc:recipesList) {
					//System.out.println("*****************");
					//String rc_name = (rc.select("div > span > a").text());
					String rc_name = (rc.select("div.rcc_recipecard span.rcc_recipename a").text());
					//linksList = document.select("div.rcc_recipecard span.rcc_recipename a");
					System.out.println("Recipe Name " + rc_name);
					
					
				}
			}
		}
	}

}
