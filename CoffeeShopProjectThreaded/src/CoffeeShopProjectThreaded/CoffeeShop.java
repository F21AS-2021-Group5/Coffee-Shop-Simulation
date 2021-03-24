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

//import CaffeeShopProject.CoffeeShop;
import CoffeeShopProjectThreaded.GUIcaffee;

public class CoffeeShop {
	
	public static HashMap<String, MenuItem> menu;
	public static HashMap<String, Customer> customerList;
	public static ArrayList<Float> money;	
	public static HashMap<String, ArrayList<String> > recipeBook; // stores recipes
	
	Cashier cashier;
	
	/**
	 * Constructor for CoffeeShop class
	 * @param name Name of cashier 
	 */
	public CoffeeShop(String name)
	{
		menu = new HashMap<>(); 
		customerList = new HashMap<>();
		cashier = null;
		money =  new ArrayList<Float>();
		
		// add initial money to cashier 
		for (int i = 0 ; i <= 6; i++) {
			money.add((float) 0);
		}
		
		// creates cashier and fills menu and customer list data structures 
		//createNewCashier(name);
		fillMenu("MenuItems");
		//fillCustomerList("CustomerList");	
				
		// fill recipe book 
		recipeBook = new HashMap<String, ArrayList<String> >();
		fillRecipeBook("RecipeBook");
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
	 * Fills recipe book 
	 * @param file Text file name 
	 */
	void fillRecipeBook(String file) {
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
	       			itemId = data[0];     			
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
   
   /**
    * Fills the customer list with information from text file 
    * @param fileName File (.txt) name with initial orders 
    */
   private void fillCustomerList(String fileName){
	   BufferedReader br = null; //reader
       try {
    	   FileReader file = new FileReader(fileName);   // Read file
    	   br = new BufferedReader(file);       // Create buffer reader for file
    	   String inputLine = br.readLine();   // Read first line
           while (inputLine != null) {                             //while its contains stuff
               String[] data = inputLine.split(";");        //split by semicolon
               if (data.length == 4) {
                   try {
                	   LocalDateTime timeStamp = stringToTimestamp(data[3]);
                	   // initialize customer list with new customer 
                	   if (customerList.isEmpty()) {
                		   Customer newCustomer = new Customer(data[1], data[0], timeStamp);
                		   customerList.put(data[0].trim(), newCustomer);
                		   cashier.setCustomer(newCustomer); 
                	   }
                	   // if customer list does not contain customer in file row, add it 
                	   if(!customerList.containsKey(data[0])) {
                		   cashier.runCashier();
                		   Customer newCustomer = new Customer(data[1], data[0], timeStamp);
                		   customerList.put(data[0].trim(), newCustomer);
                		   cashier.setCustomer(newCustomer); 
                	   }
                	   customerList.get(data[0].trim()).addItem(data[2], 1, timeStamp);
                	// catch all exceptions 
                   } catch (IllegalArgumentException e){ //If add anything anything unacceptable in MenuItem
                	   throw new IllegalArgumentException();
                   } catch (IllegalStateException e) {
                	   throw new IllegalStateException();
                   } catch (InvalidMenuItemQuantityException e) {
                	   e.printStackTrace();
                   } catch (InvalidMenuItemDataException e) {
                	   e.printStackTrace();
                   } 
               } else {
                   System.out.println("Invalid data line. Will drop it. \n");
               }
               inputLine = br.readLine();                          //read the next line
           }
           cashier.runCashier();
       // catch all possible exceptions
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
   
   
   // main method 
	public static void main(String[] args) {
		// Needs initial cashier 
		CoffeeShop shop = new CoffeeShop("Adam");
		long time = 200L;
		long time1 = 400L;
		long time2 = 600L;
		
		/*custQue.randCustomerToQueue();
		custQue.randCustomerToQueue();
		custQue.randCustomerToQueue();
		System.out.println(custQue.shopQueue.pop().cart);
		System.out.println(custQue.shopQueue.pop().cart);
		System.out.println(custQue.shopQueue.pop().cart);
		*/
		/*
		Deque<Customer> customerLine = new LinkedList<Customer>();
		
		Runnable line = new  CustomerQueue(time, customerLine);
		Thread newCustomers = new Thread(line);
		//newCustomers.lo
		newCustomers.start();
		
		Runnable cashierOne = new Cashier("Adam", time1, customerLine); // or an anonymous class, or lambda...
		Thread t1 = new Thread(cashierOne);
		t1.start();
		
		Runnable cashierTwo = new Cashier("Barbara", time2, customerLine);; // or an anonymous class, or lambda...
		Thread t2 = new Thread(cashierTwo);
		t2.start();

		Runnable cashierThree = new Cashier("Mindy", time2, customerLine);; // or an anonymous class, or lambda...
		Thread t3 = new Thread(cashierThree);
		t3.start();*/
		
		/*
		NewCustomerQueue queue = new NewCustomerQueue(false);
		
		Runnable handler = new QueueHandler(queue, 200L);
		Thread h = new Thread(handler);
		h.start();		
		
		Runnable cashierOne = new CashierTrial("Adam", 200L, queue); // or an anonymous class, or lambda...
		Thread t1 = new Thread(cashierOne);
		t1.start();
		
		Runnable cashierTwo = new CashierTrial("Barbara", 400L, queue);; // or an anonymous class, or lambda...
		Thread t2 = new Thread(cashierTwo);
		t2.start();

		Runnable cashierThree = new CashierTrial("Mindy", 600L, queue);; // or an anonymous class, or lambda...
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
		//OrderQueue kitchenQueue = new OrderQueue(false);
		OrderQueue barQueue = new OrderQueue(true);
		
		//kitchenQueue.addToQueue("Marco", "FOOD001");
		barQueue.addToQueue("Matteo", "DRINK003");
		barQueue.addToQueue("Alessandro", "PASTRY001");
		barQueue.addToQueue("Francesca", "DRINK005");
		
		Runnable baristaOne = new Staff("Paolo", barQueue, 2000L);
		Thread s1 = new Thread(baristaOne);
		s1.start();
		
		Runnable baristaTwo = new Staff("Lavinia", barQueue, 1500L);
		Thread s2 = new Thread(baristaTwo);
		s2.start();
		*/
		
		//Runnable cookOne = new Staff("Giulia", kitchenQueue, 2000L);
		//Thread s3 = new Thread(cookOne);
		//s3.start();
		
		//Runnable cookTwo = new Staff("Francesco", kitchenQueue, 2000L);
		//Thread s4 = new Thread(cookTwo);
		//s4.start();

		
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
