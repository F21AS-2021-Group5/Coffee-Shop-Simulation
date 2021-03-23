package CoffeeShopProjectThreaded;

import java.time.LocalDateTime;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Map.Entry;

public class NewCustomerQueue {
	
	private Deque<Customer> queue;
	private boolean isOnline;
	private Log log;
	
	HashMap<Integer, String> menuList;
	String[] customerNames={"Adam","Anna","Lora","Ben", 
			"James", "Ema", "Lena", "Mike", "Natasha", 
			"Sindy", "Ben", "Connor", "Steve", "Greg"};  
	
	public class OperationOutput {
		Customer customer;
		boolean success;
		int updatedSize;
		
		/**
		 * 
		 * @param customer
		 * @param success
		 */
		public OperationOutput(Customer customer, boolean success, int updatedSize) {
			this.customer = customer;
			this.success = success;
			this.updatedSize = updatedSize;
		}
		
		/**
		 * 
		 * @return
		 */
		public Customer getCustomer() {
			return customer;
		}
		
		/**
		 * 
		 * @return
		 */
		public boolean isSuccess() {
			return success;
		}
	}
	
	
	public NewCustomerQueue(boolean isOnline) {
		this.isOnline = isOnline;
		menuList = new HashMap<Integer, String>();
		
		int number = 1;
	    for (Entry<String, MenuItem> mapElement : CoffeeShop.menu.entrySet()) { 
		   String key = (String)mapElement.getKey();
		   menuList.put(number, key);
		   number ++;		   
	    }
		 
		queue = new LinkedList<Customer>();		
		log = Log.getInstance();
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
	* Adds customer to queue
	* @return Added customer and if operation was successful
	*/
    public synchronized OperationOutput addToQueue() {
    	Customer customer = null;
    	try {
		    // create random customer 
		    Random rn = new Random();
		    LocalDateTime lt = LocalDateTime.now(); 
		    String name = customerNames[rn.nextInt(customerNames.length)];
	   		customer = new Customer(name, lt); // find a way to generate random name or remove name all together 
	   		
	   		int amount = rn.nextInt(10);	   		
	   		for(int i =0; i<=amount; i++) {
	   			int itemNr = rn.nextInt(CoffeeShop.menu.size()) + 1;
	   			LocalDateTime lt1 = LocalDateTime.now(); 
	   			customer.addItem(menuList.get(itemNr), 1, lt1);
	   		}
	   		
	   		queue.add(customer);
	   		notifyAll();
	   		
	   		// Add log here for new customer 
		   		
		} catch (Exception e) {
			return new OperationOutput(customer, false,queue.size());
		}
    	return new OperationOutput(customer,true,queue.size());
    }
    
    /**
     * Removes customer from queue
     * @return Removed customer and if operation was successful
     */
    public synchronized OperationOutput removeFromQueue() {
    	Customer customer = null;
    	while(queue.isEmpty()) { 
    		try { 
    			wait(); 
    		} 
    		catch (InterruptedException e) {}  
    	}
		customer = queue.pop();
		notifyAll(); 
		
		if (customer == null)
			return new OperationOutput(customer, false,queue.size());
		else
			return new OperationOutput(customer, true,queue.size());
    }
}
