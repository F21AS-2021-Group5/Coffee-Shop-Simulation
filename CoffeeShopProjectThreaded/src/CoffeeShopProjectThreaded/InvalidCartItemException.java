/**
 * InvalidCartItemException.java - class to implement an exception for no matching cart item IDs
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

public class InvalidCartItemException extends Exception{
	
	/**
	 * Constructor of InvalidCartItemException class
	 */
	public InvalidCartItemException() {
		super("No matching item ID inside cart.");
	}
}
