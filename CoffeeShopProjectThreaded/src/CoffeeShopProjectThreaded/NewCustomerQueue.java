package CoffeeShopProjectThreaded;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class NewCustomerQueue{
	
	//List of observers
	private List<Observer> observers;
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
		observers = new ArrayList<Observer>();
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
			//notifyObservers();
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
		
		//notifyObservers();  //All observers will be notified
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
		//notifyObservers();	//All observers will be notified
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * 
	 * @param locked
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
	   		customer = new Customer(name, lt); // find a way to generate random name or remove name all together 
	   		
	   		// create random items inside cart
	   		int amount = rn.nextInt(10);	   		
	   		for(int i =0; i<=amount; i++) {
	   			int itemNr = rn.nextInt(CoffeeShop.menu.size()) + 1;
	   			LocalDateTime lt1 = LocalDateTime.now(); 
	   			customer.addItem(menuList.get(itemNr), 1, lt1);
	   		}
	   		
	   		// add customer and notify all threads that resource can be accessed again
	   		int oldSize = queue.size();
	   		queue.add(customer);
	   		Deque<Customer> queue1  =new LinkedList<Customer>();
	   		//setMessage(queue, "test2");
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
    	int oldSize = queue.size();
		customer = queue.pop();
		//setMessage(queue, "test");
		System.out.println(customer);
		System.out.println(isOnline);
		setMessage(null, customer, isOnline, "removed");
		locked = false;
		notifyAll(); 
		
		// accordingly returns the output
		if (customer == null)
			return new CustomerQueueOutput(customer, false, queue.size());
		else
			return new CustomerQueueOutput(customer, true, queue.size());
    }
    
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
    	support.addPropertyChangeListener(pcl);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
    	support.removePropertyChangeListener(pcl);
    }
    

    
    public void setMessage(Customer oldVal, Customer newVal, boolean online, String message) {
    	if (online)
    		support.firePropertyChange(message + " online", oldVal, newVal);
    	else
    		support.firePropertyChange(message + " inshop", oldVal, newVal);    
    }
    
    

//	@Override
//	public void registerObserver(Observer newObserver) {
//		observers.add(newObserver);
//		System.out.println("New Observer added");
//		
//	}
//
//	@Override
//	public void removeObserver(Observer deleteObserver) {
//		int observerIndex = observers.indexOf(deleteObserver);
//		System.out.println("Observer " + (observerIndex+1) + " has been deleted");
//		
//		observers.remove(observerIndex);  //Delete observers from Arraylist
//		
//	}
//
//	@Override
//	public void notifyObservers() {
//		for(Observer observer : observers) {	
//		observer.update();
//		}	
//	}
    
    /*
    public String endOfDayReport() {
    	return "str";
    }
    */
}
