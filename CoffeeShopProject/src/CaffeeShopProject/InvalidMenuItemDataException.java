/**
 * InvalidMenuItemDataException.java - class to implement an exception for invalid menu item data 
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

public class InvalidMenuItemDataException extends Exception {
	
	/**
	 * Constructor for InvalidMenuItemDataException class
	 */
	public InvalidMenuItemDataException(){
        super("An item of data was invalid.\n");
    }

}
