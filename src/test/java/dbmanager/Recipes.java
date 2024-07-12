//package recipeScraping;
//
//import java.io.IOException;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//public class Recipes {
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//		System.out.println("++++++++++++++++++");
//
//		String url = "https://www.tarladalal.com/RecipeAtoZ.aspx";
//		try {
//			Document document = Jsoup.connect(url).get();
//			System.out.println("==================");
//			Elements recipesList = document.select( ".rcc_recipecard");
//			System.out.println("{{{{{{{{{{{{{{{{");
//			int prepTime;
//			int cookTime;
//			
//			for(Element rc:recipesList) {
//				System.out.println("}}}}}}}}}}}}}}}");
//				String rc_name = rc.select("div > span > a").text();
//				System.out.println("rc_name " +rc_name);
//				
////				try {
////					prepTime = Integer.parseInt(recipesList.select("[itemprop=prepTime]").first().text().replaceAll("[^\\d]", "").strip());
////					cookTime = Integer.parseInt(recipesList.select("[itemprop=cookTime]").first().text().replaceAll("[^\\d]", "").strip());
////				} catch (Exception e) {
////					prepTime = 0;
////					cookTime = 0;
////				}
//			}
//				
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//}
package dbmanager;

