/**
 * Staff.java - class to implement a staff for the coffeeshop
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

import CoffeeShopProjectThreaded.OrderQueue.OperationOutput;
import CoffeeShopProjectThreaded.OrderQueue.QueueItem;

public class Staff implements Runnable{

	private String currentItem;
	private String currentCustomer;
	
	private OrderQueue queue;
	private Log log;
	
	private long delay;
	
	private String name;
	private String type;
	
	/**
	 * Constructor for Staff class
	 * @param queue Queue of orders 
	 */
	public Staff(String name, OrderQueue queue, long delay) { 
		this.name = name;
		this.queue = queue;
		this.delay = delay;
		
		type = (queue.isBar()) ? "Barista" : "Cook";
		log = Log.getInstance();
	}
	
	/**
	 * @return Staff name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set name of staff 
	 * @param name Staff name
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
		while (true) {	

			OrderQueue.OperationOutput out = queue.removeFromQueue();
			
			String status = "";
			if (out.isSuccess())
			{
				currentItem = out.getItem().getItemID();
				currentCustomer = out.getItem().getCustomerID();		
				
				// get recipe of current item 
				ArrayList<String> recipe = CoffeeShop.recipeBook.get(currentItem);
				
				// go through recipe instructions 
				for (String instruction: recipe) {
					
					// update status 
					status = "[PREPARATION] " + type+ " " + name + ": " + instruction;
					log.updateLog(status);
					
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				// update status 
				status = "[FINISHED] " + type + " " + name + ": " + CoffeeShop.menu.get(out.getItem().getItemID()) + 
						" for customer " + CoffeeShop.customerList.get(out.getItem().getCustomerID()) + " prepared.";			
				log.updateLog(status);				
			}
			/*	
			try {
				Thread.sleep(delay);
			}catch(InterruptedException e) {
				//Thread.currentThread().interrupt();
				System.out.println(e.getMessage());
			}	
			*/
		
		}
	}
}
