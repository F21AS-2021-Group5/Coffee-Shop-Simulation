package CaffeeShopProject;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

class CustomerTest {
	
	private static Customer customer;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		// initialize customer 
		LocalDateTime ldt = LocalDateTime.now();
		customer = new Customer("Valerio Franchi", ldt);
	}

	@AfterEach
	void tearDown() throws Exception {
		// clear customer's cart
		customer.getCart().clear();
	}

	@Test
	void testCustomer() {
		
		// test customer constructor, a way to recognize if ID is okay ??
		
		fail("Not yet implemented");
	}
	
	@Test
	void testEmptyName() {
		// checks that exception is thrown when empty name is input 
		assertThrows(IllegalStateException.class,
				 () -> { new Customer("", LocalDateTime.now()); }
				 );
	}
	
	@Test
	void testInvalidDateTime() {
		// check that exception is thrown when date and/or time is invalid
		
		// invalid year  
		assertThrows(DateTimeException.class,
				 () -> { new Customer("Valerio Franchi", LocalDateTime.of(20154, 6, 29, 19, 30, 40)); }
				 );
		
		// invalid month  
		assertThrows(DateTimeException.class,
				 () -> { new Customer("Valerio Franchi", LocalDateTime.of(2015, 13, 29, 19, 30, 40)); }
				 );
		assertThrows(DateTimeException.class,
				 () -> { new Customer("Valerio Franchi", LocalDateTime.of(2015, -1, 29, 19, 30, 40)); }
				 );
		
		// invalid day
		assertThrows(DateTimeException.class,
				 () -> { new Customer("Valerio Franchi", LocalDateTime.of(2015, 2, 30, 19, 30, 40)); }
				 );
		assertThrows(DateTimeException.class,
				 () -> { new Customer("Valerio Franchi", LocalDateTime.of(2015, 2, -1, 19, 30, 40)); }
				 );
		
		// invalid hour 
		assertThrows(DateTimeException.class,
				 () -> { new Customer("Valerio Franchi", LocalDateTime.of(2015, 5, 12, -1, 30, 40)); }
				 );
		assertThrows(DateTimeException.class,
				 () -> { new Customer("Valerio Franchi", LocalDateTime.of(2015, 5, 12, 27, 30, 40)); }
				 );
		
		// invalid minute
		assertThrows(DateTimeException.class,
				 () -> { new Customer("Valerio Franchi", LocalDateTime.of(2015, 5, 12, 3, -2, 40)); }
				 );
		assertThrows(DateTimeException.class,
				 () -> { new Customer("Valerio Franchi", LocalDateTime.of(2015, 5, 12, 3, 80, 40)); }
				 );
		
		// invalid second 
		assertThrows(DateTimeException.class,
				 () -> { new Customer("Valerio Franchi", LocalDateTime.of(2015, 5, 12, 3, 30, 70)); }
				 );
		assertThrows(DateTimeException.class,
				 () -> { new Customer("Valerio Franchi", LocalDateTime.of(2015, 5, 12, 3, 30, -3)); }
				 );
	}
	
	@Test
	void testIDGeneration() {		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
		// find Valerio Franchi in ASCII values and put it instead of the bunch of integers
		String expectedID = "238293293274358545" + customer.getTimestamp().format(formatter);

		assertEquals(expectedID, customer.getId());
	}
	
	@Test
	void testAddOrder() throws InvalidOrderQuantityException {
		String orderId = "Espresso123";
		Order order = new Order(orderId, "Espresso", "Drinks", 1.50f, "Authentic Italian coffee");
		
		// add normal order to cart 
		customer.addOrder(order);
		assertEquals(orderId, customer.getCart().get(orderId).getIdentifier());
		
		// add same order to cart
		int previousQuantity = customer.getCart().get(orderId).getQuantity();
		customer.addOrder(order);
		assertEquals(previousQuantity+1, customer.getCart().get(orderId).getQuantity());
	}
	
	@Test
	void testRemoveOrder() throws InvalidOrderQuantityException, NoMatchingOrderIDException {
		String orderId = "Espresso123";
		Order order = new Order(orderId, "Espresso", "Drinks", 1.50f, "Authentic Italian coffee");
		
		// remove order to cart 
		customer.addOrder(order);
		int previousSize = customer.getCart().size();
		customer.removeOrder(orderId);
		assertEquals(previousSize-1, customer.getCart().size());
		
		// remove order with quantity > 1 from to cart
		customer.addOrder(order);
		customer.addOrder(order);
		customer.addOrder(order);
		int previousQuantity = customer.getCart().get(orderId).getQuantity();
		customer.removeOrder(orderId);
		assertEquals(previousQuantity-1, customer.getCart().get(orderId).getQuantity());
	}
	
	@Test
	void testInvalidOrder() {
		// checks that exception is thrown when Order is null
		assertThrows(IllegalArgumentException.class,
				() -> {customer.addOrder(null); }
				);
	}
	
	@Test 
	void testInvalidOrderQuantity() {
		// checks that exception is thrown when order quantity is 0 or smaller
		String orderId = "Espresso123";
		Order order = new Order(orderId, "Espresso", "Drinks", 1.50f, "Authentic Italian coffee");
		
		order.setQuantity(0);
		assertThrows(InvalidOrderQuantityException.class,
				() -> {customer.addOrder(order); }
				);
		assertThrows(InvalidOrderQuantityException.class,
				() -> {customer.removeOrder(orderId); }
				);
		
		order.setQuantity(-1);
		assertThrows(InvalidOrderQuantityException.class,
				() -> {customer.addOrder(order); }
				);
		assertThrows(InvalidOrderQuantityException.class,
				() -> {customer.removeOrder(orderId); }
				);		
	}
	
	@Test 
	void testNotMatchingOrderIDs() throws InvalidOrderQuantityException {
		// checks that exception is thrown when order quantity is 0 or smaller
		String orderId = "Espresso123";
		Order order = new Order(orderId, "Espresso", "Drinks", 1.50f, "Authentic Italian coffee");
		customer.addOrder(order);
		
		assertThrows(InvalidOrderQuantityException.class,
				() -> {customer.removeOrder("123"); }
				);
	}
	
	@Test
	void testSameCustomer() {
		// test if 2 customers are the same 
		Customer customer2 = new Customer("Valerio Franchi", customer.getTimestamp());
		assertTrue(customer.equals(customer2));
		
		// test if 2 customers are different 
		Customer customer3 = new Customer("Valerio F", LocalDateTime.now());
		assertFalse(customer.equals(customer3));
	}

	@Test
	void testTotalCartPrice() throws InvalidOrderQuantityException, NoMatchingOrderIDException {
		// add orders to cart and then check final price 
		float price1 = 1.50f;
		float price2 = 0.50f;
		String orderId1 = "Espresso123";
		String orderId2 = "WaterS123";
		Order order1 = new Order(orderId1, "Espresso", "Drinks", price1, "Authentic Italian coffee");
		Order order2 = new Order(orderId2, "Sparkling Water", "Drinks", price2, "Bottled sparking water");
		
		// add orders and check price
		customer.addOrder(order1);
		customer.addOrder(order1);
		customer.addOrder(order2);
		customer.addOrder(order2);
		float expectedAmount = price1*2 + price2*2;
		assertEquals(expectedAmount, customer.getOrderTotalPrice());
		
		// remove orders and check price 
		customer.removeOrder(orderId1);
		customer.removeOrder(orderId2);
		expectedAmount -= (price1 + price2);
		assertEquals(expectedAmount, customer.getOrderTotalPrice());
	}
}