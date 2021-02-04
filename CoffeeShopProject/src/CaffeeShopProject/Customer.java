/**
 * Customer.java - class to implement a customer for the coffee shop simulation
 * 
 * @author Esther Rayssiguie 
 * @author Jake Marrocco
 * @author Karolina Judzentyte
 * @author Valerio Franchi
 * @version 0.1
 * 
 * Copyright (c) 2021 
 * All rights reserved.
 */

package CaffeeShopProject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;
import java.util.Set;

public class Customer {
	
	String id;
	String name;
	LocalDateTime timestamp;
	float orderTotalPrice;
	Hashtable<String, Order> cart;
	
	/**
	 * Constructor for Customer class
	 * @param id Customer ID 
	 * @param timestamp Time at which customer placed orders 
	 */
	public Customer(String name, LocalDateTime timestamp)
	{
		// name needs to be provided
		if (name.trim().length() == 0) {
			throw new IllegalStateException("Name of customer can't be blank.");
		}
		this.name = name.trim();
		this.timestamp = timestamp;
		
		// generate the ID based on customer name and time stamp
		this.id = generateID(name, timestamp).trim();
		
		orderTotalPrice = 0;
		cart = new Hashtable<String, Order>();
	}
	
	/**
	 * @return Customer ID
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets a customer ID for the customer
	 * @param id Customer ID
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return Time at which customer placed orders 
	 */
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Sets the time stamp at which customer placed orders
	 * @param timestamp Time at which customer placed orders 
	 */
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
	/**
	 * @return Total money owed by customer 
	 */
	public float getOrderTotalPrice() {
		return orderTotalPrice;
	}
	
	/**
	 * Sets the total amount the customer needs to pay 
	 * @param orderTotalPrice Total money owed by customer 
	 */
	public void setOrderTotalPrice(float orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}
	
	/**
	 * @return Hash table filled with the customer's orders and the orders' IDs  
	 */
	public Hashtable<String, Order> getCart() {
		return cart;
	}
	
	/**
	 * Sets the customer's cart filled with the orders and the orders' IDs  
	 * @param cart Customer orders 
	 */
	public void setCart(Hashtable<String, Order> cart) {
		this.cart = cart;
	}
	
	/**
	 * Generates a Customer ID for the current customer 
	 * @param name Name of customer
	 * @param timestamp Time at which customer placed orders 
	 * @return Customer ID
	 */
	private String generateID(String name, LocalDateTime timestamp)
	{
		String generatedID ="";
		
		// date time formatter and convert time stamp to string
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
		String timestampString = timestamp.format(formatter);
		
		// removes white spaces inside string
		name = name.replaceAll("\\s","");
		
		// convert string of chars to string of integer values 
		for(int i = 0, n = name.length() ; i < n ; i++) {
			char c = name.charAt(i);
			generatedID += String.valueOf((int)c);
		}
		
		// add time stamp and return ID		
		return generatedID += timestampString;
	}
	
	/**
	 * Compares this Customer object to another Customer object using the 'id' field
	 * @param otherCustomer
	 * @return false if they do not match, true if they match 
	 */
	private boolean equals(Customer otherCustomer)
	{
		return id.equals(otherCustomer.id);
	}
	
	/**
	 * Add the order to the customer's cart
	 * @param order The order
	 */
	private void addOrder(Order order) {		
		// if Order is null, throw exception
		if (order == null)
				throw new IllegalArgumentException();
		else {
			// if it already exists, add quantity to Order
			String id = order.getIdentifier();
			if (cart.contains(id)) {
				Order updated = cart.get(id);
				updated.setQuantity(updated.getQuantity()+1);
				cart.put(id, updated);
				
				// add price to total cost
				orderTotalPrice += updated.getCost();
			}
			// if order does not exist, add <K,V> to hash table
			else {
				cart.put(order.getIdentifier(), order);
				
				// add price to total cost
				orderTotalPrice += order.getCost();
			}		
		}
	}
	
	/**
	 * Remove the order corresponding to the ID from the customer's cart 
	 * @param order Order identification string 
	 * @throws NoMatchingOrderIDException No matching order ID
	 * @throws InvalidOrderQuantityException Invalid order ID
	 */
	private void removeOrder(String id) throws NoMatchingOrderIDException, InvalidOrderQuantityException {	
		// if order exists inside cart
		if (cart.contains(id))
			// if quantity is more than 1, remove one from quantity
			if (cart.get(id).getQuantity() > 1) {
				Order updated = cart.get(id);
				updated.setQuantity(updated.getQuantity()-1);
				cart.put(id, updated);
				
				// remove price from total cost
				orderTotalPrice -= updated.getCost();
			} 
			// if quantity is 1, remove entire order
			else if (cart.get(id).getQuantity() == 1) {
				cart.remove(id);
				
				// remove price from total cost
				orderTotalPrice -= cart.get(id).getCost();
			}
			// invalid quantity for order 
			else 
				throw new InvalidOrderQuantityException(); 
		
		// if order does not exist inside cart	
		else
			throw new NoMatchingOrderIDException();
	}
	
	/**
	 * String containing all Customer object details 
	 */
	public String receipt()
	{
		// date time formatter 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		 
		// initial customer information
		String output = String.format("Name: %10s\n", name) + String.format("ID: %10s\n", id)
			+ String.format("Timestamp: %10s\n\n", timestamp.format(formatter)) 
			+ String.format("Orders:");
		
		// print all cart orders with quantities 
		Set<String> cartSet = cart.keySet();
		for (String orderID: cartSet) {
			output += String.format("%-10s", cart.get(orderID).getName()) + 
					String.format("%-20s", "x" + String.valueOf(cart.get(orderID).getQuantity()));
		}
		
		// final price
		output += String.format("\n\nTotal price: %-20s", String.valueOf(orderTotalPrice));
		
		return output;
	}
}
