/**
 * Cook.java - class to implement a cook for the coffeeshop
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

import CoffeeShopProjectThreaded.OrderQueue.QueueItem;

public class Cook implements Runnable{

	String currentItem;
	int currentCustomer;
	
	OrderQueue queue;
	private Log log;
	
	/**
	 * Constructor for Cook class
	 * @param queue Queue of orders 
	 */
	public Cook(OrderQueue queue) { 
		this.queue = queue;
	}
	
	/**
	 * While the queue is not empty, complete orders (NOT FINISHED0
	 */
	@Override
	public void run() {
		while (!queue.kitchenQueue.isEmpty()) {
			QueueItem head = queue.removeHeadFromQueue(false);			

			if (head.isRemoved()) {
				currentItem = head.getItemID();
				currentCustomer = head.getCustomerID();		
				
				processItem();
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				System.out.println("Item " + head.getItemID() + " for customer " +
						head.getCustomerID() + " prepared.");
				log.updateLog("Item " + head.getItemID() + " for customer " +
						head.getCustomerID() + " prepared.");
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Processes each item ordered (NOT FINISHED)
	 */
	void processItem() {
		log.updateLog("Cooking food in process");
		System.out.println("Cooking food...");
	}
}
