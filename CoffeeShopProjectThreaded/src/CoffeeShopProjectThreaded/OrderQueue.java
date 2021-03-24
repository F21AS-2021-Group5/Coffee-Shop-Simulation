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
	
	private Deque<QueueItem> queue; // stores customer ID and item ID
	private final Map<String, String> foodMap = new HashMap<String, String>();
	private boolean isBar;
	
	public class OperationOutput {
		QueueItem item;
		boolean success;
		int updatedSize;
		
		/**
		 * 
		 * @param customer
		 * @param success
		 */
		public OperationOutput(QueueItem item, boolean success, int updatedSize) {
			this.item = item;
			this.success = success;
			this.updatedSize = updatedSize;
		}
		
		/**
		 * 
		 * @return
		 */
		public QueueItem getItem() {
			return item;
		}
		
		/**
		 * 
		 * @return
		 */
		public boolean isSuccess() {
			return success;
		}
	}
	

	/**
	 * Class containing item object inside queue
	 */
	public class QueueItem {
		private String itemID;
		private String customerID;
		private boolean success;
		
		/**
		 * Constructor for QueueItem class
		 * @param customerID Customer identifier
		 * @param itemID Item identifier
		 */
		public QueueItem(String customerID, String itemID) {
			this.itemID = itemID;
			this.customerID = customerID;
			success = false;
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
		public boolean isSuccess() {
			return success;
		}
	}
	
	/**
	 * Constructor for OrderQueue class
	 */
	public OrderQueue(boolean isBar) {
		this.isBar = isBar;	
		
		queue = new LinkedList<QueueItem>();
		
		// initialize map 
		foodMap.put("FOOD", "Kitchen");
	    foodMap.put("PASTRY", "Bar");
	    foodMap.put("DRINK", "Bar");
	    foodMap.put("SIDE", "Kitchen");	    
	}
	
	/**
	 * @return True if bar queue, false if kitchen queue 
	 */
	public boolean isBar() {
		return isBar;
	}

	/**
	 * @return Food orders queue 
	 */
	public Deque<QueueItem> getQueue() {
		return queue;
	}

	/**
	 * Food orders queue 
	 * @param queue Food orders queue 
	 */
	public void setQueue(Deque<QueueItem> queue) {
		this.queue = queue;
	}	

	/**
	 * Adds item to queue one thread at a time 
	 * @param customerID Customer identifier
	 * @param itemID Item identifier
	 * @return Queue item added, if successful or not, updated queue size 
	 */
	public synchronized OperationOutput addToQueue(String customerID, String itemID) {
		QueueItem item = null;
		try {
			item = new QueueItem (customerID, itemID);
			queue.add(item);	
			notifyAll();			
		}
		catch (Exception e) {
			return new OperationOutput(item, false, queue.size());
		}
		return new OperationOutput(item, true, queue.size());
	}
	
	/**
	 * Removes item from queue one thread at a time 
	 * @return Queue item added, if successful or not, updated queue size 
	 */
	public synchronized OperationOutput removeFromQueue() {
		QueueItem item;
		
		while (queue.isEmpty()) {
    		try { 
    			wait(); 
    		} 
    		catch (InterruptedException e) {} 
		}
		
		item = queue.pop();
		notifyAll();
		
		if (item == null)
			return new OperationOutput(item, false, queue.size());
		else	
			return new OperationOutput(item, true, queue.size());
	}
	
	/**
	 * Determines if item is prepared by barista or cook
	 * @param itemId Item identifier 
	 * @return True if prepared by barista, false if by cook 
	 */
	boolean fromBar(String itemId) {
		String onlyChars = itemId.replaceAll("[^A-Za-z]+", "");
		if (foodMap.get(onlyChars) == "Bar")
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