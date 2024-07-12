package dbmanager;

import java.io.IOException;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class test {
	
	public static void main(String[] args) throws IOException{
		
		Document doc =Jsoup.connect("https://www.tarladalal.com/RecipeAtoZ.aspx").timeout(5000).get();
		Elements body = doc.select(".rcc_recipecard");
		
		for (Element recipeid : body) {
			
			String recid = recipeid.select("div.rcc_recipecard").attr("id");
			String modifiedRecid = recid.replaceAll("^[a-zA-Z]+", "");
			System.out.println(modifiedRecid);
			
		}
		
	}

}
