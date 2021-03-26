package CoffeeShopProjectThreaded;

import CoffeeShopProjectThreaded.NewCustomerQueue.CustomerQueueOutput;

public class QueueHandler implements Runnable{

	NewCustomerQueue shopQueue;
	NewCustomerQueue onlineQueue;
	long delay;
	int maxCustomerNum;
	
	private Log log; // used for logging data 
	
	/**
	 * Constructor for QueueHandler class
	 * @param onlineQueue Online queue of customers
	 * @param shopQueue In-shop queue of customers
	 * @param delay Delay between each customer added to the queues 
	 * @param maxCustomerNum Maximum number of customers per queue 
	 */
	public QueueHandler(NewCustomerQueue onlineQueue, 
			NewCustomerQueue shopQueue, long delay, int maxCustomerNum) {
		this.onlineQueue = onlineQueue;
		this.shopQueue = shopQueue;
		this.delay = delay;
		this.maxCustomerNum = maxCustomerNum;
		
		log = Log.getInstance();
	}
	
	/**
	 * Main thread method to add customers to online and in-shop queues 
	 */
	@Override
	public void run() {	
		for(int i =0; i<maxCustomerNum; i++) {
		//while (true) {
			// every number of milliseconds, add customer to end of queue 
			try {			
				// if online queue exists add to it 
				if (onlineQueue != null)
				{
					CustomerQueueOutput out = onlineQueue.addToQueue();
					CoffeeShop.customerList.put(out.getCustomer().getId(), out.getCustomer());
					
					log.updateLog("Queue Handler added customer " + out.getCustomer().getName() + 
							" (ID: " + out.getCustomer().getId() + ") to online queue -> updated size: " 
							+ out.getUpdatedSize());
				}									
				
				// add to in-shop queue 
				CustomerQueueOutput out = shopQueue.addToQueue();
				CoffeeShop.customerList.put(out.getCustomer().getId(), out.getCustomer());
				
				log.updateLog("Queue Handler added customer " + out.getCustomer().getName() + 
						" (ID: " + out.getCustomer().getId() + ") to in-shop queue -> updated size: " 
						+ out.getUpdatedSize());

				// delay for visualization purposes 
				Thread.sleep(delay);
				
			// catch exception for calling sleep() function
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}		
	}	
	


}
