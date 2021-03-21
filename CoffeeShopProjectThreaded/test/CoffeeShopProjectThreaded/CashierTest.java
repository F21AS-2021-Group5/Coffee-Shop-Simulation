/**
 * CashierTest.java - JUnit Test Class for the Cashier class
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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

class CashierTest {
	
	private static Cashier cashier;
	private Customer customer;
	private static CoffeeShop shop;
	private double delta = 0.001;

	@BeforeAll
	static void setUp() throws Exception {
		
		System.out.println("Begin Cashier Test");
		
		//initialize main class
		shop = new CoffeeShop("MenuItems");	
	}
	
	@AfterEach
	void tearDown() throws Exception {
		//System.out.println("new test");
	}
	
	
	@Test
	void testcreateNewCustomer() {
		// create new customer //
		LocalDateTime t = LocalDateTime.now();
		customer = new Customer("Andrew", "A1", t);
		
		// check for customer information //
		assertTrue(customer.getName() == "Andrew");
		assertTrue(customer.getId() == "A1");
		assertTrue(customer.getTimestamp() == t);
		
	}
	
	@Test
	void testcreateNewCashier() {
		// check cashier constructor
		LocalDateTime t = LocalDateTime.now();
		customer = new Customer("Andrew", "A1", t);
		cashier = new Cashier("Beth");
		cashier.createNewCustomer(customer.getName());
	
		assertTrue(cashier.ID == "Beth");
		
	}
		
	@Test
	void testgetCartTax() {
		
		LocalDateTime t = LocalDateTime.now();
		customer = new Customer("Andrew", "A1", t);
		
		cashier = new Cashier("B");
		cashier.createNewCustomer(customer.getName());

		
		// test for zero price //
		cashier.subtotal = 0;
		assertEquals(0, cashier.getCartTax(), delta);
		
		// test for intiger price //
		cashier.subtotal = 4;
		assertEquals(1, cashier.getCartTax(), delta);
		
		// test for decimal tax //
		cashier.subtotal = (float) 2.00;
		assertEquals(0.5, cashier.getCartTax(), delta);
		
		// test for decimal price //
		cashier.subtotal = (float) 2.99;
		assertEquals(0.7475, cashier.getCartTax(), delta);
		

	}
	
	@Test
	void testgetDiscount1() throws InvalidMenuItemQuantityException, InvalidMenuItemDataException {
			
		LocalDateTime t = LocalDateTime.now();
		customer = new Customer("Andrew", "A1", t);
		
		cashier = new Cashier("B");
		cashier.createNewCustomer(customer.getName());
		cashier.currentCustomer = customer;
		
		String itemId1 = "Cappuccino";
		String itemId2 = "Panini";
		String itemId3 = "Muffin";
				
		// test discount 1 //
		customer.addItem(itemId1, 1, LocalDateTime.now());
		customer.addItem(itemId2, 1, LocalDateTime.now());
		customer.addItem(itemId3, 1, LocalDateTime.now());

		assertTrue(customer.cart.containsKey(itemId1));
		assertTrue(customer.cart.containsKey(itemId2));
		assertTrue(customer.cart.containsKey(itemId3));

		assertEquals(8.2, cashier.getCartSubtotalPrice(), delta);
		assertEquals(3.2, cashier.getDiscount(), delta);

		assertTrue(cashier.discount1 == 1);
		assertTrue(cashier.discount2 == 0);
		assertTrue(cashier.discount3 == 0);
		
	}
	
	@Test
	void testgetDiscount2() throws InvalidMenuItemQuantityException, InvalidMenuItemDataException {

		LocalDateTime t = LocalDateTime.now();
		customer = new Customer("Andrew", "A1", t);
		
		cashier = new Cashier("B");
		cashier.createNewCustomer(customer.getName());
		cashier.currentCustomer = customer;
		
		String itemId1 = "Cappuccino";
		String itemId2 = "Panini";
		String itemId3 = "Muffin";

		customer.addItem(itemId1, 3, LocalDateTime.now());
		customer.addItem(itemId2, 1, LocalDateTime.now());
		
		assertTrue(customer.cart.containsKey(itemId1));
		assertTrue(customer.cart.containsKey(itemId2));

		// test discount 2 //
		float price = cashier.getCartSubtotalPrice();
		assertEquals(price*0.2,cashier.getDiscount(), delta);

		assertTrue(cashier.discount1 == 0);
		assertTrue(cashier.discount2 == 1);
		assertTrue(cashier.discount3 == 0);
	}
	
	@Test
	void testgetDiscount3() throws InvalidMenuItemQuantityException, InvalidMenuItemDataException {
		

		LocalDateTime t = LocalDateTime.now();
		customer = new Customer("Andrew", "A1", t);
		
		cashier = new Cashier("B");
		cashier.createNewCustomer(customer.getName());
		cashier.currentCustomer = customer;
		
		String itemId1 = "Cappuccino";
		String itemId2 = "Panini";
		String itemId3 = "Muffin";

		customer.addItem(itemId3, 3, LocalDateTime.now());
		
		// test discount 3 //
		float price = cashier.getCartSubtotalPrice();
		assertEquals(price*0.25,cashier.getDiscount(), delta);

		assertTrue(cashier.discount1 == 0);
		assertTrue(cashier.discount2 == 0);
		assertTrue(cashier.discount3 == 1);
	}
	
	@Test
	void testgetDiscount4() throws InvalidMenuItemQuantityException, InvalidMenuItemDataException {
		

		LocalDateTime t = LocalDateTime.now();
		customer = new Customer("Andrew", "A1", t);
		
		cashier = new Cashier("B");
		cashier.createNewCustomer(customer.getName());
		cashier.currentCustomer = customer;
		
		String itemId1 = "Cappuccino";
		String itemId2 = "Panini";
		String itemId3 = "Muffin";

		customer.addItem(itemId1, 1, LocalDateTime.now());
		customer.addItem(itemId2, 1, LocalDateTime.now());
		customer.addItem(itemId3, 4, LocalDateTime.now());
		
		// test discount combination 1 //
		assertEquals(4.9625,cashier.getDiscount(), delta);

		assertTrue(cashier.discount1 == 1);
		assertTrue(cashier.discount2 == 0);
		assertTrue(cashier.discount3 == 1);
	}
	
	@Test
	void testgetDiscount5() throws InvalidMenuItemQuantityException, InvalidMenuItemDataException {
		

		LocalDateTime t = LocalDateTime.now();
		customer = new Customer("Andrew", "A1", t);
		
		cashier = new Cashier("B");
		cashier.createNewCustomer(customer.getName());
		cashier.currentCustomer = customer;
		
		String itemId1 = "Cappuccino";
		String itemId2 = "Panini";
		String itemId3 = "Muffin";

		customer.addItem(itemId1, 2, LocalDateTime.now());
		customer.addItem(itemId2, 2, LocalDateTime.now());
		customer.addItem(itemId3, 5, LocalDateTime.now());
		
		// test discount combination 2 //
		assertEquals(8.1625,cashier.getDiscount(), delta);

		assertTrue(cashier.discount1 == 2);
		assertTrue(cashier.discount2 == 0);
		assertTrue(cashier.discount3 == 1);
	}
	
	@Test
	void testgetDiscount6() throws InvalidMenuItemQuantityException, InvalidMenuItemDataException {
		

		LocalDateTime t = LocalDateTime.now();
		customer = new Customer("Andrew", "A1", t);
		
		cashier = new Cashier("B");
		cashier.createNewCustomer(customer.getName());
		cashier.currentCustomer = customer;
		
		String itemId1 = "Cappuccino";
		String itemId2 = "Panini";
		String itemId3 = "Muffin";

		customer.addItem(itemId1, 5, LocalDateTime.now());
		customer.addItem(itemId2, 3, LocalDateTime.now());
		customer.addItem(itemId3, 2, LocalDateTime.now());
		
		// test discount combination 3 //
		assertEquals(8.61,cashier.getDiscount(), delta);

		assertTrue(cashier.discount1 == 2);
		assertTrue(cashier.discount2 == 1);
		assertTrue(cashier.discount3 == 0);
	}
	
	@Test
	void testgetCartTotalPrice() throws InvalidMenuItemQuantityException, InvalidMenuItemDataException {
				
		LocalDateTime t = LocalDateTime.now();
		customer = new Customer("Andrew", "A1", t);
		
		cashier = new Cashier("B");
		cashier.createNewCustomer(customer.getName());
		cashier.currentCustomer = customer;
		
		String itemId1 = "Cappuccino";
		String itemId2 = "Panini";
		String itemId3 = "Muffin";
		
		// add items to cart and check cart final price
		customer.addItem(itemId1, 2, LocalDateTime.now());
		customer.addItem(itemId2, 1, LocalDateTime.now());
		customer.addItem(itemId3, 4, LocalDateTime.now());
		
		System.out.println(cashier.getCartSubtotalPrice());
		
		// check that returned total price is the same it is supposed to be
		assertEquals(17.85,cashier.getCartTotalPrice(), delta);
	}
	
	@Test
	void testGenerateCustomerReport1 () throws InvalidMenuItemQuantityException, InvalidMenuItemDataException {
		// test that generateCustomerReport() works under normal circumstances //
		LocalDateTime t = LocalDateTime.now();
		customer = new Customer("Andrew", "A1", t);
		
		cashier = new Cashier("B");
		cashier.createNewCustomer(customer.getName());
		cashier.currentCustomer = customer;
		
		String itemId1 = "Cappuccino";
		String itemId2 = "Panini";
		String itemId3 = "Muffin";
		
		customer.addItem(itemId1, 2, LocalDateTime.now());
		customer.addItem(itemId2, 1, LocalDateTime.now());
		customer.addItem(itemId3, 4, LocalDateTime.now());
		
		cashier.getCartTotalPrice();
		cashier.generateReceipt();
		cashier.generateCustomerReport();
		
	}
	
	@Test
	void testGenerateCustomerReport2 () throws InvalidMenuItemQuantityException, InvalidMenuItemDataException {
		// test that generateCustomerReport() throws NullPointerException when no receipt has been generated //
		LocalDateTime t = LocalDateTime.now();
		customer = new Customer("Andrew", "A1", t);
		
		cashier = new Cashier("B");
		cashier.createNewCustomer(customer.getName());
		cashier.currentCustomer = customer;
		
		String itemId1 = "Cappuccino";
		String itemId2 = "Panini";
		String itemId3 = "Muffin";
		
		customer.addItem(itemId1, 2, LocalDateTime.now());
		customer.addItem(itemId2, 1, LocalDateTime.now());
		customer.addItem(itemId3, 4, LocalDateTime.now());
		
		cashier.getCartTotalPrice();

		assertThrows(NullPointerException.class,
			() -> {cashier.generateCustomerReport();}
		);
	}
	

}
