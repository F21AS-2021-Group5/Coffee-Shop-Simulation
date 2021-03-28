/**
 * Subject.java - class to implement Observer-Observable pattern
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

import java.util.ArrayList;
import java.util.List;

public interface Subject {
	List<Observer> observers = new ArrayList<Observer>();
	
	//methods to register/unregister and remove observers
	public void registerObserver(Observer obj);
	public void unregister(Observer obj);
	public void removeObserver(Observer obj);
	
	//Notify all Observers
	public void notifyObservers();
	
	//method to get updates from subject
	public Object getUpdate(Observer obj);
}
