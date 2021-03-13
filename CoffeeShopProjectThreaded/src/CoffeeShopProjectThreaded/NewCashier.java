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
	public Customer currentCustomer;
	private Queue<Customer> queue;
	private NewGUI GUI;
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
