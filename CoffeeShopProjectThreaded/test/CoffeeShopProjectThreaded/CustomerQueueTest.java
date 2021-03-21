package CoffeeShopProjectThreaded;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.Duration;

class CustomerQueueTest {
	
	private static CustomerQueue queue;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		//initialize main class variables
		CoffeeShop coffeeShop = new CoffeeShop("Caffe");	
	}

	@AfterEach
	void tearDown() throws Exception {
		// clear queues 
		queue.getOnlineQueue().clear();
		queue.getShopQueue().clear();
	}

	@Test
	void testRun() {
		long delay = 1000;
		queue = new CustomerQueue("CustomerListTest", "CustomerListOnlineTest", delay);
		Thread thread = new Thread(queue);
		
		LocalDateTime start = LocalDateTime.now();
		
		thread.start();
		
		try {
			thread.join();
		} catch (InterruptedException e) {}
		
		LocalDateTime finish = LocalDateTime.now();
		
		// check queue size at the end 
		assertEquals(2, queue.getOnlineQueue().size());
		assertEquals(2, queue.getShopQueue().size());	
		
		// check names of customers in the queues 
		assertEquals("Vale", queue.getShopQueue().peekFirst().getName());
		assertEquals("Kale", queue.getShopQueue().peekLast().getName());
		assertEquals("Valerio", queue.getOnlineQueue().peekFirst().getName());
		assertEquals("Franchi", queue.getOnlineQueue().peekLast().getName());
		
		// check time passed
		Duration duration = Duration.between(start, finish);
		long durationSeconds = duration.toSeconds();
		long expectedSeconds = 4; 
		assertTrue(durationSeconds > expectedSeconds - 1 && 
				durationSeconds < expectedSeconds + 1);
	}
}
