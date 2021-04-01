/**
 * Inventory.java - class to implement the coffee shop inventory for items 
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
import java.util.*;

public class Inventory {	
	
	private Hashtable<String, Integer> inventory;
	
	/**
	 * Constructor for Inventory class 
	 */
	public Inventory() {
		inventory = new Hashtable<String, Integer>();
	}
	
	// * Class to validate thread interaction on the Inventory object *
	public class InventoryOutput{
		boolean success;
		int updatedSize;
		
		/**
		 * Constructor for InventoryOutput class 
		 * @param success True if successful operation, False if not
		 * @param updatedSize Updated size of queue 
		 */
		public InventoryOutput( boolean success, int updatedSize) {
			this.success = success;
			this.updatedSize = updatedSize;
		}
		
		/**
		 * @return True if successful operation, False if not
		 */
		public boolean isSuccess() {
			return success;
		}
	}
	
	 /**
	  * Update the inventory after cashier has processed customer 
	  * @param currentCustoemer 
	  */
	public synchronized InventoryOutput addToInventory(Customer curentCustomer) {
		try {
			// Create Set from the customers cart
			Set<String> cartSet = curentCustomer.getCart().keySet(); 
  			for (String orderID: cartSet) {
  			    // If item dosen't exist in inventory add it and quantity
  				if (!inventory.containsKey(orderID)) { 
  					inventory.put(orderID, curentCustomer.getCart().get(orderID).size());
  				}
  				// If the item does exist update its quantity
  				else {
  					inventory.put(orderID, inventory.get(orderID) + curentCustomer.getCart().get(orderID).size());
  				}
  			}
	   		notifyAll();
	   		
		} catch (Exception e) {
			return new InventoryOutput(false,inventory.size());
		}
		return new InventoryOutput(true,inventory.size());
	}
	
	public Hashtable<String, Integer> getInventory() {
		return inventory;
	}
}
