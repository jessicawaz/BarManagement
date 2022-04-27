package database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import database.data.Database;
import database.data.InventoryItem;
import database.data.InventoryOrder;

public class UI {

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy");
	
	public static void main(String[] args) {
		System.out.println("Connecting to database...");
		if(!Database.connect()) {
			System.out.println("Could not connect.");
			return;
		}
		try
		{
			Scanner s = new Scanner(System.in);
			boolean authenticated = login(s);
			if(authenticated) {
				main:
				while(s.hasNextLine()) {
					String command = s.nextLine();
					switch (command) {
						case "reviewItems":
							reviewInventory(s);
							break;
						case "reviewOrders":
							reviewOrders(s);
							break;
						case "addItem":
							addItem(s);
							break;
						case "modifyItem":
							modifyItem(s);
							break;
						case "reduceStock":
							reduceItem(s);
							break;
						case "createOrder":
							createOrder(s);
							break;
						case "completeOrder":
							completeOrder(s);
							break;
						case "help":
							printHelp();
							break;
						case "logout":
							break main;
						case "testdb":
							testDatabase();
							break;
						default:
							System.out.println("Invalid command: "+ command+" use 'help' to a list of commands.");
							break;
					}
					System.out.println("Please enter another command or enter \"logout\" to exit.");
				}
				s.close();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			System.out.println("Disconnecting from database...");
			Database.disconnect();
			System.out.println("Goodbye");
		}
	}

	private static void printHelp() {
		System.out.println("reviewItems - list all items in inventory.");
		System.out.println("reviewOrders - list all orders for inventory items.");
		System.out.println("addItem - create a new inventory item.");
		System.out.println("modifyItem - update and inventory item's name, lowAmount, orderAmount, and/or category.");
		System.out.println("reduceStock - decrease the amount of inventory in stock.");
		System.out.println("createOrder - create a new order for inventory items.");
		System.out.println("completeOrder - mark an outstanding order as complete, automatically adding it to stock.");
		System.out.println("help - prints this message.");
		System.out.println("logout - disconnect from the database.");
	}

	private static void testDatabase() {
		InventoryItem item = Database.getInventoryItemFromName("Hamburger");
		System.out.println(item);
		item.useStock(10);
		item.setFoodCategory("lunch");
		System.out.println(item);
		Database.saveInventoryItem(item);
		item = Database.getInventoryItemFromName("Hamburger");
		System.out.println(item);
		try {
			InventoryOrder order = Database.createOrder(item, 10.43, 10, DATE_FORMAT.parse("04-12-2020"));
			System.out.println(order);
			Database.completeOrder(order, DATE_FORMAT.parse("04-12-2021"));
			System.out.println(order);
			System.out.println(item);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static boolean login(Scanner s) {
		System.out.println("Enter your username");
		String username = s.nextLine();

		System.out.println("Enter your password");
		String password = s.nextLine();
		
		boolean authenticated = Database.validUser(username, password);
		if(authenticated) {
			System.out.println("Welcome " + username);
		}
		else {
			System.out.println("Invalid credentials.");
			
		}
		return authenticated;
	}
	
	// Implemented
	private static void reviewInventory(Scanner s) {
		for(InventoryItem item: Database.getInventoryItems()) {
			System.out.println(item);
		}
	}
	
	// Implemented
	private static void reviewOrders(Scanner s) {
		for(InventoryOrder order: Database.getOrders()) {
			System.out.println(order);
		}
		
	}
	
	
	private static void addItem(Scanner s) {
		System.out.println("Is the item a drink? (yes/no)");
		String itemType = s.nextLine();
		
		if (itemType.equals("No") || itemType.equals("no")) {
			System.out.println("Enter food name: ");
			String foodName = s.nextLine();
			System.out.println("Enter food category: \n"
					+ "Categories include: appetizer, lunch, dinner, "
					+ "or dessert");
			String foodCategory = s.nextLine();
			System.out.println("Enter low amount: ");
			int lowAmt = s.nextInt();
			System.out.println("Enter order amount: ");
			int orderAmt = s.nextInt();
			s.nextLine();
			System.out.println(Database.createFood(foodName, foodCategory, lowAmt, orderAmt));
		} else if (itemType.equals("Yes") || itemType.equals("yes")) {
			System.out.println("Enter drink name: ");
			String drinkName = s.nextLine();
			System.out.println("Enter drink category: \n"
					+ "Categories include: Water, SoftDrink, Alcoholic, "
					+ "or Other");
			String drinkCategory = s.nextLine();
			System.out.println("Enter low amount: ");
			int lowAmt = s.nextInt();
			System.out.println("Enter order amount: ");
			int orderAmt = s.nextInt();
			s.nextLine();
			System.out.println(Database.createDrink(drinkName, drinkCategory, lowAmt, orderAmt));
		} else {
			System.out.println("Invalid input. Please enter 'yes' if the "
					+ "item is a drink or 'no' if the item is food.");
		}
		
	}
	
	//Implemented, not tested. - Kylie F
	private static void modifyItem(Scanner s) {
	System.out.println("Enter item category name that needs inventory updated: \n"+ 
	"Categories include: appetizer, lunch, dinner, dessert"+ " or Water, SoftDrink, Alcoholic, Other \n");
	String name = s.nextLine();	
	InventoryItem i = Database.getInventoryItemFromName(name);
		
	System.out.println("Please enter the new number quantity you want associated with this item.");
	int newAmount = s.nextInt();
	i.setItemOrderAmt(newAmount);

	Database.saveInventoryItem(i);	
		
	System.out.println("You have updated: " + name + "New inventory amount will be: " + newAmount);
			
	}
	
	
	private static void reduceItem(Scanner s) {
		String names = "[";
		for(InventoryItem item : Database.getInventoryItems()) {
			names += item.getName() + ", ";
		}
		names = names.substring(0, names.length()-2) +"]";//remove the last comma
		
		System.out.println("Which item would you like to reduce the stock of? \n"
				+ "Item names: "+names);
		String itemName = s.nextLine();
		InventoryItem item = Database.getInventoryItemFromName(itemName);
		
		System.out.println("How much do you want to decrease the stock? Current stock: " + item.getQuantityInStock());
		int amount = s.nextInt();
		s.nextLine();
		if(amount <= 0 || amount >item.getQuantityInStock()) {
			System.out.println("Amount must be greater than 0 and less than or equal to the amount in stock!");
			return;
		}
		item.useStock(amount);
		Database.saveInventoryItem(item);
		System.out.println("Stock reduced. Stock is now: " + item.getQuantityInStock());
	}

	private static void createOrder(Scanner s) {
		String names = "[";
		for(InventoryItem item : Database.getInventoryItems()) {
			names += item.getName() + ", ";
		}
		names = names.substring(0, names.length()-2) +"]";//remove the last comma
		
		System.out.println("Enter item name: \n"
				+ "Items that can be ordered: "+names);

		String itemName = s.nextLine();
		InventoryItem item = Database.getInventoryItemFromName(itemName);
		
		
		System.out.println("Enter order price per unit: ");
		double price = s.nextDouble();
		
		System.out.println("Enter amount to order: ");
		int orderAmnt = s.nextInt();
		
		Date date = new Date();
		Database.createOrder(item, price, orderAmnt, date);
		System.out.println(item.getName() + " order created successfully.");
		s.nextLine();
	}
	private static void completeOrder(Scanner s) throws ParseException {
		List<InventoryOrder> incomplete = Database.getIncompleteOrders();
		System.out.println("Current incomplete orders:");
		for(int i = 0; i< incomplete.size(); i++) {
			System.out.printf("%d. %s\n", i+1, incomplete.get(i));
		}
		System.out.println("Which order has been received?\n"
				+ "Enter the index of the order (1, 2, etc): ");
		int index = s.nextInt();
		if (index < 1 || index > incomplete.size()) {
			System.out.printf("Invalid ID. Must be between %d and %d!\n", 1, incomplete.size());
		}
		System.out.println("Order received date: \n"
				+ "Format: month-day-year");
		String recDate = s.next();
		Date newDate = DATE_FORMAT.parse(recDate);
		
		InventoryOrder order = incomplete.get(index-1);
		if (Database.completeOrder(order, newDate)) {
			System.out.println("The order, \n" + order + " \nhas been completed successfully!");
		}
		s.nextLine();
	}
}
