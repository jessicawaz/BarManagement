package database;

import java.util.Scanner;

import database.data.Database;
import database.data.InventoryItem;

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
	
	private static void reviewInventory(Scanner s) {
		for(InventoryItem item: Database.getInventoryItems()) {
			System.out.println(item);
		}
	}
	
	private static void reviewOrders(Scanner s) {

		System.out.println("Not implemented");
		
	}
	
	private static void addItem(Scanner s) {

		System.out.println("Not implemented");
		
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
