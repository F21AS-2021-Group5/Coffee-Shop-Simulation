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
	
<<<<<<< HEAD
	// Data MODELS 
	NewCustomerQueue queue;
=======
	public static HashMap<String, MenuItem> menu;
	public static HashMap<String, Customer> customerList;
	public static HashMap<String, ArrayList<String> > recipeBook; // stores recipes
	public static Map<String, HashSet<String> > foodMap;
	public static HashMap<String, Cashier> activeCashiers;
	
	Cashier cashier;
	
	NewCustomerQueue shopQueue;
	NewCustomerQueue onlineQueue;
>>>>>>> aac438b270bf386eb08036bfde8ed6a33b61dbe6
	Inventory inventory;
	Bookkeeping books;
	OrderQueue kitchenQueue;
	OrderQueue barQueue;
	Employees employees;
	
	//ArrayList<Thread> cashierThreads = new ArrayList<Thread>(); 
	HashMap<String, Thread> cashierThreads;
	HashMap<String, Thread> baristaList;
	HashMap<String, Thread> cookList;
	
<<<<<<< HEAD
	String[] cashierNames={"Ron","Leslie", "April", "Donna","Andy","Ann","Ben", 
			"Tom", "Jerry", "Gerry", "Lerry"};   // -
	String[] baristaNames={"Mac","Cahrlie", "Frank", "Deandra","Dennis"};   // -
	String[] cookNames={"Dwight","Pam", "Jim", "Andy","Kelly", "Angela"};   // - 
	
	public static HashMap<String, Cashier> acctiveCashiers;  // - 
	public static HashMap<String, Customer> customerList;   // -
=======
	String[] cashierNames={"Ron","Leslie","April","Donna","Andy","Ann","Ben", 
			"Tom","Jerry","Gerry","Lerry"};  
	String[] baristaNames={"Mac","Charlie","Frank","Deandra","Dennis"};  
	String[] cookNames={"Dwight","Pam","Jim","Andy","Kelly", "Angela"};
>>>>>>> aac438b270bf386eb08036bfde8ed6a33b61dbe6
	
	/**
	 * Constructor for CoffeeShop class
	 * @param name Name of cashier 
	 */
	public CoffeeShop()
	{
		menu = new HashMap<>(); 
		customerList = new HashMap<>();
		
		fillMenu("MenuItems");                      // Fills the menu with all the items 
		
<<<<<<< HEAD
		// Model, all the data used by different components
		queue = new NewCustomerQueue(false);        // Customer queue
		inventory = new Inventory();                // Inventory of ordered items
=======
		shopQueue = new NewCustomerQueue(false);   // In-shop Customer queue
		onlineQueue = new NewCustomerQueue(true);  // Online Customer queue
		inventory = new Inventory();                  // Inventory of ordered items
>>>>>>> aac438b270bf386eb08036bfde8ed6a33b61dbe6
		books = new Bookkeeping();                  // Bookkeeping of earnings and other finances
		employees = new Employees();                // Currently working employees 
		
		kitchenQueue = new OrderQueue(false);       // Kitchen queue
		barQueue = new OrderQueue(true);            // Bar queue
		
		// Sub controllers, threads 
		cashierThreads = new HashMap<String, Thread>();
		baristaList = new HashMap<String, Thread>();
		cookList = new HashMap<String, Thread>();
<<<<<<< HEAD
=======
		
		activeCashiers = new HashMap<String, Cashier>();
		
		//fillCustomerList("CustomerList");	
>>>>>>> aac438b270bf386eb08036bfde8ed6a33b61dbe6
				
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
	
	/**
    * Converts a string to LocalDateTime object
    * @param timeString Time stamp in string format 
    * @return Corresponding LocalDateTime object 
    */
   public static LocalDateTime stringToTimestamp(String timeString){
	   LocalDateTime localDateTime = null;
	   try {
		   // set formatter and convert string to LocalDateTime 
		   String pattern = "yyyy-MM-dd HH:mm:ss.SSSSSSSSS";
		   DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		   localDateTime = LocalDateTime.from(formatter.parse(timeString));
		} catch(Exception e) { //this generic but you can control another types of exception
		    // look the origin of exception 
			System.out.println("Could't conver sting to time stamp");
		}
	    return localDateTime; // Fine to return 0 since catch in the Customer class 
   }
   
<<<<<<< HEAD
//Working on this 	
	/**
	 * Adds new cashier and creates a thread for it  
	 */
   private void addCashier() {
	   Cashier cash = employees.addCashier(); // Add cashier to the employees model
	   // Create runnable for cashier object
	   Runnable cashier = new CashierRunnable(cash.getName(), 800L, null, queue, kitchenQueue, barQueue, inventory, books, cash); // or an anonymous class, or lambda...
	   Thread t = new Thread(cashier);
	   t.setPriority(2);                      // Sets priority
	   cashierThreads.put(cash.getName(), t);   // Holds active cashier threads
	   t.start();                             // Starts the thread
	
   }
   /**
	 * Creates customer queue handler which adds customers to the queue  
	 * @param integer for the amount of customers we wish to accept to the store
	 */
   private void createHandler(int number) {
	    // Create handler which adds to the customer queue
	    Runnable handler = new QueueHandler(null, queue, 600L, number);
	    Thread h = new Thread(handler);
	    // High priority since customers are the once who are deciding when they will arrive
	    h.setPriority(8);                
		h.start(); 
   }
   
   /**
	 * Adds new barista and creates a thread for it  
	 */
   private void addBarista() {
	   String name = getRandomName( baristaNames, baristaList);
	   System.out.println("Cashier " + name + " has started their shift");
	   // Create barista object which processes orders related to Drinks and Pastry
	   Runnable barista = new Staff(name, barQueue, 2000L);
	   Thread s = new Thread(barista);
	   s.start();
   }
   
   /**
	 * Adds new cook and creates a thread for it  
	 */
   private void addCook() {
	   String name = getRandomName( cookNames, cookList);
	   // Creates cook object which processes orders related to Food and Sides
	   Runnable cook = new Staff(name, kitchenQueue, 2000L);
	   Thread s2 = new Thread(cook);
	   s2.start();
   }
   
   // DELETE
   private String getRandomName(String[] list, HashMap<String, Thread> map) {
	   Random rn = new Random();
	   String name = list[rn.nextInt(list.length)]; // Given the length of name list
	   while(map.containsKey(name)) {
		   name = list[rn.nextInt(list.length)];
	   }
	   return name;
=======
   /**
    * Creates cashier thread 
    */
   private void addCashier() {
	   Random rn = new Random();
	   String name = cashierNames[rn.nextInt(cashierNames.length)];
	   
	   while(cashierList.containsKey(name)) {
		   name = cashierNames[rn.nextInt(cashierNames.length)];
	   }
	   
	   Cashier cash = new Cashier(name);
	   activeCashiers.put(name, cash);
	   
	   Runnable cashier = new CashierTrial(name, 800L, onlineQueue, shopQueue, kitchenQueue, barQueue, inventory, books, cash); // or an anonymous class, or lambda...
	   Thread t = new Thread(cashier);
	   t.setPriority(2);
	   cashierList.put(name, t);
	   t.start();	
   }
   
   /**
    * Creates queue handler thread
    */
   private void createHandler() {
	    Runnable handler = new QueueHandler(onlineQueue, shopQueue, 600L, 15);
	    Thread h = new Thread(handler);
	    h.setPriority(8);
	    //cashierList.put("Handler", h);
		h.start();		
   }
   
   /**
    * Creates barista thread
    */
   private void addBarista() {
	   Random rn = new Random();
	   String name = baristaNames[rn.nextInt(baristaNames.length)];
	   
	   while(baristaList.containsKey(name)) {
		   name = baristaNames[rn.nextInt(baristaNames.length)];
	   }
	   
	   Runnable barista = new Staff(name, barQueue, 2000L);
	   Thread b = new Thread(barista);
	   b.start();
   }
   
   /**
    * Creates cook thread
    */
   private void addCook() {
	   Random rn = new Random();
	   String name = cookNames[rn.nextInt(cookNames.length)];
	   
	   while(cookList.containsKey(name)) {
		   name = cookNames[rn.nextInt(cookNames.length)];
	   }
>>>>>>> aac438b270bf386eb08036bfde8ed6a33b61dbe6
	   
	   Runnable cook = new Staff(name, kitchenQueue, 2000L);
	   Thread c = new Thread(cook);
	   c.start();
   }
   
   /**
<<<<<<< HEAD
	 * Kills the given cashier thread and removes it from the active cashier map
	 * @param name of the cashier to end their shift
	 */
=======
    * Remove cashier thread
    * @param name Name of cashier thread to remove  
    */
>>>>>>> aac438b270bf386eb08036bfde8ed6a33b61dbe6
   private synchronized void removeCashier(String name) {
	   System.out.println("Cashier " + name + " has ended their shift");
<<<<<<< HEAD
	   cashierThreads.get(name).interrupt();  // Interrupt the thread causing it to finalise their run 
	   cashierThreads.remove(name);  // Remove the thread from the list
	   employees.removeCashier(name); // Remove active cashier from the model 
   }
   
   
=======
	   cashierList.get(name).interrupt();
	   cashierList.remove(name);
	   activeCashiers.remove(name);	   
   }  
>>>>>>> aac438b270bf386eb08036bfde8ed6a33b61dbe6
   
   // main method 
	public static void main(String[] args) {
		CoffeeShop shop = new CoffeeShop();
		
		shop.createHandler(15);
		shop.addCashier();
		shop.addCashier();
		
<<<<<<< HEAD
=======
		shop.addBarista();
		shop.addBarista();
		
		shop.addCook();
		shop.addCook();
		
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
>>>>>>> aac438b270bf386eb08036bfde8ed6a33b61dbe6
		
		//NewGUI gui = new NewGUI(shop);
		//gui.DisplayGUI();
		//gui.run();
		
	}

}
