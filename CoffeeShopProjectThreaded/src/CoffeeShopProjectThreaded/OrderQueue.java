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
import java.util.Map;

public class OrderQueue {
	
	private Deque<QueueItem> kitchenQueue; // stores customer ID and item ID
	private Deque<QueueItem> barQueue; 
	
	private HashMap<String, ArrayList<String> > recipeBook; // stores recipes
	private String recipeFile;
	private BufferedReader recipeReader;
	
	private final Map<String, String> food = new HashMap<String, String>();

	/**
	 * Class containing item object inside queue
	 */
	public class QueueItem {
		private String itemID;
		private String customerID;
		private boolean removed;
		
		/**
		 * Constructor for QueueItem class
		 * @param customerID Customer identifier
		 * @param itemID Item identifier
		 */
		public QueueItem(String customerID, String itemID) {
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
		public String getCustomerID() {
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
		
		// initialize map 
		food.put("FOOD", "Kitchen");
	    food.put("PASTRY", "Bar");
	    food.put("DRINK", "Bar");
	    food.put("SIDE", "Kitchen");	    
	}
	
	/**
	 * @return Kitchen food orders queue 
	 */
	public Deque<QueueItem> getKitchenQueue() {
		return kitchenQueue;
	}

	/**
	 * Kitchen food orders queue 
	 * @param kitchenQueue Kitchen food orders queue 
	 */
	public void setKitchenQueue(Deque<QueueItem> kitchenQueue) {
		this.kitchenQueue = kitchenQueue;
	}

	/** 
	 * @return Bar beverages orders queue 
	 */
	public Deque<QueueItem> getBarQueue() {
		return barQueue;
	}
	
	/**
	 * Bar beverages orders queue 
	 * @param barQueue Bar beverages orders queue 
	 */
	public void setBarQueue(Deque<QueueItem> barQueue) {
		this.barQueue = barQueue;
	}
	
	/**
	 * @return Recipe book
	 */
	public HashMap<String, ArrayList<String>> getRecipeBook() {
		return recipeBook;
	}
	
	/**
	 *  Recipe book
	 * @param recipeBook Recipe book
	 */
	public void setRecipeBook(HashMap<String, ArrayList<String>> recipeBook) {
		this.recipeBook = recipeBook;
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
	 */
	synchronized void addToQueue(String customerID, String itemID) {
		if (fromBar(itemID))
			barQueue.add(new QueueItem (customerID, itemID));
		else
			kitchenQueue.add(new QueueItem (customerID, itemID));
	}
	
	/**
	 * Removes item from queue one thread at a time 
	 * @param fromBarQueue True for item inside barQueue, false for inside kitchenQueue 
	 * @return QueueItem removed 
	 */
	synchronized QueueItem removeHeadFromQueue(boolean fromBarQueue) {
		QueueItem head;
		if (fromBarQueue)
			head = barQueue.remove();
		else
			head = kitchenQueue.remove();
		head.removed = true;
		return head;
	}
	
	/**
	 * Determines if item is prepared by barista or cook
	 * @param itemId Item identifier 
	 * @return True if prepared by barista, false if by cook 
	 */
	boolean fromBar(String itemId) {
		String onlyChars = itemId.replaceAll("[^A-Za-z]+", "");
		if (food.get(onlyChars) == "Bar")
			return true;
		return false;
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