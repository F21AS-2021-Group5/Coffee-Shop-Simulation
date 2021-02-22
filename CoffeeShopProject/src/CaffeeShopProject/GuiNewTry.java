package CaffeeShopProject;

import javax.swing.*;  
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import java.util.List;

public class GuiNewTry {
	
	// we are using enum to control which 'scene' is displayed on the GUI.
	public enum Scene {
			CHECKIN, USERINFO, BAGCHECK, BAGS, ERROR1, ERROR2, RETURN, EXIT, FEES
	}
	
	static Scene scene = Scene.CHECKIN;

	// alterable width and height of the window. all components are scaled to width
	// and height
	int width = 1300;
	int height = 800;

	double fees;
	boolean feePaid;

	// declaring all swing objects
	JFrame frame = new JFrame("GUI");
	JButton checkIn = new JButton("Check In");
	JButton exit = new JButton("Exit");
	JButton noBags = new JButton("No Bag");
	JButton enterBagInfo = new JButton("Enter Bag Info");
	JButton enter = new JButton("Enter");
	JButton retry = new JButton("Retry");
	JButton confirm = new JButton("Confirm");
	JTextArea weightkgTag = new JTextArea("Bag weight (kg)");
	JTextArea error = new JTextArea();
	JTextArea bagxTag = new JTextArea("Bag width (cm)");
	JTextArea bagyTag = new JTextArea("Bag height (cm)");
	JTextArea bagzTag = new JTextArea("Bag depth (cm)");
	JTextArea surnameTag = new JTextArea("Passenger Surname:");
	JTextArea referenceTag = new JTextArea("Flight Reference:");
	JTextArea feeInfo = new JTextArea("The baggage fee for this flight is: " + fees);
	JButton acceptFee = new JButton("Accept Fee");
	JButton declineFee = new JButton("Decline Fee (remove baggage)");
	JTextField bagx = new JTextField();
	JTextField bagy = new JTextField();
	JTextField bagz = new JTextField();
	JTextField weightkg = new JTextField();
	static JTextField surname = new JTextField();
	static JTextField reference = new JTextField();
	static String surnameText;
	static String referenceText;
	static double bag_x;
	static double bag_y;
	static double bag_z;
	static double bag_kg;

	public GuiNewTry() {
	}

	/*
	 * Will generate the standard gui layout Home screen with no required input from
	 * user Just a logo or something
	 */
	public void createAndShowGUI() {

		frame.setSize(width, height);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // makes program stop on exit

		////setupListener();

		// set the bounds for all objects on screen. this determines where they are laid
		// on the JFrame.
		checkIn.setBounds(width / 4, height / 4, width / 2, height / 5);
		exit.setBounds(width / 4, 2 * height / 4, width / 2, height / 5);
		noBags.setBounds(width / 4, height / 4, width / 2, height / 5);
		enterBagInfo.setBounds(width / 4, 2 * height / 4, width / 2, height / 5);
		surname.setBounds(width / 4, height / 5, width / 2, height / 5 - 20);
		reference.setBounds(width / 4, 2 * height / 5, width / 2, height / 5 - 20);
		enter.setBounds(width / 4, 3 * height / 5, width / 2, height / 5 - 20);
		error.setBounds(width / 8, height / 4, 3 * width / 4, height / 8);
		retry.setBounds(width / 4, 2 * height / 4, width / 2, height / 5);
		surnameTag.setBounds(width / 4, height / 5 - 20, width / 2, height / 5 - 20);
		referenceTag.setBounds(width / 4, 2 * height / 5 - 20, width / 2, height / 5 - 20);
		bagxTag.setBounds(width / 4, 2 * height / 10 - 17, width / 4, height / 15);
		bagyTag.setBounds(width / 4, 3 * height / 10 - 17, width / 4, height / 15);
		bagzTag.setBounds(width / 4, 4 * height / 10 - 17, width / 4, height / 15);
		bagx.setBounds(width / 4, 2 * height / 10, width / 4, height / 15);
		bagy.setBounds(width / 4, 3 * height / 10, width / 4, height / 15);
		bagz.setBounds(width / 4, 4 * height / 10, width / 4, height / 15);
		weightkg.setBounds(2 * width / 4 + 20, 2 * height / 10, width / 4, height / 15);
		weightkgTag.setBounds(2 * width / 4 + 20, 2 * height / 10 - 17, width / 4, height / 15);
		confirm.setBounds(width / 2 + 20, height / 2, width / 4, height / 8);
		feeInfo.setBounds(0, height / 4, width, height / 5);
		acceptFee.setBounds(width / 4, height / 2, width / 4 - 20, height / 5);
		declineFee.setBounds(width / 2, height / 2, width / 4 - 20, height / 5);
	}
	
	private void paintScene() {

		switch (scene) {
		case ERROR1:
			error.setText("Flight Reference doesnt exist.");
			frame.getContentPane().add(error);
			frame.getContentPane().add(retry);
			break;
		case ERROR2:
			error.setText("Surname and Flight reference dont match");
			frame.getContentPane().add(error);
			frame.getContentPane().add(retry);
			break;
		case CHECKIN:
			frame.getContentPane().add(checkIn);
			frame.getContentPane().add(exit);
			break;
		case USERINFO:
			frame.getContentPane().add(surname);
			frame.getContentPane().add(reference);
			frame.getContentPane().add(enter);
			frame.add(surnameTag);
			frame.add(referenceTag);
			break;
		case BAGCHECK:
			frame.getContentPane().add(noBags);
			frame.getContentPane().add(enterBagInfo);
			break;
		case BAGS:
			frame.getContentPane().add(bagx);
			frame.getContentPane().add(bagy);
			frame.getContentPane().add(bagz);
			frame.getContentPane().add(bagxTag);
			frame.getContentPane().add(bagyTag);
			frame.getContentPane().add(bagzTag);
			frame.add(weightkg);
			frame.add(weightkgTag);
			frame.add(confirm);
			break;
		case FEES:
			frame.getContentPane().add(feeInfo);
			frame.getContentPane().add(acceptFee);
			frame.getContentPane().add(declineFee);
		}

		// this makes sure the frame which has been set is now painted onto the frame
		frame.repaint();
	}

	public static void main(String[] args) {  
		
		GuiNewTry GUI = new GuiNewTry();
		GUI.createAndShowGUI(); 
		GUI.paintScene();
	}  
}  
