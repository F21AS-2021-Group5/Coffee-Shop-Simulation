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

package CoffeeShopProjectThreaded;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;

public class Customer {
	
	String id;
	String name;
	LocalDateTime timestamp;
	float cartTotalPrice;
	
	HashMap<String, ArrayList<LocalDateTime>> cart;

	/**
	 * Constructor for Customer class taking only a name and time stamp
	 * @param name Customer name 
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
		cartTotalPrice = 0;
		cart = new HashMap<String, ArrayList<LocalDateTime>>();
	}
	
	/**
	 * Constructor for Customer class taking a name, time stamp and ID number 
	 * @param name Customer name 
	 * @param id Customer ID 
	 * @param timestamp Time at which customer placed orders 
	 */
	public Customer(String name, String id, LocalDateTime timestamp)
	{
		// id needs to be provided
		if ((id.trim().length() == 0) || (name.trim().length() == 0))
			throw new IllegalStateException("Name or ID of customer can't be blank.");
		this.name = name.trim();
		this.id = id.trim();
		
		// time stamp needs to have valid date
		if (!isDateTimeValid(timestamp))
			throw new DateTimeException("Date and/or time is invalid.");
		this.timestamp = timestamp;
		
		// initialize total price and cart 
		cartTotalPrice = 0;
		cart = new HashMap<String, ArrayList<LocalDateTime>>();
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
	public float getCartTotalPrice() {
		return cartTotalPrice;
	}
	
	/**
	 * Sets the total amount the customer needs to pay 
	 * @param cartTotalPrice Total money owed by customer 
	 */
	public void setCartTotalPrice(float cartTotalPrice) {
		this.cartTotalPrice = cartTotalPrice;
	}
	
	/**
	 * @return Hash table filled with the customer's item IDs and their respective time stamps
	 */
	public HashMap<String, ArrayList<LocalDateTime>> getCart() {
		return cart;
	}
	
	/**
	 * Sets the customer's cart filled with the item IDs and their respective time stamps
	 * @param cart Customer orders 
	 */
	public void setCart(HashMap<String, ArrayList<LocalDateTime>> cart) {
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
		String generatedID = "";
		Random random = new Random();
		random.setSeed(3);
		
		// generate random numbers 
		final int[] randIndices = new int[]{random.nextInt(20), random.nextInt(20), 
				random.nextInt(20), random.nextInt(20), random.nextInt(20), random.nextInt(20)};
		
		// date time formatter and convert time stamp to string
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
		String timestampString = timestamp.format(formatter).substring(9);
		
		// removes white spaces inside string
		name = name.replaceAll("\\s", "").toLowerCase();
		
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
	 * @param o	Object to compare with 
	 * @return false if they do not match, true if they match 
	 */
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof Customer))
			return false;
		
		Customer otherCustomer = (Customer) o;		
		return id.equals(otherCustomer.id);
	}
	
	/**
	 * Add an item to the customer's cart
	 * @param itemId The item identifier 
	 * @param quantity Number of items to add 
	 * @param timestamp Time at which customer placed orders 
	 * @throws InvalidMenuItemQuantityException Invalid item quantity
	 * @throws InvalidMenuItemDataException Invalid item ID
	 */
	public void addItem(String itemId, int quantity, LocalDateTime timestamp) throws InvalidMenuItemQuantityException, InvalidMenuItemDataException {		
		// if item ID is empty, throw exception
		if (itemId.length() == 0) 
			throw new IllegalStateException("Item ID can't be blank.");
		
		// time stamp needs to have valid date
		if (!isDateTimeValid(timestamp))
			throw new DateTimeException("Date and/or time is invalid.");

		// check that item ID exists inside the menu 
		if (!CoffeeShop.menu.containsKey(itemId))
			throw new InvalidMenuItemDataException();
		
		// if quantity is invalid, throw exception 
		if (quantity <= 0) 
			throw new InvalidMenuItemQuantityException(); 
		
		// if it already exists, add quantity to item in cart 
		itemId = itemId.trim();
		if (cart.containsKey(itemId)) {			
			// add quantity 			
			for (int i = 0; i < quantity; i++)
				cart.get(itemId).add(timestamp);
			
			// add price to total cost
			cartTotalPrice += quantity * CoffeeShop.menu.get(itemId).getCost();
		} 

		// if order does not exist, add <K,V> to hash table
		else {
			ArrayList<LocalDateTime> orders = new ArrayList<LocalDateTime>();
			for (int i = 0; i < quantity; i++)
				orders.add(timestamp);

			cart.put(itemId, orders);
			
			// add price to total cost
			cartTotalPrice += quantity * CoffeeShop.menu.get(itemId).getCost();
		}		
	}
	
	/**
	 * Remove the item from the customer's cart and with the relevant quantity (-1 for all)
	 * @param itemId The item identifier 
	 * @param quantity Number of items to remove  
	 * @throws NoMatchingMenuItemIDException No matching item ID
	 * @throws InvalidMenuItemQuantityException Invalid item quantity
	 * @throws InvalidMenuItemDataException Invalid item ID
	 * @throws InvalidCartItemException 
	 */
	public void removeItem(String itemId, int quantity) throws NoMatchingMenuItemIDException, InvalidMenuItemQuantityException, InvalidMenuItemDataException, InvalidCartItemException {	
		// item ID cannot be empty string
		if (itemId.length() == 0) 
			throw new IllegalStateException("Item ID can't be blank.");
		
		// check that item ID exists inside the menu 
		if (!CoffeeShop.menu.containsKey(itemId))
			throw new InvalidMenuItemDataException();
				
		// if order exists inside cart
		if (cart.containsKey(itemId)) {
			
			// check if quantity is -1 or a valid number 
			if ((quantity == 0)  || (quantity < -1) || ((cart.get(itemId).size() - quantity) < 0))
				throw new InvalidMenuItemQuantityException(); 
			
			// if removing all items, remove the <K,V> pair
			if ((quantity == -1) || ((cart.get(itemId).size() - quantity) == 0))
			{
				// remove price from total cost
				cartTotalPrice -= cart.get(itemId).size() * CoffeeShop.menu.get(itemId).getCost();
				
				// remove pair from hash map 
				cart.remove(itemId);
			// if instead removing only part of them
			} else {
				int currentQuantity = 0;
				// remove last element, so last item added to cart by customer, making it a O(1) operation
				for (int i = 0; i < quantity; i++) {
					currentQuantity = cart.get(itemId).size() - 1;
					cart.get(itemId).remove(currentQuantity);
				}
				
				// remove price from total cost
				cartTotalPrice -= quantity * CoffeeShop.menu.get(itemId).getCost();
			} 
		}
		// if order does not exist inside cart	
		else
			throw new InvalidCartItemException();
	}
	
	/**
	 * String containing all Customer object details 
	 */
	public String receipt()
	{
		// date time formatter 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String datetime = timestamp.format(formatter);
		String[] datetimeSplit = datetime.split(" ");
		 
		// initial customer information
		String output = String.format("%-10s %-10s\n", "Name:", name) + 
						String.format("%-10s %-10s\n", "ID:",id) +
						String.format("%-10s %-10s\n", "Date:", datetimeSplit[0]) +
						String.format("%-10s %-10s\n\n", "Time:",datetimeSplit[1]) +
						String.format("Orders:\n");
		
		// print all cart orders with quantities 
		Set<String> cartSet = cart.keySet();
		for (String orderID: cartSet) {
			output += String.format("%-10s %-20s %-20s\n", " ",CoffeeShop.menu.get(orderID).getName(), 
					String.valueOf(cart.get(orderID).size() + "x"));
		}
		
		// final price
		String finalPrice = String.format("%.2f", cartTotalPrice) + "£";
		output += String.format("\n%-31s %-10s", "Total Price:", finalPrice );
		
		return output;
	}
	
	
	// MAIN METHOD 
	public static void main(String[] args) {
	
	}
}
