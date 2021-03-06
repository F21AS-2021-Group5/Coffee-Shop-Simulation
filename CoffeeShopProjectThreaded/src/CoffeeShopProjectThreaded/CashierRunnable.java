/**
 * CashierRunnable.java - class to implement the thread cashier for the coffee shop simulation
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
import CoffeeShopProjectThreaded.Inventory.InventoryOutput;
import CoffeeShopProjectThreaded.Bookkeeping.BookkeepingOutput;

public class CashierRunnable implements Runnable{
	
	private Customer currentCustomer;
	private String name;
	
	// used for logging data 
	private Log log;
	
	// constructor initialized variables 
	private CustomerQueue onlineQueue;
	private CustomerQueue shopQueue;
	private OrderQueue kitchenQueue;
	private OrderQueue barQueue;
	private Inventory inventory;
	private Bookkeeping books;	
	private Cashier cashier;
	
	/**
	 * Constructor for Cashier class 
	 * @param name Cashier name
	 * @param onlineQueue Online queue of customers
	 * @param shopQueue In-shop queue of customers
	 * @param kitchenQueue Ordered items to be prepared by the kitchen staff
	 * @param barQueue Ordered items to be prepared by the bar staff
	 * @param inventory Inventory of items ordered 
	 * @param books Storage for economically relevant values 
	 * @param cashier Cashier object instance 
	 */
	public CashierRunnable(String name, CustomerQueue onlineQueue,
			CustomerQueue shopQueue, OrderQueue kitchenQueue, 
			OrderQueue barQueue, Inventory inventory, Bookkeeping books, Cashier cashier) {
		
		this.currentCustomer = null;
		this.name = name;
		this.onlineQueue = onlineQueue;
		this.shopQueue = shopQueue;
		this.kitchenQueue = kitchenQueue;
		this.barQueue = barQueue;
		this.inventory =inventory;
		this.books = books;
		this.cashier = cashier;
		
		log = Log.getInstance();
	}
	
	@Override
	public void run() {
		
		// boolean used to stop the thread if exception is thrown 
		boolean stop = false; 
		
		while (!stop) {			
			/* Removes first customer from queues based on priorities */
			
			String whichQueue = "in-shop";
			CustomerQueueOutput out = null;
			
			// if online queue empty, go to in-shop queue 
			if (onlineQueue.getQueue().isEmpty()) {
				
				// shop queue is not empty, online is empty
				if (!shopQueue.getQueue().isEmpty())
				{
					log.updateLog("[CashierRunnable]: " +"Cashier " + name + " checking in-shop queue -> current size: " + shopQueue.getQueue().size());
					
					out = shopQueue.removeFromQueue();
					currentCustomer = out.getCustomer();
					cashier.setCustomer(currentCustomer);					
					
				// both queues are empty 
				} else {
					// if queue handler is not adding any more customers to queue, stop cashier 
					if (!CoffeeShop.handlerThread.isAlive()) {
						CoffeeShop.employees.getActiveCashiers().remove(name);
		    			stop = true;
		    			cashier.EndedShift(cashier.getName());

		    		} else {
		    		
						// if online queue is not locked by other thread, go wait in its queue
						if (!onlineQueue.isLocked()) {
							log.updateLog("[CashierRunnable]: " +"Cashier " + name + " entering waiting state in online queue -> current size: " + onlineQueue.getQueue().size());
							out = onlineQueue.removeFromQueue();
							currentCustomer = out.getCustomer();
							cashier.setCustomer(currentCustomer);
							whichQueue = "online";
							
						} else {
							// if online is locked, and shop is not locked, go wait in its queue
							if (!shopQueue.isLocked()) {
								log.updateLog("[CashierRunnable]: " +"Cashier " + name + " entering waiting state in in-shop queue -> current size: " + onlineQueue.getQueue().size());
								out = shopQueue.removeFromQueue();
								currentCustomer = out.getCustomer();
								cashier.setCustomer(currentCustomer);
							}
						}	
		    		}
				}
			
				// if online queue not empty, go to it 
				} else {
					log.updateLog("[CashierRunnable]: " +"Cashier " + name + " checking online queue -> current size: " + shopQueue.getQueue().size());
					out = onlineQueue.removeFromQueue();
					currentCustomer = out.getCustomer();
					cashier.setCustomer(currentCustomer);
					whichQueue = "online";							
				}
					
			/* Adds the removed customer's processed orders to queues for food preparation */
			if (currentCustomer != null) {
				for (String item: currentCustomer.getCart().keySet()) {
					try {
						if (isBarFood(item))
							barQueue.addToQueue(currentCustomer.getId(), item);
						else 
							kitchenQueue.addToQueue(currentCustomer.getId(), item);
					} catch (NoMatchingMenuItemIDException e) {
						log.updateLog("[CashierRunnable]: " +"Cashier " + name + " NoMatchingMenuItemIDException ");
						e.printStackTrace();
					}
				}
			}			
			
			// checks if output was successful 
			if (out != null && out.isSuccess()) {

				// calculates total prices, taxes and discounts 
				cashier.runCashier();
				
				//logs information and adds it to books and inventory 
				log.updateLog("[CashierRunnable]: " +"Cashier " + name + " removed customer " + currentCustomer.getName() + " (ID: " +
						currentCustomer.getId() + ") from " + whichQueue + " queue -> updated size: " + 
						out.getUpdatedSize());
				
				InventoryOutput out1 = inventory.addToInventory(currentCustomer);
				if (out1.isSuccess()) {
					log.updateLog("[CashierRunnable]: " +"Cashier " + name + " new inventory size: "+ out1.updatedSize);
				}
				
				BookkeepingOutput out2 = books.upDateBooks(cashier.returnSums());
				if (out2.isSuccess()) {	
					log.updateLog("[CashierRunnable]: " +"Cashier " + name + " total of customers: "+ out2.getNumberOfCustomers());
				}
			}	
			
			Long delay = cashier.getDelay();
			
			// delays the thread for visualisation purposes 
			try {
				Thread.sleep(delay);
				
			} catch(InterruptedException e) {
								
				stop = true;  // KILLS THE THREAD 
				log.updateLog("[CashierRunnable]: " +"Cashier " + name + " has ended their shift ");				
			}	
		}
	}
	
	/**
	 * Determines if item is prepared by barista or cook
	 * @param item Name of item
	 * @return True if prepared by barista, false if by cook 
	 * @throws NoMatchingMenuItemIDException Menu item does not exist 
	 */
	boolean isBarFood(String name) throws NoMatchingMenuItemIDException {	
		
		if (CoffeeShop.foodMap.get("Bar").contains(name))
			return true;
		if (!CoffeeShop.foodMap.get("Kitchen").contains(name))
			throw new NoMatchingMenuItemIDException();
		return false;
	}


}
