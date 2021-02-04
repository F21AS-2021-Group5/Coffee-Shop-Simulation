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

import java.time.LocalDate;
import java.util.Hashtable;

public class Customer {
	
	String id;
	LocalDate timestamp;
	float orderTotalPrice;
	Hashtable<String, Order> cart;
	
	/**
	 * Constructor for Customer class
	 * @param id Customer ID 
	 * @param timestamp Time at which customer placed orders 
	 */
	public Customer(String id, LocalDate timestamp)
	{
		this.id = id;
		this.timestamp = timestamp;
		
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
	public LocalDate getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Sets the time stamp at which customer placed orders
	 * @param timestamp Time at which customer placed orders 
	 */
	public void setTimestamp(LocalDate timestamp) {
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
	 * Add the order to the customer's cart
	 * @param order The order
	 */
	private void addOrder(Order order) {
		// check if order exists already
		// if exists already then add only the quantity to Order object
		// if not create a new one 
		// think about possible exceptions 
	}
	
	/**
	 * Remove the order from the customer's cart 
	 * @param order The order
	 */
	private void removeOrder(Order order) {
		// check if order exists 
		// if exists then remove it 
		// if does not exist do nothing 
		// think about possible exceptions 
	}
}
