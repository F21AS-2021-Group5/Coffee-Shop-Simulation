/**
 * CustomerTest.java - JUnit Test Class for the Customer class
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


package CaffeeShopProject;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

class CustomerTest {
	
	private static Customer customer;
	private static CoffeeShop shop;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		//initialize main class
		shop = new CoffeeShop("MenuItems");
		
		// initialize customer 
		LocalDateTime ldt = LocalDateTime.now();
		customer = new Customer("Valerio Franchi", ldt);
	}

	@AfterEach
	void tearDown() throws Exception {
		// clear customer's cart and total amount
		customer.getCart().clear();
		customer.setCartTotalPrice(0.0f);
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
	
	@RepeatedTest(10)
	@Test
	void testIDGeneration() {		
		// same customer, IDs should match
		Customer customer2 = new Customer(customer.getName(), customer.getTimestamp());
		assertTrue(customer.equals(customer2));
		
		// different name, same time stamp, IDs should not match
		Customer customer3 = new Customer("Valerio F", customer.getTimestamp());
		assertFalse(customer.equals(customer3));
		
		// same name, different time stamp, IDs should not match
		Customer customer4 = new Customer(customer.getName(), LocalDateTime.of(2015, 
                Month.JULY, 29, 19, 30, 40));
		assertFalse(customer.equals(customer4));
	}
	
	@Test
	void testaddItem() throws InvalidMenuItemQuantityException, InvalidMenuItemDataException {
		String itemId = "Cappuccino";
		
		// add normal item to cart 
		customer.addItem(itemId, 1);
		assertEquals(1, customer.getCart().get(itemId));
		
		// add multiple items to cart 
		customer.addItem(itemId, 3);
		assertEquals(4, customer.getCart().get(itemId));
		
		// check that key value pair exists
		assertTrue(customer.cart.containsKey(itemId));
	}
	
	@Test 
	void testaddItemExceptions() throws InvalidMenuItemQuantityException, InvalidMenuItemDataException  {
		// checks that exception is thrown when item ID is empty string 
		assertThrows(IllegalStateException.class,
				() -> {customer.addItem("",1); }
				);
		
		// checks that exception is thrown when invalid item ID is added
		assertThrows(InvalidMenuItemDataException.class,
				() -> {customer.addItem("nothing",1); }
				);
		
		// checks that exception is thrown when item quantity is 0 or smaller
		String itemId = "Cappuccino";
		assertThrows(InvalidMenuItemQuantityException.class,
				() -> {customer.addItem(itemId, 0); }
				);
		assertThrows(InvalidMenuItemQuantityException.class,
				() -> {customer.addItem(itemId, -1); }
				);
		assertThrows(InvalidMenuItemQuantityException.class,
				() -> {customer.addItem(itemId, -2); }
				);
		
		// check if item already inside cart has incorrect quantity when adding to it;
		customer.addItem(itemId, 1);
		customer.cart.put(itemId, customer.cart.get(itemId) - 20);
		assertThrows(InvalidMenuItemQuantityException.class,
				() -> {customer.addItem(itemId, 1); }
				);
	}
	
	@Test
	void testRemoveItem() throws InvalidMenuItemQuantityException, NoMatchingMenuItemIDException, InvalidMenuItemDataException {
		String itemId1 = "Cappuccino";
		String itemId2 = "Latte";
		
		// remove item from cart 
		customer.addItem(itemId1,1);
		customer.removeItem(itemId1,1);
		assertEquals(0, customer.getCart().size());
		
		// remove item from cart that has more than one initial item 
		customer.cart.clear();
		customer.addItem(itemId1, 1);
		customer.addItem(itemId2, 1);
		customer.removeItem(itemId1,1);
		assertEquals(1, customer.getCart().size());
		
		// remove item from cart with -1 quantity
		customer.cart.clear();
		customer.addItem(itemId1,4);
		customer.removeItem(itemId1,-1);
		assertEquals(0, customer.getCart().size());
		
		// remove item with quantity > 1 from to cart
		customer.cart.clear();
		customer.addItem(itemId1,4);
		customer.removeItem(itemId1,2);
		assertEquals(2, customer.getCart().get(itemId1));
	}
	
	@Test 
	void testRemoveExceptions() throws InvalidMenuItemQuantityException, InvalidMenuItemDataException {
		// checks that exception is thrown when item ID is empty string 
		assertThrows(IllegalStateException.class,
				() -> {customer.addItem("",1); }
				);
		
		// check that exception is thrown when input quantity is 0, or less than -1
		customer.addItem("Cappuccino",4);
		assertThrows(InvalidMenuItemQuantityException.class,
				() -> {customer.removeItem("Cappuccino", 0); }
				);
		assertThrows(InvalidMenuItemQuantityException.class,
				() -> {customer.removeItem("Cappuccino", -2); }
				);
		assertThrows(InvalidMenuItemQuantityException.class,
				() -> {customer.removeItem("Cappuccino", -3); }
				);
		
		// check that exception is thrown when input quantity is greater than current quantity
		customer.cart.clear();
		customer.addItem("Cappuccino",4);
		assertThrows(InvalidMenuItemQuantityException.class,
				() -> {customer.removeItem("Cappuccino", 5); }
				);
		assertThrows(InvalidMenuItemQuantityException.class,
				() -> {customer.removeItem("Cappuccino", 6); }
				);
		
		// check that exception is thrown when item already inside cart has incorrect quantity
		customer.cart.put("Cappuccino", -5);
		assertThrows(InvalidMenuItemQuantityException.class,
				() -> {customer.removeItem("Cappuccino", 2); }
				);
		
		// checks that exception is thrown when invalid item ID is added
		assertThrows(InvalidMenuItemDataException.class,
				() -> {customer.removeItem("nothing",1); }
				);
		
		
		// check that exception is thrown when item ID to remove is not in cart 
		customer.cart.clear();
		customer.addItem("Cappuccino",4);
		assertThrows(NoMatchingMenuItemIDException.class,
				() -> {customer.removeItem("Croissant", 2); }
				);
	}
	
	@Test
	@RepeatedTest(10)
	void testSameCustomer() {
		// test if 2 customers are the same 
		Customer customer2 = new Customer("Valerio Franchi", customer.getTimestamp());
		assertTrue(customer.equals(customer2));
		
		// test if 2 customers are different 
		Customer customer3 = new Customer("Valerio F", LocalDateTime.now());
		assertFalse(customer.equals(customer3));
	}

	@Test
	void testTotalCartPrice() throws InvalidMenuItemQuantityException, NoMatchingMenuItemIDException, InvalidMenuItemDataException {
		// add items to cart and then check final price 
		String itemId1 = "Cappuccino";
		String itemId2 = "Croissant";
		
		// add items and check price
		customer.addItem(itemId1, 1);
		customer.addItem(itemId1, 1);
		customer.addItem(itemId2, 1);
		customer.addItem(itemId2, 1);

		float expectedAmount = shop.menu.get(itemId1).getCost()*2 + 
				shop.menu.get(itemId2).getCost()*2;
		assertEquals(expectedAmount, customer.getCartTotalPrice());
		
		// remove items and check price 
		customer.removeItem(itemId1, 1);
		customer.removeItem(itemId2, 1);
		
		expectedAmount -= (shop.menu.get(itemId1).getCost() + 
				shop.menu.get(itemId2).getCost());
		assertEquals(expectedAmount, customer.getCartTotalPrice());
	}
}
