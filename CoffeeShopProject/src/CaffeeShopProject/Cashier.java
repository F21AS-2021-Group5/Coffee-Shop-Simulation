package CaffeeShopProject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*; 

public class Cashier {
	
	public float subtotal;
	public float tax;
	public float discount;
	public float total;
	public int taxable;
	public String receipt;
	//HashMap<String, ArrayList<LocalDateTime>> cart;
	//Customer customer;
	public static Customer currentCustomer;
	
	private static String line = String.format("%1$" + 55 + "s", "- \n").replace(' ', '-');
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	//private static ArrayList<Float> money;
	//private static float money[];
	private static float[] money = {0,0,0,0,0,0,0};
	int discount1 = 0;
	int discount2 = 0;
	int discount3 = 0;
	
	String ID;
	
	//CoffeeShop shop = new CoffeeShop("MenuItems"); 
	EndOfDay report = new EndOfDay();
	
	public Cashier(String ID) {
		currentCustomer = null;
		this.ID =ID;
	}
	
	public String getID() {
		return ID;
	}

   /**
	 * String containing all Customer object details 
	 * @return string for customer receipt
	 */
	public String generateReceipt()
	{
		// Date time formatter 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String datetime = currentCustomer.timestamp.format(formatter);
		String[] datetimeSplit = datetime.split(" ");
		 
		// Initial customer information
		String output = String.format("%-20s \n\n", "Customer Receipt:") + 
						String.format("%-10s %-10s\n", "Name:", currentCustomer.name) + 
						String.format("%-10s %-10s\n\n", "ID:",currentCustomer.id) +
						String.format("%-10s %-10s\n", "Date:", datetimeSplit[0]) +
						String.format("%-10s %-10s\n", "Time:",datetimeSplit[1]) +
						line +
						String.format("ORDER :\n");
		
		// Print all cart orders with quantities 
		Set<String> cartSet = currentCustomer.cart.keySet();
		for (String orderID: cartSet) {
			int quantaty = currentCustomer.cart.get(orderID).size();
			String cost = df2.format(CoffeeShop.menu.get(orderID).getCost()*quantaty) +"£";
			output += String.format("%-4s %-20s %-20s %-20s\n", " ", CoffeeShop.menu.get(orderID).getName(), 
					String.valueOf(quantaty + "x"), 
					cost);
		}
		runCashier();
		output += line +  
				 String.format("%-10s %-30s %-10s\n", " ","Sub total : ", Float.toString(subtotal)+ "£")+ 
	             String.format("%-10s %-30s %-10s\n", " ", "Taxes : ",  df2.format(tax) +"£")+ 
	             String.format("%-10s %-30s %-10s\n"," ", "Discount : ", df2.format(discount) +"£" )+ 
	             String.format("%-10s %-5s %-5s %-5s %-5s %-5s %-5s \n","Type: ","D1 : ",String.valueOf(discount1),  "D2 : ",String.valueOf(discount2), "D3 : ",String.valueOf(discount3) )+ 
	             String.format("%-10s %-30s %-10s\n", " ", "TOTAL : ", df2.format(total) +"£");
		// Update the final prices 
		receipt = output;
		return output;
	}
		
		
	/**
	 * Creates new customer 
	 * @param name of the customer
	 */
   public void createNewCustomer(String name) {
 	   LocalDateTime timeStamp = LocalDateTime.now();
 	   Customer newCustomer = new Customer(name, timeStamp);
 	   CoffeeShop.customerList.put(newCustomer.getId(), newCustomer);
 	   currentCustomer = newCustomer;
    }
  
	
	public float getCartSubtotalPrice() {
		subtotal = currentCustomer.getCartTotalPrice();
		return subtotal;
	}
	
	public float getCartTax() {
		// loop through items in cart
		// if item is taxable, add 20% of item price to tax 
		tax =  (float) (subtotal*0.25);
		/*
		taxable = 1;
		float itemTax = 0;
		tax = 0;
		while (taxable == 1) {
			for (Map.Entry mapElement : currentCustomer.cart.entrySet()) { 
				if (CoffeeShop.menu.get(mapElement).getCategory() == "drink") {
					tax = tax + CoffeeShop.menu.get(mapElement).getCost();
				}
			}
		}
		*/
		return tax;
	}
	
	public float getDiscount() {
		// discount 1 = 1 drink & 1 food & 1 pastry = £5 //
		// discount 2 = 3 drinks & 1 food = 20% off //
		// discount 3 = 3 pastries = 25% off //
		
		List<Float> food = new ArrayList<Float>();
		List<Float> drink = new ArrayList<Float>();
		List<Float> pastry = new ArrayList<Float>();
		
		
		// Go through the cart to find all the items baugth
		Set<String> cartSet = currentCustomer.cart.keySet(); 
		
		for (String orderID: cartSet) {
			
			int quantity = currentCustomer.cart.get(orderID).size();
			System.out.println(quantity);
			
			if(CoffeeShop.menu.get(orderID).getCategory().equals("Drink")) {
				for(int i =1; i<=quantity; i++) {
					drink.add(CoffeeShop.menu.get(orderID).getCost());
				}
			} else if (CoffeeShop.menu.get(orderID).getCategory().equals("Food")) {
				for(int i =1; i<=quantity; i++) {
					food.add(CoffeeShop.menu.get(orderID).getCost());
				}
				
				
			} else if (CoffeeShop.menu.get(orderID).getCategory().equals("Pastry") ) {
				for(int i =1; i<=quantity; i++) {
					pastry.add(CoffeeShop.menu.get(orderID).getCost());
				}
				
			}
		
        } 
		
		boolean noMoreDiscountsAvailable = false;
		discount1 = 0;
		discount2 = 0;
		discount3 = 0;
		
		
		
		while(!noMoreDiscountsAvailable) {
			System.out.println(currentCustomer.getId());
			System.out.println("before f " + food.size() + " d " + drink.size() + " p " +pastry.size());
			// First checks to see all the combinations
			if (drink.size() >= 1 && food.size() >= 1 && pastry.size() >= 1) {
				float conbination = drink.remove(0) +food.remove(0) +pastry.remove(0);
				discount += conbination - 5;
				//System.out.println("discoubt1  "+discount);
				discount1 =discount1 +1;
			// Then if other discounts are available
			}else if (drink.size() >=3 && food.size() >= 1) { 
				float cobination = drink.remove(0) + drink.remove(0)+ drink.remove(0) + food.remove(0);
				discount += cobination/5;
				//System.out.println("discoubt2  "+discount);
				discount2 =discount2 +1;
			}else if (pastry.size() >=3 ) {
				float cobination = pastry.remove(0) +pastry.remove(0)+pastry.remove(0);
				discount += cobination/4;
				//System.out.println("discoubt3  "+discount);
				discount3 =discount3 +1;
			}else {
			// No more discounts
				
				noMoreDiscountsAvailable = true;
			}
			
			System.out.println("afther f " + food.size() + " d " + drink.size() + " p " +pastry.size());
			
			
		}
		
		return discount;
	}
	
	// For values that are initialised at the initialisation of coffeeShop
	public void setCustomer(Customer customer) {
		System.out.println("here");
		currentCustomer = customer;
	}
	
	public float[] returnSums() {
		float[] allValues = {subtotal, tax,  discount, total, discount1, discount2,discount3};
		System.out.println(allValues[0]);
		return allValues;
	}
	
	
	public float getCartTotalPrice() {
		total = subtotal + tax - discount;
		return total;
	}
	
	public void runCashier() {
		subtotal = 0;
		tax = 0;
		discount= 0;
		total = 0;
		getCartSubtotalPrice();
		getCartTax();
		getDiscount();
		getCartTotalPrice();
		report.upDateFinalSum(returnSums());
	}
	
	public String generateFinalReport() {
		return report.generateFinalReportDisplay();
	}
	
	public void generateCustomerReport() {
		   try {
			   File customerReport =new File("Receit "+currentCustomer.getId()+".txt");
			   FileWriter customerWriter = new FileWriter("Receit "+currentCustomer.getId()+".txt");
			   customerWriter.write(receipt);
			   customerWriter.close();
		   } catch (IOException e) {
			   System.out.println("An error occurred.");
			   e.printStackTrace();
		   }
	}
	
	public void generateFinalReportFile() {
		report.generateFinalReport(); 
	}
	
	
	

}
