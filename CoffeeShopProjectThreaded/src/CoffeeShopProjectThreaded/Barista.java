import CoffeeShopProjectThreaded.OrderQueue.QueueItem;

public class Barista implements Runnable{

	String currentItem;
	int currentCustomer;
	
	OrderQueue queue;
	
	/**
	 * Constructor for Barista class
	 * @param queue Queue of orders 
	 */
	public Barista(OrderQueue queue) { 
		this.queue = queue;
		currentItem = null;
		currentCustomer = -1;
	}
	
	/**
	 * While the queue is not empty, complete orders (NOT FINISHED0
	 */
	@Override
	public void run() {
		while (!queue.barQueue.isEmpty()) {			
			QueueItem head = queue.removeHeadFromQueue(true);
			
			if (head.isRemoved()) {
				currentItem = head.getItemID();
				currentCustomer = head.getCustomerID();		
				
				processItem();
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				System.out.println("Item " + head.getItemID() + " for customer " +
						head.getCustomerID() + " prepared.");
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Processes each item ordered (NOT FINISHED)
	 */
	void processItem() {
		
		System.out.println("Making beverage...");
	}
}