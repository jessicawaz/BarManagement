package database.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class Database {

	private static Connection connection;
	private static int USER_ID;
	private static final boolean DEBUG_SQL = false;
	private static final HashSet<String> foodCategories = new HashSet<>();
	private static final HashSet<String> drinkCategories = new HashSet<>();
	static {
		foodCategories.add("appetizer");
		foodCategories.add("lunch");
		foodCategories.add("dinner");
		foodCategories.add("dessert");
		drinkCategories.add("water");
		drinkCategories.add("softdrink");
		drinkCategories.add("alcholic");
		drinkCategories.add("other");
	}
	private static PreparedStatement GET_USER = null;
	public static boolean validUser(String username, String password) {
		if(GET_USER == null) {
			try {
				GET_USER = connection.prepareStatement("SELECT UserID from User where UserName = ? and UserPassword = ?;");
			} catch (SQLException e) {
				if(DEBUG_SQL) e.printStackTrace();
			}
		}
		try {
			GET_USER.clearParameters();
			GET_USER.setString(1, username);
			GET_USER.setString(2, password);
			ResultSet r = GET_USER.executeQuery();
			if(r.next()) {
				USER_ID = r.getInt(1);
				return true;
			}
		} catch (SQLException e) {
			if(DEBUG_SQL) e.printStackTrace();
		}
		return false;
	}
	
	private static PreparedStatement GET_ITEMS = null;
	public static List<InventoryItem> getInventoryItems(){
		if(GET_ITEMS == null) {
			try {
				GET_ITEMS = connection.prepareStatement("SELECT InventoryItemID, ItemType, ItemName, ItemQuantityInStock, ItemLowAmt, ItemOrderAmt, FoodCategory, DrinkCategory FROM InventoryItem left join FoodDetail using (InventoryItemID) left join DrinkDetail using (InventoryItemID);");
			} catch (SQLException e) {
				if(DEBUG_SQL) e.printStackTrace();
			}
		}
		ArrayList<InventoryItem> items= new ArrayList<>();
		try {
			ResultSet r = GET_ITEMS.executeQuery();
			while(r.next()) {
				boolean isDrink = r.getString(2).equalsIgnoreCase("drink");
				if(isDrink) {
					items.add(InventoryItem.drinkFromDatabase(r.getInt(1), r.getString(3), r.getInt(4), r.getInt(5), r.getInt(6), r.getString(8)));
				}
				else {
					items.add(InventoryItem.foodFromDatabase(r.getInt(1), r.getString(3), r.getInt(4), r.getInt(5), r.getInt(6), r.getString(7)));
				}
			}
		} catch (SQLException e) {
			//e.printStackTrace();
			return null;
		}
		
		return items;
	}
	private static PreparedStatement GET_ITEM_BY_NAME = null;
	public static InventoryItem getInventoryItemFromName(String name) {
		if(GET_ITEM_BY_NAME == null) {
			try {
				GET_ITEM_BY_NAME = connection.prepareStatement("SELECT InventoryItemID, ItemType, ItemName, ItemQuantityInStock, ItemLowAmt, ItemOrderAmt, FoodCategory, DrinkCategory FROM InventoryItem left join FoodDetail using (InventoryItemID) left join DrinkDetail using (InventoryItemID) WHERE ItemName = ?;");
			} catch (SQLException e) {
				if(DEBUG_SQL) e.printStackTrace();
			}
		}
		
		try {
			GET_ITEM_BY_NAME.clearParameters();
			GET_ITEM_BY_NAME.setString(1, name);
			ResultSet r = GET_ITEM_BY_NAME.executeQuery();
			if(r.next()) {
				boolean isDrink = r.getString(2).equalsIgnoreCase("drink");
				if(isDrink) {
					return InventoryItem.drinkFromDatabase(r.getInt(1), r.getString(3), r.getInt(4), r.getInt(5), r.getInt(6), r.getString(8));
				}
				else {
					return InventoryItem.foodFromDatabase(r.getInt(1), r.getString(3), r.getInt(4), r.getInt(5), r.getInt(6), r.getString(7));
				}
			}
		} catch (SQLException e) {
		}
		
		return null;
	}

	private static PreparedStatement UPDATE_ITEM = null;
	private static PreparedStatement UPDATE_FOOD = null;
	private static PreparedStatement UPDATE_DRINK = null;
	public static boolean saveInventoryItem(InventoryItem i) {
		if(UPDATE_ITEM == null) {
			try {
				UPDATE_ITEM = connection.prepareStatement("UPDATE InventoryItem SET ItemName = ?, ItemQuantityInStock = ?, ItemLowAmt = ?, ItemOrderAmt = ? WHERE InventoryItemID = ?");
			} catch (SQLException e) {
				if(DEBUG_SQL) e.printStackTrace();
			}
		}
		if(UPDATE_FOOD == null) {
			try {
				UPDATE_FOOD = connection.prepareStatement("UPDATE FoodDetail SET FoodCategory = ? WHERE InventoryItemID = ?");
			} catch (SQLException e) {
				if(DEBUG_SQL) e.printStackTrace();
			}
		}
		if(UPDATE_DRINK == null) {
			try {
				UPDATE_DRINK = connection.prepareStatement("UPDATE DrinkDetail SET DrinkCategory = ? WHERE InventoryItemID = ?");
			} catch (SQLException e) {
				if(DEBUG_SQL) e.printStackTrace();
			}
		}
		if(i.dirty) {
			try {
				UPDATE_ITEM.clearParameters();
				UPDATE_ITEM.setString(1, i.getName());
				UPDATE_ITEM.setInt(2, i.getQuantityInStock());
				UPDATE_ITEM.setInt(3, i.getItemLowAmt());
				UPDATE_ITEM.setInt(4, i.getItemOrderAmt());
				UPDATE_ITEM.setInt(5, i.id);
				i.dirty = UPDATE_ITEM.executeUpdate() != 0;
			} catch (SQLException e) {
				if(DEBUG_SQL) e.printStackTrace();
				return false;
			}
		}
		if(i.dirtyCategory) {
			PreparedStatement updateCategory = i.isDrink()? UPDATE_DRINK:UPDATE_FOOD;
			try {
				updateCategory.clearParameters();
				updateCategory.setInt(2, i.id);
				updateCategory.setString(1, i.getCategory());

				i.dirtyCategory = updateCategory.executeUpdate() != 0;
			} catch (SQLException e) {
				if(DEBUG_SQL) e.printStackTrace();
				return false;
			}
		}
		return true; //returns false if there was an error;
	}
	
	
	public static boolean validFoodCategory(String category) {
		return foodCategories.contains(category.toLowerCase());
	}
	public static boolean validDrinkCategory(String category) {
		return drinkCategories.contains(category.toLowerCase());
	}
	
	private static PreparedStatement ADD_FOOD = null;
	public static InventoryItem createFood(String name, String foodCategory, int lowAmount, int orderAmount) {
		if(ADD_FOOD == null) {
			try {
				ADD_FOOD = connection.prepareStatement("CALL addFood(?, ?, ?, ?);");
			} catch (SQLException e) {
				if(DEBUG_SQL) e.printStackTrace();
			}
		}
		if(!validFoodCategory(foodCategory)) {
			throw new IllegalArgumentException(foodCategory + "is not a valid category!");
		}
		
		try {
			ADD_FOOD.clearParameters();
			ADD_FOOD.setString(1, name);
			ADD_FOOD.setString(4, foodCategory);
			ADD_FOOD.setInt(2, lowAmount);
			ADD_FOOD.setInt(3, orderAmount);
			ADD_FOOD.execute();
		}
		catch(SQLException e) {
			if(DEBUG_SQL) e.printStackTrace();
			return null;
		}
		
		return getInventoryItemFromName(name);
	}
	private static PreparedStatement ADD_DRINK = null;
	public static InventoryItem createDrink(String name, String drinkCategory, int lowAmount, int orderAmount) {
		if(ADD_DRINK == null) {
			try {
				ADD_DRINK = connection.prepareStatement("CALL addDrink(?, ?, ?, ?);");
			} catch (SQLException e) {
				if(DEBUG_SQL) e.printStackTrace();
			}
		}
		if(!validDrinkCategory(drinkCategory)) {
			throw new IllegalArgumentException(drinkCategory + "is not a valid category!");
		}
		
		try {
			ADD_DRINK.clearParameters();
			ADD_DRINK.setString(1, name);
			ADD_DRINK.setString(4, drinkCategory);
			ADD_DRINK.setInt(2, lowAmount);
			ADD_DRINK.setInt(3, orderAmount);
			ADD_DRINK.execute();
		}
		catch(SQLException e) {
			if(DEBUG_SQL) e.printStackTrace();
			return null;
		}
		return getInventoryItemFromName(name);
	}
	
	public static void reduceStock(InventoryItem item, int amount) {
		if(amount <= 0) {
			throw new IllegalArgumentException("Cannot reduce stock by number less than 1!");
		}
		item.useStock(amount);
		saveInventoryItem(item);
	}
	
	private static List<InventoryOrder> getOrdersFromQuery(ResultSet r) throws SQLException{
		ArrayList<InventoryOrder> orders = new ArrayList<>();
		while(r.next()) {
			int offset = 5;
			InventoryItem item = null;
			boolean isDrink = r.getString(offset+2).equalsIgnoreCase("drink");
			if(isDrink) {
				item = InventoryItem.drinkFromDatabase(r.getInt(offset+1), r.getString(offset+3), r.getInt(offset+4), r.getInt(offset+5), r.getInt(offset+6), r.getString(offset+8));
			}
			else {
				item = InventoryItem.foodFromDatabase(r.getInt(offset+1), r.getString(offset+3), r.getInt(offset+4), r.getInt(offset+5), r.getInt(offset+6), r.getString(offset+7));
			}
			orders.add(InventoryOrder.fromDB(r.getInt(1), item, r.getString(offset+9), r.getString(offset+10), r.getDouble(2), r.getInt(3), r.getDate(4), r.getDate(5)));
		}
		return orders;
	}
	private static PreparedStatement GET_ORDERS = null;
	public static List<InventoryOrder> getOrders(){
		if(GET_ORDERS == null) {
			try {
				GET_ORDERS = connection.prepareStatement("SELECT OrderID, OrderPricePerUnit, OrderQuantity, OrderDate, CompleteDate, InventoryItemID, ItemType, ItemName, ItemQuantityInStock, ItemLowAmt, ItemOrderAmt, FoodCategory, DrinkCategory, UserFirstName, UserLastName "
						+ "FROM InventoryOrder JOIN InventoryItem USING (InventoryItemID) LEFT JOIN FoodDetail USING (InventoryItemID) LEFT JOIN DrinkDetail USING (InventoryItemID) JOIN User USING (UserID);");
			} catch (SQLException e) {
				if(DEBUG_SQL) e.printStackTrace();
			}
		}
		try {
			ResultSet r = GET_ORDERS.executeQuery();
			List<InventoryOrder> items = getOrdersFromQuery(r);
			return items;
		} catch (SQLException e) {
			if(DEBUG_SQL) e.printStackTrace();
			return null;
		}
		
	}

	private static PreparedStatement GET_INCOMPLETE_ORDERS = null;
	public static List<InventoryOrder> getIncompleteOrders(){
		if(GET_INCOMPLETE_ORDERS == null) {
			try {
				GET_INCOMPLETE_ORDERS = connection.prepareStatement("SELECT OrderID, OrderPricePerUnit, OrderQuantity, OrderDate, CompleteDate, InventoryItemID, ItemType, ItemName, ItemQuantityInStock, ItemLowAmt, ItemOrderAmt, FoodCategory, DrinkCategory, UserFirstName, UserLastName "
						+ "FROM InventoryOrder JOIN InventoryItem USING (InventoryItemID) LEFT JOIN FoodDetail USING (InventoryItemID) LEFT JOIN DrinkDetail USING (InventoryItemID) JOIN User USING (UserID) "
						+ "WHERE CompleteDate IS NULL;");
			} catch (SQLException e) {
				if(DEBUG_SQL) e.printStackTrace();
			}
		}
		try {
			ResultSet r = GET_INCOMPLETE_ORDERS.executeQuery();
			List<InventoryOrder> items = getOrdersFromQuery(r);
			return items;
		} catch (SQLException e) {
			if(DEBUG_SQL) e.printStackTrace();
			return null;
		}
		
	}

	private static PreparedStatement GET_ORDER_BY_ID = null;
	public static InventoryOrder getOrderByID(int id) {
		if(GET_ORDER_BY_ID == null) {
			try {
				GET_ORDER_BY_ID = connection.prepareStatement("SELECT OrderID, OrderPricePerUnit, OrderQuantity, OrderDate, CompleteDate, InventoryItemID, ItemType, ItemName, ItemQuantityInStock, ItemLowAmt, ItemOrderAmt, FoodCategory, DrinkCategory, UserFirstName, UserLastName "
						+ "FROM InventoryOrder JOIN InventoryItem USING (InventoryItemID) LEFT JOIN FoodDetail USING (InventoryItemID) LEFT JOIN DrinkDetail USING (InventoryItemID) JOIN User USING (UserID) "
						+ "WHERE OrderID = ?;");
			} catch (SQLException e) {
				if(DEBUG_SQL) e.printStackTrace();
			}
		}
		try {
			GET_ORDER_BY_ID.clearParameters();
			GET_ORDER_BY_ID.setInt(1, id);
			ResultSet r = GET_ORDER_BY_ID.executeQuery();
			List<InventoryOrder> items = getOrdersFromQuery(r);
			if(items.isEmpty()) {
				return null;
			}
			return items.get(0);
		} catch (SQLException e) {
			if(DEBUG_SQL) e.printStackTrace();
			return null;
		}
	}
	
	private static PreparedStatement ADD_ORDER = null;
	public static InventoryOrder createOrder(InventoryItem item, double pricePerUnit, int amount, Date date) {
		if(ADD_ORDER == null) {
			try {
				ADD_ORDER = connection.prepareStatement("INSERT INTO InventoryOrder (OrderDate, OrderPricePerUnit, OrderQuantity, CompleteDate, InventoryItemID, UserID) "
						+ "values(?, ?, ?, null, ?, ?);", Statement.RETURN_GENERATED_KEYS);
			} catch (SQLException e) {
				if(DEBUG_SQL) e.printStackTrace();
			}
		}
		try {
			ADD_ORDER.clearParameters();
			ADD_ORDER.setDate(1, new java.sql.Date(date.getTime()));
			ADD_ORDER.setDouble(2, pricePerUnit);
			ADD_ORDER.setInt(3, amount);
			ADD_ORDER.setInt(4, item.id);
			ADD_ORDER.setInt(5, USER_ID);
			ADD_ORDER.executeUpdate();
			ResultSet r = ADD_ORDER.getGeneratedKeys();
			if(r.next()) {
				int id = r.getInt(1);
				return getOrderByID(id);
			}
		} catch (SQLException e) {
			if(DEBUG_SQL) e.printStackTrace();
			return null;
		}
		
		return null;
	}
	public static InventoryOrder createOrder(InventoryItem item, double pricePerUnit, int amount) {
		return createOrder(item, pricePerUnit, amount, new Date());
	}

	private static PreparedStatement MARK_COMPLETE = null;
	public static boolean completeOrder(InventoryOrder o, Date date) {
		if(MARK_COMPLETE == null) {
			try {
				MARK_COMPLETE = connection.prepareStatement("UPDATE InventoryOrder SET CompleteDate = ? WHERE OrderID = ?;");
			} catch (SQLException e) {
				if(DEBUG_SQL) e.printStackTrace();
			}
		}
		o.complete(date); 
		if(o.dirty) {
			try {
				MARK_COMPLETE.clearParameters();
				MARK_COMPLETE.setDate(1, new java.sql.Date(o.getCompleteDate().getTime()));
				MARK_COMPLETE.setInt(2, o.id);
				if(MARK_COMPLETE.executeUpdate() != 0) {
					return saveInventoryItem(o.getItem());
				}
			} catch (SQLException e) {
				if(DEBUG_SQL) e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	public static boolean completeOrder(InventoryOrder o) {
		return completeOrder(o, new Date());
	}

	
	public static boolean connect() {

		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/barmgmt";
			String username = "Java";
			String password = "java";
			Class.forName(driver);
			connection = DriverManager.getConnection(url, username, password);
			return true;
		}
		catch(Exception e) {
			if(DEBUG_SQL) e.printStackTrace();
		}
		return false;
	}

	public static void disconnect() {
		try {
			connection.close();
		} catch (SQLException e) {
			if(DEBUG_SQL) e.printStackTrace();
		}
		finally {
			connection = null;
		}
		
	}

}
