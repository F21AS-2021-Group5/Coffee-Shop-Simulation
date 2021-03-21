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

import java.util.ArrayList;

import CoffeeShopProjectThreaded.OrderQueue.QueueItem;

public class Barista implements Runnable{

	private String currentItem;
	private String currentCustomer;
	
	private OrderQueue queue;
	private Log log;
	
	private long delay;
	
	private String name;
	
	/**
	 * Constructor for Barista class
	 * @param queue Queue of orders 
	 */
	public Barista(String name, OrderQueue queue, long delay) { 
		this.name = name;
		this.queue = queue;
		this.delay = delay;
		
		currentItem = null;
		currentCustomer = "";
		log = Log.getInstance();
	}
	
	/**
	 * @return Barista name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set name of barista 
	 * @param name Barista name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return Current item identifier
	 */
	public String getCurrentItem() {
		return currentItem;
	}
	
	/**
	 * Sets the current item identifier
	 * @param currentItem Current item identifier
	 */
	public void setCurrentItem(String currentItem) {
		this.currentItem = currentItem;
	}
	
	/**
	 * @return Current customer identifier
	 */
	public String getCurrentCustomer() {
		return currentCustomer;
	}
	
	/**
	 * Sets the current customer identifier
	 * @param currentCustomer Current customer identifier
	 */
	public void setCurrentCustomer(String currentCustomer) {
		this.currentCustomer = currentCustomer;
	}
	
	/**
	 * @return OrderQueue object 
	 */
	public OrderQueue getQueue() {
		return queue;
	}
	
	/**
	 * Sets the OrderQueue object 
	 * @param queue OrderQueue object 
	 */
	public void setQueue(OrderQueue queue) {
		this.queue = queue;
	}
	
	/**
	 * While the queue is not empty, complete orders 
	 */
	@Override
	public void run() {
		while (!queue.getBarQueue().isEmpty()) {			
			QueueItem head = queue.removeHeadFromQueue(true);
			String status = "";
			
			if (head.isRemoved()) {
				currentItem = head.getItemID();
				currentCustomer = head.getCustomerID();		
				
				// get recipe of current item 
				ArrayList<String> recipe = queue.getRecipeBook().get(currentItem);
				
				// go through recipe instructions 
				for (String instruction: recipe) {
					
					// update status 
					status = "[PREPARATION] Barista " + name + ": " + instruction;
					log.updateLog(status);
					
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				// update status 
				status = "[FINISHED] Barista " + name + ": " + CoffeeShop.menu.get(head.getItemID()) + 
						" for customer " + CoffeeShop.customerList.get(head.getCustomerID()) + " prepared.";			
				log.updateLog(status);
				
				/*try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
			}
		}
	}
}