/**
 * CustomerAdditionException.java - class to implement an exception for invalid addition to customer deque 
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

public class CustomerAdditionException extends Exception{

	/**
	 * Constructor for CustomerAdditionException class
	 */
	public CustomerAdditionException() {
		super("Customer was not added from text file to deque.");
	}
}
