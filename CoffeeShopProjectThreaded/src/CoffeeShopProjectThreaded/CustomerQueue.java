/**
 * CustomerQueue.java - class to implement the queues for the customers 
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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Deque;
import java.util.LinkedList;

public class CustomerQueue implements Runnable{
	
	// in-shop queue and online queue 
	Deque<Customer> shopQueue;
	Deque<Customer> onlineQueue;
	
	// delay between thread iterations
	long delay;
	
	// variables for text file reading 
	BufferedReader shopReader;
	BufferedReader onlineReader;	
	
	/**
	 * Constructor for CustomerQueue class
	 */
	public CustomerQueue(String shopQueueFile, String onlineQueueFile, long delay) {
		// initialize queues 
		shopQueue = new LinkedList<Customer>();
		onlineQueue = new LinkedList<Customer>();				
		
		// set delay 
		this.delay = delay;
		
		// initialize readers 
		try {
			shopReader = new BufferedReader(new FileReader(shopQueueFile));
			onlineReader = new BufferedReader(new FileReader(onlineQueueFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		}
	}

	@Override
	public void run() {		
		// initialize lines in respective text files 
		String online = null; 
		String inshop = null;		
		
		while (true) {
			// every number of milliseconds, add customer to end of queue 
			try {
				// add to queue while prioritizing the online queue 
				online = onlineReader.readLine();	
				
				if (online != null) 
					addToQueue(online, true);
				else {
					inshop = shopReader.readLine();
					if (inshop != null) 
						addToQueue(inshop, false);
				}
				
				if (online == null && inshop == null)
					break;
				
				// delay for visualization purposes 
				Thread.sleep(delay);
				
			// catch exception for calling sleep() function
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	

   /**
    * Fills the customer queue with information from text file
    * @param line Line from text file containing customer information 
    * @param online Online or in-shop queue
    */
   public void addToQueue(String line, boolean online){

	   try {	   		
	   		// while loop variables 
	   		Customer customer = null;
	   		int count = 0;		   		
	   		do {  	       		
	       		//split by semicolon
	   			String[] data = line.split(";");
	       		
	   			// check whether line contains correct number of information 
		       	if (data.length == 4) {
		       		
		       		LocalDateTime timeStamp = CoffeeShop.stringToTimestamp(data[3]);
		       		
		   			if (count == 0) {		
		   				// create customer 			    	        	            	   
			    	    customer = new Customer(data[1], data[0], timeStamp); 
			    	    count++;
		   			} 
		   			else {
		   				customer.addItem(data[2], 1, timeStamp);
		   				try {
			    			   if (online)
			    				   line = onlineReader.readLine();
			    			   else
			    				   line = shopReader.readLine();
		    		   } catch (IOException e) {
		    			   e.printStackTrace();
		    		   }
		   			}
	   			} else {
	   					System.out.println("Invalid data line. Will drop it. \n");
			    }
	   		}
	   		while (line != null && customer.getId().equals(line.split(";")[0]));		   	      
	    	   
    	   // add to deque
		   boolean success = false;
		   if (online)
			   success = onlineQueue.add(customer);
		   else
			   success = shopQueue.add(customer);
		   
		   if (!success)
			   System.out.println("Customer object not added to queue.");
			   
		   System.out.println(customer.receipt());		   
	    	   
	   // catch all exceptions 
		       
	   } catch (IllegalArgumentException e){ 
		   throw new IllegalArgumentException();
		   
	   } catch (IllegalStateException e) {
		   throw new IllegalStateException();
		   
	   } catch (InvalidMenuItemQuantityException e) {
		   e.printStackTrace();
		   
	   } catch (InvalidMenuItemDataException e) {
		   e.printStackTrace();
		   
	   }
   }
}
