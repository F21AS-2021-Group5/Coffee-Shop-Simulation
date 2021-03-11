/**
 * CustomerQueu.java - class to implement the queues for the customers 
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

import java.util.LinkedList;
import java.util.Queue;

public class CustomerQueue implements Runnable{
	
	// in-shop queue and online queue 
	Queue<String> shopQueue = new LinkedList<String>();
	Queue<String> onlineQueue = new LinkedList<String>();
	
	/**
	 * Constructor for CustomerQueue class
	 */
	public CustomerQueue()
	{
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}	

}
