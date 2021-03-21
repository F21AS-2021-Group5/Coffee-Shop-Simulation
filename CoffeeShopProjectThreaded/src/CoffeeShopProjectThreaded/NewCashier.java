/**
 * NewCashier.java - cashier class, each cashier is a thread dealing with customers orders 
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

import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;


public class NewCashier implements Runnable{
	
	public String ID;	//Cashier ID
	public String name; //Cashier name
	public Customer currentCustomer;
	public CustomerQueue queue;
	public HashMap<Customer, String> allcustomers; //NEED TO CHANGE THIS LATER
	private NewGUI GUI;
	private static int cashierCount = 0;
	public int cashierNumber = 0;
	private int delay = 1000; //delay for thread
	
	private Log log;
	
	/**
	 * Constructor for NewCashier class
	 */
	public NewCashier(String name, String id, HashMap<Customer, String> queue, NewGUI gui) {
		this.ID = id;
		this.name = name;
		queue = new HashMap<>();
		this.GUI = gui;
		cashierNumber = cashierCount++;
	}
	
	
	@Override
	public void run() {
		
		// TODO Auto-generated method stub
		while(true) {
			//First manage online customers (they have priority)
			while(!(queue.getOnlineQueue().isEmpty())) {
				System.out.println("Currently waiting in queue " + queue.getOnlineQueue().size());
				if(!(queue.getOnlineQueue().isEmpty())) {
					customers(); //Call method customer below
					//Customer who purchased their order is removed form the queue
					currentCustomer = queue.getOnlineQueue().remove();	
				}
				try {
					Thread.sleep(delay); //Delay for visualisation
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			//Then manage customers in shop
			while(!(queue.getShopQueue().isEmpty())) {
				System.out.println("Currently waiting in queue " + queue.getShopQueue().size());
				if(!(queue.getShopQueue().isEmpty())) {
					customers();
					//Customer who purchased their order is removed form the queue
					currentCustomer = queue.getShopQueue().remove();
				}
				try {
					Thread.sleep(delay);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Update Log file with actions
	 */
	public synchronized void customers() {
		log.updateLog(name + " " + ID + ": Is processing order of  " + currentCustomer.getName());
		//String orderID = currentCustomer.get Order ID
		double price = currentCustomer.cartTotalPrice;
		log.updateLog(name + " " + ID + ": Processed the oder of " + currentCustomer.getName() + " Total amount is: " + price + "£"); 
		//ADD CUSTOMERS TO GENERAL QUEUE +THEIR ORDER 
	}
	
	/*
	 * public void setQueue(Queue<Customer> queue) { this.customerqueue.shopQueue =
	 * queue; }
	 * 
	 * public Queue<Customer> getQueue(){ return customerqueue.shopQueue; }
	 */

}
