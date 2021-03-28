/**
 * Observer.java - class to implement Observer-Observable pattern
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

public interface Observer {
	//protected Subject subject;
	
	public void update();  //method to update the observer, used by subject
	public void setSubject(Subject sub);  //attach with subject to observe
}
