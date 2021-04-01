/**
 * Bookkeeping.java - class to implement book keeping for the coffee shop's day 
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

import java.util.ArrayList;

public class Bookkeeping {
	
	// Holds sub-total, tax, discount, total, discount1, discount2, discount3, customerAmount
	private ArrayList<Float> allValues; 

	/**
	 * Constructor for Bookkeeping class
	 */
	public Bookkeeping () {		
		// Initialise Bookkeeping object with 0 values 
		allValues = new ArrayList<Float>();   
		
		for (int i =0 ; i< 8; i++) {
			allValues.add((float) 0.0);
		}
	}
	
	// Class to validate thread interaction on the Bookkeeping object
	public class BookkeepingOutput{
		private boolean success;
		private float numberOfCustomers;

		/**
		 * Constructor for BookkeepingOutput class
		 * @param success Boolean variable for success of operation
		 * @param numberOfCustomers Number of customers 
		 */
		public BookkeepingOutput( boolean success, float numberOfCustomers) {
			this.success = success;
			this.numberOfCustomers = numberOfCustomers;
		}
		
		/**
		 * Validate thread interaction on the Bookkeeping object  
		 * @return True for successful operation, False for failed operation 
		 */
		public boolean isSuccess() {
			return success;
		}
		
		/**
		 * @return Updated number of customers 
		 */
		public float getNumberOfCustomers() {
			return numberOfCustomers;
		}
	}	
	
	/**
	 * @return sub-total, tax, discount, total, discount1, discount2, discount3, customerAmount
	 */
	public ArrayList<Float> getAllValues() {
		return allValues;
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
			
			// update customer number 
			float newValue = allValues.get(7) + 1;
			allValues.set(7, newValue);
	   		
	   		notifyAll();  // Notify all threads 
	   	
	   		// return BookkeepingOutput object based on exceptions caught 
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
