/**
 * FoodStaff.java - class to implement a staff for the coffeeshop
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

import CoffeeShopProjectThreaded.OrderQueue.OrderQueueItem;

public class FoodStaff {
	
	private String name;
	private String type;
	
	private Customer currentCustomer;
	private OrderQueueItem currentItem; 
	
	/**
	 * Constructor for Staff class
	 * @param queue Queue of orders 
	 */
	public FoodStaff(String name, boolean isBarista) { 
		this.name = name;
		
		type = (isBarista) ? "Barista" : "Cook";
	}
	
	/**
	 * @return Staff name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set name of staff 
	 * @param name Staff name
	 */
	public void setName(String name) {
		this.name = name;
	}
	

	/**
	 * @return Staff type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Set type of staff 
	 * @param name Staff type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @return Current item identifier
	 */
	public OrderQueueItem getCurrentItem() {
		return currentItem;
	}
	
	/**
	 * Sets the current item identifier
	 * @param currentItem Current item identifier
	 */
	public void setCurrentItem(OrderQueueItem currentItem) {
		this.currentItem = currentItem;
	}
	
	/**
	 * @return Current customer 
	 */
	public Customer getCurrentCustomer() {
		return currentCustomer;
	}
	
	/**
	 * Sets the current customer 
	 * @param currentCustomer Current customer 
	 */
	public void setCurrentCustomer(Customer currentCustomer) {
		this.currentCustomer = currentCustomer;
	}

}
