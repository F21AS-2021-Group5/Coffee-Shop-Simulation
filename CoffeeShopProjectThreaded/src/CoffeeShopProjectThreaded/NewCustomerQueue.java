package CoffeeShopProjectThreaded;

import java.time.LocalDateTime;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Map.Entry;

public class NewCustomerQueue {
	
	private Deque<Customer> queue; // customer queue
	private boolean isOnline; // states whether queue is online or in-shop
	private Log log; // used for logging data 
	
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
		
		queue = new LinkedList<Customer>();		
		log = Log.getInstance();
		menuList = new HashMap<Integer, String>();
		fillMenuList();	
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
	   		queue.add(customer);
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
		notifyAll(); 
		
		// accordingly returns the output
		if (customer == null)
			return new CustomerQueueOutput(customer, false, queue.size());
		else
			return new CustomerQueueOutput(customer, true, queue.size());
    }
    
    /*
    public String endOfDayReport() {
    	return "str";
    }
    */
}
