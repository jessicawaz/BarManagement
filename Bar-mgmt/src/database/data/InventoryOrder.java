package database.data;

import java.util.Date;

public class InventoryOrder {
	private InventoryItem item;
	private String userFullName;
	private double pricePerUnit;
	private int orderQuantity;
	private Date orderDate;
	private Date completeDate;
	
	InventoryOrder(){
		this.item = new InventoryItem();
		this.userFullName = "Robert Smith";
		this.pricePerUnit = 3.50;
		this.orderQuantity = 50;
		this.orderDate = new Date(new Date().getTime() - 60000*60*24*10); // 10 days ago
		this.completeDate = null;
	}
	public Date getCompleteDate() {
		return completeDate;
	}
	public boolean isComplete() {
		return this.getCompleteDate() != null;
	}
	public void complete(Date completeDate) {
	}
	public InventoryItem getItem() {
		return item;
	}
	public String getUserFullName() {
		return userFullName;
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
	
	
}
