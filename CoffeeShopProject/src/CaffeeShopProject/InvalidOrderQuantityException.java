/**
 * InvalidOrderQuantityException.java - class to implement an exception for invalid order quantity
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

public class InvalidOrderQuantityException extends Exception{

	/**
	 * Constructor for InvalidOrderQuantityException class
	 */
	public InvalidOrderQuantityException() {
		super("Invalid order quantity.");
	}
}
