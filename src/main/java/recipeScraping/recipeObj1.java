package recipeScraping;

import java.util.ArrayList;

public class recipeObj1 {


//		private String ID;
//		private String name;
//		private String recipeCategory; 
//		private String foodCategory;
//		private ArrayList<String> ingredients;
//		private int prepTime;
//		private int cookTime;
//		private ArrayList<String> tags;
//		private int numServings;
//		private String cuisineCategory;
//		private String description;
//		private ArrayList<String> prepMethod;
//		private ArrayList<String> nutrients;
//		private String URL;

	private String ID;
	private String name;
	private String recipeCategory; 
	private String foodCategory;
	private String ingredients;
	private int prepTime;
	private int cookTime;
	private String tags;
	private String numServings;
	private String cuisineCategory;
	private String description;
	private String prepMethod;
	private String nutrients;
	private String URL;
	

		public recipeObj1
		(
				String ID, 
				String name, 
				String recipeCategory, 
				String foodCategory,
				String ingredients, 
				int prepTime, 
				int cookTime, 
				String tags,
				String numServings,
				String cuisineCategory,
				String description,
				String prepMethod,
				String nutrients,
				String URL 
		)
		{
			this.ID = ID;
			this.name = name;
			this.recipeCategory = recipeCategory; 
			this.foodCategory =foodCategory; 
			this.ingredients = ingredients;
			this.prepTime = prepTime;
			this.cookTime = cookTime;
			this.tags = tags;
			this.numServings = numServings;
			this.cuisineCategory =cuisineCategory;
			this.description = description; 
			this.prepMethod = prepMethod;
			this.nutrients = nutrients; 
			this.URL = URL;
		}

		public String getRecipeCategory() {
			return recipeCategory;
		}

		public void setRecipeCategory(String recipeCategory) {
			this.recipeCategory = recipeCategory;
		}

		public String getFoodCategory() {
			return foodCategory;
		}

		public void setFoodCategory(String foodCategory) {
			this.foodCategory = foodCategory;
		}

		public String getTags() {
			return tags;
		}

		public void setTags(String tags) {
			this.tags = tags;
		}

		public String getNumServings() {
			return numServings;
		}

		public void setNumServings(String numServings) {
			this.numServings = numServings;
		}

		public String getCuisineCategory() {
			return cuisineCategory;
		}

		public void setCuisineCategory(String cuisineCategory) {
			this.cuisineCategory = cuisineCategory;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getNutrients() {
			return nutrients;
		}

		public void setNutrients(String nutrients) {
			this.nutrients = nutrients;
		}

		public String getID() {
			return ID;
		}

		public void setID(String ID) {
			this.ID = ID;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getIngredients() {
			return ingredients;
		}

		public void setIngredients(String ingredients) {
			this.ingredients = ingredients;
		}

		public int getPrepTime() {
			return prepTime;
		}

		public void setPrepTime(int prepTime) {
			this.prepTime = prepTime;
		}

		public int getCookTime() {
			return cookTime;
		}

		public void setCookTime(int cookTime) {
			this.cookTime = cookTime;
		}

		public String getPrepMethod() {
			return prepMethod;
		}

		public void setPrepMethod(String prepMethod) {
			this.prepMethod = prepMethod;
		}

		public String getURL() {
			return URL;
		}

		public void setURL(String URL) {
			this.URL = URL;
		}

		
		@Override
		public String toString() {
			return "Recipe id: " + ID + 
					"\nRecipe Name: " + name + 
					"\nRecipe Category:  " + recipeCategory +
					"\nFood Category:  " + foodCategory +
					"\nTags:  " + tags +
					"\nNumber of Servings:  " + numServings +
					"\nCuisine Category:  " + cuisineCategory +
					"\nDescription:  " + description +
					"\nNutrients Value:  " + nutrients +
					"\nURL: " + URL + 
					"\nPrep Time: " + prepTime + 
					" mins\nCook Time: " + cookTime + 
					" mins\nIngredients: " + ingredients.toString() + 
					"\nPreparation Method: \n" + getPrepMethod().toString();
		}
	}
