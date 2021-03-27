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
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import javax.swing.*;

//import CaffeeShopProject.CoffeeShop;


public class NewGUI{
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
		
	//JList
	JList shopcustomerlist = new JList();
	JList onlinecustomerlist = new JList();
	JList cashierlist = new JList();
	JList cooklist = new JList();
	JList baristalist = new JList();

	//Text Filed (Enter names)
	JTextField cashierTime = new JTextField(3);
	JTextField baristaTime = new JTextField(3);
	JTextField cookTime = new JTextField(3);	
	
	Customer currentCustomer;
	CoffeeShop coffeeshop;
	Employees employees;
	NewCustomerQueue newCustomerQueue;
	private Queue<Customer> queue;
	
	//Min & Max value for delay
	int min = 200;
	int max = 2000;
	
	//GUI constructor
	public NewGUI(CoffeeShop main) {
		this.coffeeshop = main;
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
				//Create new cashier
				if (e.getSource() == addCashier) {
					
				}
				//Create new cook
				if (e.getSource() == addCook) {
					
				}
				//Create new barista
				if (e.getSource() == addBarista) {
					
				}
				//Remove selected cashier
				if (e.getSource() == removecashiers) {
					String selectedcashier = (String) cashierlist.getSelectedValue();
					selectedcashier = selectedcashier.substring(0, 60).trim(); //remove string from list
				}
				//Remove selected cook
				if (e.getSource() == removecooks) {
					String selectedcook = (String) cooklist.getSelectedValue();
					selectedcook = selectedcook.substring(0, 60).trim(); //remove string from list
				}
				//Remove selected barista
				if (e.getSource() == removebaristas) {
					String selectedbarista = (String) baristalist.getSelectedValue();
					selectedbarista = selectedbarista.substring(0, 60).trim(); //remove string from list
				}
				//Set Delay Time for selected cashier
				if (e.getSource() == cashierTimeOK) {
					String selectedcashier = (String) cashierlist.getSelectedValue();
					String time = cashierTime.getText();
					int itime = Integer.parseInt(time);
					if(time.trim().isEmpty()) {	//If delay time empty, error
						JOptionPane.showMessageDialog(null,
						        "Error: you did not enter a delay time for cashier, please enter a delay time.", 
						         "Error", JOptionPane.ERROR_MESSAGE);
					}
					else if(itime<200 || itime>2000) {
						JOptionPane.showMessageDialog(null,
						        "Error: you did not enter a valid delay time, please enter a delay time between 200 and 2000.", 
						         "Error", JOptionPane.ERROR_MESSAGE); //error message
						cashierTime.setText("");	//empty text box
					}
					else {
						//change delay time for selected cashier
					}
					
				}
				//Set Delay Time for selected cook
				if (e.getSource() == cookTimeOK) {
					String selectedcook = (String) cooklist.getSelectedValue();
					String time = cookTime.getText();
					int itime = Integer.parseInt(time);
					if(time.trim().isEmpty()) {	//If delay time empty, error
						JOptionPane.showMessageDialog(null,
						        "Error: you did not enter a delay time for cook, please enter a delay time.", 
						         "Error", JOptionPane.ERROR_MESSAGE);
					}
					else if(itime<200 || itime>2000) {
						JOptionPane.showMessageDialog(null,
						        "Error: you did not enter a valid delay time, please enter a delay time between 200 and 2000.", 
						         "Error", JOptionPane.ERROR_MESSAGE);
						cookTime.setText("");	//empty text box
					}
					else {
						//change delay time for selected cook
					}
					
				}
				//Set Delay Time for selected barista
				if (e.getSource() == baristaTimeOK) {
					String selectedbarista = (String) baristalist.getSelectedValue();
					String time = baristaTime.getText();
					int itime = Integer.parseInt(time);
					if(time.trim().isEmpty()) {	//If delay time empty, error
						JOptionPane.showMessageDialog(null,
						        "Error: you did not enter a delay time for barista, please enter a delay time.", 
						         "Error", JOptionPane.ERROR_MESSAGE);
					}
					else if(itime<200 || itime>2000) {
						JOptionPane.showMessageDialog(null,
						        "Error: you did not enter a valid delay time, please enter a delay time between 200 and 2000.", 
						         "Error", JOptionPane.ERROR_MESSAGE);
						baristaTime.setText("");	//empty text box
					}
					else {
						//change delay time for selected barista
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
    private void displayCustomer() {
    	//Display their name + delay time
    	newCustomerQueue.getQueue();
    }

	/**
	 * Display the list of existent cashier
	 */
    private void displayCashier() {
    	//Display their name + delay time
    }
    
	/**
	 * Display the list of existent cook
	 */
    private void displayCook() {
    	//Display their name + delay time
    }
    
	/**
	 * Display the list of existent barista
	 */
    private void displayBarista() {
    	//Display their name + delay time
    }
	
	
	//While GUI is running, keep updating
	public void run() {
		while(true) {
			//update();
			try {
				Thread.sleep(100);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}