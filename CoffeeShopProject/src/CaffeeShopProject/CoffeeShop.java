package CaffeeShopProject;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors

public class CoffeeShop {
	
	public static HashMap<String, MenuItem> menu;
	public static HashMap<String, Customer> customerList;
	public static Customer currentCustomer;
	private static File finalReport;
	private static HashMap<String,Integer> inventory;
	private static double[] prices = {0.00, 0.00,0.00,0.00};
	private static String line = String.format("%1$" + 55 + "s", "- \n").replace(' ', '-');
	
	Cashier cashier = new Cashier();

	public CoffeeShop(String filename)
	{
		menu = new HashMap<>(); 
		customerList = new HashMap<>();
		finalReport =new File("finalReport.txt");
		inventory = new HashMap<>();
		currentCustomer = null;
		fillMenu(filename);
		fillCustomerList("CustomerList");
	}
	
	double sub = 0.00;
	double tax = 0.00;
	double dis = 0.00;
	double total = 0.00;
	
	double[] money = {0.00,0.00,0.00,0.00};
	
	
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
                	   if(!customerList.containsKey(data[0])) {
                		   Customer newCustomer = new Customer(data[1], data[0], timeStamp);
                		   customerList.put(data[0].trim(), newCustomer);
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
   
   public void GenerateCustomerReport(String curentCustomerID) {
	   try {
		   File customerReport =new File("customerReport"+curentCustomerID+".txt");
		   FileWriter customerWriter = new FileWriter("customerReport"+curentCustomerID+".txt");
		   customerWriter.write(customerList.get(curentCustomerID).receipt());
		   customerWriter.close();
	   } catch (IOException e) {
		   System.out.println("An error occurred.");
		   e.printStackTrace();
	   }
   }
   
   public void GenerateFinalReport(String report) {
	   calculateInventory();
	   try {
		   FileWriter finalWriter = new FileWriter("finalReport.txt");
		   finalWriter.write(report);
		   finalWriter.close();
	   } catch (IOException e) {
		   System.out.println("An error occurred.");
		   e.printStackTrace();
	   }
   }
   
   public String GenerateReceitDisplay() {
	    float[] values = returnAllValuesOfCustomer(currentCustomer);
	    for (int i=0; i<money.length; i++) 
	    { 
	        money[i] += values[i];
	    }
	   
	   String receit = "";
	   return receit;
   }
   
   public String GenerateFinalReportDisplay() {
	   calculateInventory();
	   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	   String datetime = LocalDateTime.now().format(formatter);
	   String[] datetimeSplit = datetime.split(" ");
	   
	   cashier.getDiscount();
	   
	   String output = String.format("%-20s \n\n", "End of the day report") + 
					   String.format("%-10s %-10s\n", "Date:", datetimeSplit[0]) +
					   String.format("%-10s %-10s\n", "Time:",datetimeSplit[1]) +
					   line;
	   
	   String drink = ""; String food = "" ; String pastry = ""; 
	   
	   for (Map.Entry m:inventory.entrySet()) { 
		   String category = menu.get(m.getKey()).getCategory();
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
	   
	   output += String.format("%-20s \n\n", "FOOD") + food+ line +
			   String.format("%-20s \n\n", "DRINKS") + drink+ line +
			   String.format("%-20s \n\n", "PASTRIES") + pastry + line +"\n" ;
	   Double.toString(money[0]);
	   output += String.format("%-30s %-10s\n", "Number of customers: ", customerList.size())+ 
	             String.format("%-40s %-10s\n", "Sub total of day : ", Double.toString(money[0])+ "£")+ 
	             String.format("%-40s %-10s\n", "Taxes : ", Double.toString(money[1])+ "£")+ 
	             String.format("%-40s %-10s\n", "Discount : ", String.valueOf("-"+ money[2] + "£"))+ 
	             String.format("%-40s %-10s\n", "Total of the day : ", String.valueOf("="+money[3] + "£"));
	   return output;
   }
   
   private float[] returnAllValuesOfCustomer(Customer customer) {
	   cashier.setCustomer(customer);
	   float[] allValues = {cashier.getCartSubtotalPrice(), cashier.getCartTax(), cashier.getDiscount(), cashier.getCartTotalPrice()};
	   return allValues;
   }
      
   private void calculateInventory() {
	   // Go though customer list
       for (Map.Entry m:customerList.entrySet()) { 
           Customer cus = (Customer) m.getValue();  // Get Customer object 
           float[] allValues = returnAllValuesOfCustomer(cus);
         
           // Go through all orders of a customer  
   			Set<String> cartSet = cus.getCart().keySet();
   			for (String orderID: cartSet) {
   				if (!inventory.containsKey(orderID)) { // If item dosen't exist in inventory add it and quantity
   					inventory.put(orderID, cus.getCart().get(orderID).size());
   				}
   				else { // If the item does exist update its quantity
   					inventory.put(orderID, inventory.get(orderID) + cus.getCart().get(orderID).size());
   				}
   			}
       }        
       
   }
   
   
   public void createNewCustomer(String name) {
	   LocalDateTime timeStamp = LocalDateTime.now();
 	   Customer newCustomer = new Customer(name, timeStamp);
 	   currentCustomer = newCustomer;
   }

   
	public static void main(String[] args) {
		CoffeeShop item = new CoffeeShop("MenuItems");
		
		item.fillCustomerList("CustomerList");
		GUIcaffee GUI = new GUIcaffee();
		GUI.initializeGUI(); 
		GUI.paintScreen();
		GUI.setUp();
	}

}
