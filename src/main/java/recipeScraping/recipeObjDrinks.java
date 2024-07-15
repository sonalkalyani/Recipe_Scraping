package recipeScraping;

public class recipeObjDrinks {

	private String ID;
	private String name;
	private String ingredients;
	private String prepMethod;
	private String link;

	public recipeObjDrinks(String ID, String name, String ingredients, String prepMethod, String link) {
		this.ID = ID;
		this.name = name;
		this.ingredients = ingredients;
		this.prepMethod = prepMethod;
		this.link = link;
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

	public String getPrepMethod() {
		return prepMethod;
	}

	public void setPrepMethod(String prepMethod) {
		this.prepMethod = prepMethod;
	}

	public String getlink() {
		return link;
	}

	public void setlink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return "Recipe id: " + ID + "\nRecipe Name: " + name + "\nlink: " + link
				+ " mins\nIngredients: " + ingredients.toString() + "\nPreparation Method: \n"
				+ getPrepMethod().toString();
	}
}
