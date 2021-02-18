package CaffeeShopProject;

import java.awt.EventQueue;
import org.omg.PortableServer.THREAD_POLICY_ID;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import javax.swing.*;


public class CoffeeGUI{

	//Size of window
	int width = 600;
	int height = 400;
	
	private JFrame frame = new JFrame();
	
	Customer currentCustomer;
	MenuItem menuItem;
	
	//First panel
	JPanel menu = new JPanel();
	JButton newCustomer = new JButton();
	JTextArea customer = new JTextArea();
	JScrollPane menuList = new JScrollPane();
	JButton categories = new JButton();
	JTextArea description = new JTextArea();
	JTextArea price = new JTextArea();
    

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CoffeeGUI window = new CoffeeGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CoffeeGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
	}
	
private void DisplayCustomer() {
		
	}
	
	private void DisplayMenu() {
		
	}
	
	private void DisplayOrder() {
		
	
	}
	
	private void DisplayReceipt() {
		
		
	}
	
	private void DisplayFinalReport() {
		
	}
	
	private void SelectItem() {
		
	}
	
	private void SelectCategory() {
		
	}

}
