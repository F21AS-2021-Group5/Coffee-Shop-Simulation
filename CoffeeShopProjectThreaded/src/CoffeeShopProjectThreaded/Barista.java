/**
 * Barista.java - class to implement a barista for the coffeeshop
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

public class Barista implements Runnable{

	private String currentItem;
	private int currentCustomer;
	
	private OrderQueue queue;
	private Log log;
	
	/**
	 * Constructor for Barista class
	 * @param queue Queue of orders 
	 */
	public Barista(OrderQueue queue) { 
		this.queue = queue;
		currentItem = null;
		currentCustomer = -1;
		log = Log.getInstance();
	}
	
	/**
	 * While the queue is not empty, complete orders (NOT FINISHED0
	 */
	@Override
	public void run() {
		while (!queue.barQueue.isEmpty()) {			
			QueueItem head = queue.removeHeadFromQueue(true);
			
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
		
		System.out.println("Making beverage...");
	}
}