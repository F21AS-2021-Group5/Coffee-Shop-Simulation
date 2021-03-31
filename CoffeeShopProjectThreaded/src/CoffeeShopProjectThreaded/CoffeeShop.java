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
//import CoffeeShopProjectThreaded.GUIcaffee;


public class CoffeeShop {
	// Written data files 
	public static HashMap<String, MenuItem> menu;                 // Stores menu items 
	public static HashMap<String, ArrayList<String> > recipeBook; // Stores recipes
	public static Map<String, HashSet<String> > foodMap;          // Map
	
	static NewGUI gui;
	

	// Data MODELS 
	NewCustomerQueue queue;
	
	public static NewCustomerQueue shopQueue;
	public static NewCustomerQueue onlineQueue;

	public static Inventory inventory;
	public static Bookkeeping books;
	public static OrderQueue kitchenQueue;
	public static OrderQueue barQueue;
	public static Employees employees;
	
	//ArrayList<Thread> cashierThreads = new ArrayList<Thread>(); 
	public static HashMap<String, Thread> cashierThreads;
	public static HashMap<String, Thread> baristaThreads;
	public static HashMap<String, Thread> cookThreads;
	

	String[] cashierNames={"Ron","Leslie", "April", "Donna","Andy","Ann","Ben", 
			"Tom", "Jerry", "Gerry", "Lerry"};   // -
	String[] baristaNames={"Mac","Cahrlie", "Frank", "Deandra","Dennis"};   // -
	String[] cookNames={"Dwight","Pam", "Jim", "Andy","Kelly", "Angela"};   // - 
	
	//public static HashMap<String, Cashier> acctiveCashiers;  // - 
	public static HashMap<String, Customer> customerList;   // -
	//public static NewCustomerQueue inshopQueue;



	
	/**
	 * Constructor for CoffeeShop class
	 * @param name Name of cashier 
	 */
	public CoffeeShop()
	{
		menu = new HashMap<>(); 
		customerList = new HashMap<>();
		//inshopQueue = new NewCustomerQueue(false);
		
		fillMenu("MenuItems");                      // Fills the menu with all the items 
		

		// Model, all the data used by different components
		queue = new NewCustomerQueue(false);        // Customer queue
		inventory = new Inventory();                // Inventory of ordered items

		shopQueue = new NewCustomerQueue(false);   // In-shop Customer queue
		onlineQueue = new NewCustomerQueue(true);  // Online Customer queue
		inventory = new Inventory();                  // Inventory of ordered items
		books = new Bookkeeping();                  // Bookkeeping of earnings and other finances
		employees = new Employees();                // Currently working employees 
		
		kitchenQueue = new OrderQueue(false);       // Kitchen queue
		barQueue = new OrderQueue(true);            // Bar queue
		
		// Sub controllers, threads 
		cashierThreads = new HashMap<String, Thread>();
		baristaThreads = new HashMap<String, Thread>();
		cookThreads = new HashMap<String, Thread>();

		
		//activeCashiers = new HashMap<String, Cashier>();
		
		//fillCustomerList("CustomerList");	

				
		// Fill recipe book 
		recipeBook = new HashMap<String, ArrayList<String> >();
		fillRecipeBook("RecipeBook");	
		// Fill food map 
		foodMap = new HashMap<String, HashSet<String> >();
		fillFoodMap();
		
		//acctiveCashiers = new HashMap<String, Cashier>();  // -
		//fillCustomerList("CustomerList");	  // -
	}
	
	/**
	 * Read menu data and store it in menu map 
	 * @param fileName String
	 */
   public void fillMenu(String fileName) {
       BufferedReader br = null;                         // Reader
       try {
    	   FileReader file = new FileReader(fileName);   // Read file
    	   br = new BufferedReader(file);                // Create buffer reader for file
    	   String inputLine = br.readLine();             // Read first line
           while (inputLine != null) {                   // While its contains stuff
               String[] data = inputLine.split(";");     // Split by semicolon
               if (data.length == 5) {
                   try {
                       MenuItem item = new MenuItem(data[0], data[1], data[2], 
                    		   Float.parseFloat(data[3]), data[4]); // New MenuItem
                       menu.put(data[1].trim(), item);    // Put MenuItem in menu
                   } catch (IllegalArgumentException e){  //If add anything anything unacceptable in MenuItem
                	   throw new IllegalArgumentException();
                   }
               } else {
                  System.out.println("Invalid data line. Will drop it. \n");
               }
               inputLine = br.readLine();                 // Read the next line
           }
       // Catch possible exceptions 
       } catch (FileNotFoundException e) {
    	   System.out.println("File does not exist. \n"); 
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           try {
               br.close();
           } catch (IOException e) {
               // Do nothing
           }
       }
   }
   
   /**
    * Converts a string to LocalDateTime object
    * @param timeString Time stamp in string format 
    * @return Corresponding LocalDateTime object 
    */
   public static LocalDateTime stringToTimestamp(String timeString){
	   LocalDateTime localDateTime = null;
	   try {
		   // Set formatter and convert string to LocalDateTime 
		   String pattern = "yyyy-MM-dd HH:mm:ss.SSSSSSSSS";
		   DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		   localDateTime = LocalDateTime.from(formatter.parse(timeString));
		} catch(Exception e) { 						// Generic exception
		    // Look the origin of exception 
			System.out.println("Could't convert sting to time stamp");
		}
	    return localDateTime;                       // Fine to return 0 since catch in the Customer class 
   }
   
   /**
    * Fills the food map that tells who needs to make the food: kitchen or bar 
    */
   public void fillFoodMap() {
		// Initialise map
		foodMap.put("Kitchen", new HashSet<String>());
		foodMap.put("Bar", new HashSet<String>());		
		
		for (String name: menu.keySet())
		{
			String itemId = menu.get(name).getIdentifier();             // Get ID of the menu item 
			String onlyChars = itemId.replaceAll("[^A-Za-z]+", "");     // Find what type it is 
			if (onlyChars.equals("FOOD") || onlyChars.equals("SIDE"))   // If it is FOOD or SISE 
				foodMap.get("Kitchen").add(name);                       // Add to kitchen staff
			else if (onlyChars.equals("PASTRY") || onlyChars.equals("DRINK")) 
				foodMap.get("Bar").add(name);                           // Add to bar staff
		}
		
   }
   
	/**
	 * Fills recipe book 
	 * @param file Text file name 
	 */
   public void fillRecipeBook(String file) {
		BufferedReader recipeReader = null;
		try {	   		
			// Create buffered reader 
			recipeReader = new BufferedReader(new FileReader(file));		   		
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		}
		
		String line = null;
		ArrayList<String> instructions = new ArrayList<String>();
		String itemId = null;
		
		while(true) {						
  			// Add to recipe book
  			try {
  				line = recipeReader.readLine();
  			} catch (IOException e1) {
  				e1.printStackTrace();
  			}	
			
			if (line != null) {		
	   			// Split by semicolon
	   			String[] data = line.split(";");
	       		
	   			// Check whether line contains correct number of information 		       		
	       		if (data.length == 3) {	

			   		// Add recipe to recipe book 
	       			if (instructions.size() > 0 && itemId != null) {
	       				
				   		recipeBook.put(itemId, new ArrayList<String>(instructions));		       			 
				   		instructions.clear(); 
	       			}	       			
	   				// Assign item ID and clear instructions	
	       			itemId = data[1];     			
	   			} 
	       		
	       		// Add instruction if line is not empty 
	   			else if (data.length == 1 && !data[0].trim().isEmpty())	   				
	   				instructions.add(data[0]);	 	   			 		   			
		   		
	       		// Line empty 
	   			else 
	   				System.out.println("Invalid data line. Will drop it. \n");
			} 
			else {
				// Add recipe to recipe book 
      			if (instructions.size() > 0 && itemId != null) 
      				recipeBook.put(itemId, new ArrayList<String>(instructions));
 
				break;
			}
		}		
	}
   
//Working on this 	
	/**
	 * Adds new cashier and creates a thread for it  
	 */
   public static Cashier addCashier(Long delay) {
	   Cashier cash = employees.addCashier(delay); // Add cashier to the employees model
	   cash.addPropertyChangeListener(gui);  // Establish OBSERVER-OBSERVABLE channel 
	   // Create runnable for cashier object
	   Runnable cashier = new CashierRunnable(cash.getName(), onlineQueue, shopQueue, kitchenQueue, barQueue, inventory, books, cash); // or an anonymous class, or lambda...
	   Thread t = new Thread(cashier);
	   t.setPriority(2);                      // Sets priority
	   cashierThreads.put(cash.getName(), t);   // Holds active cashier threads
	   t.start(); 
	   
	   return cash;
   }
   /**
	 * Creates customer queue handler which adds customers to the queue  
	 * @param integer for the amount of customers we wish to accept to the store
	 */
   private void createHandler(int number) {
	    // Create handler which adds to the customer queue
	    Runnable handler = new QueueHandler(onlineQueue, shopQueue, 1000L, number);
	    Thread h = new Thread(handler);
	    // High priority since customers are the once who are deciding when they will arrive
	    h.setPriority(8);                
		h.start(); 
   }
   
   /**
	 * Adds new barista and creates a thread for it  
	 */
   //THIS METHOD WAS PUBLIC VOID ADDBARISTA()
   //CHANGED TO STATIC FOODSTAFF
   public static FoodStaff addBarista(Long delay) {
	   FoodStaff barStaff = employees.addBarista(delay); // Add cashier to the employees model
	   barStaff.addPropertyChangeListener(gui);
	   //String name = getRandomName( baristaNames, baristaList);
	   //System.out.println("Cashier " + name + " has started their shift");
	   
	   // Create barista object which processes orders related to Drinks and Pastry	   
	   Runnable barista = new FoodStaffRunnable(barStaff, barQueue);
	   Thread s = new Thread(barista);
	   baristaThreads.put(barStaff.getName(), s);
	   s.start();
	   
	   return barStaff;
   }
   
   /**
	 * Adds new cook and creates a thread for it  
	 */
   //THIS METHOD WAS PUBLIC VOID ADDCOOK()
   //CHANGED TO STATIC FOODSTAFF
   public static FoodStaff addCook(Long delay) {
	   FoodStaff kitchenStaff = employees.addCook(delay); // Add cashier to the employees model
	   kitchenStaff.addPropertyChangeListener(gui);
	   // Creates cook object which processes orders related to Food and Sides
	   Runnable cook = new FoodStaffRunnable(kitchenStaff, kitchenQueue);
	   Thread s2 = new Thread(cook);
	   cookThreads.put(kitchenStaff.getName(), s2);
	   s2.start();
	 
	   
	   return kitchenStaff;
   }
   
   // DELETE
   private String getRandomName(String[] list, HashMap<String, Thread> map) {
	   Random rn = new Random();
	   String name = list[rn.nextInt(list.length)]; // Given the length of name list
	   while(map.containsKey(name)) {
		   name = list[rn.nextInt(list.length)];
	   }
	   return name;

   }
	
   /**
	 * Kills the given cashier thread and removes it from the active cashier map
	 * @param name of the cashier to end their shift
	 */
   public static synchronized void removeCashier(String name) {
	   System.out.println("Cashier " + name + " has ended their shift");
	   cashierThreads.get(name).interrupt();  // Interrupt the thread causing it to finalise their run 
	   cashierThreads.remove(name);  // Remove the thread from the list
	   employees.removeCashier(name); // Remove active cashier from the model 
   }
   
   
   /**
	 * Kills the given cook thread and removes it from the active cook map
	 * @param name of the cook to end their shift
	 */
   public static synchronized void removeCook(String name) {
	   System.out.println("Cook " + name + " has ended their shift");
	   cookThreads.get(name).interrupt();  // Interrupt the thread causing it to finalise their run 
	   cookThreads.remove(name);  // Remove the thread from the list
	   employees.removeCook(name); // Remove active cashier from the model 
   }
   
   /**
  	 * Kills the given barista thread and removes it from the active barista map
  	 * @param name of the barista to end their shift
  	 */
   public static synchronized void removeBarista(String name) {
	   System.out.println("Barista" + name + " has ended their shift");
	   cashierThreads.get(name).interrupt();  // Interrupt the thread causing it to finalise their run 
	   cashierThreads.remove(name);  // Remove the thread from the list
	   employees.removeCashier(name); // Remove active cashier from the model 
   }
   


   // main method 
	public static void main(String[] args) {
		CoffeeShop shop = new CoffeeShop();
		shop.createHandler(21);
		
		//shop.addCashier();
		//shop.addCashier();

		gui = new NewGUI(shop.shopQueue, shop.cashierThreads, shop.cookThreads, shop.baristaThreads);
		//shopQueue.addPropertyChangeListener(gui);
		//MyPropertyChange observer = new MyPropertyChange();

		//observable.addPropertyChangeListener(observer);
		CoffeeShop.shopQueue.addPropertyChangeListener(gui);
		CoffeeShop.onlineQueue.addPropertyChangeListener(gui);
		CoffeeShop.employees.addPropertyChangeListener(gui);
		//CoffeeShop.cashierThreads.addPropertyChangeListener(gui);
	
		
		gui.initializeGUI();
		gui.paintScreen();
		gui.run();

		//shop.addBarista();
		//shop.addBarista();
		
		//shop.addCook();
		//shop.addCook();
		
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

		
		//NewGUI gui = new NewGUI(shop);
		//gui.DisplayGUI();
		//gui.run();
		
	}

}
