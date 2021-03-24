package CoffeeShopProjectThreaded;
import java.time.LocalDateTime;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class Inventory {
	
	private Log log;
	
	private Hashtable<String,Integer> inventory;
	
	///////
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
	///////
	
	public Inventory() {
		inventory = new Hashtable<>();
	}
	
	public synchronized InventoryOutput addToInventory(Customer curentCustomer) {
		try {
		    // create random customer 
			Set<String> cartSet = curentCustomer.getCart().keySet();
  			for (String orderID: cartSet) {
  				if (!inventory.containsKey(orderID)) { // If item dosen't exist in inventory add it and quantity
  					inventory.put(orderID, curentCustomer.getCart().get(orderID).size());
  				}
  				else { // If the item does exist update its quantity
  					inventory.put(orderID, inventory.get(orderID) + curentCustomer.getCart().get(orderID).size());
  				}
  			}
	   		
	   		notifyAll();
	   		
	   		// Add log here for new customer 
		   		
		} catch (Exception e) {
			return new InventoryOutput(false,inventory.size());
		}
		return new InventoryOutput(true,inventory.size());
	}
	
	
	//private void UpdateInventory

}
