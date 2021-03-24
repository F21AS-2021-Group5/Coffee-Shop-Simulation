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
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import javax.swing.*;


public class NewGUI extends Thread{
	
	//Width and height of the window
	//Components are scaled to width and height
	int width = 600;
	int height = 600;
	
	JFrame frame;
	private static Customer currentCustomer;
	JTextArea customerlist = new JTextArea("Customer List");
	JTextArea onlineCustomerlist = new JTextArea("Online Customer List");
	JTextArea orders = new JTextArea("Order List");
	JTextArea temp = new JTextArea("temp");
	JPanel cashier = new JPanel();
	
	CoffeeShop main;
	CustomerQueue customerQueue;
	private Queue<Customer> queue;
	
	String CustomerName;
	String Items;
	int queueSize;
	//GUI constructor
	public NewGUI(CoffeeShop main) {
		this.main = main;
	}
	
	//Create and show GUI
	public void DisplayGUI() {
		//Create GUI
		frame = new JFrame();
		frame.setSize(width, height);
		//Show GUI
		frame.setVisible(true);
		//Close when Exit
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	

	
	public synchronized void DisplayCustomers() {
		//Clear customer list
		customerlist.removeAll();
		//Size is 1/4 of the height of the frame and half the height
		int customerListWidth = width/2;
		int customerListHeight = height / 4;
		customerlist.setBounds(10, 0, customerListWidth, customerListHeight);
		
		
		
		frame.getContentPane().add(customerlist);
		customerlist.setText("queue:");
		customerlist.append("\n Name:  ");
	}
	
	public synchronized void DisplayOnlineCustomers() {
		onlineCustomerlist.removeAll();
		
		int onlineCustomerListWidth = width/2;
		int onlineCustomerListHeight = height / 4;
		onlineCustomerlist.setBounds(width/2, 0, onlineCustomerListWidth, onlineCustomerListHeight);
		
		frame.getContentPane().add(onlineCustomerlist);
		onlineCustomerlist.setText("online queue:");
	}
	
	public void DisplayCashiers() {
		int cashierWidth = width/2;
		int cashierHeight = height / 4;
		customerlist.setBounds(0, cashierHeight*2, cashierWidth, cashierHeight);
		
		
		CustomerName = main.cashier.currentCustomer.getName();
		Items = main.cashier.currentCustomer.cart.toString();
		
		System.out.println(main.cashier.currentCustomer.getCartTotalPrice());
		
		frame.getContentPane().add(customerlist);
		customerlist.setText("Current Customer:");
		customerlist.append("\n Name:  ");
		customerlist.append(CustomerName);
		customerlist.append("\n Items:  ");
		for (String key : main.cashier.currentCustomer.cart.keySet()) {
            customerlist.append(key + "\n");
            //System.out.println(key);
        }
		customerlist.append("\n Total Price:  ");
		customerlist.append(String.valueOf(main.cashier.currentCustomer.getCartTotalPrice()));
	}
	
	public void DisplayOrders() {
		int orderWidth = width;
		int orderHeight = height/4;
		orders.setBounds(0, height/4, orderWidth, orderHeight);
		//if cashier has a customer (currentCustomer not null)
		// Orders cashierDisp = get Cashier ID handling current customer
		// orders.setText 
		//frame.getContentPane().add(orders);
		orders.setText("orders:");
		//orders.append("orders:");
		orders.setVisible(true);
		frame.add(orders);
		
	}
	
	public synchronized void DisplayTemp() {		
		int onlineCustomerListWidth = width;
		int onlineCustomerListHeight = height / 4;
		temp.setBounds(width, height, onlineCustomerListWidth, onlineCustomerListHeight);
		
		frame.getContentPane().add(temp);
		temp.setText("queue:");
	}
	
	
	void CreateCashierDisplay(Cashier cash, int totalCashier, int currentCashier) {
		int cashierWidth = width / totalCashier;
		int cashierHeight = height / 4;
		JTextArea cashierText = new JTextArea();
		cashierText.setName("Cashier" + currentCashier);
		cashierText.setBounds((cashierWidth*currentCashier) - cashierWidth, height / 4, cashierWidth, cashierHeight);
		//if cashier has a customer
		// cashierText.setText(cashier.currentCustomer.getName() + "ordered" + cashier.currentCustomer.order() + ".\ price of" + cashier.currentCustomer.orderCost()); 
		
		cashierText.setVisible(true);
		cashier.add(cashierText);
	}
	
	//Update GUI with new information
	public void update() {
		DisplayCustomers();
		DisplayOnlineCustomers();
		DisplayCashiers();
		DisplayOrders();
		DisplayTemp();
		
		frame.repaint();
	}
	
	//While GUI is running, keep updating
	public void run() {
		while(true) {
			update();
			try {
				Thread.sleep(100);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
