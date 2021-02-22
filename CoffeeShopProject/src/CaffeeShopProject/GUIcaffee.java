package CaffeeShopProject;

import javax.swing.*;  
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import java.util.List;

public class GUIcaffee {
	
	int width = 1200;
	int height = 800;
	
	double fees;
	boolean feePaid;
	
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
	JList summaryView = new JList(); //data
	
	
	public GUIcaffee() {
		
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
	
    public static void main(String[] args) {  
		
    	GUIcaffee GUI = new GUIcaffee();
		GUI.initializeGUI(); 
		GUI.paintScreen();
	}  

}
