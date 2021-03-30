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

/**
 * Keeps list of all the observers that needs to know
 * if information has changed
 * 
 */
public interface Subject {
	//Add new Observer
	public void registerObserver(Observer newObserver);
	//Remove Observer
	public void removeObserver(Observer deleteObserver);
	//Notify all Observers
	public void notifyObservers();	
}


//private List<Observer> observers = new ArrayList<Observer>();

//observers.add(newObserver);
//System.out.println("New Observer added");

//int observerIndex = observers.indexOf(deleteObserver);
//System.out.println("Observer " + (observerIndex+1) + " has been deleted");
//
//observers.remove(observerIndex);  //Delete observers from Arraylist

//for(Observer observer : observers) {	
//observer.update();
//}	