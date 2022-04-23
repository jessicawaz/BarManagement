package database.data;

public class InventoryItem {
	private final int id = 1;
	private final boolean isDrink;
	private String name;
	private int quantityInStock;
	private int itemLowAmt;
	private int itemOrderAmt;
	private String category;
	
	InventoryItem() {
		this.isDrink = false;
		this.name = "Hamburger";
		this.quantityInStock = 100;
		this.itemLowAmt = 20;
		this.itemOrderAmt = 150;
		this.category = "Dinner";
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getQuantityInStock() {
		return quantityInStock;
	}
	public void useStock(int quantityUsed) {
	}
	
	public int getItemLowAmt() {
		return itemLowAmt;
	}
	public void setItemLowAmt(int itemLowAmt) {
	}
	

	public int getItemOrderAmt() {
		return itemOrderAmt;
	}
	public void setItemOrderAmt(int itemOrderAmt) {
		
	}
	
	public String getDrinkcategory() {
		return getCategory();
	}
	public void setDrinkCategory(String category) {
	}
	
	public String getFoodcategory() {
		return getCategory();
	}
	public void setFoodCategory(String category) {
	}
	
	public boolean isDrink() {
		return isDrink;
	}
	public boolean isFood() {
		return !isDrink;
	}
	
	private String getCategory() {

		return category;
	}
	public String toString() {
		return String.format("Name: %s, Type: %s, Category: %s, Stock: %d", 
				this.getName(), 
				this.isDrink()?"Drink":"Food", 
				this.getCategory(),
				this.getQuantityInStock());
	}
}
