/**
 * NoMatchingOrderIDException.java - class to implement an exception for no matching order IDs
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

package CaffeeShopProject;

public class NoMatchingOrderIDException extends Exception{
	
	/**
	 * Constructor of NoMatchingOrderIDException class
	 */
	public NoMatchingOrderIDException() {
		super("No matching order ID.");
	}
}
