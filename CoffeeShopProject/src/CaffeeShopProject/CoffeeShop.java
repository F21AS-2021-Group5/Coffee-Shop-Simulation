package CaffeeShopProject;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Hashtable;
import java.io.*;
import java.util.*;

public class CoffeeShop {
	
	HashMap<String, MenuItem> menu;
	
	public CoffeeShop()
	{
		menu = new HashMap<>(); 
		
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
               String[] data = inputLine.split("/");        //split by slashes
               if (data.length == 5) {
                   try {
                       MenuItem item = new MenuItem(data[0], data[1], data[2], Float.parseFloat(data[3]), data[4]); //New MenuItem
                       menu.put(data[1], item);                             // Put MenuItem in menu
                   } catch (IllegalArgumentException e){ //If add anything anything unaceptable in MenuItem
                	   throw new IllegalArgumentException();
                   }
               } else {
                   System.out.println("Invalid data line. Will drop it. \n");
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
       System.out.println(menu.get("Latte").getCategory());
   }

	public static void main(String[] args) {
		CoffeeShop item = new CoffeeShop();
		item.fillMenu("MenuItems");
		// TODO Auto-generated method stub

	}

}
