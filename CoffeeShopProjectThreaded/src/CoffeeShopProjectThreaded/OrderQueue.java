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

import java.util.Deque;
import java.util.LinkedList;

public class OrderQueue {
	
	Deque<QueueItem> kitchenQueue; // stores customer ID and item ID
	Deque<QueueItem> barQueue; 
	
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
	public OrderQueue() {
		kitchenQueue = new LinkedList<QueueItem>();
		barQueue = new LinkedList<QueueItem>();
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
}