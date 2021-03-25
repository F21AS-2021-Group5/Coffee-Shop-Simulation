/**
 * CoffeeShop.java - class to implement the coffee shop simulation
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

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
// Import the File class
// Import the IOException class to handle errors
import java.io.*;
import java.security.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Random;

//import CaffeeShopProject.CoffeeShop;
import CoffeeShopProjectThreaded.GUIcaffee;


public class CoffeeShop {
	
	public static HashMap<String, MenuItem> menu;
	public static HashMap<String, Customer> customerList;
	public static HashMap<String, ArrayList<String> > recipeBook; // stores recipes
	public static Map<String, HashSet<String> > foodMap;
	
	Cashier cashier;
	
	NewCustomerQueue queue;
	Inventory inventory;
	Bookkeeping books;
	OrderQueue kitchenQueue;
	OrderQueue barQueue;
	
	//ArrayList<Thread> cashierList = new ArrayList<Thread>(); 
	HashMap<String, Thread> cashierList;
	
	String[] cashierNames={"Ron","Leslie", "April", "Donna","Andy","Ann","Ben", 
			"Tom", "Jerry", "Gerry", "Lerry"};  
	
	/**
	 * Constructor for CoffeeShop class
	 * @param name Name of cashier 
	 */
	public CoffeeShop()
	{
		menu = new HashMap<>(); 
		customerList = new HashMap<>();
		cashier = null; 
		
		fillMenu("MenuItems");
		
		queue = new NewCustomerQueue(false);   // Customer queue
		inventory = new Inventory();                  // Inventory of ordered items
		books = new Bookkeeping();                  // Bookkeeping of earnings and other finances
		
		kitchenQueue = new OrderQueue(false);
		barQueue = new OrderQueue(true);
		// creates cashier and fills menu and customer list data structures 
		//createNewCashier(name);
		cashierList = new HashMap<String, Thread>();
		
		//fillCustomerList("CustomerList");	
				
		// fill recipe book 
		recipeBook = new HashMap<String, ArrayList<String> >();
		fillRecipeBook("RecipeBook");
		
		// fill food map 
		foodMap = new HashMap<String, HashSet<String> >();
		fillFoodMap();
	
	}
	
	/**
	 * Creates a new Cashier instance 
	 * @param id Cashier identifier 
	 */
	/*
	private void createNewCashier(String id, Long delay) {
		cashier = new Cashier(id, delay);
	}
	*/
	
	/**
	 * Read menu data and store it in menu map 
	 * @param fileName String
	 */
   public void fillMenu(String fileName) {
       BufferedReader br = null; //reader
       try {
    	   FileReader file = new FileReader(fileName);   // Read file
    	   br = new BufferedReader(file);       // Create buffer reader for file
    	   String inputLine = br.readLine();   // Read first line
           while (inputLine != null) {                             //while its contains stuff
               String[] data = inputLine.split(";");        //split by semicolon
               if (data.length == 5) {
                   try {
                       MenuItem item = new MenuItem(data[0], data[1], data[2], Float.parseFloat(data[3]), data[4]); //New MenuItem
                       menu.put(data[1].trim(), item);                             // Put MenuItem in menu
                   } catch (IllegalArgumentException e){ 	//If add anything anything unacceptable in MenuItem
                	   throw new IllegalArgumentException();
                   }
               } else {
                  System.out.println("Invalid data line. Will drop it. \n");
               }
               inputLine = br.readLine();                          //read the next line
           }
       // catch possible exceptions 
       } catch (FileNotFoundException e) {
    	   System.out.println("File does not exist. \n"); 
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           try {
               br.close();
           } catch (IOException e) {
               //do nothing
           }
       }
   }
   
   /**
    * Fills the food map that tells who needs to make the food: kitchen or bar 
    */
   public void fillFoodMap() {
		// initialize map
		foodMap.put("Kitchen", new HashSet<String>());
		foodMap.put("Bar", new HashSet<String>());		
		
		for (String name: menu.keySet())
		{
			String itemId = menu.get(name).getIdentifier();
			String onlyChars = itemId.replaceAll("[^A-Za-z]+", "");
			if (onlyChars.equals("FOOD") || onlyChars.equals("SIDE"))
				foodMap.get("Kitchen").add(name);
			else if (onlyChars.equals("PASTRY") || onlyChars.equals("DRINK"))
				foodMap.get("Bar").add(name);
		}
		
   }
   
	/**
	 * Fills recipe book 
	 * @param file Text file name 
	 */
	public void fillRecipeBook(String file) {
		BufferedReader recipeReader = null;
		try {	   		
			// create buffered reader 
			recipeReader = new BufferedReader(new FileReader(file));		   		
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		}
		
		String line = null;
		ArrayList<String> instructions = new ArrayList<String>();
		String itemId = null;
		
		while(true) {						
  			// add to recipe book
  			try {
  				line = recipeReader.readLine();
  			} catch (IOException e1) {
  				e1.printStackTrace();
  			}	
			
			if (line != null) {		
	   			//split by semicolon
	   			String[] data = line.split(";");
	       		
	   			// check whether line contains correct number of information 		       		
	       		if (data.length == 3) {	

			   		// add recipe to recipe book 
	       			if (instructions.size() > 0 && itemId != null) {
	       				
				   		recipeBook.put(itemId, new ArrayList<String>(instructions));		       			 
				   		instructions.clear(); 
	       			}	       			
	   				// assign item ID and clear instructions	
	       			itemId = data[1];     			
	   			} 
	       		
	       		// add instruction if line is not empty 
	   			else if (data.length == 1 && !data[0].trim().isEmpty())	   				
	   				instructions.add(data[0]);	 	   			 		   			
		   		
	       		// line empty 
	   			else 
	   				System.out.println("Invalid data line. Will drop it. \n");
			} 
			else {
				// add recipe to recipe book 
      			if (instructions.size() > 0 && itemId != null) 
      				recipeBook.put(itemId, new ArrayList<String>(instructions));
 
				break;
			}
		}		
	}	
   
//Working on this 	
   private void createNewCashier() {
	   Random rn = new Random();
	   String name = cashierNames[rn.nextInt(cashierNames.length)];
	   while(cashierList.containsKey(name)) {
		   name = cashierNames[rn.nextInt(cashierNames.length)];
	   }
	   System.out.println(name);
	   Runnable cashier = new CashierTrial(name, 800L, null, queue, kitchenQueue, barQueue, inventory, books); // or an anonymous class, or lambda...
	   Thread t = new Thread(cashier);
	   t.setPriority(2);
	   cashierList.put(name, t);
	   t.start();
	   
   }
   
   private void createHandler() {
	    Runnable handler = new QueueHandler(null, queue, 600L, 15);
	    Thread h = new Thread(handler);
	    h.setPriority(8);
	    //cashierList.put("Handler", h);
		h.start();
   }
   
   private synchronized void removeCashier(String name) {
	   //while(cashierList.get("Handler").isAlive()) {	   
	   //}
	   System.out.println("Cashier " + name + " has ended their shift");
	   cashierList.get(name).interrupt();
	   cashierList.remove(name);

   }
  
   

   
   
   // main method 
	public static void main(String[] args) {
		// Needs initial cashier 
		
		CoffeeShop shop = new CoffeeShop();
		//NewCustomerQueue queue = new NewCustomerQueue(false); 
		shop.createHandler();
		shop.createNewCashier();
		shop.createNewCashier();
		
		//shop.removeCashier();
		
		//System.out.println("AFTHER");
		//System.out.println(shop.books.getCustomerNumber());
		//shop.removeCashier("Barbara");
		
		
		//long time = 400L;
		//long time1 = 800L;
		//long time2 = 1000L;
		
		/*
		NewCustomerQueue queue = new NewCustomerQueue(false);   // Customer queue
		Inventory inventory = new Inventory();                  // Inventory of ordered items
		Bookkeeping books = new Bookkeeping();                  // Bookkeeping of earnings and other finances
		
		OrderQueue kitchenQueue = new OrderQueue(false);
		OrderQueue barQueue = new OrderQueue(true);
		
		Runnable handler = new QueueHandler(null, queue, 700L, 6);
		Thread h = new Thread(handler);
		h.setPriority(8);
		h.start();		
		
		
		Runnable cashierOne = new CashierTrial("Adam", 800L, null, queue, kitchenQueue, barQueue, inventory, books); // or an anonymous class, or lambda...
		Thread t1 = new Thread(cashierOne);
		t1.setPriority(2);
		t1.start();
		
		
		
		Runnable cashierTwo = new CashierTrial("Barbara", 900L, null, queue, kitchenQueue, barQueue, inventory, books);; // or an anonymous class, or lambda...
		Thread t2 = new Thread(cashierTwo);
		t2.setPriority(2);
		t2.start();
		*/ 
		 
		
		/*

		Runnable cashierThree = new CashierTrial("Mindy", 1000L, queue);; // or an anonymous class, or lambda...
		Thread t3 = new Thread(cashierThree);
		t3.start();
		 */
		
		
		
		//cashier2.start();
		//cashier2.run();
		/*
		while(true) {
			try {
		          Thread.sleep(1000);
		          System.out.println(CustomerQueue.shopQueue.size());
		    } catch(InterruptedException e) {
		          
		    }
		}
		*/
		//System.out.println(CustomerQueue.shopQueue.pop().cart);
	
	    //System.out.println(CustomerQueue.shopQueue.pop().cart);
		
		//Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		//System.out.println(threadSet);
		//custQue1.run();


		//custQue.randCustomerToQueue();
		
		
		// Each cashier has their own GUI 
//		GUIcaffee GUI = new GUIcaffee(shop.cashier);
//		GUI.initializeGUI(); 
//		GUI.paintScreen();		
		
		//Thread addCustomerThread = new Thread(new CustomerQueue("CustomerList", "CustomerListOnline", 1500));
		//addCustomerThread.start();
		/*
		OrderQueue kitchenQueue = new OrderQueue(false);
		OrderQueue barQueue = new OrderQueue(true);
		
		kitchenQueue.addToQueue("Marco", "Toastie");
		barQueue.addToQueue("Matteo", "Espresso");
		barQueue.addToQueue("Alessandro", "Croissant");
		kitchenQueue.addToQueue("Fabrizio", "Fries");
		barQueue.addToQueue("Francesca", "Cappuccino");	
		*/	
		 ///THIIIIIS
		/*
		Runnable baristaOne = new Staff("Paolo", barQueue, 2000L);
		Thread s1 = new Thread(baristaOne);
		s1.start();
		
		Runnable baristaTwo = new Staff("Lavinia", barQueue, 1500L);
		Thread s2 = new Thread(baristaTwo);
		s2.start();
		
		
		Runnable cookOne = new Staff("Giulia", kitchenQueue, 2000L);
		Thread s3 = new Thread(cookOne);
		s3.start();
		
		Runnable cookTwo = new Staff("Francesco", kitchenQueue, 2000L);
		Thread s4 = new Thread(cookTwo);
		s4.start();
        */
		
		//orderQueue.addToQueue(1, "FOOD001", false);
		//orderQueue.addToQueue(1, "DRINK003", true);
		//orderQueue.addToQueue(2, "PASTRY001", false);
		//orderQueue.addToQueue(3, "DRINK005", true);
		
		//System.out.println(orderQueue.barQueue);
		
		//Thread cook1 = new Thread(new Cook(orderQueue));
		//Thread barista1 = new Thread(new Barista(orderQueue));
		
		//cook1.start();
		//barista1.start();
		
	}

}
