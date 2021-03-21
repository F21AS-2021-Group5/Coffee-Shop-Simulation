/**
 * OrderQueue.java - class to implement order queue for baristas/cooks to prepare
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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

public class OrderQueue {
	
	Deque<QueueItem> kitchenQueue; // stores customer ID and item ID
	Deque<QueueItem> barQueue; 
	
	HashMap<String, ArrayList<String> > recipeBook; // stores recipes
	String recipeFile;
	BufferedReader recipeReader;
	
	/**
	 * Class containing item object inside queue
	 */
	public class QueueItem {
		private String itemID;
		private int customerID;
		private boolean removed;
		
		/**
		 * 
		 * @param customerID
		 * @param itemID
		 */
		public QueueItem(int customerID, String itemID) {
			this.itemID = itemID;
			this.customerID = customerID;
			
			removed = false;
		}
		
		/**
		 * Item identifier 
		 */
		public String getItemID() {
			return itemID;
		}
		
		/**
		 *	Customer identifier 
		 */
		public int getCustomerID() {
			return customerID;
		}
		
		/**
		 * State of item inside queue 
		 */
		public boolean isRemoved() {
			return removed;
		}
	}
	
	/**
	 * Constructor for OrderQueue class
	 */
	public OrderQueue(String recipeFile) {
		kitchenQueue = new LinkedList<QueueItem>();
		barQueue = new LinkedList<QueueItem>();
		recipeBook = new HashMap<String, ArrayList<String> >();
		this.recipeFile = recipeFile; 
		
		try {	   		
			// create buffered reader 
			recipeReader = new BufferedReader(new FileReader(recipeFile));		   		
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		}
		
		// fill recipe book 
		fillRecipeBook();
	}
	
	/**
	 * Fills recipe book 
	 */
	void fillRecipeBook() {
		String line = null;
		ArrayList<String> instructions = new ArrayList<String>();
		String itemId = null;
		
		while(true) {						
   			// add to recipe book
   			try {
   				line = recipeReader.readLine();
   			} catch (IOException e1) {
   				e1.printStackTrace();
   			}	
			
			if (line != null) {		
	   			//split by semicolon
	   			String[] data = line.split(";");
	       		
	   			// check whether line contains correct number of information 		       		
	       		if (data.length == 3) {	

			   		// add recipe to recipe book 
	       			if (instructions.size() > 0 && itemId != null) {
	       				
				   		recipeBook.put(itemId, new ArrayList<String>(instructions));		       			 
				   		instructions.clear(); 
	       			}	       			
	   				// assign item ID and clear instructions	
	       			itemId = data[0];     			
	   			} 
	       		
	       		// add instruction if line is not empty 
	   			else if (data.length == 1 && !data[0].trim().isEmpty())	   				
	   				instructions.add(data[0]);	 	   			 		   			
		   		
	       		// line empty 
	   			else 
	   				System.out.println("Invalid data line. Will drop it. \n");
			} 
			else {
				// add recipe to recipe book 
       			if (instructions.size() > 0 && itemId != null) 
       				recipeBook.put(itemId, new ArrayList<String>(instructions));
  
				break;
			}
		}		
	}
	
	/**
	 * Adds item to queue one thread at a time 
	 * @param customerID Customer identifier
	 * @param itemID Item identifier
	 * @param isBeverage True for beverage, false for solid food 
	 */
	synchronized void addToQueue(int customerID, String itemID, boolean isBeverage) {
		if (isBeverage)
			barQueue.add(new QueueItem (customerID, itemID));
		else
			kitchenQueue.add(new QueueItem (customerID, itemID));
	}
	
	/**
	 * Removes item from queue one thread at a time 
	 * @param isBeverage True for beverage, false for solid food 
	 * @return QueueItem removed 
	 */
	synchronized QueueItem removeHeadFromQueue(boolean isBeverage) {
		QueueItem head;
		if (isBeverage)
			head = barQueue.remove();
		else
			head = kitchenQueue.remove();
		head.removed = true;
		return head;
	}
	
	public static void main(String[] args) {
		/*OrderQueue queue = new OrderQueue("RecipeBook");
		
		
		System.out.println("\n");
		for (String item: queue.recipeBook.keySet()) {
			System.out.println(item);
			System.out.println(queue.recipeBook.get(item).toString());
		}*/
	}
}