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

import CoffeeShopProjectThreaded.NewCustomerQueue.OperationOutput;
import CoffeeShopProjectThreaded.Inventory.InventoryOutput;
import CoffeeShopProjectThreaded.Bookkeeping.BookkeepingOutput;

public class CashierTrial implements Runnable{
	
	public float subtotal;
	public float tax;
	public float discount;
	public float total;
	public String receipt;
	public static Customer currentCustomer;
	
	private static String line = String.format("%1$" + 55 + "s", "- \n").replace(' ', '-');
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	int discount1 = 0;
	int discount2 = 0;
	int discount3 = 0;
	
	String ID;
	Long delay;
	
	EndOfDay report = new EndOfDay();
	NewCustomerQueue onlineQueue;
	NewCustomerQueue shopQueue;
	OrderQueue kitchenQueue;
	OrderQueue barQueue;
	Inventory inventory;
	Bookkeeping books;
	Thread th;
	
	Cashier cashier;
	
	/**
	 * Constructor for Cashier class
	 * @param ID Cashier identifier
	 */
	public CashierTrial(String ID, Long delay, NewCustomerQueue onlineQueue,
			NewCustomerQueue shopQueue, OrderQueue kitchenQueue, 
			OrderQueue barQueue, Inventory inventory, Bookkeeping books, Cashier cashier) {
		currentCustomer = null;
		this.ID =ID;
		this.delay = delay;
		this.onlineQueue = onlineQueue;
		this.shopQueue = shopQueue;
		this.kitchenQueue = kitchenQueue;
		this.barQueue = barQueue;
		this.inventory =inventory;
		this.books = books;
		this.cashier = cashier;
	}
	
	/**
	 * Constructor for Cashier class
	 * @param ID Cashier identifier
	 */
	public CashierTrial(String ID, Long delay, NewCustomerQueue shopQueue,
			OrderQueue kitchenQueue, OrderQueue barQueue) {
		currentCustomer = null;
		this.ID =ID;
		this.delay = delay;
		this.shopQueue = shopQueue;
		this.kitchenQueue = kitchenQueue;
		this.barQueue = barQueue;
	}
	
	/**
	 * @return Cashier identifier 
	 */
	public String getID() {
		return ID;
	}
	
	@Override
	public void run() {
		
		boolean stop = false;
		while (!stop) {
			
			System.out.println("Cashier " + ID + " checking queue -> size: " + shopQueue.getQueue().size());
			
			OperationOutput out = shopQueue.removeFromQueue();
			currentCustomer = out.getCustomer();
			cashier.setCustomer(currentCustomer);
			
			///// ADDING ITEM TO QUEUE ////
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
			/////////////////////////////
			
			if (out.isSuccess()) {
				cashier.getCartSubtotalPrice();
				cashier.getCartTax();
				cashier.getDiscount();
				cashier.getCartTotalPrice();
				System.out.println("Cashier " + ID + " removed customer " + currentCustomer.getId() + " -> size: " + out.updatedSize);
				InventoryOutput out1 = inventory.addToInventory(currentCustomer);
				//if(out1.isSuccess()) {
					//System.out.println("Cashier " + ID + " -> inventory size: "+ out1.updatedSize);
				//}
				BookkeepingOutput out2 = books.upDateBooks(cashier.returnSums());
				if(out2.isSuccess()) {
					System.out.println("Cashier " + ID + " -> total# of customers: "+ out2.numberOfCustomers);
				}
			}	
			//System.out.println(books.getCustomerNumber());
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
	
	/**
	 * @return Final report for customer 
	 */
	public String generateFinalReport() {
		return report.generateFinalReportDisplay();
	}
	
	/**
	 * Writes report to text file 
	 */
	public void generateCustomerReport() {
		   try {
			   FileWriter customerWriter = new FileWriter("Receit " + currentCustomer.getId() + ".txt");
			   customerWriter.write(receipt);
			   customerWriter.close();
		   } catch (IOException e) {
			   System.out.println("An error occurred.");
			   e.printStackTrace();
		   }
	}
	
	/**
	 * Calls function that generates the final report text file 
	 */
	public void generateFinalReportFile() {
		report.generateFinalReport(); 
	}
	
	
	

}
