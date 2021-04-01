package CoffeeShopProjectThreaded;
import java.time.LocalDateTime;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class Inventory {
	
	
	private Hashtable<String,Integer> inventory;
	
	// * Class to validate thread interaction on the Inventory object *
	public class InventoryOutput{
		boolean success;
		int updatedSize;
		
		public InventoryOutput( boolean success, int updatedSize) {
			this.success = success;
			this.updatedSize = updatedSize;
		}
		
		public boolean isSuccess() {
			return success;
		}
	}
	
	public Inventory() {
		inventory = new Hashtable<String,Integer>();
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
}
