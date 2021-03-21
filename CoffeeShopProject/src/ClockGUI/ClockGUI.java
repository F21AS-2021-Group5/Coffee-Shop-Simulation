package ClockGUI;

import java.awt.*;
import javax.swing.*;
import java.util.*;

class ClockGUI extends JFrame {
	JLabel h, m, s, ap;

	public ClockGUI() {
		setLayout(new GridLayout(2, 1));
		Font f = new Font("Serif", Font.ITALIC, 36);
		h = new JLabel("", JLabel.CENTER);
		h.setFont(f);
		m = new JLabel("", JLabel.CENTER);
		m.setFont(f);
		s = new JLabel("", JLabel.CENTER);
		s.setFont(f);
		ap = new JLabel("", JLabel.CENTER);
		ap.setFont(f);

		JPanel p = new JPanel(new GridLayout(1, 3));
		p.add(h);
		p.add(m);
		p.add(s);

		add(p);
		add(ap);
	}
	
	private void pause(long millis) {
		long start = Calendar.getInstance().getTimeInMillis();
		while(Calendar.getInstance().getTimeInMillis() - start > millis);
	}

	public void update() {
		while (true) {
			Calendar c = Calendar.getInstance();
			int ih = c.get(Calendar.HOUR);
			h.setText(ih > 9 ? Integer.toString(ih) : "0"+Integer.toString(ih));
			int im = c.get(Calendar.MINUTE);
			m.setText(im > 9 ? Integer.toString(im) : "0"+Integer.toString(im));
			int is = c.get(Calendar.SECOND);
			s.setText(is > 9 ? Integer.toString(is) : "0"+Integer.toString(is));
			if (c.get(Calendar.AM_PM) == Calendar.AM)
				ap.setText("AM");
			else
				ap.setText("PM");
			pause(500);
		}
	}
}

