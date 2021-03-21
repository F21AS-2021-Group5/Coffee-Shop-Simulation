package ClockGUI;

import javax.swing.JFrame;

public class TestClock {

	public static void main(String[] args) {
		ClockGUI c = new ClockGUI();
		c.setSize(170, 100);
		c.setTitle("Clock");
		c.setVisible(true);
		c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.update();
	}

}