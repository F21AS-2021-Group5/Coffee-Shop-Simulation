package CoffeeShopProjectThreaded;

import java.util.ArrayList;
import java.util.Set;

import CoffeeShopProjectThreaded.Inventory.InventoryOutput;

public class Bookkeeping {
	
	private ArrayList<Float> allValues; 
	private Log log;
	
	//////
	public class BookkeepingOutput{
		boolean success;
		float numberOfCustomers;
		
		public BookkeepingOutput( boolean success, float numberOfCustomers) {
			this.success = success;
			this.numberOfCustomers = numberOfCustomers;
		}
		
		public boolean isSuccess() {
			return success;
		}
	}
	//////
	
	public Bookkeeping () {
		allValues = new ArrayList<Float>();
		for (int i =0 ; i< 8; i++) {
			allValues.add((float) 0.0);
		}
	}
	
	public synchronized BookkeepingOutput upDateBooks(float[] books) {
		try {
		    // create random customer 
			for (int i =0 ; i< 7; i++) {
				float newValue = books[i] + allValues.get(i);
				allValues.set(i, newValue);
			}
			float newValue = allValues.get(7)+1;
			allValues.set(7, newValue);
	   		
	   		notifyAll();
	   		
	   		// Add log here for new customer 
		   		
		} catch (Exception e) {
			return new BookkeepingOutput(false, allValues.get(7));
		}
		return new BookkeepingOutput(true, allValues.get(7));
	}
	
	public float getCustomerNumber() {
		return allValues.get(7);
	}

}
