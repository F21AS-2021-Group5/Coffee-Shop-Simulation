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


public class NewGUI{
	
	//Width and height of the window
	//Components are scaled to width and height
	int width = 600;
	int height = 600;
	
	JFrame frame;
	Customer currentCustomer;
	JTextArea customerlist = new JTextArea("Customer Queue:");
	JTextArea onlineCustomerlist = new JTextArea("Online Customer List");
	JTextArea orders = new JTextArea("Order List");
	JTextArea cashiers = new JTextArea("Active Cashiers:");
	JTextArea baristas = new JTextArea("Active Baristas:");
	JTextArea cooks = new JTextArea("Active Cooks:");

	JTextArea temp = new JTextArea("temp");
	JPanel cashier = new JPanel();
	JTextField removeCashier = new JTextField(3);
	JTextField removeBarista = new JTextField(3);
	JTextField removeCook = new JTextField(3);
	
	
	// Buttons
	JButton addCashier = new JButton("Add Cashier");
	JButton addBarista = new JButton("Add Barista");
	JButton addCook = new JButton("Add Cook");
	JButton removecashiers = new JButton("Remove Cashier");
	JButton removebaristas = new JButton("Remove Barista");
	JButton removecooks = new JButton("Remove Cook");
	
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
		
		addCashier.setBounds(50, 155, 120, 40);
		addBarista.setBounds(50, 205, 120, 40);
		addCook.setBounds(50, 255, 120, 40);
		removecashiers.setBounds(200, 155, 180, 40);
		removebaristas.setBounds(200, 205, 180, 40);
		removecooks.setBounds(200, 255, 180, 40);
		removeCashier.setBounds(400, 155, 120, 40);
		removeBarista.setBounds(400, 205, 120, 40);
		removeCook.setBounds(400, 255, 120, 40);
		frame.getContentPane().add(addCashier);
		frame.getContentPane().add(addBarista);
		frame.getContentPane().add(addCook);
		frame.getContentPane().add(removecashiers);
		frame.getContentPane().add(removebaristas);
		frame.getContentPane().add(removecooks);		
		frame.getContentPane().add(removeCashier);
		frame.getContentPane().add(removeBarista);
		frame.getContentPane().add(removeCook);		
		
	}
	

	
	public synchronized void DisplayCustomers() {
		//Clear customer list
		customerlist.removeAll();
		//Size is 1/4 of the height of the frame and half the height
		
		int customerListWidth = width/2;
		int customerListHeight = height / 4;
		customerlist.setBounds(0, 0, customerListWidth, customerListHeight);
		frame.getContentPane().add(customerlist);
		
		
		//customerlist.setText("Customer Queue:");
		
	}
	
	public synchronized void DisplayOnlineCustomers() {
		onlineCustomerlist.removeAll();
		int onlineCustomerListWidth = width/2;
		int onlineCustomerListHeight = height / 4;
		onlineCustomerlist.setBounds(onlineCustomerListWidth, 0, onlineCustomerListWidth, onlineCustomerListHeight);
		
		frame.getContentPane().add(onlineCustomerlist);

		//onlineCustomerlist.setText("online queue:");
	}
	
	public void DisplayCashiers() {
		int cashierWidth = width;
		int cashierHeight = height / 8;
		cashiers.setBounds(0, height/2, cashierWidth, cashierHeight);
		
		//System.out.println(customerQueue.customerNames);
		
		frame.getContentPane().add(cashiers);
		//cashiers.setText("Active Cashiers:");
	}
	
	public void DisplayBaristas() {
		int baristaWidth = width;
		int baristaHeight = height / 8;
		baristas.setBounds(0, height/8*5, baristaWidth, baristaHeight);
				
		frame.getContentPane().add(baristas);
		//baristas.setText("Active Baristas:");
	}
	
	public void DisplayCooks() {
		int cookWidth = width;
		int cookHeight = height / 8;
		cooks.setBounds(0, height/4*3, cookWidth, cookHeight);
				
		frame.getContentPane().add(cooks);
		//cooks.setText("Active Cooks:");
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

		temp.setBounds(width, height, 0, 0);
		
		frame.getContentPane().add(temp);
		//temp.setText("");
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
		//DisplayOnlineCustomers();
		DisplayCashiers();
		DisplayBaristas();
		DisplayCooks();
		//DisplayOrders();
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
