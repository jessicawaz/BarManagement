package database;

import java.util.Scanner;

import database.data.Database;
import database.data.InventoryItem;
import database.data.InventoryOrder;

public class UI {

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
						default:
							System.out.println("Invalid command: "+ command);
							break;
					}
					System.out.println("Please enter another command or enter \"logout\" to exit.");
				}
			}
		}
		finally {
			System.out.println("Disconnecting from database...");
			Database.disconnect();
			System.out.println("Goodbye");
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
		System.out.println("Is the item a drink?");
		String itemType = s.nextLine();
		
		if (itemType.equals("No") || itemType.equals("no")) {
			System.out.println("Enter item name: ");
			String foodName = s.nextLine();
			System.out.println("Enter item category: \n"
					+ "Categories include: appetizer, lunch, dinner, "
					+ "or dessert \n");
			String foodCategory = s.nextLine();
			System.out.println("Enter low amount: ");
			int lowAmt = s.nextInt();
			System.out.println("Enter order amount: ");
			int orderAmt = s.nextInt();
			Database.createFood(foodName, foodCategory, lowAmt, orderAmt);
		} else if (itemType.equals("Yes") || itemType.equals("yes")) {
			System.out.println("Enter item name: ");
			String drinkName = s.nextLine();
			System.out.println("Enter item category: \n"
					+ "Categories include: Water, SoftDrink, Alcoholic, "
					+ "or Other \n");
			String drinkCategory = s.nextLine();
			System.out.println("Enter low amount: ");
			int lowAmt = s.nextInt();
			System.out.println("Enter order amount: ");
			int orderAmt = s.nextInt();
			Database.createDrink(drinkName, drinkCategory, lowAmt, orderAmt);
		} else {
			System.out.println("Invalid input. Please enter 'yes' if the "
					+ "item is a drink or 'no' if the item is food.");
		}
		
	}
	
	private static void modifyItem(Scanner s) {
		System.out.println("Not implemented");
		
	}
	
	private static void reduceItem(Scanner s) {
		System.out.println("Not implemented");
		
	}

	private static void createOrder(Scanner s) {
		System.out.println("Not implemented");
		
	}
	private static void completeOrder(Scanner s) {
		System.out.println("Not implemented");
		
	}
}
