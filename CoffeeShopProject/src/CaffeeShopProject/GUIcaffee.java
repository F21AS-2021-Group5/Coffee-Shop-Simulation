package CaffeeShopProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

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

public class GUIcaffee {
	
	int width = 1200;
	int height = 800;
	
	double fees;
	boolean feePaid;
	
	private int itemQuantity;
	HashMap<String, LocalDateTime> cart;
	
	JFrame frame = new JFrame("GUI");
	JButton newCust = new JButton("New Customer");
	JTextField nameCust = new JTextField(20);
	JButton drink = new JButton("Drink");
	JButton food = new JButton("Food");
	JButton pastry = new JButton("Pastry");
	JList menuItem = new JList(); //data
	JButton removeItem = new JButton("Remove");
	JButton addItem = new JButton("Add");
	JButton purchase = new JButton("Purchase");
	SpinnerModel quantityModel  = new SpinnerNumberModel(1, 0,100,1);
	JSpinner quantity = new JSpinner(quantityModel);
	JTextArea description = new JTextArea();
	JTextArea descriptionSum = new JTextArea();
	JButton receipt = new JButton("Generate Receipt");
	JButton endOfDay = new JButton("End of day report");
	
	JList orderList = new JList(); //data
	JList receipView = new JList(); //data
	JList summaryView = new JList(); //new JList(); //data
	
	BufferedReader lector = null;
    DefaultListModel lista = new DefaultListModel();
    File finalReport = new File("finalReport.txt");
    
    BufferedReader lect = null;
    DefaultListModel lis = new DefaultListModel();
    File custReport = new File("customerReport.txt");
	
	Customer customer;
	MenuItem menu_item;
	CoffeeShop coffee;
	Cashier cashier;
	
	
	public GUIcaffee() {
		//addWindowListener(this);
		//newCust.addActionListener(this);
		
		
	}
	
	public void initializeGUI(){
		frame.setSize(width, height);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // makes program stop on exit
		
		newCust.setBounds(250, 20, 150, 40);
		nameCust.setBounds(450, 20, 150, 40);
		drink.setBounds(50, 85, 150, 40);
		food.setBounds(250, 85, 150, 40);
		pastry.setBounds(450, 85, 150, 40);
		menuItem.setBounds(50, 150, 550, 250);
		
		removeItem.setBounds(50, 425, 150, 40);
		addItem.setBounds(250, 425, 150, 40);
		quantity.setBounds(450, 425, 150, 40);
		
		description.setBounds(50, 490, 550, 30);
		
		descriptionSum.setBounds(50, 545, 300, 200);
		purchase.setBounds(400, 545, 200, 47);
		receipt.setBounds(400, 617, 200, 47);
		endOfDay.setBounds(400, 689, 200, 47);
		
		orderList.setBounds(650, 20, 225, 300);
		receipView.setBounds(650, 370, 225, 350);
		summaryView.setBounds(925, 20, 225, 700);
		
		
	}
	
	public void paintScreen() {
		frame.getContentPane().add(newCust);
		frame.getContentPane().add(nameCust);
		frame.getContentPane().add(food);
		frame.getContentPane().add(drink);
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
	}
	
    public void main(String[] args) {  
		
    	GUIcaffee GUI = new GUIcaffee();
		GUI.initializeGUI(); 
		GUI.paintScreen();
		
		DisplayCustomer();
		SelectCategory();
		DisplayOrder();
		AddItem();
		RemoveItem();
		DisplayItemDescription();
		DisplayReceipt();
		DisplayFinalReport();
	}  
    
    // Display Current Customer and their ID
    private void DisplayCustomer() {
    	//Add action to button New Customer
    	newCust.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			//Get ID from customer class
    			nameCust.setText(customer.getId());
    		}
    	}); 
    }
    
    // Select category and fill list of items from category chosen
    private void SelectCategory() {
    	
		int iCount = coffee.menu.size(); // go through size of the menu
		menuItem.removeAll();	//Empty list first

		//Pressing Drink button fill menuItem list
    	drink.addActionListener(new ActionListener() {	//when drink pressed
    		public void actionPerformed(ActionEvent e) {
    			for (int i = 0; i < iCount; i++) {	// go through menu
    				if (menu_item.getCategory() == "Drink") {	//if menu item is drink
    					Object[] data = {menu_item.getName()};	//fill in list
    					menuItem = new JList(data);		//display in JList menuItem
    				}
    			}
    		}
    	}); 
    	
    	//Pressing Food button fill menuItem list
    	food.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			for (int i = 0; i < iCount; i++) {
    				if (menu_item.getCategory() == "Food") {	//if menu item is food
    					Object[] data = {menu_item.getName()};	//fill in list
    					menuItem = new JList(data);		//display in JList menuItem
    				}
    			}
    			
    		}
    	});
    	
    	//Pressing Pastry button fill menuItem list
    	pastry.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			for (int i = 0; i < iCount; i++) {
    				if (menu_item.getCategory() == "Pastry") {
    					Object[] data = {menu_item.getName()};	//fill in list
    					menuItem = new JList(data);		//display in JList menuItem
    				}
    			}
    		}
    	}); 
    }
    
    //add to cart Item ID and time stamp, for quantity of order 
    private void AddItem() {
		// Quantity of the Item selected
		itemQuantity = quantity.getComponentCount();
		LocalDateTime time = customer.timestamp;
		
		addItem.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			for(int i = 0; i< coffee.menu.size(); i ++) {
    				if(menuItem.getSelectedValue().toString() == menu_item.getName()) {
    					for (int j = 0; i < itemQuantity; i++) {
    						cart.put(menu_item.getIdentifier(), time); //add item to cart number of time it was selected
    					}
    						
    				}
    			}
    			
    		}
    	}); 
    }
    
    //Remove item from cart
    private void RemoveItem() {
    	removeItem.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			for(int i = 0; i< coffee.menu.size(); i ++) {
    				if(menuItem.getSelectedValue().toString() == menu_item.getName()) {
    					cart.remove(menu_item.getIdentifier()); //remove item from cart			
    				}
    			}
    			
    		}
    	});
	}
	
    //Pass list to customer class
	private void Purchase() {
		purchase.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
//    			cart.entrySet().forEach(null);
//    			customer.setCart(cart.ge);
    			
    		}
    	}); 
	}
	
	// Display description of item chosen
	private void DisplayItemDescription() {
		//get the description of the item selected
		for(int i = 0; i< coffee.menu.size(); i ++) {
			if(menuItem.getSelectedValue().toString() == menu_item.getName()) {
				description.setText(menu_item.getDescription());	// Display description
			}
		}
	}
		
	// Display Price
	private void DisplayPrice() {
		descriptionSum.setText("Tax  " + cashier.getCartTax() +
								"\"Subtotal  \" + cashier.getCartSubtotalPrice()" +
								"Total  " + customer.getCartTotalPrice());
	}
		
	// Display List of Current Order from Current Customer
	private void DisplayOrder() {
		Object[] order = {cart.keySet()};	//get list of item names added
		orderList = new JList(order);		//display in JList orderList
	}
		
		
	// Display Receipt of Current Customer
	private void DisplayReceipt() {
		try {
			lect = new BufferedReader(new FileReader(custReport));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
           String texta = null;
            try {
			while ((texta = lect.readLine()) != null) {	//while there are still lines
			    lis.addElement(texta);	//add the lines to listModel
				}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
           orderList.setModel(lis);	//show receipt in JList
	}
	
	// Display Final Report of Coffee Shop
	private void DisplayFinalReport() {
		endOfDay.addActionListener(new ActionListener() {
	   		public void actionPerformed(ActionEvent e) {
	   			try {
					lector = new BufferedReader(new FileReader(finalReport));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	               String texta = null;
	                try {
					while ((texta = lector.readLine()) != null) {	//while there are still lines
					    lista.addElement(texta);	//add the lines to listModel
       
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    			summaryView.setModel(lista);	//show report in JList
    		}
    	}); 
	}
    

}
    
