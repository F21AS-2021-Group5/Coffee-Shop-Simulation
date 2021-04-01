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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;


public class CoffeeShop {
	
	// Written data files 
	public static HashMap<String, MenuItem> menu;                 // Stores menu items 
	public static HashMap<String, ArrayList<String> > recipeBook; // Stores recipes
	public static Map<String, HashSet<String> > foodMap;          // Map
	
	static GUI gui;
	public static String report;
	
	private long addToQueueDelay;	

	// Data MODELS 
	CustomerQueue queue;
	
	// customer queues 
	public static CustomerQueue shopQueue;
	public static CustomerQueue onlineQueue;
	
	// item inventory and books (revenue)
	public static Inventory inventory;
	public static Bookkeeping books;
	
	// food orders queues 
	public static OrderQueue kitchenQueue;
	public static OrderQueue barQueue;
	
	public static Employees employees; // staff member storage 
	
	public static boolean isFinished = false; 
	
	private static String line = String.format("%1$" + 55 + "s", "- \n").replace(' ', '-');
	private static DecimalFormat df2 = new DecimalFormat("#.##");	
	
	// hash maps mapping staff names to running threads
	public static HashMap<String, Thread> cashierThreads;
	public static HashMap<String, Thread> baristaThreads;
	public static HashMap<String, Thread> cookThreads;
	
	// queue handler thread 
	public static Thread handlerThread;	

	// possible staff member names 
	String[] cashierNames={"Ron","Leslie","April", "Donna","Andy","Ann","Ben", 
			"Tom", "Jerry", "Gerry", "Lerry"};   // -
	String[] baristaNames={"Mac","Charlie", "Frank", "Deandra","Dennis"};   
	String[] cookNames={"Dwight","Pam", "Jim", "Andy","Kelly", "Angela"};    
	
	// hashmap mapping customer name to customer object 
	public static HashMap<String, Customer> customerList;  
	
	// logs all information from all classes 
	private static Log log;

	/**
	 * Constructor for CoffeeShop class
	 * @param addToQueueDelay Delay for queue handler 
	 */
	public CoffeeShop(long addToQueueDelay)
	{
		this.addToQueueDelay = addToQueueDelay;

		customerList = new HashMap<>();
		
		log = Log.getInstance();		
		

		// Fills the menu with all the items 
		menu = new HashMap<>(); 
		fillMenu("MenuItems");  

		// Model, all the data used by different components
		inventory = new Inventory();                // Inventory of ordered items

		shopQueue = new CustomerQueue(false, addToQueueDelay, 10);   // In-shop Customer queue
		onlineQueue = new CustomerQueue(true, addToQueueDelay, 10);  // Online Customer queue
		inventory = new Inventory();                  // Inventory of ordered items
		books = new Bookkeeping();                  // Bookkeeping of earnings and other finances
		employees = new Employees();                // Currently working employees 
		
		kitchenQueue = new OrderQueue(false);       // Kitchen queue
		barQueue = new OrderQueue(true);            // Bar queue
		
		// Sub controllers, threads 
		cashierThreads = new HashMap<String, Thread>();
		baristaThreads = new HashMap<String, Thread>();
		cookThreads = new HashMap<String, Thread>();
						
		// Fill recipe book 
		recipeBook = new HashMap<String, ArrayList<String> >();
		fillRecipeBook("RecipeBook");	
		
		// Fill food map 
		foodMap = new HashMap<String, HashSet<String> >();
		fillFoodMap(); 
		
	}
	
	/**
	 * Sets the isFinished variable to true 
	 */
	public static void setIsFinished() {
		isFinished = true;
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
			if (onlyChars.equals("FOOD") || onlyChars.equals("SIDE"))   // If it is FOOD or SIDE 
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
	 * Adds new cashier and creates a thread for it  
	 */
   public static Cashier addCashier(Long delay) {
	   // create cashier object 
	   Cashier cash = employees.addCashier(delay); 
	   cash.addPropertyChangeListener(gui);  // Establish OBSERVER-OBSERVABLE channel 
	   
	   // Create runnable for cashier object
	   Runnable cashier = new CashierRunnable(cash.getName(), onlineQueue, shopQueue, kitchenQueue, barQueue, inventory, books, cash); // or an anonymous class, or lambda...
	   Thread t = new Thread(cashier);
	   t.setPriority(2);                      // Sets priority
	   cashierThreads.put(cash.getName(), t);   // Holds active cashier threads
	   
	   // start thread 
	   t.start(); 
	   
	   return cash;
   }
   /**
	 * Creates customer queue handler which adds customers to the queue  
	 * @param integer for the amount of customers we wish to accept to the store
	 */
   public static void createHandler() {
	    // Create handler which adds to the customer queue
	    Runnable handler = new QueueHandler(onlineQueue, shopQueue, 1000L);
	    handlerThread = new Thread(handler);
	    
	    // High priority since customers are the once who are deciding when they will arrive
	    handlerThread.setPriority(8);                
	    handlerThread.start(); // start thread
   }
   
   /**
	 * Adds new barista and creates a thread for it  
	 */
   public static FoodStaff addBarista(Long delay) {
	   FoodStaff barStaff = employees.addBarista(delay); // Add barista to the employees model
	   barStaff.addPropertyChangeListener(gui); // Establish OBSERVER-OBSERVABLE channel 
	    
	   Runnable barista = new FoodStaffRunnable(barStaff, barQueue);
	   Thread s = new Thread(barista);
	   baristaThreads.put(barStaff.getName(), s);
	   s.start(); // start thread 
	   
	   return barStaff;
   }
   
   /**
	 * Adds new cook and creates a thread for it  
	 */
   public static FoodStaff addCook(Long delay) {
	   FoodStaff kitchenStaff = employees.addCook(delay); // Add cashier to the employees model
	   kitchenStaff.addPropertyChangeListener(gui); // Establish OBSERVER-OBSERVABLE channel 
	   	   	   
	   // Creates cook object which processes orders related to Food and Sides
	   Runnable cook = new FoodStaffRunnable(kitchenStaff, kitchenQueue);
	   Thread s2 = new Thread(cook);
	   cookThreads.put(kitchenStaff.getName(), s2);
	   s2.start(); // start thread 	 
	   
	   return kitchenStaff;
   }
	
   /**
	 * Kills the given cashier thread and removes it from the active cashier map
	 * @param name of the cashier to end their shift
	 */
   public static synchronized void removeCashier(String name) {
	   log.updateLog("[MAIN] Cashier " + name + " has ended their shift");
	   cashierThreads.get(name).interrupt();  // Interrupt the thread causing it to finalise their run 
	   cashierThreads.remove(name);  // Remove the thread from the list
	   employees.removeCashier(name); // Remove active cashier from the model 
   }
   
   
   /**
	 * Kills the given cook thread and removes it from the active cook map
	 * @param name of the cook to end their shift
	 */
   public static synchronized void removeCook(String name) {
	   log.updateLog("[MAIN] Cook " + name + " has ended their shift");
	   cookThreads.get(name).interrupt();  // Interrupt the thread causing it to finalise their run 
	   cookThreads.remove(name);  // Remove the thread from the list
	   employees.removeCook(name); // Remove active cashier from the model 
   }
   
   /**
  	 * Kills the given barista thread and removes it from the active barista map
  	 * @param name of the barista to end their shift
  	 */
   public static synchronized void removeBarista(String name) {
	   log.updateLog("[MAIN] Barista" + name + " has ended their shift");
	   baristaThreads.get(name).interrupt();  // Interrupt the thread causing it to finalise their run 
	   baristaThreads.remove(name);  // Remove the thread from the list
	   employees.removeBarista(name); // Remove active cashier from the model 
   }   
   
   /**
    * Generates end of day report 
    */
   public static void generateEndOfDayReport() {
	   Hashtable<String,Integer> in = inventory.getInventory();
	   // Format the date time string
	   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	   String datetime = LocalDateTime.now().format(formatter);
	   String[] datetimeSplit = datetime.split(" ");
	   
	   // Initialise end of day report string 
	   String output = String.format("%-20s \n\n", "End of the day report") + 
					   String.format("%-10s %-10s\n", "Date:", datetimeSplit[0]) +
					   String.format("%-10s %-10s\n", "Time:",datetimeSplit[1]) +
					   line;
	   
	   // Strings to hold category informations
	   String drink = ""; String food = "" ; String pastry = ""; 
	   // Goes trough inventory
	   for (Map.Entry m: in.entrySet()) { 
		   String category = CoffeeShop.menu.get(m.getKey()).getCategory();
		   if (category.equals("Drink")) {
			  drink += String.format("%-5s %-25s %-10s \n", " ",m.getKey(), 
						String.valueOf(m.getValue() + "x"));
	      }if (category.equals("Food")) {
	    	  food += String.format("%-5s %-25s %-10s \n", " ",m.getKey(), 
						String.valueOf(m.getValue() + "x"));
		  }if (category.equals("Pastry")) {
			  pastry += String.format("%-5s %-25s %-10s \n", " ",m.getKey(), 
						String.valueOf(m.getValue() + "x"));
		  }
       }
	   
	   // creates final report by concatenating strings 
	   output += String.format("%-20s \n\n", "FOOD") + food + line +
			     String.format("%-20s \n\n", "DRINKS") + drink + line +
			     String.format("%-20s \n\n", "PASTRIES") + pastry + line + "\n" ;
	   
	   
	   output += String.format("%-30s %-10s\n", "Number of customers: ", books.getAllValues().get(7)) + 
	             String.format("%-40s %-10s\n", "Sub total of day : ", df2.format(books.getAllValues().get(0)) + "£") + 
	             String.format("%-40s %-10s\n", "Taxes : ", df2.format(books.getAllValues().get(1)) + "£")+ 
	             String.format("%-40s %-10s\n", "Discount : ", df2.format(books.getAllValues().get(2)) + "£")+ 
	             String.format("%-50s %-10s\n", "Type D1 : ", df2.format(books.getAllValues().get(4)) ) + 
	             String.format("%-50s %-10s\n", "Type D2 : ", df2.format(books.getAllValues().get(5)) ) + 
	             String.format("%-50s %-10s\n", "Type D3 : ", df2.format(books.getAllValues().get(6)) ) + 
	             String.format("%-40s %-10s\n", "Total of the day : ", df2.format(books.getAllValues().get(3)) + "£");
	   
	   report = output;
   }
   
   /**
    * Writes end of day report to text file 
    */
   public static void generateFinalReport() {
	   try {
		   // creates a text file and writes the report to it 
		   generateEndOfDayReport();
		   FileWriter finalWriter = new FileWriter("FinalReport.txt");
		   finalWriter.write(report);
		   finalWriter.close();
		   
	   } catch (IOException e) {
		   System.out.println("An error occurred.");
		   e.printStackTrace();
	   }
	   
	   try {
		   // creates a text file and writes the report to it 
		   System.out.println(log.getLog());
		   FileWriter finalW = new FileWriter("Log.txt");
		   finalW.write(log.getLog());
		   finalW.close();
		   
	   } catch (IOException e) {
		   System.out.println("An error occurred.");
		   e.printStackTrace();
	   }	   
  }
   
   // main method 
	public static void main(String[] args) {
		CoffeeShop shop = new CoffeeShop(1000L);
		
		gui = new GUI();
		CoffeeShop.shopQueue.addPropertyChangeListener(gui);
		CoffeeShop.onlineQueue.addPropertyChangeListener(gui);
		CoffeeShop.employees.addPropertyChangeListener(gui);
	}
}
