/**
 * EndOfDay.java - class to implement the end of day report generation 
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

package CaffeeShopProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EndOfDay {
	
	private static String line = String.format("%1$" + 55 + "s", "- \n").replace(' ', '-');
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	public static String report;
	private static File finalReport;
	
	private static HashMap<String,Integer> inventory;
	
	/**
	 * Constructor for EndOfDay class
	 */
	public EndOfDay() {
		inventory = new HashMap<>();
		finalReport =new File("FinalReport.txt");
	}
	
	/**
	 * Generates a string for the final report 
	 * @return string for final report  
	 */   
   public String generateFinalReportDisplay() {
	   calculateInventory();   // Calculate inventory 
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
	   for (Map.Entry m: inventory.entrySet()) { 
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
	   
	   output += String.format("%-30s %-10s\n", "Number of customers: ", CoffeeShop.customerList.size()) + 
	             String.format("%-40s %-10s\n", "Sub total of day : ", df2.format(CoffeeShop.money.get(0)) + "£") + 
	             String.format("%-40s %-10s\n", "Taxes : ", df2.format(CoffeeShop.money.get(1)) + "£")+ 
	             String.format("%-40s %-10s\n", "Discount : ", df2.format(CoffeeShop.money.get(2)) + "£")+ 
	             String.format("%-50s %-10s\n", "Type D1 : ", df2.format(CoffeeShop.money.get(4)) ) + 
	             String.format("%-50s %-10s\n", "Type D2 : ", df2.format(CoffeeShop.money.get(5)) ) + 
	             String.format("%-50s %-10s\n", "Type D3 : ", df2.format(CoffeeShop.money.get(6)) ) + 
	             String.format("%-40s %-10s\n", "Total of the day : ", df2.format(CoffeeShop.money.get(3)) + "£");
	   
	   report = output;
	  
	   return output;
   }
   
   /**
	 * Calculates inventory for the final report 
	 */   
  private void calculateInventory() {
	  inventory.clear();
	   // Go though customer list
      for (Map.Entry m: CoffeeShop.customerList.entrySet()) { 
          Customer cus = (Customer) m.getValue();  // Get Customer object 
        
          // Go through all orders of a customer  
  			Set<String> cartSet = cus.getCart().keySet();
  			System.out.println(cartSet);
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
  
  /**
   * Updates coffee shop's total money 
   * @param money Array storing money to add 
   */
  public void updateFinalSum(float[] money) {
	  // for all elements in float array, add to Coffeeshop money ArrayList 
	   for (int i = 0; i <= CoffeeShop.money.size()-1; i++) {
		   float newValue = CoffeeShop.money.get(i) + money[i];
		   CoffeeShop.money.set(i, newValue) ;
	   }
  }
  
  /**
   * Generates final report text file 
   */
  public void generateFinalReport() {
	   try {
		   // creates a text file and writes the report to it 
		   FileWriter finalWriter = new FileWriter("FinalReport.txt");
		   finalWriter.write(report);
		   finalWriter.close();
	   } catch (IOException e) {
		   System.out.println("An error occurred.");
		   e.printStackTrace();
	   }
  }
	

}
