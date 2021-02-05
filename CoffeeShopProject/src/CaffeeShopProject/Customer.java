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

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Set;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

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
		if (name.trim().length() == 0) 
			throw new IllegalStateException("Name of customer can't be blank.");
		this.name = name.trim();
		
		// time stamp needs to have valid date
		if (!isDateTimeValid(timestamp))
			throw new DateTimeException("Date and/or time is invalid.");
		this.timestamp = timestamp;
		
		// generate the ID based on customer name and time stamp
		this.id = generateID(name, timestamp).trim();
		
		// initialize total price and cart 
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
	 * @return Customer name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets name for the customer
	 * @param name Customer name
	 */
	public void setName(String name) {
		this.name = name;
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
	 * Gets maximum number of days for month/year combination
	 * @param month Integer corresponding to current month
	 * @param year Integer corresponding to current year 
	 * @return Number of days in month
	 */
	private int getMaxDays(int month, int year) { 
		// initialize calendar and set month and year
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month-1); // February -> only month that changes max days
	    
		// get maximum day number 
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH); 
	}
	
	/**
	 * Checks if the date and time input is valid
	 * @param ldt The local date/time
	 * @return true if local date/time is valid, false if not valid 
	 */
	private boolean isDateTimeValid(LocalDateTime ldt) {		
		// convert to date and time
		String date = ldt.toLocalDate().toString();
		String time = ldt.toLocalTime().toString();
		
		// split date and time into individual elements
		int[] dateVals = Arrays.stream(date.split("-")).mapToInt(Integer::parseInt).toArray();
		int[] timeVals =  Arrays.stream(time.split("\\.")[0].split(":")).mapToInt(Integer::parseInt).toArray();
		
		
		// conditions 
		return dateVals.length == 3 && String.valueOf(dateVals[0]).length() == 4 && 
				dateVals[1] >= 1 && dateVals[1] <= 12 && dateVals[2] >= 1 && 
				dateVals[2] <= getMaxDays(dateVals[0], dateVals[1]) && 
				timeVals[0] >= 0 && timeVals[0] <= 23 && timeVals[1] >= 0 && 
				timeVals[1] <= 59 && timeVals[2] >= 0 && timeVals[2] <= 59;
	}
	
	/**
	 * Generates a Customer ID for the current customer 
	 * @param name Name of customer
	 * @param timestamp Time at which customer placed orders 
	 * @return Customer ID
	 */
	public String generateID(String name, LocalDateTime timestamp)
	{
		// initialize random object and seed it 
		String generatedID ="";
		Random random = new Random();
		random.setSeed(3);
		
		// generate random numbers 
		final int[] randIndices = new int[]{random.nextInt(20), random.nextInt(20), 
				random.nextInt(20), random.nextInt(20), random.nextInt(20), random.nextInt(20)};
		
		// date time formatter and convert time stamp to string
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
		String timestampString = timestamp.format(formatter).substring(9);
		
		// removes white spaces inside string
		name = name.replaceAll("\\s","").toLowerCase();
		
		// convert string of chars to string of integer values 
		for(int i: randIndices) {
			// retrieve character inside name using random indices generated before
			int index = i % name.length();
			char c = name.charAt(index);
			
			// transform char to int and sum ints that make it up
			int num = (int)c;
			int sum = 0;
	        while (num > 0) {
	            sum = sum + num % 10;
	            num = num / 10;
	        }
	        
	        // add together 
			generatedID += String.valueOf((int)sum);
		}
		
		// add time stamp and return ID		
		return generatedID += timestampString;
	}
	
	/**
	 * Compares this Customer object to another Customer object using the 'id' field
	 * @param otherCustomer
	 * @return false if they do not match, true if they match 
	 */
	public boolean equals(Customer otherCustomer)
	{
		return id.equals(otherCustomer.id);
	}
	
	/**
	 * Add the order to the customer's cart
	 * @param order The order
	 * @throws InvalidOrderQuantityException Invalid order quantity
	 */
	public void addOrder(Order order) throws InvalidOrderQuantityException {		
		// if Order is null, throw exception
		if (order == null)
				throw new IllegalArgumentException();
		else {
			// if quantity is invalid, throw exception 
			if (order.getQuantity() <= 0) 
				throw new InvalidOrderQuantityException(); 
			
			// if it already exists, add quantity to Order
			String id = order.getIdentifier();
			if (cart.containsKey(id)) {
				Order updated = cart.get(id);
				
				// if quantity is invalid, throw exception 
				if ((order.getQuantity() <= 0) || (updated.getQuantity() <= 0))
					throw new InvalidOrderQuantityException(); 
				
				updated.setQuantity(updated.getQuantity()+1);
				cart.put(id, updated);
				
				// add price to total cost
				orderTotalPrice += order.getCost();
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
	 * @throws NoMatchingOrderIDException No matching order quantity
	 * @throws InvalidOrderQuantityException Invalid order ID
	 */
	public void removeOrder(String id) throws NoMatchingOrderIDException, InvalidOrderQuantityException {	
		// if order exists inside cart
		if (cart.containsKey(id)) {
			
			// if Order is null, throw exception
			if (cart.get(id) == null)
					throw new IllegalArgumentException();
			
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
				// remove price from total cost
				orderTotalPrice -= cart.get(id).getCost();
				
				// remove order 
				cart.remove(id);
			}
			// invalid quantity for order 
			else 
				throw new InvalidOrderQuantityException(); 
		}
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
	
	
	// MAIN METHOD 
	public static void main(String[] args) {
		
	}
}
