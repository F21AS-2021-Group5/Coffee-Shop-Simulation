/**
 * NewGUI.java - class to create and display GUI 
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

//import org.omg.PortableServer.THREAD_POLICY_ID;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.Set;

import javax.swing.*;




public class NewGUI implements PropertyChangeListener{
	//Frame
	JFrame frame = new JFrame("Coffee Shop");
	
	// Buttons
	JButton addCashier = new JButton("Add Cashier");
	JButton addBarista = new JButton("Add Barista");
	JButton addCook = new JButton("Add Cook");
	JButton removecashiers = new JButton("Remove Cashier");
	JButton removebaristas = new JButton("Remove Barista");
	JButton removecooks = new JButton("Remove Cook");
	JButton cashierTimeOK = new JButton("OK");
	JButton cookTimeOK = new JButton("OK");
	JButton baristaTimeOK = new JButton("OK");
	
	//Labels 
	JLabel label = new JLabel();
	JLabel delayLabel = new JLabel("Delay:");	//For spinner
	JLabel customerLabel = new JLabel("In shop Customer queue:");
	JLabel onlinecustomerlabel = new JLabel("Online Customer queue:");
	JLabel activecashierLabel = new JLabel("Active Cashiers:");
	JLabel activecookLabel = new JLabel("Active Cooks:");
	JLabel activebaristaLabel = new JLabel("Active Baristas:");
	JLabel cashierTimeLabel = new JLabel("Set Cashier Delay:");
	JLabel cookTimeLabel = new JLabel("Set Cook Delay:");
	JLabel baristaTimeLabel = new JLabel("Set Barista Delay:");
	
	//Model
	DefaultListModel shopModel = new DefaultListModel();
	DefaultListModel onlineModel = new DefaultListModel();
	DefaultListModel cashierModel = new DefaultListModel();
	DefaultListModel cookModel = new DefaultListModel();
	DefaultListModel baristaModel = new DefaultListModel();
	
	//JList
	JList shopcustomerlist = new JList(shopModel);
	JList onlinecustomerlist = new JList(onlineModel);
	JList cashierlist = new JList(cashierModel);
	JList cooklist = new JList(cookModel);
	JList baristalist = new JList(baristaModel);

	//Text Filed (Enter names)
	JTextField cashierTime = new JTextField(3);
	JTextField baristaTime = new JTextField(3);
	JTextField cookTime = new JTextField(3);	
	
	Customer currentCustomer;
	CoffeeShop coffeeshop;
	Employees employees;
	NewCustomerQueue newCustomerQueue;	//Subject class
	CustomerQueue onlineQueue;
	//CustomerQueue shopQueue;
	CashierRunnable cashierRunnable;
	FoodStaffRunnable foodStaffRun;
	Log log;
	
	NewCustomerQueue inshopqueue;
	
	//For observer
	private Deque<Customer> shopQueueObserver;
	private Deque<Customer> onlineQueueObserver;
	private long delayObserver;
	
	private static int observerIDTracker = 0;	//Used as counter
//	private int observerID;	//Track observers
	private Subject subject;	//Holds reference to NewGUI object
	NewCustomerQueue shopQueue;	
	Deque<Customer> shopQueueObse;
	
	//GUI constructor
	public NewGUI(NewCustomerQueue shopQueue) { //CoffeeShop main, 
		//this.coffeeshop = main;
		//shopQueueObse = new 
		this.shopQueue = shopQueue;
		shopcustomerlist.setVisible(true);
	
		CoffeeShop.shopQueue.addPropertyChangeListener(this);//(e -> System.out.println("HERE")); //setQueue((Deque<Customer>) e.getNewValue())
		shopcustomerlist.setModel(shopModel);
    	
		//propertyChange();
		//shopQueue.registerObserver(this);
//		this.newCustomerQueue = custQ;	//remember clock object
//		custQ.registerObserver(this);
		//update();	//set initial customer queue
		
		
//		this.subject = subject;
		//subject.registerObserver(this); //Stores reference to NewGUI object
		
		//this.newGui = newGui;	
		//this.observerID = ++observerIDTracker;	//Assign an observerID and increment static counter
		//System.out.println("New Observer " + this.observerID);	//Notify of new observer
		//newGui.registerObserver(this);	//Add observer to Subjects ArrayList

		
	}
	
	public void setQueue(	Deque<Customer> queue) {
		System.out.println("GUI");
		this.shopQueueObse = queue;
	}
	

	
	//Create and show GUI
	public void initializeGUI() {
		frame.setSize(800, 670);
		frame.setLayout(null);
		frame.setVisible(true); //Show GUI
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //Close when Exit
		
		setupListener();
		
		//Buttons
		addCashier.setBounds(20, 270, 220, 40);
		addCook.setBounds(280, 270, 220, 40);
		addBarista.setBounds(540, 270, 220, 40);
		removecashiers.setBounds(20, 460, 220, 40);
		removecooks.setBounds(280, 460, 220, 40);
		removebaristas.setBounds(540, 460, 220, 40);
		cashierTimeOK.setBounds(180, 550, 60, 40);
		baristaTimeOK.setBounds(440, 550, 60, 40);
		cookTimeOK.setBounds(700, 550, 60, 40);
		
		//Labels
		customerLabel.setBounds(10, 2, 150, 50);
		onlinecustomerlabel.setBounds(400, 2, 150, 50); 
		activecashierLabel.setBounds(10, 310, 150, 40);
		activecookLabel.setBounds(270, 310, 150, 40);
		activebaristaLabel.setBounds(530, 310, 150, 40);
		cashierTimeLabel.setBounds(20, 520, 200, 40);
		cookTimeLabel.setBounds(280, 520, 200, 40);
		baristaTimeLabel.setBounds(540, 520, 200, 40);
		
		//JList
		shopcustomerlist.setBounds(10, 50, 375, 200);
		onlinecustomerlist.setBounds(400, 50, 375, 200);
		cashierlist.setBounds(10, 340, 250, 100); 
		cooklist.setBounds(270, 340, 250, 100);
		baristalist.setBounds(530, 340, 250, 100);
		
		//Text fields
		cashierTime.setBounds(20, 550, 150, 40);
		cookTime.setBounds(280, 550, 150, 40);
		baristaTime.setBounds(540, 550, 150, 40);
			
			
		label.setText("Coffee Shop");
		Font  f2 = new Font(Font.SANS_SERIF, Font.BOLD, 35);
		label.setFont(f2);
	}
	
	/**
	 * Adds elements to the GUI 
	 */
	public void paintScreen() {
		frame.getContentPane().add(addCashier);
		frame.getContentPane().add(addBarista);
		frame.getContentPane().add(addCook);
		frame.getContentPane().add(removecashiers);
		frame.getContentPane().add(removebaristas);
		frame.getContentPane().add(removecooks);
		frame.getContentPane().add(cashierTimeOK);
		frame.getContentPane().add(cookTimeOK);
		frame.getContentPane().add(baristaTimeOK);
		
		frame.getContentPane().add(delayLabel);
		frame.getContentPane().add(customerLabel);
		frame.getContentPane().add(onlinecustomerlabel); 
		frame.getContentPane().add(activecashierLabel);
		frame.getContentPane().add(activecookLabel);
		frame.getContentPane().add(activebaristaLabel);
		frame.getContentPane().add(cashierTimeLabel);
		frame.getContentPane().add(cookTimeLabel);
		frame.getContentPane().add(baristaTimeLabel);
		
		frame.getContentPane().add(shopcustomerlist);
		frame.getContentPane().add(onlinecustomerlist);
		frame.getContentPane().add(cashierlist); 
		frame.getContentPane().add(cooklist);
		frame.getContentPane().add(baristalist);
		
		frame.getContentPane().add(cashierTime);
		frame.getContentPane().add(baristaTime);
		frame.getContentPane().add(cookTime);
				
	}
	
	/**
	 * Resets GUI to initial state 
    */
	private void reset() {
		frame.repaint();
		cashierTimeOK.setEnabled(false);
		cookTimeOK.setEnabled(false);
		baristaTimeOK.setEnabled(true);
		cashierlist.setModel(cashierModel);
    }

	/**
	 * Set up once delay time is entered
    */
	private void set() {
		cashierTimeOK.setEnabled(true);
		cookTimeOK.setEnabled(true);
		baristaTimeOK.setEnabled(true);
	}
	
	/**
	 * Sets up action listeners for all elements in the GUI 
	 */
	private void setupListener() {
		
		
		ActionListener Listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//newCustomerQueue.setQueue(shopQueueObserver); //Set queue
				//Create new cashier
				if (e.getSource() == addCashier) {
					int cashierSize = coffeeshop.cashierThreads.size(); //Store number of existent cashier
					if (cashierSize == 11 || cashierSize>11) {	//Cannot have more than 11 cashiers
						JOptionPane.showMessageDialog(null,
						        "Error: you cannot create more than eleven cashiers", 
						         "Error", JOptionPane.ERROR_MESSAGE);
					}
					else {
						coffeeshop.addCashier(); //Create New cashier
						newCashierFrame(); //create new window for cashier showing customer handled and their order
					}
				}
				//Create new cook
				if (e.getSource() == addCook) {
					int cookSize = coffeeshop.cookList.size();	//Store number of existent cook
					if (cookSize == 6) {
						JOptionPane.showMessageDialog(null,
						        "Error: you cannot create more than six cooks", 
						         "Error", JOptionPane.ERROR_MESSAGE);
					}
					else {
						coffeeshop.addCook();  //Create new cook
						newCookFrame();  //create new window with cook and orders handled and customer
					}
					
				}
				//Create new barista
				if (e.getSource() == addBarista) {
					int baristaSize = coffeeshop.baristaList.size();
					if (baristaSize == 5) {
						JOptionPane.showMessageDialog(null,
						        "Error: you cannot create more than five baristas", 
						         "Error", JOptionPane.ERROR_MESSAGE);
					}
					else {
						coffeeshop.addBarista();  //Create new Barista
						newBaristaFrame();  //create new window with barista and orders handles and customer
					}
					
				}
				//Remove selected cashier
				if (e.getSource() == removecashiers) {
					String selectedcashier = (String) cashierlist.getSelectedValue();
					selectedcashier = selectedcashier.substring(0, 20).trim(); //remove string from list
					int cashierSize = coffeeshop.cashierThreads.size(); //Store number of existent cashier
					if(cashierSize == 1) {	//Cannot remove cashier if size less than 1
						JOptionPane.showMessageDialog(null,
						        "Error: you cannot have less than one cashier", 
						         "Error", JOptionPane.ERROR_MESSAGE);
					}
					else {
						coffeeshop.removeCashier(selectedcashier);
						//close window ??how
					}
					
				}
				//Remove selected cook
				if (e.getSource() == removecooks) {
					String selectedcook = (String) cooklist.getSelectedValue();
					selectedcook = selectedcook.substring(0, 20).trim(); //remove string from list
					int cookSize = coffeeshop.cookList.size(); //Store number of existent barista
					if(cookSize == 1) { //Cannot remove barista if size less than 1
						JOptionPane.showMessageDialog(null,
						        "Error: you cannot have less than one cook", 
						         "Error", JOptionPane.ERROR_MESSAGE);
					}
					else {
						coffeeshop.removeCook(selectedcook);
						//close window
					}
				}
				//Remove selected barista
				if (e.getSource() == removebaristas) {
					String selectedbarista = (String) baristalist.getSelectedValue();
					selectedbarista = selectedbarista.substring(0, 20).trim(); //remove string from list
					int baristaSize = coffeeshop.baristaList.size(); //Store number of existent barista
					if(baristaSize == 1) { //Cannot remove barista if size less than 1
						JOptionPane.showMessageDialog(null,
						        "Error: you cannot have less than one barista", 
						         "Error", JOptionPane.ERROR_MESSAGE);
					}
					else {
						coffeeshop.removeBarista(selectedbarista);
						//close window
					}
				}
				//Set Delay Time for selected cashier
				if (e.getSource() == cashierTimeOK) {
					String time = cashierTime.getText();
					try {
						int itime = Integer.parseInt(time);
					}
					catch(NumberFormatException e1){	//Check if entered text is an int and not null
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null,
						        "Error: Invalid or null int entered", 
						         "Error", JOptionPane.ERROR_MESSAGE);
					}
					int itime = Integer.parseInt(time);
//					if(time.trim().isEmpty()) {	//If delay time empty, error
//						JOptionPane.showMessageDialog(null,
//						        "Error: you did not enter a delay time for cashier, please enter a delay time.", 
//						         "Error", JOptionPane.ERROR_MESSAGE);
//					}
					if(itime<200 || itime>2000) { //Check delay time valid
						JOptionPane.showMessageDialog(null,
						        "Error: you did not enter a valid delay time, please enter a delay time between 200 and 2000.", 
						         "Error", JOptionPane.ERROR_MESSAGE); //error message
						cashierTime.setText("");	//empty text box
					}
					else {
						//change delay time for selected cashier
						String selectedcashier = (String) cashierlist.getSelectedValue();
					}
					
				}
				//Set Delay Time for selected cook
				if (e.getSource() == cookTimeOK) {
					String time = cookTime.getText();
					try {
						int itime = Integer.parseInt(time);
					}
					catch(NumberFormatException e1){	//Check if entered text is an int and not null
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null,
						        "Error: Invalid or null int entered", 
						         "Error", JOptionPane.ERROR_MESSAGE);
					}
					int itime = Integer.parseInt(time);
					if(itime<200 || itime>2000) {	//Check delay time valid
						JOptionPane.showMessageDialog(null,
						        "Error: you did not enter a valid delay time, please enter a delay time between 200 and 2000.", 
						         "Error", JOptionPane.ERROR_MESSAGE);
						cookTime.setText("");	//empty text box
					}
					else {
						//change delay time for selected cook
						String selectedcook = (String) cooklist.getSelectedValue();
					}
					
				}
				//Set Delay Time for selected barista
				if (e.getSource() == baristaTimeOK) {
					String time = baristaTime.getText();
					//Try-Catch exception: Error message if entered time is invalid
					try {
						int itime = Integer.parseInt(time);
					}
					catch(NumberFormatException e1){	//Check if entered text is an int and not null
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null,
						        "Error: Invalid or null int entered", 
						         "Error", JOptionPane.ERROR_MESSAGE);
						
					}
					//Error message is time entered is too short or too long
					int itime = Integer.parseInt(time);
					if(itime<200 || itime>2000) { //Check delay time valid
						JOptionPane.showMessageDialog(null,
						        "Error: you did not enter a valid delay time, please enter a delay time between 200 and 2000.", 
						         "Error", JOptionPane.ERROR_MESSAGE);
						baristaTime.setText("");	//empty text box
					}
					//If time is valid, change barista delay time
					else {
						String selectedbarista = (String) baristalist.getSelectedValue();
						//foodStaffRun.getFoodStaff().
					}
				}
			}
		};
		
		addCashier.addActionListener(Listener);
		addCook.addActionListener(Listener);
		addBarista.addActionListener(Listener);
		removecashiers.addActionListener(Listener);
		removecooks.addActionListener(Listener);
		removebaristas.addActionListener(Listener);
		cashierTimeOK.addActionListener(Listener);
		cookTimeOK.addActionListener(Listener);
		baristaTimeOK.addActionListener(Listener);
		
	}
	

	public synchronized void update() {
		//System.out.println("I AM HEREEEEEEEE HELLOO");
		displayCustomer();
		//paintScreen();
//		displayOnlineCustomer();
//		displayCashier();
//		displayCook();
//		displayBarista();
		
		//observerIDTracker++;	//Increment counter
		//System.out.println("GUI has been updated " + observerIDTracker + " times.");
	}
	
	/**
	 * Display the list of in shop customer queue
	 */
    private synchronized void displayCustomer() {    	
    	NewCustomerQueue q = CoffeeShop.shopQueue;			//newCustomerQueue.getQueue();		//shopQueue.getShopQueue();
    	//NewCustomerQueue q = (NewCustomerQueue) tes.clone();
    	//HERE
    

    	//Customer customer = coffeeshop.shopQueue.getQueue()
    	shopModel.clear();
//    	for(Customer customer : CoffeeShop.shopQueue.getQueue()){
//    	//for(int i = 0; i<coffeeshop.shopQueue.getQueue().size(); i++) {
//    		String display = String.format("%-10s %-5s %-10s %-5s %-5s\n", customer.getTimestamp(), " Name: ",customer.getName(), "  Cart: ", customer.getCart().size() );
//    		//String display = String.format("%-10s\n", "hello");
//    		shopModel.addElement(display);
//    	}
    	
        //shopModel.addElement(shopQueue.getQueue().size());
    	if(shopQueueObse!=null) {
    	String display1 = String.format("%-10s %-10s %-10s\n", "There are currently ", shopQueueObse.size(), " in shop customers waiting\n\n");
    	
    	shopModel.addElement(display1);
        
    	
    		for(Customer customer : shopQueueObse){
            	String display = String.format("%-10s\n", customer.getName());
            	shopModel.addElement(display);
            }

    	}
    	
        
       
    	//}
        //shopModel.addElement(shopQueue.getQueue().size());
		shopcustomerlist.setModel(shopModel);
    	shopcustomerlist.setVisible(true);
    	
    	
//    	HashMap<String, Customer> q = coffeeshop.q
//    	String display1 = String.format("%-10s %-10s %-10s\n", "There are currently ", q.size(), " active customers");
//    	shopModel.addElement(display1);	//Display number of active cashiers
//		
//    	for (Map.Entry m: q.entrySet()) {	//For each cashiers
//    		if(!(q.isEmpty())) {
//    			String Cname = m.toString();
//        		String display = String.format("%-10s\n", Cname);  //Display their name
//        		shopModel.addElement(display);
//    		}
//    	}
//    	shopcustomerlist.setModel(shopModel);
    }
    
	/**
	 * Display the list of online customer queue
	 */
//    private void displayOnlineCustomer() {    	
//    	Deque<Customer> q = shopQueue;
//    	if(!(q.isEmpty())) {
//    		String display1 = String.format("%-10s %-10s %-10s\n", "There are currently ", q.size(), " online customers waiting\n\n");
//    		onlineModel.addElement(display1);
//        	for(int i = 0; i<q.size(); i++) {
//        		String display = String.format("%-10s\n", q.toString());
//        		onlineModel.addElement(display);
//        	}
//    	}
//		onlinecustomerlist.setModel(onlineModel);
//		onlinecustomerlist.setVisible(true);
//    }

	/**
	 * Display the list of existent cashiers
	 */
    private void displayCashier() {
    	HashMap<String, Cashier> q = employees.getActiveCashiers();	//Access List of cashiers
    	String display1 = String.format("%-10s %-10s %-10s\n", "There are currently ", q.size(), " active cashiers.");	
		cashierModel.addElement(display1);	//Display number of active cashiers
		
    	for (Map.Entry m: q.entrySet()) {	//For each cashiers
    		if(!(q.isEmpty())) {
    			String Cname = m.toString();
        		String display = String.format("%-10s\n", Cname);  //Display their name
        		cashierModel.addElement(display);
    		}
    	}
    	cashierlist.setModel(cashierModel);
    }
    
	/**
	 * Display the list of existent cooks
	 */
    private void displayCook() {
    	HashMap<String, FoodStaff> q = employees.getActiveCooks();	//Access List of cooks
    	String display1 = String.format("%-10s %-10s %-10s\n", "There are currently ", q.size(), " active cooks.");	
    	cookModel.addElement(display1);	//Display number of active cooks
		
    	for (Map.Entry m: q.entrySet()) {  //For each cooks
    		String Cname = m.toString();
    		String display = String.format("%-40s %-4s", Cname);  //Display their name
    		cookModel.addElement(display);
    	}
    	cooklist.setModel(cookModel);
    }
    
	/**
	 * Display the list of existent baristas
	 */
    private void displayBarista() {
    	HashMap<String, FoodStaff> q = employees.getActiveBaristas();	//Access List of baristas
    	String display1 = String.format("%-10s %-10s %-10s\n", "There are currently ", q.size(), " active baristas.");	
    	baristaModel.addElement(display1);	//Display number of active baristas
    	
    	for (Map.Entry m: q.entrySet()) {  //For each baristas
    		String Bname = m.toString();
    		String display = String.format("%-40s %-4s", Bname);  //Format the display
    		baristaModel.addElement(display);
    	}
    	baristalist.setModel(baristaModel);
    }
    
    /**
	 * Create New frame and Display cashier orders handled
	 */
    private void newCashierFrame() {
    	//Display their name + delay time
		JFrame frame1 = new JFrame();
		frame1.setSize(400, 400);
		frame1.setTitle("New Cashier");
		frame1.setLayout(null);
		frame1.setVisible(true); //Show GUI
		
		//Display name of cashier
		JLabel CashierName = new JLabel("Cashier: "); //+ cashierRunnable.cashier.getName() //HOW DO I ACCESS NAME OF CREATED CASHIER???
		CashierName.setBounds(50, 5, 200, 20);
		frame1.getContentPane().add(CashierName);
		
		//Display name of customer
		JLabel custName = new JLabel("Customer: " + cashierRunnable.currentCustomer.getName());
		custName.setBounds(10, 50, 150, 30);
		frame1.getContentPane().add(custName);
		
		//Display customer orders + total price
		JTextArea custList = new JTextArea("");
		custList.setBounds(10, 90, 300, 200);
		frame1.getContentPane().add(custList);
		String output = " ";
		String out2 = " ";
		Set<String> customerCart = cashierRunnable.currentCustomer.cart.keySet();
		for (String orderID: customerCart) {
			output += String.format("%-10s %-10s %-10s %-10s\n", String.valueOf(cashierRunnable.currentCustomer.cart.get(orderID).size()), 
					    coffeeshop.menu.get(orderID).getName(), String.valueOf(coffeeshop.menu.get(orderID).getCost()), "£");
			out2 = String.format("%-10s %-10s %-10s\n", "Total price: ", String.valueOf(cashierRunnable.currentCustomer.getCartTotalPrice()), "£");
		}
		custList.setText(output + out2);
		
    }
    
    /**
	 * Create New frame and Display cook orders handled
	 */
    private void newCookFrame() {
    	//Display their name + delay time
		JFrame frame1 = new JFrame();
		frame1.setSize(400, 400);
		frame1.setTitle("New Cook");
		frame1.setLayout(null);
		frame1.setVisible(true); //Show GUI
		
		//Display name of cook
		JLabel CookName = new JLabel("Cook: "); //+ foodStaffRun.getFoodStaff().getName() //HOW DO I ACCESS NAME OF CREATED COOK???
		CookName.setBounds(50, 5, 200, 20);
		frame1.getContentPane().add(CookName);
				
		//Display name of customer
		JLabel custName = new JLabel("Customer Processed: " + cashierRunnable.currentCustomer.getName());
		custName.setBounds(10, 40, 250, 30);
		frame1.getContentPane().add(custName);
				
		//Display ordered Food and Pastry handled by cook for current customer
		JTextArea custList = new JTextArea("");
		custList.setBounds(10, 80, 300, 200);
		frame1.getContentPane().add(custList);
		String output = " ";
		Set<String> customerCart = cashierRunnable.currentCustomer.cart.keySet();
		for (String orderID: customerCart) {
			String category = coffeeshop.menu.get(orderID).getCategory();
			if(category.equals("Pastry") || category.equals("Food")) {
				output += String.format("%-10s %-10s %-10s %10s\n", String.valueOf(cashierRunnable.currentCustomer.cart.get(orderID).size()), 
						   coffeeshop.menu.get(orderID).getName(), String.valueOf(coffeeshop.menu.get(orderID).getCost()), "£");
			}
		}
		custList.setText(output);
    }
    
    /**
	 * Create New frame and Display barista orders handled
	 */
    private void newBaristaFrame() {
    	//Display their name + delay time
		JFrame frame1 = new JFrame();
		frame1.setSize(400, 400);
		frame1.setTitle("New Barista");
		frame1.setLayout(null);
		frame1.setVisible(true); //Show GUI
		
		//Display name of barista
		JLabel BaristaName = new JLabel("Barista: "); //HOW DO I ACCESS NAME OF CREATED BARISTA???
		BaristaName.setBounds(50, 5, 200, 20);
		frame1.getContentPane().add(BaristaName);
		
		//Display name of customer
		JLabel custName = new JLabel("Customer Processed: " + cashierRunnable.currentCustomer.getName());
		custName.setBounds(10, 40, 250, 30);
		frame1.getContentPane().add(custName);
		
		//Display ordered Drink handled by barista for current customer
		JTextArea custList = new JTextArea("");
		custList.setBounds(10, 80, 300, 200);
		frame1.getContentPane().add(custList);
		String output = " ";
		Set<String> customerCart = cashierRunnable.currentCustomer.cart.keySet();
		for (String orderID: customerCart) {
			String category = coffeeshop.menu.get(orderID).getCategory();
			if(category.equals("Drink")) {
				output += String.format("%-10s %-10s %-10s %10s\n", String.valueOf(cashierRunnable.currentCustomer.cart.get(orderID).size()), 
							coffeeshop.menu.get(orderID).getName(), String.valueOf(coffeeshop.menu.get(orderID).getCost()), "£");
			}
		}
		custList.setText(output);
    }
	
	//While GUI is running, keep updating

	public void run() {
		while(true) {
			//displayCustomer();
			//this.propertyChange(syso);
			//CoffeeShop.shopQueue.();
		
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		CoffeeShop.shopQueue.setMessage((Deque<Customer>) evt.getNewValue());
		System.out.println(evt.getNewValue());
		//setNews((Deque<Customer>) evt.getNewValue());
		
	}
    
    


}