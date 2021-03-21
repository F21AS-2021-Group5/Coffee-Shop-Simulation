/**
 * Cashier.java - class to implement the cashier for the coffee shop simulation
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*; 

public class Cashier implements Runnable{
	
	public float subtotal;
	public float tax;
	public float discount;
	public float total;
	public String receipt;
	public static Customer currentCustomer;
	
	private static String line = String.format("%1$" + 55 + "s", "- \n").replace(' ', '-');
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	int discount1 = 0;
	int discount2 = 0;
	int discount3 = 0;
	
	String ID;
	Long delay;
	
	EndOfDay report = new EndOfDay();
	Deque<Customer> shopQueue;
	Thread th;
	
	/**
	 * Constructor for Cashier class
	 * @param ID Cashier identifier
	 */
	public Cashier(String ID, Long delay, Deque<Customer> shopQueue) {
		currentCustomer = null;
		this.ID =ID;
		this.delay = delay;
		this.shopQueue = shopQueue;
		//th = new Thread();
		//th.run();
		//this.th = th ;
	}
	
	/**
	 * @return Cashier identifier 
	 */
	public String getID() {
		return ID;
	}
	

	
	@Override
	public void run(){	
		while (true) {
			if(shopQueue.isEmpty() == false) {
				serveCustomer();
			}
			try {
				Thread.sleep(delay);
			}catch(InterruptedException e) {
				//Thread.currentThread().interrupt();
				System.out.println(e.getMessage());
			}	
		}
	}
	
	public void serveCustomer() {
		synchronized (shopQueue){
			//shopQueue
			if(shopQueue.isEmpty() == false) {
			currentCustomer = shopQueue.pop();
			
			System.out.println("Cash: " + ID+  "  Size: " +shopQueue.size() ); //+ " " +currentCustomer.getName()
			}
			
			//System.out.println(currentCustomer.name);
			shopQueue.notifyAll();
			 /*
			try {
				shopQueue.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 */
	    }
		
	}

   /**
	 * String containing all Customer object details 
	 * @return string for customer receipt
	 */
	public String generateReceipt()
	{
		// Date time formatter 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String datetime = currentCustomer.getTimestamp().format(formatter);
		String[] datetimeSplit = datetime.split(" ");
		 
		// Initial customer information
		String output = String.format("%-20s \n\n", "Customer Receipt:") + 
						String.format("%-10s %-10s\n", "Name:", currentCustomer.getName()) + 
						String.format("%-10s %-10s\n\n", "ID:",currentCustomer.getId()) +
						String.format("%-10s %-10s\n", "Date:", datetimeSplit[0]) +
						String.format("%-10s %-10s\n", "Time:",datetimeSplit[1]) +
						line +
						String.format("ORDER :\n");
		
		// Print all cart orders with quantities 
		Set<String> cartSet = currentCustomer.cart.keySet();
		for (String orderID: cartSet) {
			int quantaty = currentCustomer.cart.get(orderID).size();
			String cost = df2.format(CoffeeShop.menu.get(orderID).getCost()*quantaty) +"£";
			output += String.format("%-4s %-20s %-20s %-20s\n", " ", CoffeeShop.menu.get(orderID).getName(), 
					String.valueOf(quantaty + "x"), 
					cost);
		}
		runCashier();
		output += line +  
				 String.format("%-10s %-30s %-10s\n", " ","Sub total : ", Float.toString(subtotal)+ "£")+ 
	             String.format("%-10s %-30s %-10s\n", " ", "Taxes : ",  df2.format(tax) +"£")+ 
	             String.format("%-10s %-30s %-10s\n"," ", "Discount : ", df2.format(discount) +"£" )+ 
	             String.format("%-10s %-5s %-5s %-5s %-5s %-5s %-5s \n","Type: ","D1 : ",String.valueOf(discount1),  "D2 : ",String.valueOf(discount2), "D3 : ",String.valueOf(discount3) )+ 
	             String.format("%-10s %-30s %-10s\n", " ", "TOTAL : ", df2.format(total) +"£");
		
		// Update the final prices 
		receipt = output;
		return output;
	}
		
		
	/**
	 * Creates new customer 
	 * @param name of the customer
	 */
   public void createNewCustomer(String name) {
 	   LocalDateTime timeStamp = LocalDateTime.now();
 	   Customer newCustomer = new Customer(name, timeStamp);
 	   CoffeeShop.customerList.put(newCustomer.getId(), newCustomer);
 	   currentCustomer = newCustomer;
    }
  
	/**
	 * @return Cart sub total price 
	 */
	public synchronized float getCartSubtotalPrice() {
		subtotal = currentCustomer.getCartTotalPrice();
		return subtotal;
	}
	
	/**
	 * @return Cart tax 
	 */
	public float getCartTax() {
		// loop through items in cart
		// if item is taxable, add 20% of item price to tax 
		tax = (float) (subtotal*0.25);
		return tax;
	}
	
	/**
	 * Calculates the discounted price based on the items inside the cart 
	 * @return Discounted price 
	 */
	public float getDiscount() {
		// discount 1 = 1 drink & 1 food & 1 pastry = £5 //
		// discount 2 = 3 drinks & 1 food = 20% off //
		// discount 3 = 3 pastries = 25% off //
		
		List<Float> food = new ArrayList<Float>();
		List<Float> drink = new ArrayList<Float>();
		List<Float> pastry = new ArrayList<Float>();
		
		
		// Go through the cart to find all the items but
		Set<String> cartSet = currentCustomer.cart.keySet(); 
		
		for (String orderID: cartSet) {
			
			int quantity = currentCustomer.cart.get(orderID).size();
			//System.out.println(quantity);
			
			if (CoffeeShop.menu.get(orderID).getCategory().equals("Drink")) {
				for(int i = 1; i <= quantity; i++) {
					drink.add(CoffeeShop.menu.get(orderID).getCost());
				}
			} else if (CoffeeShop.menu.get(orderID).getCategory().equals("Food")) {
				for(int i = 1; i <= quantity; i++) {
					food.add(CoffeeShop.menu.get(orderID).getCost());
				}
				
			} else if (CoffeeShop.menu.get(orderID).getCategory().equals("Pastry") ) {
				for(int i = 1; i <= quantity; i++) {
					pastry.add(CoffeeShop.menu.get(orderID).getCost());
				}	
			}
        } 
		
		boolean noMoreDiscountsAvailable = false;
		discount1 = 0;
		discount2 = 0;
		discount3 = 0;
		
		while(!noMoreDiscountsAvailable) {
			//System.out.println(currentCustomer.getId());
			//System.out.println("before f " + food.size() + " d " + drink.size() + " p " + pastry.size());
			
			// First checks to see all the combinations
			if (drink.size() >= 1 && food.size() >= 1 && pastry.size() >= 1) {
				float combination = drink.remove(0) + food.remove(0) + pastry.remove(0);
				discount += combination - 5;
				discount1 = discount1 + 1;
				
			// Then if other discounts are available
			} else if (drink.size() >= 3 && food.size() >= 1) { 
				float combination = drink.remove(0) + drink.remove(0) + drink.remove(0) + food.remove(0);
				discount += combination / 5;
				discount2 = discount2 + 1;
				
			} else if (pastry.size() >= 3) {
				float combination = pastry.remove(0) + pastry.remove(0) + pastry.remove(0);
				discount += combination / 4;
				discount3 = discount3 + 1;
				
			} else {
				// No more discounts
				noMoreDiscountsAvailable = true;
			}	
			//System.out.println("after f " + food.size() + " d " + drink.size() + " p " +pastry.size());
		}	
		return discount;
	}
	
	/**	
	 * @param customer Customer object 
	 */
	public void setCustomer(Customer customer) {
		// For values that are initialised at the initialisation of coffeeShop
		//System.out.println("here");
		currentCustomer = customer;
	}
	
	/**
	 * @return Sub total, tax, discount, total, discount n.1, discount n.2, and discount n.3
	 */
	public float[] returnSums() {
		float[] allValues = {subtotal, tax, discount, total, discount1, discount2, discount3};
		//System.out.println(allValues[0]);
		return allValues;
	}
	
	/**
	 * @return Cart total price 
	 */
	public float getCartTotalPrice() {
		total = subtotal + tax - discount;
		return total;
	}
	
	/**
	 * Updates final customer cart sum 
	 */
	public void runCashier() {
		subtotal = 0;
		tax = 0;
		discount = 0;
		total = 0;
		
		getCartSubtotalPrice();
		getCartTax();
		getDiscount();
		getCartTotalPrice();
		
		report.updateFinalSum(returnSums());
	}
	
	/**
	 * @return Final report for customer 
	 */
	public String generateFinalReport() {
		return report.generateFinalReportDisplay();
	}
	
	/**
	 * Writes report to text file 
	 */
	public void generateCustomerReport() {
		   try {
			   FileWriter customerWriter = new FileWriter("Receit " + currentCustomer.getId() + ".txt");
			   customerWriter.write(receipt);
			   customerWriter.close();
		   } catch (IOException e) {
			   System.out.println("An error occurred.");
			   e.printStackTrace();
		   }
	}
	
	/**
	 * Calls function that generates the final report text file 
	 */
	public void generateFinalReportFile() {
		report.generateFinalReport(); 
	}
	
	
	

}
