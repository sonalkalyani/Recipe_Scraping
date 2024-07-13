// Call recipesFinal list from RecipesToAvoid class in order to iterate the eliminate list
// Call eliminate list against recipesFinal data set
// Check if Add ingredients exists in the list after elimination






//package recipeScraping;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.Objects;
//
//import org.jsoup.HttpStatusException;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//public class LFV_FullVegan {
//
//			
//		}
//=======
//package recipeScraping;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Objects;
//
//import org.jsoup.HttpStatusException;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//public class LFV_FullVegan {
//
//		
//		private static ArrayList<recipeObj> recipes = new ArrayList<>();
//		private static ArrayList<String> eliminateList = new ArrayList<String>();
//
//		static String foodCategory;
//		static String recipeCategory;
//		static ArrayList<String> tags = new ArrayList<>();	
//		static int numServings;
//		static String cuisineCategory;
//		static String description;
//		static ArrayList<String> nutrients = new ArrayList<>();
//		
//		
//		//MAIN METHOD
//		/////////////////////
//		////////////////////
//			public static void main(String[] args) {
//				ArrayList<String> links = new ArrayList<>();
//				ArrayList<String> ids = new ArrayList<>();
//						
//				
//				
//				eliminateList.add("Butter");
//				eliminateList.add("Eggs");
//				eliminateList.add("Pork");
//				eliminateList.add("ham");
//				
//				System.out.println("eliminate items " + eliminateList );
//				
//				//WEBSITE LAUNCH
//				////////////////////
//				////////////////////
//				
//				String url = "https://www.tarladalal.com/RecipeAtoZ.aspx";
//				try {
//					Document document = Jsoup.connect(url).get();
//					Elements recipesList = document.select( ".rcc_recipecard");
//					
//					//RECIPE LINKS
//					Elements linksList = document.select("div span a");
//					for (Element link:linksList) {
//						System.out.println("link initially " + link.attr("abs:href"));
//						links.add(link.attr("abs:href"));
//					}
//					
//					
//
//					//RECIPE IDS
//					for(Element rc:recipesList) {
//						System.out.println("*****************");
//						String rc_name = (rc.select("div > span > a").text());
//						System.out.println("Recipe Name " + rc_name);
//						String recipeId =  rc.select("div.rcc_recipecard").attr("id");
//						ids.add(recipeId.substring(3));
//					}	
//					
//					int i = 0;
//
//					// loop through the links to access the data for each recipe in a single page
//					for (i = 0; i < links.size(); i++) {
//						String link = links.get(i);
//						Document r_page = null;
//						try 
//						{
//							r_page = Jsoup.connect(link).get();
//						} 
//						catch (HttpStatusException e) 
//						{
//							if (e.getStatusCode() == 404) continue;
//						}
//
//
//						// INGREDIENTS
//						///////////////////
//						///////////////////
//						ArrayList<String> ingredients = new ArrayList<>();
//						Element ingredients_div = r_page.select("div#rcpinglist").first();
//						// get the list of ingredients which are in bold
//						assert ingredients_div != null;
//						Elements ingredients_links = ingredients_div.select("a");
//
//						for (Element e : ingredients_links) {
//							ingredients.add(e.text());
//						}
//						
//						
//						//CHECK IF RECIPE HAS ITEMS FROM ELIMINATED LIST
//						if (hasEliminatedItems(ingredients)) continue;
//
//						
//						// NAME
//						//////////////////////////
//						/////////////////////////
//						String name = Objects.requireNonNull(r_page.select("#ctl00_cntrightpanel_lblRecipeName")).text();
//						System.out.println("name " + name);
//					
//						
//						
//						//PREP AND COOK TIME
//						//////////////////////////
//						/////////////////////////
//						int prepTime;
//						int cookTime;
//						try {
//							prepTime = Integer.parseInt(r_page.select("[itemprop=prepTime]").first().text().replaceAll("[^\\d]", "").strip());
//							System.out.println("Prep time " + prepTime);
//							cookTime = Integer.parseInt(r_page.select("[itemprop=cookTime]").first().text().replaceAll("[^\\d]", "").strip());
//							System.out.println("Cook time " + cookTime);
//							
//						} catch (Exception e) {
//							prepTime = 0;
//							cookTime = 0;
//						}
//
//						
//						
//						//PREP METHOD
//						//////////////////////////
//						/////////////////////////
//						ArrayList<String> prepMethod = new ArrayList<>();
//
//						Element prepMethod_div = r_page.select("#recipe_small_steps").first();
//						Elements prepMethod_links = prepMethod_div.select("[itemprop = text]");
//
//						for (Element e : prepMethod_links) {
//							prepMethod.add(e.text());
//						}
//
//						
//						
//						//RECIPE CATEGORY
//						//////////////////////////
//						/////////////////////////
//						recipeCategory = "Kashmiri";
//						//EXTRACT FROM TAG OR RECIPE NAME
//						
//						//FOOD CATEGORY
//						//////////////////////////
//						/////////////////////////
//						foodCategory = "Lunch";
//
//						
//						//TAGS
//						//////////////////////////
//						/////////////////////////
//						tags.add("lunch");
//						tags.add("Vegan");
//						
//						//NUMBER OF SERVINGS
//						//////////////////////////
//						/////////////////////////
//						numServings = 4;
//						
//						//CUISINE CATEGORY
//						//////////////////////////
//						/////////////////////////
//						cuisineCategory = "Thai";
//						
//						//DESCRIPTION
//						//////////////////////////
//						/////////////////////////
//						description = "Its for LFV recipe for vegans.";
//						
//						
//						//NUTRIENTS
//						//////////////////////////
//						/////////////////////////
//						nutrients.add("Trans Fat: 0");
//						nutrients.add("Sodium: 145gm");
//						nutrients.add("Protein: 30gm");
//						
//						 //new Recipe -------> calls the *constructor* 
//						
//						recipes.add(new recipeObj(
//								ids.get(i), 
//								name, 
//								recipeCategory,
//								foodCategory,
//								ingredients, 
//								prepTime, 
//								cookTime,
//								tags,
//								numServings,
//								cuisineCategory,
//								description,
//								prepMethod,
//								nutrients,
//								link));
//					}
//
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			
//
//			//HAS ELIMINATED ITEMS FUNCTION
//			///////////////////////////////
//			//////////////////////////////
//			public static boolean hasEliminatedItems(ArrayList<String> Ingredients) throws IOException {
//				for (String ingredient : Ingredients) {
//					for (String restrictedItem : eliminateList) {
//						if (restrictedItem.contains(ingredient)) {
//							System.out.println("Eliminated item " + restrictedItem);
//							return true;
//						}
//					}
//				}
//				return  false;
//			}
//	}
//
