package database;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
						case "reduceItem":
							reduceItem(s);
							break;
						case "createOrder":
							createOrder(s);
							break;
						case "completeOrder":
							completeOrder(s);
							break;
						case "logout":
							break main;
						case "testdb":
							testDatabase();
							break;
						default:
							System.out.println("Invalid command: "+ command);
							break;
					}
					System.out.println("Please enter another command or enter \"logout\" to exit.");
				}
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
	//Implemented, I used the same logic as the modify item since it seemed to make the most sense? But correct me if I'm wrong. Or if we want to trim out reduce item all together and just keep modify. - Kylie F
	private static void reduceItem(Scanner s) {
	System.out.println("Enter item category name that needs inventory reduced: \n"+ "Categories include: appetizer, lunch, dinner, dessert"+ "or Water, SoftDrink, Alcoholic, Other \n");
	String name = s.nextLine();	
	InventoryItem i = Database.getInventoryItemFromName(name);
		
	System.out.println("Please enter the new number quantity you want associated with this item.");
	int newAmount = s.nextInt();
	i.setItemOrderAmt(newAmount);

	Database.saveInventoryItem(i);	
	
	System.out.println("You have updated: " + name + "New inventory amount will be: " + newAmount);
		
	}

	private static void createOrder(Scanner s) {
		System.out.println("Enter item name: \n"
				+ "Items that can be ordered: " + Database.getInventoryItems());
		String itemName = s.nextLine();
		InventoryItem item = Database.getInventoryItemFromName(itemName);
		
		
		System.out.println("Enter order price per unit: ");
		double price = s.nextDouble();
		
		System.out.println("Enter amount to order: ");
		int orderAmnt = s.nextInt();
		
		Date date = new Date();
		Database.createOrder(item, price, orderAmnt, date);
		System.out.println(item + " ordered successfully.");
	}
	private static void completeOrder(Scanner s) throws ParseException {
		List<InventoryOrder> incomplete = Database.getIncompleteOrders();
		System.out.println("Current incomplete orders: \n" 
				+ incomplete);
		System.out.println("Which order has been received?\n"
				+ "Enter the index of the order (1, 2, etc): ");
		int index = s.nextInt();
		if (index < 1 || index > incomplete.size()) {
			System.out.printf("Invalid ID. Must be between %d and %d!\n", 1, incomplete.size());
		}
		System.out.println("Order received date: \n"
				+ "Format: month-day-year");
		String recDate = s.nextLine();
			Date newDate = new SimpleDateFormat("mm-dd-yyyy").parse(recDate);
			
			InventoryOrder order = incomplete.get(index-1);
			if (Database.completeOrder(order, newDate)) {
				System.out.println(order + " \nhas been added successfully!");
			
		}
	}
}
