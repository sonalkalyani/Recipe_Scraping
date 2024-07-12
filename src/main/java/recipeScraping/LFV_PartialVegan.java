package recipeScraping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LFV_PartialVegan {


	private static ArrayList<recipeObj> recipes = new ArrayList<>();
		private static ArrayList<String> eliminateList = new ArrayList<>();

		static String foodCategory;
		static String recipeCategory;
		static ArrayList<String> recipeCategoryXL = new ArrayList<>();
		static ArrayList<String> foodCategoryXL = new ArrayList<>();
		static ArrayList<String> cuisineCategoryXL = new ArrayList<>();
		
		static ArrayList<String> tags = new ArrayList<>();	
		static int numServings;
		static String cuisineCategory;
		static String description;
		static ArrayList<String> nutrients = new ArrayList<>();
		
		//MAIN METHOD
			public static void main(String[] args) {
				ArrayList<String> links = new ArrayList<>();
				ArrayList<String> ids = new ArrayList<>();
				
				
				//Logic
				
				
				//Allowed in partial vegan
				/* Butter
				ghee
				salmon
				mackerel
				sardines */
				
				// HARD CODED
				eliminateList.add("bacon");
				eliminateList.add("Eggs");
				eliminateList.add("Pork");
				eliminateList.add("ham");
				eliminateList.add("butter");
				eliminateList.add("ghee");
				
				
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
						System.out.println("*********************************************************");
						System.out.println("Recipe id ------------>    " + recipeId);
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
						}
						
						//CHECK IF RECIPE HAS ITEMS FROM ELIMINATED LIST
						if (hasEliminatedItems(ingredients)) continue;
						
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
						Elements tagsList = r_page.select("div.tags a span");
						for (Element ele:tagsList) {
							tags.add(ele.text());
						}
						
						//RECIPE CATEGORY
						recipeCategory = "";
						//EXTRACT FROM TAG OR RECIPE NAME
						//HARD CODED
						recipeCategoryXL.add("Breakfast");
						recipeCategoryXL.add("Lunch");
						recipeCategoryXL.add("Snack");
						recipeCategoryXL.add("Dinner");
						
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
						foodCategoryXL.add("Vegan");
						foodCategoryXL.add("Vegetarian");
						foodCategoryXL.add("Jain");
						foodCategoryXL.add("Eggitarian");
						foodCategoryXL.add("Non-veg");
						foodCategory = "";
						for(String tag : tags) {
							if (foodCategoryXL.contains(tag))
							{
								foodCategory = foodCategory + tag;
								System.out.println("food category ------------>  " + foodCategory);
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
						System.out.println("Serving size   -------->   " + numServings );
								
						//CUISINE CATEGORY
						//HARD CODED
						cuisineCategory = "";
						cuisineCategoryXL.add("Indian");
						cuisineCategoryXL.add("South Indian");
						cuisineCategoryXL.add("Rajathani");
						cuisineCategoryXL.add("Punjabi");
						cuisineCategoryXL.add("Kashmiri");
						cuisineCategoryXL.add("North Indian");
						cuisineCategoryXL.add("North Indian");
						
						//Compare above cuisine category master list list with tags and assign value to cuisineCategory 
						// call the function checkTags

//						for(String tag:tags)
//						{
//							String tag_found = checkTags(tag, cuisineCategoryXL);
//							if (tag_found != ""){
//								cuisineCategory = cuisineCategory + "," + tag_found ;
//								System.out.println("Cuisine category of this recipe is " + cuisineCategory);
//								System.out.println("*********************************");
//								break;
//							}
//							else
//								System.out.println("Tag not found " + tag);
//							
//						}
						for(String tag : tags) {
							if (cuisineCategoryXL.contains(tag))
							{
								cuisineCategory = cuisineCategory + tag;
								System.out.println("Cuisine category ---------->       " + cuisineCategory);
								break;
							}
							else {
								//System.out.println("Tag NOT FOUND " + tag);
							}
						}
							
						
						//DESCRIPTION
						Element descriptionEle = r_page.select("[name=description]").first();
						description = descriptionEle.attr("content");
						System.out.println("Description ------------>     " + descriptionEle.attr("content"));
						
						
						//NUTRIENTS
						nutrients.add("Trans Fat: 0");
						nutrients.add("Sodium: 145gm");
						nutrients.add("Protein: 30gm");
						
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
			                        System.out.print(cell.text().trim() + "\t");
			                    }
			                    System.out.println(); // Move to the next line after each row
			                }
			            } else {
			                System.out.println("Table not found on the page.");
			            }
						
						
						
						 //new Recipe -------> calls the *constructor* 
						
						recipes.add(new recipeObj(
								ids.get(i), 
								name, 
								recipeCategory,
								foodCategory,
								ingredients, 
								prepTime, 
								cookTime,
								tags,
								numServings,
								cuisineCategory,
								description,
								prepMethod,
								nutrients,
								link));
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			

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
			
//			//Check if tag exists in the Cuisine Category Master List
//			public static String checkTags(String tag, ArrayList<String> cuisineCategoryXL) {
//			  Iterator<String> it = cuisineCategoryXL.iterator();
//			  while(it.hasNext()){
//			    if(tag.contains(it.next()))
//			       return it.next();
//			  }
//			  return "";
//			}

		
	}
