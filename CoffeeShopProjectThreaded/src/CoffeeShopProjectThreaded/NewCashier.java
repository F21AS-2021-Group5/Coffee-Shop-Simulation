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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;


public class NewCashier implements Runnable{
	
	public String ID;	//Cashier ID
	public String name; //Cashier name
	public Customer currentCustomer;
	private Queue<Customer> queue;
	private NewGUI GUI;
	private static int cashierCount = 0;
	public int cashierNumber = 0;
	
	private Log log;
	
	public NewCashier(String name, String id, Queue<Customer> queue, NewGUI gui) {
		this.ID = id;
		this.name = name;
		this.setQueue(queue);
		this.GUI = gui;
		cashierNumber = cashierCount++;
	}
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			while(!(queue.isEmpty())) {
				System.out.println("Currently waiting in queue " + queue.size());
				if(!(queue.isEmpty())) {
					customers();
				}
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public synchronized void customers() {
		//Customer who purchased their order is removed form the queue
		currentCustomer = queue.remove();
		log.updateLog(name + " " + ID + ": Is processing order of  " + currentCustomer.getName());
		//String orderID = currentCustomer.get Order ID
		double price = currentCustomer.cartTotalPrice;
		log.updateLog(name + " " + ID + ": Processed the oder of " + currentCustomer.getName() + " Total amount is: " + price + "£"); 
	}
	
	public void setQueue(Queue<Customer> queue) {
		this.queue = queue;
	}
	
	public Queue<Customer> getQueue(){
		return queue;
	}

}
