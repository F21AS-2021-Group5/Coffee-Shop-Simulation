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
	
	private Deque<OrderQueueItem> queue; // stores customer ID and item ID
	private boolean isBar; // bar or kitchen food boolean 
	
	/**
	 * Constructor for OrderQueue class
	 * @param isBar Whether queue stores bar or kitchen food 
	 */
	public OrderQueue(boolean isBar) {
		this.isBar = isBar;	
		
		queue = new LinkedList<OrderQueueItem>();
	}
	
	public class OrderQueueOutput {
		
		private OrderQueueItem item;
		private boolean success;
		private int updatedSize;
		
		/**
		 * Constructor for OrderQueueOutput class
		 * @param item Order queue item object
		 * @param success Successful operation or not
		 * @param updatedSize Updated size of customer queue after operation
		 */
		public OrderQueueOutput(OrderQueueItem item, boolean success, int updatedSize) {
			this.item = item;
			this.success = success;
			this.updatedSize = updatedSize;
		}		

		/**
		 * @return Order queue item object
		 */
		public OrderQueueItem getItem() {
			return item;
		}
		
		/**
		 * Sets the order queue item object
		 * @param item Order queue item object
		 */
		public void setCustomer(OrderQueueItem item) {
			this.item = item;
		}
		
		/**
		 * @return Successful operation or not 
		 */
		public boolean isSuccess() {
			return success;
		}		
		
		/**
		 * Sets the success variable 
		 * @param success Successful operation or not 
		 */
		public void setSuccess(boolean success) {
			this.success = success;
		}
		
		/**
		 * @return Updated size of customer queue 
		 */
		public int getUpdatedSize() {
			return updatedSize;
		}
		
		/**
		 * Sets the updated size of the queue 
		 * @param updatedSize Updated size of customer queue 
		 */
		public void setUpdatedSize(int updatedSize) {
			this.updatedSize = updatedSize;
		}
	}	

	/**
	 * Class containing item object inside queue
	 */
	public class OrderQueueItem {
		private String name;
		private String customerID;
		
		/**
		 * Constructor for QueueItem class
		 * @param customerID Customer identifier
		 * @param name Item name
		 */
		public OrderQueueItem(String customerID, String name) {
			this.name = name;
			this.customerID = customerID;
		}
		
		/**
		 * @return Item name 
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * Sets the item name
		 * @param name Item name 
		 */
		public void setItem(String name) {
			this.name = name;
		}
		
		/**
		 *	@return Customer identifier 
		 */
		public String getCustomerID() {
			return customerID;
		}
		
		/**
		 * Sets the customer ID
		 * @param customerID Customer identifier 
		 */
		public void setCustomerID(String customerID) {
			this.customerID = customerID;
		}
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
	public Deque<OrderQueueItem> getQueue() {
		return queue;
	}

	/**
	 * Food orders queue 
	 * @param queue Food orders queue 
	 */
	public void setQueue(Deque<OrderQueueItem> queue) {
		this.queue = queue;
	}	

	/**
	 * Adds item to queue one thread at a time 
	 * @param customerID Customer identifier
	 * @param itemID Item identifier
	 * @return OrderQueueOutput item added, if successful or not, updated queue size 
	 */
	public synchronized OrderQueueOutput addToQueue(String customerID, String itemID) {
		OrderQueueItem item = null;
		
		// create instance based on parameters
		try {
			item = new OrderQueueItem (customerID, itemID);
			
			// add item and notify all threads that resource can be accessed again
			queue.add(item);	
			notifyAll();			
		}
		catch (Exception e) {
			return new OrderQueueOutput(item, false, queue.size());
		}
		return new OrderQueueOutput(item, true, queue.size());
	}
	
	/**
	 * Removes item from queue one thread at a time 
	 * @return Queue item added, if successful or not, updated queue size 
	 */
	public synchronized OrderQueueOutput removeFromQueue() {
		OrderQueueItem item;
		
		// thread waits until queue is not empty anymore
		while (queue.isEmpty()) {
    		try { 
    			wait(); 
    		} 
    		catch (InterruptedException e) {} 
		}
		
		// removes item and notify all threads that resource can be accessed again
		item = queue.pop();
		notifyAll();
		
		// accordingly returns the output
		if (item == null)
			return new OrderQueueOutput(item, false, queue.size());
		else	
			return new OrderQueueOutput(item, true, queue.size());
	}
}