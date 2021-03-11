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
import java.io.*;
import java.security.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors

public class CoffeeShop {
	
	public static HashMap<String, MenuItem> menu;
	public static HashMap<String, Customer> customerList;
	public static ArrayList<Float> money;
	
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
		
		Thread addCustomerThread = new Thread(new CustomerQueue("CustomerList", "CustomerListOnline", 1500));
		addCustomerThread.start();
	}
	
	/**
	 * Creates a new Cashier instance 
	 * @param id Cashier identifier 
	 */
	private void createNewCashier(String id) {
		cashier = new Cashier(id);
	}
	
	/**
	 * Read menu data and store it in menu map 
	 * @param fileName String
	 */
   private void fillMenu(String fileName) {
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
		
		// Each cashier has their own GUI 
		//GUIcaffee GUI = new GUIcaffee(shop.cashier);
		//GUI.initializeGUI(); 
		//GUI.paintScreen();
	}

}
