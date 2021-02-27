package CaffeeShopProject;

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
	

	public CoffeeShop(String name)
	{
		menu = new HashMap<>(); 
		customerList = new HashMap<>();
		cashier = null;
		money =  new ArrayList<Float>();
		for (int i =0 ;i <=6; i++) {
			money.add((float) 0);
		}
		
		createNewCashier(name);
		fillMenu("MenuItems");
		fillCustomerList("CustomerList");	
	}
	
	private void createNewCashier(String id) {
		Cashier newCashier = new Cashier(id);
		cashier = newCashier;
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
               String[] data = inputLine.split(";");        //split by slashes
               if (data.length == 5) {
                   try {
                       MenuItem item = new MenuItem(data[0], data[1], data[2], Float.parseFloat(data[3]), data[4]); //New MenuItem
                       menu.put(data[1].trim(), item);                             // Put MenuItem in menu
                   } catch (IllegalArgumentException e){ //If add anything anything unaceptable in MenuItem
                	   throw new IllegalArgumentException();
                   }
               } else {
                  // System.out.println("Invalid data line. Will drop it. \n");
               }
               inputLine = br.readLine();                          //read the next line
           }
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
       //System.out.println(menu.get("Latte").getCategory());
   }
   
   private void fillCustomerList(String fileName){
	   BufferedReader br = null; //reader
       try {
    	   FileReader file = new FileReader(fileName);   // Read file
    	   br = new BufferedReader(file);       // Create buffer reader for file
    	   String inputLine = br.readLine();   // Read first line
           while (inputLine != null) {                             //while its contains stuff
               String[] data = inputLine.split(";");        //split by slashes
               if (data.length == 4) {
                   try {
                	   LocalDateTime timeStamp = stingToTimestam(data[3]);
                	   if (customerList.isEmpty()) {
                		   Customer newCustomer = new Customer(data[1], data[0], timeStamp);
                		   customerList.put(data[0].trim(), newCustomer);
                		   cashier.setCustomer(newCustomer); 
                	   }
                	   if(!customerList.containsKey(data[0])) {
                		   cashier.runCashier();
                		   Customer newCustomer = new Customer(data[1], data[0], timeStamp);
                		   customerList.put(data[0].trim(), newCustomer);
                		   cashier.setCustomer(newCustomer); 
                	   }
                	   customerList.get(data[0].trim()).addItem(data[2], 1, timeStamp);
                   } catch (IllegalArgumentException e){ //If add anything anything unaceptable in MenuItem
                	   throw new IllegalArgumentException();
                   } catch (IllegalStateException e) {
                	   throw new IllegalStateException();
                   } catch (InvalidMenuItemQuantityException e) {
					e.printStackTrace();
                   } catch (InvalidMenuItemDataException e) {
					e.printStackTrace();
                   } 
               } else {
                   //System.out.println("Invalid data line. Will drop it. \n");
               }
               inputLine = br.readLine();                          //read the next line
           }
           System.out.println("loop");
           cashier.runCashier();
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
   
   
   private LocalDateTime stingToTimestam(String timeString){
	   LocalDateTime localDateTime = null;
	   try {
		   String pattern = "yyyy-MM-dd HH:mm:ss.SSSSSSSSS";
		   DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		   localDateTime = LocalDateTime.from(formatter.parse(timeString));
		} catch(Exception e) { //this generic but you can control another types of exception
		    // look the origin of excption 
			System.out.println("Could't conver sting to time stamp");
		}
	    return localDateTime; // Fine to return 0 since catch in the Customer class 
   }
   
   
   
   
   
	public static void main(String[] args) {
		// Needs initial cashier 
		CoffeeShop shop = new CoffeeShop("Adam");
		
		// Each cashier has their own GUI 
		GUIcaffee GUI = new GUIcaffee(shop.cashier);
		GUI.initializeGUI(); 
		GUI.paintScreen();
	}

}
