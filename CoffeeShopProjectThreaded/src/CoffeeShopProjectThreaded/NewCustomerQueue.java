/**
 * CustomerQueue.java - class to implement the queues for the customers 
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

import java.time.LocalDateTime;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Map.Entry;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class NewCustomerQueue{
	
	//List of observers
	private PropertyChangeSupport support;
	
	private Deque<Customer> queue; // customer queue
	private boolean isOnline; // states whether queue is online or in-shop
	private Log log; // used for logging data 
	
	private boolean locked;
	
	// stores all items in the menu in the form of a hashmap 
	private HashMap<Integer, String> menuList;
	
	// possible names of customers to choose from for random generation 
	private String[] customerNames = {"Adam","Anna","Lora","Ben", 
			"James", "Ema", "Lena", "Mike", "Natasha", 
			"Sindy", "Ben", "Connor", "Steve", "Greg"}; 
	
	/**
	 * Constructor for CustomerQueue class
	 * @param isOnline Online or in-shop queue 
	 */
	public NewCustomerQueue(boolean isOnline) {
		this.isOnline = isOnline;
		
		support = new PropertyChangeSupport(this);
		queue = new LinkedList<Customer>();		
		log = Log.getInstance();
		menuList = new HashMap<Integer, String>();
		fillMenuList();	
		locked = false;
	}
	
	public class CustomerQueueOutput {
		private Customer customer;
		private boolean success;
		public int updatedSize;
		
		/**
		 * Constructor for CustomerQueueOutput
		 * @param customer Customer object
		 * @param success Successful operation or not
		 * @param updatedSize Updated size of customer queue after operation
		 */
		public CustomerQueueOutput(Customer customer, boolean success, int updatedSize) {
			this.customer = customer;
			this.success = success;
			this.updatedSize = updatedSize;			
		}
		
		/**
		 * @return Customer object
		 */
		public Customer getCustomer() {
			return customer;
		}
		
		/**
		 * Sets the customer object
		 * @param customer Customer object 
		 */
		public void setCustomer(Customer customer) {
			this.customer = customer;
		}
		
		/**
		 * @return Successful operation or not 
		 */
		public boolean isSuccess() {
			return success;
		}
		
		
		/**
		 * Sets the success variable 
		 * @param success Successful operation or not 
		 */
		public void setSuccess(boolean success) {
			this.success = success;
		}
		
		/**
		 * @return Updated size of customer queue 
		 */
		public int getUpdatedSize() {
			return updatedSize;
		}
		
		/**
		 * Sets the updated size of the queue 
		 * @param updatedSize Updated size of customer queue 
		 */
		public void setUpdatedSize(int updatedSize) {
			this.updatedSize = updatedSize;
		}
	}	

	/**
	 * @return Customer queue
	 */
	public Deque<Customer> getQueue() {
		return queue;
	}

	/**
	 * Sets the customer queue 
	 * @param queue Customer queue
	 */
	public void setQueue(Deque<Customer> queue) {
		support.firePropertyChange("queue", this.queue, queue);
		this.queue = queue;
	}

	/**
	 * @return Online or in-shop queue
	 */
	public boolean getType() {
		return isOnline;
	}
	
	/**
	 * Sets the queue type 
	 * @param isOnline Online or in-shop queue
	 */
	public void setType(boolean isOnline) {
		this.isOnline = isOnline;
	}
	
	/**
	 * @return Locked state of current class object 
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * Set locked state 
	 * @param locked Locked state of current class object 
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * Fills a list made of all the menu items 
	 */
	void fillMenuList() {
		int number = 1;
		for (Entry<String, MenuItem> mapElement : CoffeeShop.menu.entrySet()) { 
			   String key = (String)mapElement.getKey();
			   menuList.put(number, key);
			   number ++;
		}
	}
		
	/**
	* Adds customer to queue
	* @return Added customer and if operation was successful
	*/
    public synchronized CustomerQueueOutput addToQueue() {
    	Customer customer = null;
    	try {
		    // create random customer 
		    Random rn = new Random();
		    LocalDateTime lt = LocalDateTime.now(); 
		    String name = customerNames[rn.nextInt(customerNames.length)];
	   		customer = new Customer(name, lt); 
	   		
	   		// create random items inside cart
	   		int amount = rn.nextInt(10);	   		
	   		for(int i =0; i<=amount; i++) {
	   			int itemNr = rn.nextInt(CoffeeShop.menu.size()) + 1;
	   			LocalDateTime lt1 = LocalDateTime.now(); 
	   			customer.addItem(menuList.get(itemNr), 1, lt1);
	   		}
	   		
	   		// add customer and notify all threads that resource can be accessed again
	   		queue.add(customer);
	   		setMessage(null, customer, isOnline, "added");
	   		notifyAll();
	   		
		   		
		} catch (Exception e) {
			return new CustomerQueueOutput(customer, false,queue.size());
		}
    	return new CustomerQueueOutput(customer,true,queue.size());
    }
    
    /**
     * Removes customer from queue
     * @return Removed customer and if operation was successful
     */
    public synchronized CustomerQueueOutput removeFromQueue() {
    	locked = true;
    	Customer customer = null;
    	
    	// thread waits until queue is not empty anymore
    	while(queue.isEmpty()) { 
    		try { 
    			wait(); 
    		} 
    		catch (InterruptedException e) {}  
    	}
    	
    	// removes customer and notify all threads that resource can be accessed again
		customer = queue.pop();
		setMessage(null, customer, isOnline, "removed");
		locked = false;
		notifyAll(); 
		
		// accordingly returns the output
		if (customer == null)
			return new CustomerQueueOutput(customer, false, queue.size());
		else
			return new CustomerQueueOutput(customer, true, queue.size());
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
     * @param oldVal Old customer 
     * @param newVal Updated customer 
     * @param online Online or in-shop queue
     * @param message Operation type 
     */
    public void setMessage(Customer oldVal, Customer newVal, boolean online, String message) {
    	if (online)
    		support.firePropertyChange(message + " online", oldVal, newVal);
    	else
    		support.firePropertyChange(message + " inshop", oldVal, newVal);    
    }

}
