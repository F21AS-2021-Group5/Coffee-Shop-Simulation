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
	Customer currentCustomer;
	JTextArea customerlist = new JTextArea("Customer List");
	JTextArea orders = new JTextArea("Order List");
	JPanel cashier = new JPanel();
	
	CoffeeShop main;
	private Queue<Customer> queue;
	
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
		
		frame.repaint();
	}
	
	public synchronized void DisplayCustomers() {
		//Clear customer list
		customerlist.removeAll();
		
		//Size is 1/4 of the width of the frame and same height
		int customerListWidth = width;
		int customerListHeight = height / 4;
		customerlist.setBounds(0, 0, customerListWidth, customerListHeight);
		
		
		
	}
	
	public synchronized void DisplayOnlineCustomers() {
		
	}
	
	public void DisplayCashiers() {
		
	}
	
	public void DisplayOrders() {
		int orderWidth = width;
		int orderHeight = height/4;
		orders.setBounds(0, 2 * height / 4, orderWidth, orderHeight);
		//if cashier has a customer (currentCustomer not null)
		// Orders cashierDisp = get Cashier ID handling current customer
		// orders.setText 
		orders.setVisible(true);
		frame.add(orders);
		
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
