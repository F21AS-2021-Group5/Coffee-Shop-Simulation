package CoffeeShopProjectThreaded;

import java.util.HashMap;
import java.util.Random;

public class Employees {
	
	private final String[] cashierNames={"Ron","Leslie", "April", "Donna","Andy","Ann","Ben", 
			"Tom", "Jerry", "Gerry", "Lerry"};  
	private final String[] baristaNames={"Mac","Cahrlie", "Frank", "Deandra","Dennis"};  
	private final String[] cookNames={"Dwight","Pam", "Jim", "Andy","Kelly", "Angela"};
	
	private HashMap<String, Cashier> activeCashiers;
	private HashMap<String, FoodStaff> activeBaristas;
	private HashMap<String, FoodStaff> activeCooks;
	
	/**
	 * Constructor for Employees class 
	 */
	public Employees() {
		activeCashiers = new HashMap<String, Cashier>();
		activeBaristas = new HashMap<String, FoodStaff>();
		activeCooks = new HashMap<String, FoodStaff>();
	}
	
	/**
	 * @return Currently working cashiers 
	 */
	public HashMap<String, Cashier> getActiveCashiers() {
		return activeCashiers;
	}
	
	/**
	 * Set the list of active cashiers
	 * @param activeCashiers Currently working cashiers
	 */
	public void setActiveCashiers(HashMap<String, Cashier> activeCashiers) {
		this.activeCashiers = activeCashiers;
	}
	
	/**
	 * @return Currently working baristas 
	 */
	public HashMap<String, FoodStaff> getActiveBaristas() {
		return activeBaristas;
	}

	/**
	 * Set the list of active baristas
	 * @param activeCashiers Currently working baristas
	 */
	public void setActiveBaristas(HashMap<String, FoodStaff> activeBaristas) {
		this.activeBaristas = activeBaristas;
	}

	/**
	 * @return Currently working cooks 
	 */
	public HashMap<String, FoodStaff> getActiveCooks() {
		return activeCooks;
	}

	/**
	 * Set the list of active cooks
	 * @param activeCashiers Currently working cooks
	 */
	public void setActiveCooks(HashMap<String, FoodStaff> activeCooks) {
		this.activeCooks = activeCooks;
	}

	/**
	 * @return Cashier names 
	 */
	public String[] getCashierNames() {
		return cashierNames;
	}
	
	/**
	 * @return Barista names 
	 */
	public String[] getBaristaNames() {
		return baristaNames;
	}
	
	/**
	 * @return Cook names 
	 */
	public String[] getCookNames() {
		return cookNames;
	}

	/**
	 * Adds cashier to cashier list 
	 * @return Cashier added
	 */
	public Cashier addCashier() {
		String name = getRandomName(cashierNames, "cashier");
		Cashier cashier = new Cashier(name);
		activeCashiers.put(name, cashier);
		return cashier;
	}
	
	/**
	 * Removes cashier from list 
	 * @param name Cashier name 
	 */
	public void removeCashier(String name) {
		activeCashiers.remove(name);
	}
	
	/**
	 * Adds barista to barista list 
	 * @return Barista added
	 */
	public FoodStaff addBarista() {
		String name = getRandomName(baristaNames, "barista");
		FoodStaff barista = new FoodStaff(name, true);
		activeBaristas.put(name, barista);
		return barista;
	}
	
	/**
	 * Removes barista from list 
	 * @param name Barista name 
	 */
	public void removeBarista(String name) {
		activeBaristas.remove(name);
	}
	
	/**
	 * Adds cook to cook list 
	 * @return Cook added
	 */
	public FoodStaff addCook() {
		String name = getRandomName(cookNames, "cook");
		FoodStaff cook = new FoodStaff(name, false);
		activeCooks.put(name, cook);
		return cook;
	}
	
	/**
	 * Removes cook from list 
	 * @param name Cook name 
	 */
	public void removeCook(String name) {
		activeCooks.remove(name);
	}
	
	/**
	 * Picks random names from correct list 
	 * @param list List of names 
	 * @param staffType Type of staff
	 * @return Random name
	 */
	private String getRandomName(String[] list, String staffType) {
		   Random rn = new Random();
		   String name = list[rn.nextInt(list.length)]; // Given the length of name list
		   if (staffType.equals("cashier")) {			   
			   while(activeCashiers.containsKey(name)) {
				   name = list[rn.nextInt(list.length)];
			   }
		   } 		   
		   else if(staffType.equals("barista")) {			   
			   while(activeBaristas.containsKey(name)) {
				   name = list[rn.nextInt(list.length)];
			   }
		   } 		   
		   else if (staffType.equals("cook")) {			   
			   while(activeCooks.containsKey(name)) {
				   name = list[rn.nextInt(list.length)];
			   }
		   }
		   return name;
     }
}
