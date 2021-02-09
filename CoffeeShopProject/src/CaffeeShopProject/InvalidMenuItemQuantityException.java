/**
 * InvalidOrderQuantityException.java - class to implement an exception for invalid item quantity
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

public class InvalidMenuItemQuantityException extends Exception{

	/**
	 * Constructor for InvalidOrderQuantityException class
	 */
	public InvalidMenuItemQuantityException() {
		super("Invalid item quantity.");
	}
}