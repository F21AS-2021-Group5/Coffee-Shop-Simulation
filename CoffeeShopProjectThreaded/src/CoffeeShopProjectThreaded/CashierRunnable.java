/**
 * CashierTrial.java - class to implement the thread cashier for the coffee shop simulation
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import CoffeeShopProjectThreaded.NewCustomerQueue.CustomerQueueOutput;
import CoffeeShopProjectThreaded.Inventory.InventoryOutput;
import CoffeeShopProjectThreaded.Bookkeeping.BookkeepingOutput;

public class CashierRunnable implements Runnable{
	

	public static Customer currentCustomer;
	
	String name;
	// used for logging data 
	private Log log;
	
	// constructor initialized variables 
	String ID;
	Long delay;	
	NewCustomerQueue onlineQueue;
	NewCustomerQueue shopQueue;
	OrderQueue kitchenQueue;
	OrderQueue barQueue;
	Inventory inventory;
	Bookkeeping books;	
	Cashier cashier;
	
	/**
	 * Constructor for Cashier class 
	 * @param ID Cashier identifier
	 * @param delay Time delay between each operation
	 * @param onlineQueue Online queue of customers
	 * @param shopQueue In-shop queue of customers
	 * @param kitchenQueue Ordered items to be prepared by the kitchen staff
	 * @param barQueue Ordered items to be prepared by the bar staff
	 * @param inventory Inventory of items ordered 
	 * @param books Storage for economically relevant values 
	 * @param cashier Cashier object instance 
	 */
	public CashierRunnable(String name, Long delay, NewCustomerQueue onlineQueue,
			NewCustomerQueue shopQueue, OrderQueue kitchenQueue, 
			OrderQueue barQueue, Inventory inventory, Bookkeeping books, Cashier cashier) {
		
		this.currentCustomer = null;

		this.name =name;
		this.ID = ID;

		this.delay = delay;
		this.onlineQueue = onlineQueue;
		this.shopQueue = shopQueue;
		this.kitchenQueue = kitchenQueue;
		this.barQueue = barQueue;
		this.inventory =inventory;
		this.books = books;
		this.cashier = cashier;
		
		log = Log.getInstance();

	}
	
	/**
	 * Constructor for Cashier class
	 * @param ID Cashier identifier
	 */
	public CashierRunnable(String ID, Long delay, NewCustomerQueue shopQueue,
			OrderQueue kitchenQueue, OrderQueue barQueue) {
		this.currentCustomer = null;
		this.name =name;
		this.delay = delay;
		this.shopQueue = shopQueue;
		this.kitchenQueue = kitchenQueue;
		this.barQueue = barQueue;

		
		log = Log.getInstance();

	}
	
	/**
	 * @return Cashier identifier 
	 */
	public String getID() {
		return name;
	}
	
	@Override
	public void run() {
		
		// boolean used to stop the thread if exception is thrown 
		boolean stop = false; 
		
		while (!stop) {
			

			System.out.println("Cashier " + name + " checking queue -> size: " + shopQueue.getQueue().size());

			/* Removes first customer from queues based on priorities */
			
			String whichQueue = "in-shop";
			CustomerQueueOutput out = null;

			
			log.updateLog("Cashier " + name + " checking in-shop queue -> current size: " + shopQueue.getQueue().size());
			
			// check if online queue exists 
			if (onlineQueue != null) {
				log.updateLog("Cashier " + name + " checking online queue -> current size: " + onlineQueue.getQueue().size());
				
				// if online queue empty, go to in-shop queue 
				if (onlineQueue.getQueue().isEmpty()) {
					if (!shopQueue.getQueue().isEmpty())
					{
						out = shopQueue.removeFromQueue();
						currentCustomer = out.getCustomer();
						cashier.setCustomer(currentCustomer);
					}
					
				// if online queue not empty, go to it 
				} else {
					out = onlineQueue.removeFromQueue();
					currentCustomer = out.getCustomer();
					cashier.setCustomer(currentCustomer);
					whichQueue = "online";
				}
			}
			else {
				// if no online queue, just process in-shop queue 
				out = shopQueue.removeFromQueue();
				currentCustomer = out.getCustomer();
				cashier.setCustomer(currentCustomer);
				
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
						e.printStackTrace();
					}
				}
			}
			
			
			// checks if output was successful 
			if (out != null && out.isSuccess()) {

				cashier.runCashier();
				System.out.println("Cashier " + name + " removed customer " + currentCustomer.getId() + " -> size: " + out.updatedSize);

				// calculates total prices, taxes and discounts 

				
				//logs information and adds it to books and inventory 
				log.updateLog("Cashier " + name + " removed customer " + currentCustomer.getName() + " (ID: " +
						currentCustomer.getId() + ") from " + whichQueue + " queue -> updated size: " + 
						out.getUpdatedSize());
				

				InventoryOutput out1 = inventory.addToInventory(currentCustomer);
				//if(out1.isSuccess()) {
					//System.out.println("Cashier " + ID + " -> inventory size: "+ out1.updatedSize);
				//}
				BookkeepingOutput out2 = books.upDateBooks(cashier.returnSums());
				if(out2.isSuccess()) {

					System.out.println("Cashier " + name + " -> total# of customers: "+ out2.numberOfCustomers);

					//log.updateLog("Cashier " + name + " -> total# of customers: "+ out2.numberOfCustomers);

				}
			}	
			//System.out.println(books.getCustomerNumber());
			
			// delays the thread for visualisation purposes 
			try {
				Thread.sleep(delay);
			}catch(InterruptedException e) {
				//Thread.currentThread().interrupt();
				stop = true;  ///// KILLS THE THREAD //////
				System.out.println(e.getMessage());				
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
