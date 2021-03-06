/**
 * FoodStaff.java - class to implement a staff for the coffeeshop
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import CoffeeShopProjectThreaded.OrderQueue.OrderQueueItem;

public class FoodStaff {
	
	// name and type of staff member 
	private String name;
	private String type;
	
	private PropertyChangeSupport support; // for observer-observable model 
	
	private Customer currentCustomer;
	private OrderQueueItem currentItem; 
	Long delay;
	String instruction = "";
	private Log log;
	
	/**
	 * Constructor for Staff class
	 * @param queue Queue of orders 
	 * @param isBarista True if staff is barista False if cook
	 * @param delay Delay for FoodStaffRunnable  
	 */
	public FoodStaff(String name, boolean isBarista, Long delay) { 
		this.name = name;
		support = new PropertyChangeSupport(this);
		currentCustomer = null;
		this.delay = delay;
		type = (isBarista) ? "Barista" : "Cook";
		this.log = Log.getInstance();
	}
	
	/**
	 * Set the current instruction the staff is executing
	 * @param string instruction
	 **/
	public void setInstruction(String instruction) {
		this.instruction = instruction;
		setMessage(null, name,  "instruction"+ getType());
		log.updateLog("[FoodStaff]: " +"FoodStaff " + name + " has started another task ");
	}
	
	/**
	 * Get the current instruction the staff is executing
	 * @return string instruction
	 **/
	public String getInstruction() {
		return this.instruction;
	}
	
	/**
	 * Set the delay for the staffs thread 
	 * @param Long value of delay
	 **/
	public void setDelay(Long delay) {
		this.delay = delay;
	}
	
	/**
	 * Get the delay for the staffs thread 
	 * @return Long value of delay
	 **/
	public Long getDelay() {
		return this.delay;
	}
	
	/**
	 * @return Staff name
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * Set name of staff 
	 * @param name Staff name
	 */
	public void setName(String name) {
		this.name = name;
	}
	

	/**
	 * @return Staff type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Set type of staff 
	 * @param name Staff type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @return Current item identifier
	 */
	public OrderQueueItem getCurrentItem() {
		return currentItem;
	}
	
	/**
	 * Sends message to listener for shift end 
	 * @param name Name of staff member 
	 */
	public void EndedShift(String name) {
		setMessage(null, name, "endedShift"+getType());
	}
	
	/**
	 * Sets the current item identifier
	 * @param currentItem Current item identifier
	 */
	public void setCurrentItem(OrderQueueItem currentItem) {
		this.currentItem = currentItem;
	}
	
	/**
	 * @return Current customer 
	 */
	public Customer getCurrentCustomer() {
		return currentCustomer;
	}
	
	/**
	 * Sets the current customer 
	 * @param currentCustomer Current customer 
	 */
	public void setCurrentCustomer(Customer currentCustomer) {
		this.currentCustomer = currentCustomer;
	}
	 
    /**
     * Adds listener 
     * @param pcl Listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
    	support.addPropertyChangeListener(pcl);
    }
    
    /**
     * Removes listener 
     * @param pcl Listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
    	support.removePropertyChangeListener(pcl);
    }
    
    /**
     * Fires a message to the listener 
     * @param oldVal Old name 
     * @param newVal Updated name 
     * @param message Operation type 
     */
	public void setMessage(String oldVal, String newVal,  String message) {
		support.firePropertyChange(message, oldVal, newVal);
	}	
}
