package database.data;

import java.util.HashMap;
import java.util.HashSet;

public class InventoryItem {
	
	final int id;
	private final boolean isDrink;
	private String name;
	private int quantityInStock;
	private int itemLowAmt;
	private int itemOrderAmt;
	private String category;
	boolean dirty = false;
	boolean dirtyCategory = false;
	
	private static HashMap<Integer, InventoryItem> cache = new HashMap<>();
	static InventoryItem drinkFromDatabase(int id, String name, int quantityInStock, int itemLowAmt, int itemOrderAmt, String category) {
		return cacheOrCreate(id, true, name, quantityInStock, itemLowAmt, itemOrderAmt, category);
	}
	static InventoryItem foodFromDatabase(int id, String name, int quantityInStock, int itemLowAmt, int itemOrderAmt, String category) {
		return cacheOrCreate(id, false, name, quantityInStock, itemLowAmt, itemOrderAmt, category);
	}
	private static InventoryItem cacheOrCreate(int id, boolean isDrink, String name, int quantityInStock, int itemLowAmt, int itemOrderAmt,
			String category) {
		if(cache.containsKey(id)) {
			InventoryItem i = cache.get(id);
			i.name = name;
			i.quantityInStock = quantityInStock;
			i.itemLowAmt = itemLowAmt;
			i.itemOrderAmt = itemOrderAmt;
			i.category = category;
			return i;
		}
		InventoryItem i = new InventoryItem(id, isDrink, name, quantityInStock, itemLowAmt, itemOrderAmt, category);
		cache.put(id, i);
		return i;
	}
	private InventoryItem(int id, boolean isDrink, String name, int quantityInStock, int itemLowAmt, int itemOrderAmt,
			String category) {
		this.id = id;
		this.isDrink = isDrink;
		this.name = name;
		this.quantityInStock = quantityInStock;
		this.itemLowAmt = itemLowAmt;
		this.itemOrderAmt = itemOrderAmt;
		this.category = category;
	}

	public int hashCode() {
		return this.id;
	}
	public String getName() {
		return name;
	}
	public int getQuantityInStock() {
		return quantityInStock;
	}
	public int getItemLowAmt() {
		return itemLowAmt;
	}
	public int getItemOrderAmt() {
		return itemOrderAmt;
	}
	public String getDrinkcategory() {
		return getCategory();
	}
	public String getFoodcategory() {
		return getCategory();
	}
	String getCategory() {
		return category;
	}
	public boolean isDrink() {
		return isDrink;
	}
	public boolean isFood() {
		return !isDrink;
	}
	
	
	public void setName(String name) {
		this.name = name;
		this.dirty = true;
	}
	public void useStock(int quantityUsed) {
		if(quantityUsed <= 0) {
			throw new IllegalArgumentException("Quantity used must be positive!");
		}
		this.quantityInStock -= quantityUsed;
		this.dirty = true;
	}
	public void setItemLowAmt(int itemLowAmt) {
		if(itemLowAmt < 0) {
			throw new IllegalArgumentException("Item low amount must be positive!");
		}
		this.itemLowAmt = itemLowAmt;
		this.dirty = true;
	}
	public void setItemOrderAmt(int itemOrderAmt) {
		if(itemLowAmt < 0) {
			throw new IllegalArgumentException("Item order amount must be positive!");
		}
		this.itemOrderAmt = itemOrderAmt;
		this.dirty = true;
	}
	public void addStock(int quantityAdded) {
		if(quantityAdded < 0) {
			throw new IllegalArgumentException("Quantity added must be positive!");
		}
		this.quantityInStock += quantityAdded;
		this.dirty = true;
	}
	
	public void setDrinkCategory(String category) {
		if(!Database.validDrinkCategory(category)) {
			throw new IllegalArgumentException("Invalid drink category!");
		}
		if(this.isDrink()) {
			this.setCategory(category);
		}
	}
	public void setFoodCategory(String category) {
		if(!Database.validFoodCategory(category)) {
			throw new IllegalArgumentException("Invalid food category!");
		}
		if(this.isFood()) {
			this.setCategory(category);
		}
	}
	private void setCategory(String category) {
		this.category = category;
		this.dirtyCategory = true;
	}

	

	public String toString() {
		return String.format("Name: %s, Type: %s, Category: %s, Stock: %d", 
				this.getName(), 
				this.isDrink()?"Drink":"Food", 
				this.getCategory(),
				this.getQuantityInStock());
	}
}
