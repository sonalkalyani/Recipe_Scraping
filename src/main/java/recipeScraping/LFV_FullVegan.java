
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

public class LFV_FullVegan {


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
		
		//MAIN METHOD
			public static void main(String[] args) throws IOException {
				ArrayList<String> links = new ArrayList<>();
				ArrayList<String> ids = new ArrayList<>();
				
				
				//Logic
				//removed eliminates + allowed partial 
				
				//Allowed in partial vegan
				/* Butter
				ghee
				salmon
				mackerel
				sardines */
				
				// HARD CODED
				//integate with Reading Excel
				
//				eliminateList.add("mangoes");

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
//				eliminateList.add("butter");
//				eliminateList.add("ghee");
				
				//reading eliminateList from excel
				
				String fileName = "C:\\Users\\vmman\\git\\Recipe_Scraping\\Ingredients.xlsx";
				eliminateList=Get_IngredientsList.get_EliminateList(fileName, 0);
				
				//System.out.println("eliminate items " + eliminateList );
				
				//WEBSITE LAUNCH
				String url = "https://www.tarladalal.com/RecipeAtoZ.aspx";
				try {
					Document document = Jsoup.connect(url).timeout(10 * 1000).get();
					Elements recipesList = document.select( ".rcc_recipecard");
					
					//RECIPE LINKS
					Elements linksList = document.select("div span a");
					for (Element link:linksList) {
						//System.out.println("link initially " + link.attr("abs:href"));
						links.add(link.attr("abs:href"));
					}
					
					//RECIPE IDS
					for(Element rc:recipesList) {
						//System.out.println("*****************");
						String rc_name = (rc.select("div > span > a").text());
						//System.out.println("Recipe Name " + rc_name);
						String recipeId =  rc.select("div.rcc_recipecard").attr("id");
						//System.out.println("Recipe id ------------>    " + recipeId);
						ids.add(recipeId.substring(3));
					}	
					
					int i = 0;

					// loop through the links to access the data for each recipe in a single page
					for (i = 0; i < links.size()-1; i++) {
						//System.out.println("Number of Links " + links.size());
						
						String link = links.get(i);
						Document r_page = null;
						try 
						{
							r_page = Jsoup.connect(link).get();
						} 
						catch (HttpStatusException e) 
						{
							if (e.getStatusCode() == 404) continue;
						}


						// INGREDIENTS
						ArrayList<String> ingredients = new ArrayList<>();
						Element ingredients_div = r_page.select("div#rcpinglist").first();
						// get the list of ingredients which are in bold
						assert ingredients_div != null;
						Elements ingredients_links = ingredients_div.select("a");

						for (Element e : ingredients_links) {
							ingredients.add(e.text());
							//System.out.println("Ingre list ------------->    " + e.text());
						}
						
						//CHECK IF RECIPE HAS ITEMS FROM ELIMINATED LIST
						if (hasEliminatedItems(ingredients)) 
							{ //System.out.println(" has elminated is TRUE");
							continue;
							}
						
						// NAME
						String name = Objects.requireNonNull(r_page.select("#ctl00_cntrightpanel_lblRecipeName")).text();
						System.out.println("Recipe Name ----------->      " + name);
					
						//PREP AND COOK TIME
						int prepTime;
						int cookTime;
						try {
							prepTime = Integer.parseInt(r_page.select("[itemprop=prepTime]").first().text().replaceAll("[^\\d]", "").strip());
							//System.out.println("Prep time " + prepTime);
							cookTime = Integer.parseInt(r_page.select("[itemprop=cookTime]").first().text().replaceAll("[^\\d]", "").strip());
							//System.out.println("Cook time " + cookTime);
							
						} catch (Exception e) {
							prepTime = 0;
							cookTime = 0;
						}

						
						//PREP METHOD
						ArrayList<String> prepMethod = new ArrayList<>();

						Element prepMethod_div = r_page.select("#recipe_small_steps").first();
						Elements prepMethod_links = prepMethod_div.select("[itemprop = text]");

						for (Element e : prepMethod_links) {
							prepMethod.add(e.text());
						}

						//TAGS
						tags.clear();
						Elements tagsList = r_page.select("div.tags a span");
						for (Element ele:tagsList) {
							tags.add(ele.text());
						}
						
						//RECIPE CATEGORY
						recipeCategory = "";
						//EXTRACT FROM TAG OR RECIPE NAME
						//HARD CODED
//						recipeCategoryXL.add("Breakfast");
//						recipeCategoryXL.add("Lunch");
//						recipeCategoryXL.add("Snack");
//						recipeCategoryXL.add("Dinner");
						
						//reading recipeCategory from excel
						recipeCategoryXL = Get_IngredientsList.get_FoodCategory(fileName, 2);
						//System.out.println("recipeCategory : " +recipeCategoryXL);
						
						for(String tag : tags) {
							if (recipeCategoryXL.contains(tag))
							{
								recipeCategory = recipeCategory + tag;
								System.out.println("Recipe category ----------> " + recipeCategory);
								break;
							}
							else {
								//System.out.println("Tag NOT FOUND " + tag);
							}
						}
						
						//FOOD CATEGORY
						// HARD CODED
//						foodCategoryXL.add("Vegan");
//						foodCategoryXL.add("Vegetarian");
//						foodCategoryXL.add("Jain");
//						foodCategoryXL.add("Eggitarian");
//						foodCategoryXL.add("Non-veg");
						
						//reading foodCategory from excel
						foodCategoryXL = Get_IngredientsList.get_FoodCategory(fileName, 0);
						//System.out.println("foodCategory : " +foodCategoryXL);
						
						foodCategory = "";
						for(String tag : tags) {
							if (foodCategoryXL.contains(tag))
							{
								foodCategory = foodCategory + tag;
								//System.out.println("food category ------------>  " + foodCategory);
								break;
							}
							else {
								//System.out.println("Tag NOT FOUND " + tag);
							}
								
						}
						
						
						//NUMBER OF SERVINGS
						//HARD CODED
						//numServings = 4;
						numServings = Integer.parseInt( r_page.select("[itemprop=recipeYield]").first().text());
						//System.out.println("Serving size   -------->   " + numServings );
								
						//CUISINE CATEGORY
						//HARD CODED
						cuisineCategory.clear();
//						cuisineCategoryXL.add("Indian");
//						cuisineCategoryXL.add("South Indian");
//						cuisineCategoryXL.add("Rajasthani");
//						cuisineCategoryXL.add("Punjabi");
//						cuisineCategoryXL.add("Kashmiri");
//						cuisineCategoryXL.add("North Indian");
						
						//reading cuisineCategory from excel
						cuisineCategoryXL=Get_IngredientsList.get_FoodCategory(fileName, 1);
						//System.out.println("cuisineCategory : " +cuisineCategoryXL);
						
						//Compare above cuisine category master list list with tags and assign value to cuisineCategory 
						// call the function checkTags

						for(String tag:tags)
						{
							String tag_found = checkTags(tag, cuisineCategoryXL);
							if (tag_found != ""){
								//System.out.println("Checking for tag --------> " + tag);
								cuisineCategory.add(tag_found) ;
								continue;
							}
						}
						//System.out.println("Cuisine category ------------->   " + cuisineCategory);
						String cuisineCategoryFinal ="";
						cuisineCategoryFinal = String.join(",", cuisineCategory);
						System.out.println("Cuisine category ------------->   " + cuisineCategoryFinal);
						System.out.println("*******************************************************");
						
						
						//Checking for only the first tag and breaks the loop
						
//						for(String tag : tags) {
//							if (cuisineCategoryXL.contains(tag))
//							{
//								cuisineCategory = cuisineCategory + tag;
//								System.out.println("Cuisine category ---------->       " + cuisineCategory);
//								break;
//							}
//							else {
//								//System.out.println("Tag NOT FOUND " + tag);
//							}
//						}
							
						
						//DESCRIPTION
						Element descriptionEle = r_page.select("[name=description]").first();
						description = descriptionEle.attr("content");
						//System.out.println("Description ------------>     " + descriptionEle.attr("content"));
						
						
						//NUTRIENTS
//						nutrients.add("Trans Fat: 0");
//						nutrients.add("Sodium: 145gm");
//						nutrients.add("Protein: 30gm");
						
						Element nutrientsList = r_page.selectFirst("table#rcpnutrients");
						
						if (nutrientsList != null) {
			                // Select all rows from the table
			                Elements rows = nutrientsList.select("tr");

			                // Iterate over the rows
			                for (Element row : rows) {
			                    // Select all cells in the row
			                    Elements cells = row.select("td");

			                    // Print cell text
			                    for (Element cell : cells) {
			                        //System.out.print(cell.text().trim() + "\t");
			                    }
			                    //System.out.println(); // Move to the next line after each row
			                }
			            } else {
			                System.out.println("Table not found on the page.");
			            }
						
						//What the other ways of writing ingredients
						//How to store it in DB - convert into a string
						
						
						 //new Recipe -------> calls the *constructor* 
						
						
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
//								cuisineCategoryFinal,
//								description,
//								prepMethod,
//								nutrients,
//								link));
				}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			//Check if its working by testing some keywords 

			//HAS ELIMINATED ITEMS FUNCTION
			public static boolean hasEliminatedItems(ArrayList<String> Ingredients) throws IOException {
				for (String ingredient : Ingredients) {
					for (String restrictedItem : eliminateList) {
						if (restrictedItem.contains(ingredient)) {
							System.out.println("Eliminated item ----->    " + restrictedItem);
							return true;
						}
					}
				}
				return  false;
			}
			
			//Check if tag exists in the Cuisine Category Master List
			public static String checkTags(String tag, ArrayList<String> cuisineCategoryXL) 
			{
				String tags_found = "";
				String excelVal ="";
				
				ArrayList<String> tagList = new ArrayList<>(Arrays.asList(tag.split(" ")));
				System.out.println(tagList);
				
				Iterator<String> it = cuisineCategoryXL.iterator();
				  while(it.hasNext()){
					  excelVal = it.next();
					  if(tagList.contains(excelVal))
				    	{
//						  System.out.println("it.next()  ---->   " + excelVal);
						  tags_found =  excelVal;
				    	}
				  }
				  
				  System.out.println("Returning tags -------->   " + tags_found);
				  return tags_found;
				}
			
			}
				
