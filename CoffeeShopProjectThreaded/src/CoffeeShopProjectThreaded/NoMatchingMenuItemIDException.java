/**
 * NoMatchingOrderIDException.java - class to implement an exception for no matching item IDs
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

public class NoMatchingMenuItemIDException extends Exception{
	
	/**
	 * Constructor of NoMatchingOrderIDException class
	 */
	public NoMatchingMenuItemIDException() {
		super("No matching item ID.");
	}
}
