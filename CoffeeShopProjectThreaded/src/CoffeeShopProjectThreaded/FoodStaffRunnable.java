/**
 * FoodStaffRunnable.java - class to implement a barista/cook staff thread
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

import CoffeeShopProjectThreaded.OrderQueue.OrderQueueOutput;

public class FoodStaffRunnable implements Runnable{

	private String currentItem;
	private String currentCustomer;
	
	private OrderQueue queue;
	private Log log;
	
	private FoodStaff foodStaff;	
	
	/**
	 * Constructor for Staff class
	 * @param foodStaff FoodStaff object 
	 * @param queue Queue of orders 
	 */
	public FoodStaffRunnable(FoodStaff foodStaff, OrderQueue queue) { 
		this.foodStaff = foodStaff;
		this.queue = queue;
		
		log = Log.getInstance();
	}
	
	/**
	 * @return Food staff object
	 */
	public FoodStaff getFoodStaff() {
		return foodStaff;
	}
	
	/**
	 * Sets the food staff object
	 * @param foodStaff Food staff object
	 */
	public void setFoodStaff(FoodStaff foodStaff) {
		this.foodStaff = foodStaff;
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
		boolean stop = false; 
		while (!stop) {	
			
			try {
				// remove item from queue 
				OrderQueueOutput out = queue.removeFromQueue();
				
				String status = "";
				
				// check if operation successful
				if (out.isSuccess())
				{
					currentItem = out.getItem().getName();
					currentCustomer = out.getItem().getCustomerID();		
					
					foodStaff.setCurrentCustomer(CoffeeShop.customerList.get(currentCustomer));
					foodStaff.setCurrentItem(out.getItem());
					
					// get recipe of current item 
					ArrayList<String> recipe = CoffeeShop.recipeBook.get(currentItem);
					
					if (recipe == null)
						System.out.println("Item does not exist in the menu.");
					
					// go through recipe instructions 
					for (String instruction: recipe) {
						
						// update status 
						status = "[FoodStaffRunable]: "+" Preparation: " + foodStaff.getType() + " " + foodStaff.getName() 
							+ ": " + instruction;
						log.updateLog(status);
						
						foodStaff.setInstruction(instruction);
						Long delay = foodStaff.getDelay();
						
						
						// delay for visualisation purposes 
						
						try {
							Thread.sleep(delay);
						} catch (InterruptedException e) {	
							status = "[FoodStaffRunable]: "+foodStaff.getType()+" "+ foodStaff.getName()+ " has finished their shift";
							log.updateLog(status);
							stop = true;
						}
					}
					
					// update status				
					status = "[FoodStaffRunable]: "+" Finished: " + foodStaff.getType() + " " + foodStaff.getName() + ": " + currentItem + 
							" for customer " + foodStaff.getCurrentCustomer().getName() + 
							" (ID: " + currentCustomer + ") prepared.";			
					log.updateLog(status);
					
					
					if(CoffeeShop.employees.getActiveCashiers().isEmpty()) {
						stop = true;
						foodStaff.EndedShift(foodStaff.getName());
					}
					// Close the baristas and cooks
					
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}				
		}
	}
}
