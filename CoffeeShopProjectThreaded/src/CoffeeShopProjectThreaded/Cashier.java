/**
 * Cashier.java - class to implement the cashier for the coffee shop simulation
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*; 

public class Cashier {
	
	public float subtotal;
	public float tax;
	public float discount;
	public float total;
	
	public static Customer currentCustomer;
	
	int discount1 = 0;
	int discount2 = 0;
	int discount3 = 0;
	Long speed;
	private PropertyChangeSupport support;
	
	String name;
	
	/**
	 * Constructor for Cashier class
	 * @param ID Cashier identifier
	 */
	public Cashier(String name, Long speed) {
		support = new PropertyChangeSupport(this);
		currentCustomer = null;
		this.name =name;
		this.speed = speed;
	}
	
	/**
	 * @return Cashier identifier 
	 */
	public String getName() {
		return name;
	}
  
	/**
	 * @return Cart sub total price 
	 */
	public float getCartSubtotalPrice() {
		subtotal = currentCustomer.getCartTotalPrice();
		return subtotal;
	}
	
	/**
	 * @return Cart tax 
	 */
	public float getCartTax() {
		tax = (float) (subtotal*0.25);
		return tax;
	}
	
	/**
	 * Calculates the discounted price based on the items inside the cart 
	 * @return Discounted price 
	 */
	public float getDiscount() {
		// discount 1 = 1 drink & 1 food & 1 pastry = £5 //
		// discount 2 = 3 drinks & 1 food = 20% off //
		// discount 3 = 3 pastries = 25% off //
		
		List<Float> food = new ArrayList<Float>();
		List<Float> drink = new ArrayList<Float>();
		List<Float> pastry = new ArrayList<Float>();
		
		
		// Go through the cart to find all the items but
		Set<String> cartSet = currentCustomer.cart.keySet(); 
		
		for (String orderID: cartSet) {
			
			int quantity = currentCustomer.cart.get(orderID).size();
		
			
			if (CoffeeShop.menu.get(orderID).getCategory().equals("Drink")) {
				for(int i = 1; i <= quantity; i++) {
					drink.add(CoffeeShop.menu.get(orderID).getCost());
				}
			} else if (CoffeeShop.menu.get(orderID).getCategory().equals("Food")) {
				for(int i = 1; i <= quantity; i++) {
					food.add(CoffeeShop.menu.get(orderID).getCost());
				}
				
			} else if (CoffeeShop.menu.get(orderID).getCategory().equals("Pastry") ) {
				for(int i = 1; i <= quantity; i++) {
					pastry.add(CoffeeShop.menu.get(orderID).getCost());
				}	
			}
        } 
		
		boolean noMoreDiscountsAvailable = false;
		discount1 = 0;
		discount2 = 0;
		discount3 = 0;
		
		while(!noMoreDiscountsAvailable) {

			
			// First checks to see all the combinations
			if (drink.size() >= 1 && food.size() >= 1 && pastry.size() >= 1) {
				float combination = drink.remove(0) + food.remove(0) + pastry.remove(0);
				discount += combination - 5;
				discount1 = discount1 + 1;
				
			// Then if other discounts are available
			} else if (drink.size() >= 3 && food.size() >= 1) { 
				float combination = drink.remove(0) + drink.remove(0) + drink.remove(0) + food.remove(0);
				discount += combination / 5;
				discount2 = discount2 + 1;
				
			} else if (pastry.size() >= 3) {
				float combination = pastry.remove(0) + pastry.remove(0) + pastry.remove(0);
				discount += combination / 4;
				discount3 = discount3 + 1;
				
			} else {
				// No more discounts
				noMoreDiscountsAvailable = true;
			}	
		
		}	
		return discount;
	}
	
	/**	
	 * @param customer Customer object 
	 */
	public void setCustomer(Customer customer) {
		System.out.println("inside cashier class");
		
		
		// For values that are initialised at the initialisation of coffeeShop
		customer.setCashierServing(name);
		System.out.println(customer.getCashierServing());
		System.out.println(customer.getName());
		currentCustomer = customer;
		setMessage(null, customer,"newCustomer");

	}
	
	/**
	 * @return Sub total, tax, discount, total, discount n.1, discount n.2, and discount n.3
	 */
	public float[] returnSums() {
		float[] allValues = {subtotal, tax, discount, total, discount1, discount2, discount3};
		return allValues;
	}
	
	/**
	 * @return Cart total price 
	 */
	public float getCartTotalPrice() {
		total = subtotal + tax - discount;
		return total;
	}
	
	/**
	 * Updates final customer cart sum 
	 */
	public void runCashier() {
		subtotal = 0;
		tax = 0;
		discount = 0;
		total = 0;
		
		getCartSubtotalPrice();
		getCartTax();
		getDiscount();
		getCartTotalPrice();
		
	}
	
	public Long getSpeed() {
		return this.speed;
	}
	
	public void setSpeed(Long speed) {
		this.speed = speed;
	}
	
	 public void addPropertyChangeListener(PropertyChangeListener pcl) {
		 support.addPropertyChangeListener(pcl);
	 }
	    
	 public void removePropertyChangeListener(PropertyChangeListener pcl) {
	    support.removePropertyChangeListener(pcl);
	 }
	 
	 public void setMessage(Customer oldVal, Customer newVal,  String message) {
	    support.firePropertyChange(message, oldVal, newVal);
	 }

}
