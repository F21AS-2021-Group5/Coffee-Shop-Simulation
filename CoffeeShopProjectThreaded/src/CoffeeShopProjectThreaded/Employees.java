package CoffeeShopProjectThreaded;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Random;

public class Employees {
	
	private final String[] cashierNames={"Ron","Leslie", "April", "Donna","Andy","Ann","Ben", 
			"Tom", "Jerry", "Gerry", "Lerry"};  
	private final String[] baristaNames={"Mac","Cahrlie", "Frank", "Deandra","Dennis"};  
	private final String[] cookNames={"Dwight","Pam", "Jim", "Andy","Kelly", "Angela"};
	
	public static HashMap<String, Cashier> activeCashiers;
	public static  HashMap<String, FoodStaff> activeBaristas;
	public static  HashMap<String, FoodStaff> activeCooks;
	private Log log;
	
	private PropertyChangeSupport support;
	
	/**
	 * Constructor for Employees class 
	 */
	public Employees() {
		activeCashiers = new HashMap<String, Cashier>();
		activeBaristas = new HashMap<String, FoodStaff>();
		activeCooks = new HashMap<String, FoodStaff>();
		support = new PropertyChangeSupport(this);
		log = Log.getInstance();
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
	public Cashier addCashier(Long delay) {
		String name = getRandomName(cashierNames, "cashier");
		Cashier cashier = new Cashier(name, delay);
		
		activeCashiers.put(name, cashier);
		log.updateLog("[Employees]: " +"Cashier " + name + " has started their shift ");
		setMessage(0, activeCashiers.size(), "cashierAdded");
		return cashier;
	}
	
	/**
	 * Removes cashier from list 
	 * @param name Cashier name 
	 */
	public void removeCashier(String name) {
		activeCashiers.remove(name);
		setMessage(0, activeCashiers.size(), "cashierRemoved");
		log.updateLog("[Employees]: " +"Cashier " + name + " has ended their shift ");
	}
	
	/**
	 * Adds barista to barista list 
	 * @return Barista added
	 */
	public FoodStaff addBarista(Long delay) {
		String name = getRandomName(baristaNames, "barista");
		FoodStaff barista = new FoodStaff(name, true, delay);
		activeBaristas.put(name, barista);
		
		setMessage(0, activeBaristas.size(), "baristaAdded");
		log.updateLog("[Employees]: " +"Barista " + name + " has started their shift ");
		return barista;
	}
	
	/**
	 * Removes barista from list 
	 * @param name Barista name 
	 */
	public void removeBarista(String name) {
		activeBaristas.remove(name);
		setMessage(0, activeBaristas.size(), "baristaRemoved");
		log.updateLog("[Employees]: " +"Barista " + name + " has ended their shift ");
	}
	
	/**
	 * Adds cook to cook list 
	 * @return Cook added
	 */
	public FoodStaff addCook(Long delay) {
		String name = getRandomName(cookNames, "cook");
		FoodStaff cook = new FoodStaff(name, false, delay);
		activeCooks.put(name, cook);
		
		setMessage(0, activeCooks.size(), "cookAdded");
		log.updateLog("[Employees]: " +"Cook " + name + " has started their shift ");
		return cook;
	}
	
	/**
	 * Removes cook from list 
	 * @param name Cook name 
	 */
	public void removeCook(String name) {
		activeCooks.remove(name);
		setMessage(0, activeCooks.size(), "cookRemoved");
		log.updateLog("[Employees]: " +"Cook " + name + " has ended their shift ");
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
	
	
	// Observer observable
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
    	support.addPropertyChangeListener(pcl);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
    	support.removePropertyChangeListener(pcl);
    }
        
    public void setMessage(int oldVal, int newVal, String message) {
    	support.firePropertyChange(message, oldVal, newVal);
    }
}
