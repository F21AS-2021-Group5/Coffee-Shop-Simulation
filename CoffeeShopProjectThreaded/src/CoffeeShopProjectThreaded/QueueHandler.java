/**
 * QueueHandler.java - class to implement thread that adds customers to queues 
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

import CoffeeShopProjectThreaded.CustomerQueue.CustomerQueueOutput;

public class QueueHandler implements Runnable{

	private CustomerQueue shopQueue;
	private CustomerQueue onlineQueue;
	private long delay;
	private int maxCustomerNumOnline;
	private int maxCustomerNumShop;
	
	private Log log; // used for logging data 
	
	/**
	 * Constructor for QueueHandler class
	 * @param onlineQueue Online queue of customers
	 * @param shopQueue In-shop queue of customers
	 * @param delay Delay between each customer added to the queues 
	 */
	public QueueHandler(CustomerQueue onlineQueue, 
			CustomerQueue shopQueue, long delay) {
		this.onlineQueue = onlineQueue;
		this.shopQueue = shopQueue;
		this.delay = delay;
		
		maxCustomerNumOnline = onlineQueue.getMaxCustomerNumber();
		maxCustomerNumShop = shopQueue.getMaxCustomerNumber();		
		
		log = Log.getInstance();
	}
	
	/**
	 * Main thread method to add customers to online and in-shop queues 
	 */
	@Override
	public void run() {	
		int maxNum = (maxCustomerNumOnline > maxCustomerNumShop) ? maxCustomerNumOnline : maxCustomerNumShop;
		
		for(int i = 0; i < maxNum; i++) {
			// every number of milliseconds, add customer to end of queue 
			try {			
				// if online queue exists add to it 
				if (i < maxCustomerNumOnline) {
					CustomerQueueOutput out = onlineQueue.addToQueue();
					CoffeeShop.customerList.put(out.getCustomer().getId(), out.getCustomer());
					
					log.updateLog("[QueueHandler]: "+ "Queue Handler added customer " + out.getCustomer().getName() + 
							" (ID: " + out.getCustomer().getId() + ") to online queue -> updated size: " 
							+ out.getUpdatedSize());
				}

				if (i < maxCustomerNumShop) {
					// add to in-shop queue 
					CustomerQueueOutput out = shopQueue.addToQueue();
					CoffeeShop.customerList.put(out.getCustomer().getId(), out.getCustomer());
					
					log.updateLog("[QueueHandler]: "+ "Queue Handler added customer " + out.getCustomer().getName() + 
							" (ID: " + out.getCustomer().getId() + ") to in-shop queue -> updated size: " 
							+ out.getUpdatedSize());
				}						

				// delay for visualization purposes 
				Thread.sleep(onlineQueue.getDelay());
				
			// catch exception for calling sleep() function
			} catch (InterruptedException e) {
				log.updateLog("[QueueHandler]: "+ "Interuption in queueHandler");
				System.out.println(e.getMessage());
			}
		}		
	}	
}
