package CoffeeShopProjectThreaded;

import CoffeeShopProjectThreaded.NewCustomerQueue.OperationOutput;

public class QueueHandler implements Runnable{

	NewCustomerQueue shopQueue;
	NewCustomerQueue onlineQueue;
	long delay;
	int maxCustomerNum;
	
	public QueueHandler(NewCustomerQueue onlineQueue, 
			NewCustomerQueue shopQueue, long delay, int maxCustomerNum) {
		this.onlineQueue = onlineQueue;
		this.shopQueue = shopQueue;
		this.delay = delay;
		this.maxCustomerNum = maxCustomerNum;
	}
	
	public QueueHandler(NewCustomerQueue shopQueue, long delay) {
		this.shopQueue = shopQueue;
		this.delay = delay;
	}

	@Override
	public void run() {	
		for(int i =0; i<maxCustomerNum; i++) {
		//while (true) {
			// every number of milliseconds, add customer to end of queue 
			try {
				OperationOutput out = shopQueue.addToQueue();				

				System.out.println("Queue Handler added customer " + out.getCustomer().getId() + "-> size: " + out.updatedSize);
				
				/*
				if (onlineQueue != null)
					onlineQueue.addToQueue();
					*/				
				
				// delay for visualization purposes 
				Thread.sleep(delay);
				
			// catch exception for calling sleep() function
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
		
	}	
	


}
