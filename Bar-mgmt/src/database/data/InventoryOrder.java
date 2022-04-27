package database.data;

import java.util.Date;
import java.util.HashMap;

import database.UI;

public class InventoryOrder {
	final int id;
	private final InventoryItem item;
	private final String userFullName;
	private final double pricePerUnit;
	private final int orderQuantity;
	private final Date orderDate;
	private Date completeDate;
	boolean dirty = false;
	
	
	private static HashMap<Integer, InventoryOrder> cache = new HashMap<>();
	protected static InventoryOrder fromDB(int id, InventoryItem item, String userFirst, String userLast, double pricePerUnit, 
			int orderQuantity, java.sql.Date orderDate, java.sql.Date completeDate) {
		Date orderDateJ = new Date(orderDate.getTime());
		Date completeDateJ = null;
		if(completeDate != null) {
			completeDateJ = new Date(completeDate.getTime());
		}
		String fullName = String.format("%s %s", userFirst, userLast);
		if(cache.containsKey(id)) {
			InventoryOrder o = cache.get(id);
			o.completeDate = completeDateJ;
			return o;
		}
		InventoryOrder o = new InventoryOrder(id, item, fullName, pricePerUnit, orderQuantity, orderDateJ, completeDateJ);
		cache.put(id, o);
		return o;
	}
	private InventoryOrder(int id, InventoryItem item, String fullName, double pricePerUnit, int orderQuantity, Date orderDate, Date completeDate){
		this.id = id;
		this.userFullName = fullName;
		this.item = item;
		this.pricePerUnit = pricePerUnit;
		this.orderQuantity = orderQuantity;
		this.orderDate = orderDate;
		this.completeDate = completeDate;
		
	}
	public int hashCode() {
		return this.id;
	}
	public Date getCompleteDate() {
		return completeDate;
	}
	public boolean isComplete() {
		return this.getCompleteDate() != null;
	}
	public InventoryItem getItem() {
		return item;
	}
	public String getUserFullName() {
		return userFullName;
	}
	public double getTotalCost() {
		return this.getPricePerUnit() * this.getOrderQuantity();
	}
	public double getPricePerUnit() {
		return pricePerUnit;
	}
	public int getOrderQuantity() {
		return orderQuantity;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	

	void complete(Date completeDate) {
		this.completeDate = completeDate;
		this.item.addStock(this.getOrderQuantity());
		dirty = true;
	}
	
	public String toString() {
		return String.format("Item: %s, Total Cost: %.2f, Completed: %s, Order Date: %s, Price: %.2f, Quantity: %d, User: %s", 
				this.getItem().getName(), 
				this.getTotalCost(),
				this.isComplete()? UI.DATE_FORMAT.format(this.getCompleteDate()) : "No",
				UI.DATE_FORMAT.format(this.getOrderDate()),
				this.getPricePerUnit(),
				this.getOrderQuantity(),
				this.getUserFullName() );
	}
	
	
}
