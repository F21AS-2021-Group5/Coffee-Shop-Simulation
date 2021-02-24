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
import java.util.Map;
import java.util.Set;

public class GUIcaffee {
	
	private String CreateInitialText() {
		String output = String.format("%-20s \n\n", "Discounts when buying") + 
				String.format("%-10s %-20s\n", " ", "3 Drinks and 1 Food: ") +
				String.format("%-30s %-20s\n\n", " ", "20% off for the combination")+
				String.format("%-10s %-20s\n", " ", "3 Pastry: ") +
				String.format("%-30s %-20s\n\n", " ", "25% off for the combination")+
				String.format("%-10s %-20s\n", " ", "1 Drinks, 1 Food, 1 Pastry")+
				String.format("%-30s %-20s\n\n", " ", "6£ for the combination");
		return output;
	}
	
	int width = 1200;
	int height = 800;
	
	double fees;
	boolean feePaid;
	
	private int itemQuantity;
	HashMap<String, LocalDateTime> cart;
	
	JFrame frame = new JFrame("GUI");
	JButton newCust = new JButton("New Customer");
	JButton drink = new JButton("Drink");
	JButton food = new JButton("Food");
	JButton pastry = new JButton("Pastry");
	JTextField nameCust = new JTextField(20);
	
	DefaultListModel listModel=new DefaultListModel();
	JList menuItem =new JList();
	
	JButton removeItem = new JButton("Remove");
	JButton addItem = new JButton("Add");
	JButton purchase = new JButton("Purchase");
	SpinnerModel quantityModel  = new SpinnerNumberModel(1, 0,100,1);
	JSpinner quantity = new JSpinner(quantityModel);
	
	JTextArea description = new JTextArea("Product Description:");
	JTextArea descriptionSum = new JTextArea(CreateInitialText());
	
	JButton receipt = new JButton("Generate Receipt");
	JButton endOfDay = new JButton("End of day report");
	
	JTextArea orderList = new JTextArea("Order list:"); 
	JTextArea receipView = new JTextArea("Customer Receipt: "); 
	JTextArea summaryView = new JTextArea("Caffee end of the day summary: "); 
	
	
	private static Customer currentCustomer;
	
	private DefaultListModel drinkCategory = new DefaultListModel();
	private DefaultListModel foodCategory = new DefaultListModel(); 
	private DefaultListModel pastryCategory = new DefaultListModel();
	
	CoffeeShop shop = new CoffeeShop("MenuItems");
	
	
	public GUIcaffee() {
		
		//addWindowListener(this);
		//newCust.addActionListener(this);

	}
	
	public void initializeGUI(){
		frame.setSize(width, height);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // makes program stop on exit
		
		setupListener();
		
		newCust.setBounds(250, 20, 150, 40);
		nameCust.setBounds(450, 20, 150, 40);
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
		
		orderList.setBounds(650, 20, 225, 300);
		receipView.setBounds(650, 370, 225, 350);
		summaryView.setBounds(925, 20, 225, 700);
		
		orderList.setEditable(false);
		receipView.setEditable(false);
		summaryView.setEditable(false);
		description.setEditable(false);
		descriptionSum.setEditable(false);
		
		
	}
	
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
	}
	
	public void setUp() {
		DiscountDescription();
		Display();
	}
	
	private void setupListener() {
		Display();
		//DiscountDescription();
		initialiseCategoryList();
		ActionListener Listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == newCust) {
					String name = nameCust.getText(); //need to define invalid names or something
	    			createNewCustomer(name); 
				}if (e.getSource() == drink) {
			        menuItem.setModel(drinkCategory);
				}if (e.getSource() == food) {
					menuItem.setModel(foodCategory);
				}if (e.getSource() == pastry) {
					menuItem.setModel(pastryCategory);
				}if (e.getSource() == addItem) {
					String selectedItem = (String) menuItem.getSelectedValue();
					selectedItem = selectedItem.substring(0, 20).trim();
					int itemQuantaty = (Integer)quantity.getValue();
					LocalDateTime timeStamp = LocalDateTime.now();
					try {
						currentCustomer.addItem(selectedItem, itemQuantaty, timeStamp);
						displayOrder();
					} catch (InvalidMenuItemQuantityException | InvalidMenuItemDataException e1) {
						e1.printStackTrace();
					}
				}if (e.getSource() == removeItem) {
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
				}if (e.getSource() == purchase) {
			  	    CoffeeShop.customerList.put(currentCustomer.getId(), currentCustomer);
					shop.GenerateCustomerReport(currentCustomer.getId());
					
				}if (e.getSource() == endOfDay) {
					String report  = shop.GenerateFinalReportDisplay();
					summaryView.setText(report);
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
		endOfDay.addActionListener(Listener);
	}
    
	
    public void createNewCustomer(String name) {
 	   LocalDateTime timeStamp = LocalDateTime.now();
 	   Customer newCustomer = new Customer(name, timeStamp);
 	   currentCustomer = newCustomer;
    }
    
    private void DiscountDescription() {

  	    description.setText("Product Description:");
  	    orderList.setText("Order list:");
    }
    
    private void displayOrder() {
    	orderList.setText("");
    	String output = "Order list: \n\n";
    	Set<String> cartSet = currentCustomer.cart.keySet();
		for (String orderID: cartSet) {
			output += String.format("%-10s %-30s %-20s\n", " ",CoffeeShop.menu.get(orderID).getName(), 
					String.valueOf(currentCustomer.cart.get(orderID).size() + "x"));
		}
    	orderList.setText(output);
    }
    
    private void initialiseCategoryList() {
    	for (Map.Entry m : CoffeeShop.menu.entrySet()) { 
   		 	 MenuItem item = (MenuItem) m.getValue();
   		 	 
    		 String category = item.getCategory();
    		 String display = String.format("%-40s %-4s", item.getName(),String.valueOf(item.getCost() + "£"));
    		 
    		 if(category.equals("Drink")) {
    			 drinkCategory.addElement(display);
    		 } else if (category.equals( "Pastry")) {
    			 pastryCategory.addElement(display);
    		 } else if (category.equals("Food")) {
    			foodCategory.addElement(display);
    		 }
    	 }
    }
    
    private void Display() {
    	menuItem.addMouseListener(new MouseAdapter()  {
		      @Override
		      public void mouseClicked(MouseEvent e) {
		    	  String selectedItem = (String) menuItem.getSelectedValue();
		    	  selectedItem = selectedItem.substring(0, 20).trim();
		    	  MenuItem item = CoffeeShop.menu.get(selectedItem);
		    	  String product = "Product Description: \n";
		    	  description.setText(product+item.getDescription());
		      }
		    });
    }
    
    private void CustomerPurchase(){
    	String ID = currentCustomer.getId();
  	    CoffeeShop.customerList.put(ID, currentCustomer);
  	    
  	    String order = "Customer Receipt: \n\n";
  	    Set<String> cartSet = currentCustomer.cart.keySet();
		for (String orderID: cartSet) {
			order += String.format("%-10s %-30s %-20s\n", " ",CoffeeShop.menu.get(orderID).getName(), 
					String.valueOf(currentCustomer.cart.get(orderID).size() + "x"));
		}
		receipView.setText(order);
  	    
    }

}
    

