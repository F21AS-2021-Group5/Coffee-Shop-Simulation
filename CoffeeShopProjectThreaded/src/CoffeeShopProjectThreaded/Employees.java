package CoffeeShopProjectThreaded;

import java.util.HashMap;
import java.util.Random;

public class Employees {
	
	String[] cashierNames={"Ron","Leslie", "April", "Donna","Andy","Ann","Ben", 
			"Tom", "Jerry", "Gerry", "Lerry"};  
	String[] baristaNames={"Mac","Cahrlie", "Frank", "Deandra","Dennis"};  
	String[] cookNames={"Dwight","Pam", "Jim", "Andy","Kelly", "Angela"};
	public static HashMap<String, Cashier> acctiveCashiers;
	
	
	public Employees() {
		acctiveCashiers = new HashMap<String, Cashier>();
	}
	
	public Cashier addCashier() {
		String name = getRandomName(cashierNames, "casshier");
		Cashier cashier = new Cashier(name);
		acctiveCashiers.put(name, cashier);
		return cashier;
	}
	
	public void removeCashier(String name) {
		acctiveCashiers.remove(name);
	}
	
	private String getRandomName(String[] list, String staffType) {
		   Random rn = new Random();
		   String name = list[rn.nextInt(list.length)]; // Given the length of name list
		   if(staffType.equals("cashier")) {
			   while(acctiveCashiers.containsKey(name)) {
				   name = list[rn.nextInt(list.length)];
			   }
		   }else if(staffType.equals("barista")) {
			   
		   }else {
			   
		   }
		   return name;
     }
	

}
