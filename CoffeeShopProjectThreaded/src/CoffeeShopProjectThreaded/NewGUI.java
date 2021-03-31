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
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Queue;
import java.util.Set;

import javax.swing.*;



public class NewGUI implements PropertyChangeListener {
	// Frame
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

	// Labels
	JLabel label = new JLabel();
	JLabel delayLabel = new JLabel("Delay:"); // For spinner
	JLabel customerLabel = new JLabel("In shop Customer queue:");
	JLabel onlinecustomerlabel = new JLabel("Online Customer queue:");
	JLabel activecashierLabel = new JLabel("Active Cashiers:");
	JLabel activecookLabel = new JLabel("Active Cooks:");
	JLabel activebaristaLabel = new JLabel("Active Baristas:");
	JLabel cashierTimeLabel = new JLabel("Set Cashier Delay:");
	JLabel cookTimeLabel = new JLabel("Set Cook Delay:");
	JLabel baristaTimeLabel = new JLabel("Set Barista Delay:");

	// Model
	DefaultListModel shopModel = new DefaultListModel();
	DefaultListModel onlineModel = new DefaultListModel();
	DefaultListModel cashierModel = new DefaultListModel();
	DefaultListModel cookModel = new DefaultListModel();
	DefaultListModel baristaModel = new DefaultListModel();


	// JList
	JList shopcustomerlist = new JList(shopModel);
	JList onlinecustomerlist = new JList(onlineModel);
	JList cashierlist = new JList(cashierModel);
	JList cooklist = new JList(cookModel);
	JList baristalist = new JList(baristaModel);
	
	//JScroll
	JScrollPane cashierScroll = new JScrollPane(cashierlist);
	JScrollPane cookScroll = new JScrollPane(cooklist);
	JScrollPane baristaScroll = new JScrollPane(baristalist);

	// Text Filed (Enter names)
	JTextField cashierTime = new JTextField(3);
	JTextField baristaTime = new JTextField(3);
	JTextField cookTime = new JTextField(3);

	Customer currentCustomer;
	CoffeeShop coffeeshop;
	Employees employees;
	NewCustomerQueue newCustomerQueue; // Subject class
	CustomerQueue onlineQueue;

	CashierRunnable cashierRunnable;
	FoodStaffRunnable foodStaffRun;
	Log log;

	NewCustomerQueue inshopqueue;
	NewCustomerQueue shopQueue;
	Deque<Customer> shopQueueObse;
	Deque<Customer> onlineQueueObse;
	
	HashMap<String, JFrame> cashierFrames;
	HashMap<String, JFrame> cookFrames;
	HashMap<String, JFrame> baristaFrames;

	// GUI constructor
	public NewGUI(NewCustomerQueue shopQueue, HashMap<String, Thread> cashierT, HashMap<String, Thread> cookT, HashMap<String, Thread> baristaT) {

		this.shopQueue = shopQueue;
		
		//shopcustomerlist.setVisible(true);
		//onlinecustomerlist.setVisible(true);
		//cashierlist.setVisible(true);
		
		cashierScroll.setVisible(true);
		cookScroll.setVisible(true);
		baristaScroll.setVisible(true);

		// CoffeeShop.shopQueue.addPropertyChangeListener(this);//(e ->
		// System.out.println("HERE")); //setQueue((Deque<Customer>) e.getNewValue())
		shopcustomerlist.setModel(shopModel);
		onlinecustomerlist.setModel(onlineModel);
		cashierlist.setModel(cashierModel);
		cooklist.setModel(cookModel);
		baristalist.setModel(baristaModel);
		
		//Set Scroll only vertical
		cashierScroll.setViewportView(cashierlist);
		cashierScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		cashierScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		cookScroll.setViewportView(cooklist);
		cookScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		cookScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		baristaScroll.setViewportView(baristalist);
		baristaScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		baristaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		cashierFrames = new HashMap<String, JFrame>();
		cookFrames = new HashMap<String, JFrame>();
		baristaFrames = new HashMap<String, JFrame>();

	}

	public void setQueue(Deque<Customer> queue) {
		System.out.println("GUI");
		this.shopQueueObse = queue;
		this.onlineQueueObse = queue;
	}	

	// Create and show GUI
	public void initializeGUI() {
		frame.setSize(800, 670);
		frame.setLayout(null);
		frame.setVisible(true); // Show GUI
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Close when Exit

		setupListener();

		// Buttons
		addCashier.setBounds(20, 270, 220, 40);
		addCook.setBounds(280, 270, 220, 40);
		addBarista.setBounds(540, 270, 220, 40);
		removecashiers.setBounds(20, 460, 220, 40);
		removecooks.setBounds(280, 460, 220, 40);
		removebaristas.setBounds(540, 460, 220, 40);
		cashierTimeOK.setBounds(180, 550, 60, 40);
		baristaTimeOK.setBounds(440, 550, 60, 40);
		cookTimeOK.setBounds(700, 550, 60, 40);

		// Labels
		customerLabel.setBounds(10, 2, 350, 50);
		onlinecustomerlabel.setBounds(400, 2, 350, 50);
		activecashierLabel.setBounds(10, 310, 150, 40);
		activecookLabel.setBounds(270, 310, 150, 40);
		activebaristaLabel.setBounds(530, 310, 150, 40);
		cashierTimeLabel.setBounds(20, 520, 200, 40);
		cookTimeLabel.setBounds(280, 520, 200, 40);
		baristaTimeLabel.setBounds(540, 520, 200, 40);

		// JList
		shopcustomerlist.setBounds(10, 50, 375, 200);
		onlinecustomerlist.setBounds(400, 50, 375, 200);
		//cashierlist.setBounds(10, 340, 250, 100);
		//cooklist.setBounds(270, 340, 250, 100);
		//baristalist.setBounds(530, 340, 250, 100);
		cashierScroll.setBounds(10, 340, 250, 100);
		cookScroll.setBounds(270, 340, 250, 100);
		baristaScroll.setBounds(530, 340, 250, 100);

		// Text fields
		cashierTime.setBounds(20, 550, 150, 40);
		cookTime.setBounds(280, 550, 150, 40);
		baristaTime.setBounds(540, 550, 150, 40);

		label.setText("Coffee Shop");
		Font f2 = new Font(Font.SANS_SERIF, Font.BOLD, 35);
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
		//frame.getContentPane().add(cashierlist);
		//frame.getContentPane().add(cooklist);
		//frame.getContentPane().add(baristalist);
		frame.getContentPane().add(cashierScroll);
		frame.getContentPane().add(cookScroll);
		frame.getContentPane().add(baristaScroll);

		frame.getContentPane().add(cashierTime);
		frame.getContentPane().add(baristaTime);
		frame.getContentPane().add(cookTime);

	}
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
				/////////////// Creates employees ////////////////
				// Create new cashier
				if (e.getSource() == addCashier) {
					int cashierSize = CoffeeShop.employees.activeCashiers.size(); // Store number of existent cashier
					if (cashierSize == 11 || cashierSize > 11) { // Cannot have more than 11 cashiers
						JOptionPane.showMessageDialog(null, "Error: you cannot create more than eleven cashiers",
								"Error", JOptionPane.ERROR_MESSAGE);
					} else {
						Cashier cashier = CoffeeShop.addCashier(2000L); // Create New cashier
						newCashierFrame(cashier); // create new window for cashier showing customer handled and their order
					}
				}
				// Create new cook
				if (e.getSource() == addCook) {
					int cookSize = CoffeeShop.employees.activeCooks.size(); // Store number of existent cook
					if (cookSize == 6) {
						JOptionPane.showMessageDialog(null, "Error: you cannot create more than six cooks", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						FoodStaff cook = CoffeeShop.addCook(2000L); // Create new cook
						newCookFrame(cook); // create new window with cook and orders handled and customer
					}
				}
				// Create new barista
				if (e.getSource() == addBarista) {
					int baristaSize = CoffeeShop.employees.activeBaristas.size();
					if (baristaSize == 5) {
						JOptionPane.showMessageDialog(null, "Error: you cannot create more than five baristas", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						FoodStaff barista = CoffeeShop.addBarista(2000L); // Create new Barista
						newBaristaFrame(barista); // create new window with barista and orders handles and customer
					}
				}
				/////////////// Removes employees ////////////////
				// Remove selected cashier
				if (e.getSource() == removecashiers) {
					String selectedcashier = (String) cashierlist.getSelectedValue();
					selectedcashier = selectedcashier.trim(); // remove string from list  
					int cashierSize = CoffeeShop.employees.activeCashiers.size(); // Store number of existent cashier
					if (cashierSize == 1) { // Cannot remove cashier if size less than 1
						JOptionPane.showMessageDialog(null, "Error: you cannot have less than one cashier", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						cashierFrames.get(selectedcashier).dispose();  // CLOSE WINDOW 
						CoffeeShop.removeCashier(selectedcashier);
					}
				}
				// Remove selected cook
				if (e.getSource() == removecooks) {
					String selectedcook = (String) cooklist.getSelectedValue();
					selectedcook = selectedcook.trim(); // remove string from list
					int cookSize = CoffeeShop.employees.activeCooks.size(); // Store number of existent barista
					if (cookSize == 1) { // Cannot remove barista if size less than 1
						JOptionPane.showMessageDialog(null, "Error: you cannot have less than one cook", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						cookFrames.get(selectedcook).dispose(); //Close Window
						CoffeeShop.removeCook(selectedcook);	//Interrupt selected thread cook 
					}
				}
				// Remove selected barista
				if (e.getSource() == removebaristas) {
					String selectedbarista = (String) baristalist.getSelectedValue();
					selectedbarista = selectedbarista.trim(); // remove string from list
					int baristaSize = CoffeeShop.employees.activeBaristas.size(); // Store number of existent barista
					if (baristaSize == 1) { // Cannot remove barista if size less than 1
						JOptionPane.showMessageDialog(null, "Error: you cannot have less than one barista", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						baristaFrames.get(selectedbarista).dispose(); //Close Window
						CoffeeShop.removeBarista(selectedbarista);
					}
				}
				
				/////////////// Set time delays ////////////////
				// Set Delay Time for selected cashier
				if (e.getSource() == cashierTimeOK) {
					String time = cashierTime.getText();
					try {
						int itime = Integer.parseInt(time);
					} catch (NumberFormatException e1) { // Check if entered text is an int and not null
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "Error: Invalid or null int entered", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
					int itime = Integer.parseInt(time);
					if (itime < 200 || itime > 8000) { // Check delay time valid
						JOptionPane.showMessageDialog(null,
								"Error: you did not enter a valid delay time, please enter a delay time between 200 and 8000.",
								"Error", JOptionPane.ERROR_MESSAGE); // error message
						cashierTime.setText(""); // empty text box
					} else {
						// change delay time for selected cashier
						String selectedcashier = (String) cashierlist.getSelectedValue();
						selectedcashier = selectedcashier.trim();
				
						Long l = Long.valueOf(itime);
						CoffeeShop.employees.activeCashiers.get(selectedcashier).setSpeed(l);
					}

				}
				// Set Delay Time for selected cook
				if (e.getSource() == cookTimeOK) {
					String time = cookTime.getText();
					try {
						int itime = Integer.parseInt(time);
					} catch (NumberFormatException e1) { // Check if entered text is an int and not null
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "Error: Invalid or null int entered", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
					int itime = Integer.parseInt(time);
					if (itime < 200 || itime > 8000) { // Check delay time valid
						JOptionPane.showMessageDialog(null,
								"Error: you did not enter a valid delay time, please enter a delay time between 200 and 8000.",
								"Error", JOptionPane.ERROR_MESSAGE);
						cookTime.setText(""); // empty text box
					} else {
						// change delay time for selected cook
						String selectedcook = (String) cooklist.getSelectedValue();
						selectedcook = selectedcook.trim();
						
						Long l = Long.valueOf(itime);
						CoffeeShop.employees.activeCooks.get(selectedcook).setDelay(l);
					}

				}
				// Set Delay Time for selected barista
				if (e.getSource() == baristaTimeOK) {
					String time = baristaTime.getText();
					try {
						int itime = Integer.parseInt(time);
					} catch (NumberFormatException e1) { // Check if entered text is an int and not null
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "Error: Invalid or null int entered", "Error",
								JOptionPane.ERROR_MESSAGE);

					}
					// Error message is time entered is too short or too long
					int itime = Integer.parseInt(time);
					if (itime < 200 || itime > 8000) { // Check delay time valid
						JOptionPane.showMessageDialog(null,
								"Error: you did not enter a valid delay time, please enter a delay time between 200 and 8000.",
								"Error", JOptionPane.ERROR_MESSAGE);
						baristaTime.setText(""); // empty text box
					}
					// If time is valid, change barista delay time
					else {
						String selectedbarista = (String) baristalist.getSelectedValue();
						selectedbarista = selectedbarista.trim();
						Long l = Long.valueOf(itime);
						CoffeeShop.employees.activeBaristas.get(selectedbarista).setDelay(l);
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

	/**
	 * Display the list of in shop customer queue
	 */
	private synchronized void displayCustomer() {
		shopModel.clear();

		if (shopQueueObse != null) {
			String display1 = String.format("%-10s %-10s %-10s\n", "There are currently ", shopQueueObse.size(),
					" in shop customers waiting\n\n");

			shopModel.addElement(display1);

			for (Customer customer : shopQueueObse) {
				String display = String.format("%-10s\n", customer.getName());
				shopModel.addElement(display);
			}

		}

		String display1 = String.format("%-10s %-10s %-10s\n", "There are currently ", shopQueueObse.size(),
				" in shop customers waiting\n\n");
		
		shopcustomerlist.setModel(shopModel);
		shopcustomerlist.setVisible(true);
	}

	/**
	 * Display the list of online customer queue
	 */
    private synchronized void displayOnlineCustomer() {    
    	onlineModel.clear();

		if (onlineQueueObse != null) {
			String display1 = String.format("%-10s %-10s %-10s\n", "There are currently ", onlineQueueObse.size(),
					" in shop customers waiting\n\n");

			onlineModel.addElement(display1);

			for (Customer customer : onlineQueueObse) {
				String display = String.format("%-10s\n", customer.getName());
				onlineModel.addElement(display);
			}
		}

		String display1 = String.format("%-10s %-10s %-10s\n", "There are currently ", onlineQueueObse.size(),
				" online customers waiting\n\n");

		onlinecustomerlist.setModel(onlineModel);
		onlinecustomerlist.setVisible(true);
    }

	/**
	 * Display the list of existent cashiers
	 */
	private synchronized void displayCashier() {
		cashierModel.clear();  //Clear Display
		HashMap<String, Cashier> cashiers = CoffeeShop.employees.activeCashiers;
		if (!cashiers.isEmpty()) {  
			String display1 = String.format("%-10s %-10s %-10s\n", "There are currently ", cashiers.size(), " active cashiers\n\n");
			cashierModel.addElement(display1); // Display number of active cashiers
		
			for(Entry<String, Cashier> m : cashiers.entrySet()) {  //iterate through existent cashiers stored in hash map
				String key = m.getKey();
				String display = String.format("%-10s\n", key);  //Display their name
				cashierModel.addElement(display);
			}	
		}
		cashierlist.setModel(cashierModel);
		cashierlist.setVisible(true);
	}

	/**
	 * Display the list of existent cooks
	 */
	private synchronized void displayCook() {
		cookModel.clear();
		HashMap<String, FoodStaff> cooks = CoffeeShop.employees.activeCooks;
		if (!cooks.isEmpty()) {
			String display1 = String.format("%-10s %-10s %-10s\n", "There are currently ", cooks.size(), " active cooks\n\n");
			cookModel.addElement(display1); // Display number of active cooks
	
			for(Map.Entry<String, FoodStaff> m : cooks.entrySet()) {
				String key = m.getKey();
				String display = String.format("%-10s\n", key);
				cookModel.addElement(display);
			}
		}
		cooklist.setModel(cookModel);
		cooklist.setVisible(true);
	}

	/**
	 * Display the list of existent baristas
	 */
	private synchronized void displayBarista() {
		baristaModel.clear();
		HashMap<String, FoodStaff>  baristas = CoffeeShop.employees.activeBaristas;
		if (!baristas.isEmpty()) {
			String display1 = String.format("%-10s %-10s %-10s\n", "There are currently ", baristas.size(), " active baristas\n\n");
			baristaModel.addElement(display1); // Display number of active baristas
	
			for(Map.Entry<String, FoodStaff> m : baristas.entrySet()) {
				String key = m.getKey();
				String display = String.format("%-10s\n", key);
				baristaModel.addElement(display);
			}
		}
		baristalist.setModel(baristaModel);
		baristalist.setVisible(true);
	}

	/**
	 * Create New frame and Display cashier orders handled
	 */
	private void newCashierFrame(Cashier cashier) {
		//Set Frame
		JFrame frame1 = new JFrame();
		frame1.setSize(300, 400);
		frame1.setTitle("New Cashier");
		frame1.setLayout(null);
		frame1.setVisible(true);

		// Display name of cashier
		JLabel CashierName = new JLabel("Cashier: " + cashier.getName());
		CashierName.setBounds(50, 5, 200, 20);
		frame1.getContentPane().add(CashierName);
		
		//Display customer receipt
		if (cashier.currentCustomer != null ) {
			JTextArea custList = new JTextArea("");
			custList.setBounds(10, 40, 250, 300);
			frame1.getContentPane().add(custList);
			String output = " ";
			output += String.format("%-10s\n", "Customer: " + cashier.currentCustomer.getName());
			String out2 = " ";
			Set<String> customerCart = cashier.currentCustomer.cart.keySet();
			for (String orderID : customerCart) {
				output += String.format("%-10s %-10s %-10s %-10s\n",
						String.valueOf(cashier.currentCustomer.cart.get(orderID).size()),
						coffeeshop.menu.get(orderID).getName(), String.valueOf(coffeeshop.menu.get(orderID).getCost()),
						"�");
				out2 = String.format("%-10s %-10s %-10s\n", "Total price: ",
						String.valueOf(cashier.currentCustomer.getCartTotalPrice()), "�");
			}
			custList.setText(output + out2);		
			
		} else {
			JOptionPane.showMessageDialog(null,
					"Error: New cashiers cannot be added. Still waiting for customer.",
					"Error", JOptionPane.ERROR_MESSAGE);
		}	
		
		cashierFrames.put(cashier.getName(), frame1);
	}
	
	/**
	 * Update cashier frames with their orders handled
	 */
	public void updateCashierFrame(String name, Customer customer) {		
		JFrame frame = cashierFrames.get(name);
		if (frame == null)
			return;
	
		// Display customer orders + total price
		JTextArea custList = new JTextArea("");
		custList.setBounds(10, 40, 250, 300);
		frame.getContentPane().add(custList);
		String output = " ";
		output += String.format("%-10s\n", "Customer: " + customer.getName());
		String out2 = " ";
		Set<String> customerCart = customer.cart.keySet();
		for (String orderID : customerCart) {
			output += String.format("%-10s %-10s %-10s %-10s\n",
					String.valueOf(customer.cart.get(orderID).size()),
					coffeeshop.menu.get(orderID).getName(), String.valueOf(coffeeshop.menu.get(orderID).getCost()),
					"�");
			out2 = String.format("%-10s %-10s %-10s\n", "Total price: ",
					String.valueOf(customer.getCartTotalPrice()), "�");
		}
		custList.setText(output + out2);
		
	}
	

	/**
	 * Create New frame and Display cook orders handled
	 */
	private void newCookFrame(FoodStaff cook) {
		// Display their name
		JFrame frame1 = new JFrame();
		frame1.setSize(350, 200); //350, 400
		frame1.setTitle("New Cook");
		frame1.setLayout(null);
		frame1.setVisible(true); // Show GUI

		// Display name of cook
		JLabel CookName = new JLabel("Cook: " + cook.getName()); 
		CookName.setBounds(10, 5, 200, 20);
		frame1.getContentPane().add(CookName);
		
		JTextArea custList = new JTextArea("");
		custList.setBounds(10, 40, 300, 100);
		frame1.getContentPane().add(custList);
		String output = " ";
		output += String.format("%-10s\n", "Cooks is preparing food for customer:  "  + cook.getCurrentCustomer().getName());
		output += String.format("%-10s\n",  cook.getInstruction());  //Display actions taken
		custList.setText(output);
		cookFrames.put(cook.getName(), frame1);
	}
	
	/**
	 * Update cashier frames with their orders handled
	 */
	public void updateCookFrame(String name, FoodStaff cook) {		
		//Get JFrame
		JFrame frame = cookFrames.get(name);
		if (frame == null)
			return;

		// Display customer ordered Food handled
		JTextArea custList = new JTextArea("");
		custList.setBounds(10, 40, 300, 100);
		frame.getContentPane().add(custList);
		String output = " ";
		output += String.format("%-10s\n", "Cook is preparing food for customer:  "  + cook.getCurrentCustomer().getName());
		output += String.format("%-10s\n",  "Preparing item: " + cook.getCurrentItem().getName());
		output += String.format("%-10s\n",  "Currently doing: ");
		output += String.format("%-10s\n",  cook.getInstruction());  //Display current actions
		custList.setText(output);
	}

	/**
	 * Create New frame and Display barista orders handled
	 */
	private void newBaristaFrame(FoodStaff barista) {
		JFrame frame1 = new JFrame();
		frame1.setSize(350, 200);
		frame1.setTitle("New Barista");
		frame1.setLayout(null);
		frame1.setVisible(true); // Show GUI

		// Display name of barista
		JLabel BaristaName = new JLabel("Barista: " + barista.getName());
		BaristaName.setBounds(10, 5, 200, 20);
		frame1.getContentPane().add(BaristaName);
		
		// Display ordered Drink handled by barista for current customer
		JTextArea custList = new JTextArea("");
		custList.setBounds(10, 40, 300, 100);
		frame1.getContentPane().add(custList);
		String output = " ";
		output += String.format("%-10s\n", "Barista is preparing drink for customer : " + barista.getCurrentCustomer().getName());
		output += String.format("%-10s\n",  barista.getInstruction());  //Display current actions
		custList.setText(output);
		baristaFrames.put(barista.getName(), frame1);
	}
	
	/**
	 * Update cashier frames with their orders handled
	 */
	public void updateBaristaFrame(String name, FoodStaff barista) {		
		// Get Jframe
		JFrame frame = baristaFrames.get(name);
		if (frame == null)
			return;

		// Display customer orders 
		JTextArea custList = new JTextArea("");
		custList.setBounds(10, 40, 300, 100);
		frame.getContentPane().add(custList);
		String output = " ";
		output += String.format("%-10s\n", "Customer: " + barista.getName());
		String out2 = " ";
		output += String.format("%-10s\n", "Barista is preparing drink for customer:  "  + barista.getCurrentCustomer().getName());
		output += String.format("%-10s\n",  "Preparing item: " + barista.getCurrentItem().getName());
		output += String.format("%-10s\n",  "Currently doing: ");
		output += String.format("%-10s\n",  barista.getInstruction());   //Display current actions
		custList.setText(output);	
	}

	/**
	 * Automatic update of GUI if an observable element changes
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		System.out.println("Name = " + evt.getPropertyName());

		if (!evt.getPropertyName().equals("updated queue size")) {
			// System.out.println("Old Value = " + ((Customer)
			// evt.getOldValue()).getName());

			//System.out.println("New Value = " + ((Customer) evt.getNewValue()).getName());

			//System.out.println("**********************************");

			String type = (String) evt.getPropertyName();
			//System.out.println("}}}}}}}}}}}" +type);
			
			//If new Customer is created
			if (type == "newCustomer") {
				Customer customer = (Customer) evt.getNewValue();
				System.out.println("Cashier " + customer.getCashierServing() + "added customer " + customer.getName());
				updateCashierFrame(customer.getCashierServing(), customer);
			}
			//When a Cashier is added or removed, update the display
			if(type == "cashierAdded" || type == "cashierRemoved") {
				displayCashier();
			}
			//When a Cook is added or removed, update the display
			if(type == "cookAdded" || type == "cookRemoved") {
				displayCook();
			}
			//When a Barista is added or removed, update the display
			if(type == "baristaAdded" || type == "baristaRemoved") {
				displayBarista();
			}
			//When a Cook is added, this is to display their actions
			if(type.equals("instructionCook")) {
				String cook = (String) evt.getNewValue();
				FoodStaff Cook = CoffeeShop.employees.activeCooks.get(cook);
				updateCookFrame(Cook.getName(), Cook);
			}
			//When a Barista is added, this is to display their actions
			if(type.equals("instructionBarista")) {
				String barista = (String) evt.getNewValue();
				FoodStaff Barista = CoffeeShop.employees.activeBaristas.get(barista);
				updateBaristaFrame(Barista.getName(), Barista);
			}
			
			
			String[] split = type.split(" ");  //Used to distinguish customers from in shop/online queue

			//If a customer is added...
			if (split[0].equals("added")) {
				//...to in shop queue
				if (split[1].equals("inshop")){
					//Add customer to display in shop customer list
					Customer customer = (Customer) evt.getNewValue();
					String display = String.format("%-10s %-5s %-10s %-5s %-5s\n", customer.getTimestamp(), " Name: ",
							customer.getName(), "  Cart: ", customer.getCart().size());
					shopModel.addElement(display);
				}else{
					//...to online queue
					//Add customer to display in online customer list
					Customer customer = (Customer) evt.getNewValue();
					String display = String.format("%-10s %-5s %-10s %-5s %-5s\n", customer.getTimestamp(), " Name: ",
							customer.getName(), "  Cart: ", customer.getCart().size());
					onlineModel.addElement(display); 
					
				}

			}
			//If a customer is removed...
			else if (split[0].equals("removed")) {
				//...from the in shop queue
				if (split[1].equals("inshop")){
					System.out.println("trying to remove");
					if (!shopModel.isEmpty())
						shopModel.remove(0); //Remove customer from display
					System.out.println("successfully removed");
				}
				else{
					//...from the online queue
					if (!onlineModel.isEmpty())
						onlineModel.remove(0);   //Remove customer from display
				}
			}
			//Update the number of inshop/online customer if the customer was added/removed from queue
			customerLabel.setText("In shop Customer queue Number of Customers :" + shopModel.size()); 
			onlinecustomerlabel.setText("Online Customer queue Number of Customers :" + onlineModel.size());
			
			//Update customer lists display
			shopcustomerlist.setModel(shopModel);
			onlinecustomerlist.setModel(onlineModel);

			
			/*
			 * if(shopModel.size()!=0) { shopModel.remove(0); }
			 * 
			 * String display1 = String.format("%-10s %-10s %-10s\n",
			 * "There are currently ", shopQueueObserver.size(),
			 * " in shop customers waiting\n\n"); shopModel.addElement(display1);
			 */
			// shopModel.set(0, display1);
			/*
			 * System.out.println(shopQueueObserver.size()); for(Customer customer :
			 * shopQueueObserver) { String display =
			 * String.format("%-10s %-5s %-10s %-5s %-5s\n", customer.getTimestamp(),
			 * " Name: ",customer.getName(), "  Cart: ", customer.getCart().size() );
			 * System.out.println("test2a"); shopModel.addElement(display);
			 * System.out.println("test2b"); } System.out.println("test3");
			 * 
			 * shopModel.remove(0);
			 */

		}

	}

}