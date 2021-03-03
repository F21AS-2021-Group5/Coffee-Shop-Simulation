/**
 * GUIcaffee.java - class to implement the GUI for the coffee shop simulation
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GUIcaffee {
	
	private int width = 1240;
	private int height = 800;
	
	// Frame
	JFrame frame = new JFrame("Coffee Shop");
	
	// Buttons
	JButton newCust = new JButton("New Customer");
	JButton drink = new JButton("Drink");
	JButton food = new JButton("Food");
	JButton pastry = new JButton("Pastry");
	JButton removeItem = new JButton("Remove");
	JButton addItem = new JButton("Add");
	JButton purchase = new JButton("Purchase");
	JButton receipt = new JButton("Generate Receipt");
	JButton endOfDay = new JButton("End of day report");
	JButton reset = new JButton("Reset");
	
	// Text Area 
	JTextArea description = new JTextArea("");
	JTextArea descriptionSum = new JTextArea(CreateInitialText());
	JTextArea orderList = new JTextArea(""); 
	JTextArea receipView = new JTextArea(""); 
	JTextArea summaryView = new JTextArea(""); 
	
	// Other
	JTextField nameCust = new JTextField(20);
	JList menuItem = new JList();
	SpinnerModel quantityModel  = new SpinnerNumberModel(1, 0,100,1);
	JSpinner quantity = new JSpinner(quantityModel);
	
	DefaultListModel listModel = new DefaultListModel();
	private DefaultListModel drinkCategory = new DefaultListModel();
	private DefaultListModel foodCategory = new DefaultListModel(); 
	private DefaultListModel pastryCategory = new DefaultListModel();
	
	// labels 
	JLabel label = new JLabel();
	JLabel nameLabel = new JLabel("Customer Name:");
	
	private static Customer currentCustomer;
	
	Cashier cashier;
	
	double fees;
	boolean feePaid;
	
	private int itemQuantity;
	HashMap<String, LocalDateTime> cart;
	
	/**
	 * Constructor for GUIcaffee class
	 * @param cashier
	 */
	public GUIcaffee(Cashier cashier) {
		this.cashier = cashier;		
		InitialiseCategoryList(); // Initialise Category list 
		reset();
	}
	
	/**
	 * Creates Discount text 
	 */
	private String CreateInitialText() {
		String output = String.format("%-20s \n\n", "Discounts when buying") + 
				String.format("%-10s %-20s\n", " ", "1 Drinks, 1 Food, 1 Pastry")+
				String.format("%-30s %-20s\n\n", " ", "5£ for the combination")+
				String.format("%-10s %-20s\n", " ", "3 Drinks and 1 Food: ") +
				String.format("%-30s %-20s\n\n", " ", "20% off for the combination")+
				String.format("%-10s %-20s\n", " ", "3 Pastry: ") +
				String.format("%-30s %-20s\n\n", " ", "25% off for the combination");
		return output;
	}
	
	/**
	 * GUI initializer 
	 */
	public void initializeGUI(){
		frame.setSize(width, height);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // makes program stop on exit
		
		setupListener();
		label.setBounds(10, 10, 300, 60);
		newCust.setBounds(250, 20, 150, 40);
		nameLabel.setBounds(450, 10, 150, 20);
		nameCust.setBounds(450, 30, 150, 30);
		drink.setBounds(50, 85, 150, 40);
		food.setBounds(250, 85, 150, 40);
		pastry.setBounds(450, 85, 150, 40);
		menuItem.setBounds(50, 150, 550, 250);
		
		removeItem.setBounds(50, 425, 150, 40);
		addItem.setBounds(250, 425, 150, 40);
		quantity.setBounds(450, 425, 150, 40);
		
		description.setBounds(50, 490, 550, 33);
		descriptionSum.setBounds(50, 545, 300, 200);
		
		purchase.setBounds(400, 545, 200, 47);
		receipt.setBounds(400, 617, 200, 47);
		endOfDay.setBounds(400, 689, 200, 47);
		
		orderList.setBounds(650, 20, 250, 250);
		receipView.setBounds(650, 320, 250, 350);
		reset.setBounds(650, 690, 250, 47);
		summaryView.setBounds(925, 20, 250, 715);
		
		orderList.setEditable(false);
		receipView.setEditable(false);
		summaryView.setEditable(false);
		description.setEditable(false);
		descriptionSum.setEditable(false);
		
		label.setText("Coffee Shop");
		Font  f2 = new Font(Font.SANS_SERIF, Font.BOLD, 35);
		label.setFont(f2);
	}
	
	/**
	 * Adds elements to the GUI 
	 */
	public void paintScreen() {
		frame.getContentPane().add(newCust);
		frame.getContentPane().add(nameCust);
		frame.getContentPane().add(drink);
		frame.getContentPane().add(food);
		frame.getContentPane().add(pastry);
		frame.getContentPane().add(menuItem);
		frame.getContentPane().add(removeItem);
		frame.getContentPane().add(addItem);
		frame.getContentPane().add(purchase);
		frame.getContentPane().add(quantity);
		frame.getContentPane().add(description);
		frame.getContentPane().add(descriptionSum);
		frame.getContentPane().add(receipt);
		frame.getContentPane().add(endOfDay);
		frame.getContentPane().add(orderList);
		frame.getContentPane().add(receipView);
		frame.getContentPane().add(summaryView);
		frame.getContentPane().add(reset);
		frame.getContentPane().add(label);
		frame.getContentPane().add(nameLabel);
	}
	
	/**
	 * Resets GUI to initial state 
    */
	private void reset() {
		nameCust.setText(" ");
		description.setText("Product Description:");
		menuItem.setModel(listModel);
		orderList.setText("Order list:");
		receipView.setText("Customer Receipt:"); 
		summaryView.setText("Caffee end of the day summary:"); 
		purchase.setEnabled(false);
		receipt.setEnabled(false);
		addItem.setEnabled(false);
		removeItem.setEnabled(false);
		newCust.setEnabled(true);
    }
	
	/**
	 * Set up once new customer is established
    */
	private void set() {
		addItem.setEnabled(true);
		removeItem.setEnabled(true);
		pastry.setEnabled(true);
		purchase.setEnabled(true);
	}
	
	/**
	 * Sets up action listeners for all elements in the GUI 
	 */
	private void setupListener() {
		Display(); // Will display item description when item is chosen 
		
		ActionListener Listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// handles creation of new customer 
				if (e.getSource() == newCust) {
					String name = nameCust.getText(); 
					if(name.trim().isEmpty()){ //ERROR HANDLING if the text field is empty
					    JOptionPane.showMessageDialog(null,
					        "Error you did not enter a customer's name, please try again.", 
					         "Error", JOptionPane.ERROR_MESSAGE);
					} else {
						set();	
						cashier.createNewCustomer(name);
						currentCustomer = cashier.currentCustomer;
						newCust.setEnabled(false);
					}		
				// if the categories are clicked on, display items in category 
				} if (e.getSource() == drink) {
			        menuItem.setModel(drinkCategory);
				} if (e.getSource() == food) {
					menuItem.setModel(foodCategory);
				} if (e.getSource() == pastry) {
					menuItem.setModel(pastryCategory);
				} if (e.getSource() == addItem) {
					// if add item button is clicked 
					String selectedItem = (String) menuItem.getSelectedValue();
					selectedItem = selectedItem.substring(0, 20).trim();
					int itemQuantaty = (Integer)quantity.getValue();     // From the 
					LocalDateTime timeStamp = LocalDateTime.now();
					try {
						currentCustomer.addItem(selectedItem, itemQuantaty, timeStamp);
						displayOrder();
					} catch (InvalidMenuItemQuantityException | InvalidMenuItemDataException e1) {
						e1.printStackTrace();
					}
					// if remove item button is clicked 
				} if (e.getSource() == removeItem) {
					String selectedItem = (String) menuItem.getSelectedValue();
					selectedItem = selectedItem.substring(0, 20).trim();
					int itemQuantaty = (Integer)quantity.getValue();
					try {
						currentCustomer.removeItem(selectedItem, itemQuantaty);
						displayOrder();
					} catch (NoMatchingMenuItemIDException | InvalidMenuItemQuantityException
							| InvalidMenuItemDataException | InvalidCartItemException e1) {
						e1.printStackTrace();
					}
					// if purchase button is clicked 
				} if (e.getSource() == purchase) {  
					if(currentCustomer.cart.size() == 0){ // if empty 
					    int reply = JOptionPane.showConfirmDialog(null,
					        "The current customer dosent have anything in his cart. Do you wish to prosseed?", 
					       "Warning", JOptionPane.YES_NO_OPTION);
					    if (reply == JOptionPane.YES_OPTION) {
					    	addItem.setEnabled(false);
							removeItem.setEnabled(false);
							receipView.setText(cashier.generateReceipt());
						} 
					} else {
						addItem.setEnabled(false);
						removeItem.setEnabled(false);
						receipView.setText(cashier.generateReceipt());
					}
					receipt.setEnabled(true);
				
				// if receipt button is clicked
				} if (e.getSource() == receipt) {
					System.out.println("report");
					cashier.generateCustomerReport();
					
				// if end of day report button is clicked 
				} if (e.getSource() == endOfDay) {
					
					String report = cashier.generateFinalReport();
					summaryView.setText(report);
					cashier.generateFinalReportFile();
				} if (e.getSource() == reset) {
					reset();
				}
			}
		};
		
		newCust.addActionListener(Listener);
		food.addActionListener(Listener);
		drink.addActionListener(Listener);
		pastry.addActionListener(Listener);
		addItem.addActionListener(Listener);
		removeItem.addActionListener(Listener);
		purchase.addActionListener(Listener);
		receipt.addActionListener(Listener);
		endOfDay.addActionListener(Listener);
		reset.addActionListener(Listener);
	}
    
	/**
	 * Display the list of orders current customer has established
	 */
    private void displayOrder() {
    	String output = "Order list: \n\n";
    	Set<String> cartSet = currentCustomer.cart.keySet(); 
    	// gets the customer cart and displays the orders of the current customer 
		for (String orderID: cartSet) {
			output += String.format("%-10s %-30s %-20s\n", " ",CoffeeShop.menu.get(orderID).getName(), 
					String.valueOf(currentCustomer.cart.get(orderID).size() + "x"));
		}
    	orderList.setText(output);
    }
    
	/**
	 * Goes through the menu map and inserts the item to the given category of DefaultListModel 
	 * Which will be inserted to menuItem JList
	 */
    private void InitialiseCategoryList() {
    	for (Map.Entry m: CoffeeShop.menu.entrySet()) {  // Go though each entry of menu
   		 	 MenuItem item = (MenuItem) m.getValue();     // Get the value menu item
   		 	 
    		 String category = item.getCategory();        // Get category 
    		 String display = String.format("%-40s %-4s", item.getName(), String.valueOf(item.getCost() + "£")); // Format the display
    		 // Depending in the category inserts item 
    		 if(category.equals("Drink")) { 
    			 drinkCategory.addElement(display);
    		 } else if (category.equals("Pastry")) {
    			 pastryCategory.addElement(display);
    		 } else if (category.equals("Food")) {
    			foodCategory.addElement(display);
    		 }
    	 }
    }
    
    /**
	 * When menu item is selected its description is showed and value is set to 1 
	 */
    private void Display() {
    	menuItem.addMouseListener(new MouseAdapter()  {
		      @Override
		      public void mouseClicked(MouseEvent e) {
		    	  String selectedItem = (String) menuItem.getSelectedValue(); // Get the name
		    	  selectedItem = selectedItem.substring(0, 20).trim();  // Trim it 
		    	  MenuItem item = CoffeeShop.menu.get(selectedItem);    // Get the item from the coffeeshop 
		    	  String product = "Product Description: \n";           
		    	  description.setText(product + item.getDescription());   // Set description
		    	  quantity.setValue(1); // Reset the quantity to 1 upon pressing to new object
		      }
    	});
    }
}
    

