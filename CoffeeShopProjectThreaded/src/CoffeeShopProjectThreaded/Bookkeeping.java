package CoffeeShopProjectThreaded;

import java.util.ArrayList;
import java.util.Set;

import CoffeeShopProjectThreaded.Inventory.InventoryOutput;

public class Bookkeeping {
	// Holds subtotal, tax, discount, total, discount1, discount2, discount3, customerAmount
	private ArrayList<Float> allValues; 
	private Log log;
	
	// * Class to validate thread interaction on the Bookkeeping object *
	public class BookkeepingOutput{
		boolean success;
		float numberOfCustomers;
		
		public BookkeepingOutput( boolean success, float numberOfCustomers) {
			this.success = success;
			this.numberOfCustomers = numberOfCustomers;
		}
		
		/**
		 * Validate thread interaction on the Bookkeeping object  
		 */
		public boolean isSuccess() {
			return success;
		}
	}
	
	public Bookkeeping () {
		// Initialise Bookkeeping object
		allValues = new ArrayList<Float>();   
		for (int i =0 ; i< 8; i++) {
			allValues.add((float) 0.0);
		}
	}
	
	
	/**
	 * Update books after cashier has processed customer 
	 * @param float[] of all the values from the cashier 
	 */
	public synchronized BookkeepingOutput upDateBooks(float[] books) {
		try {
			for (int i =0 ; i< 7; i++) {
				// Update the bookkeeping values depending on the information gotten from the cashier 
				float newValue = books[i] + allValues.get(i); 
				allValues.set(i, newValue);
			}
			float newValue = allValues.get(7)+1;
			allValues.set(7, newValue);
	   		
	   		notifyAll();  // Notify all threads 
		} catch (Exception e) {
			return new BookkeepingOutput(false, allValues.get(7));
		}
		return new BookkeepingOutput(true, allValues.get(7));
	}
	
	/**
	 * Get amount of customers 
	 * @return float of amount of customers
	 */
	public float getCustomerNumber() {
		return allValues.get(7);
	}

}
