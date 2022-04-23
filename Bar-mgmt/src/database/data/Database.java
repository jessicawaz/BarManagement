package database.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

public class Database {

	private static Connection connection;
	private static final HashSet<String> foodCategories = new HashSet<>();
	private static final HashSet<String> drinkCategories = new HashSet<>();
	static {
		foodCategories.add("appetizer");
		foodCategories.add("lunch");
		foodCategories.add("dinner");
		foodCategories.add("dessert");
		drinkCategories.add("Water");
		drinkCategories.add("SoftDrink");
		drinkCategories.add("Alcholic");
		drinkCategories.add("Other");
	}
	
	public static boolean validUser(String username, String password) {
		
		return username.equals("bob") && password.equals("bob");
	}
	
	public static List<InventoryItem> getInventoryItems(){
		
		return Arrays.asList(new InventoryItem());
	}
	public static InventoryItem getInventoryItemFromName(String name) {
		if(name.equals("Hamburger")) {
			return new InventoryItem();
		}
		return null;
	}
	public static boolean saveInventoryItem(InventoryItem i) {
		return true; //returns false if there was an error;
	}
	
	
	public static boolean validFoodCategory(String category) {
		return foodCategories.contains(category);
	}
	public static boolean validDrinkCategory(String category) {
		return drinkCategories.contains(category);
	}
	
	public static InventoryItem createFood(String name, String foodCategory, int lowAmount, int orderAmount) {
		if(!validFoodCategory(foodCategory)) {
			throw new IllegalArgumentException(foodCategory + "is not a valid category!");
		}
		return new InventoryItem();
	}
	public static InventoryItem createDrink(String name, String drinkCategory, int lowAmount, int orderAmount) {
		if(!validDrinkCategory(drinkCategory)) {
			throw new IllegalArgumentException(drinkCategory + "is not a valid category!");
		}
		return new InventoryItem();
	}
	
	public static void reduceStock(InventoryItem item, int amount) {
		if(amount <= 0) {
			throw new IllegalArgumentException("Cannot reduce stock by number less than 1!");
		}
	}
	
	public static List<InventoryOrder> getOrders(){
		return Arrays.asList(new InventoryOrder());
	}
	public static List<InventoryOrder> getIncompleteOrders(){
		return Arrays.asList(new InventoryOrder());
	}
	public static InventoryOrder createOrder(InventoryItem item, double pricePerUnit, int amount) {
		return new InventoryOrder();
	}
	public void completeOrder(InventoryOrder i) {
		i.complete(new Date()); 
	}
	
	public static boolean connect() {

		try {
//			String driver = "com.mysql.cj.jdbc.Driver";
//			String url = "jdbc:mysql://localhost:3306/java_example";
//			String username = "Java";
//			String password = "java";
//			Class.forName(driver);
//			connection = DriverManager.getConnection(url, username, password);
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void disconnect() {
		
	}

}
