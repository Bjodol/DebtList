package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class HeaderMenu extends JPanel {
	
	private GridBagConstraints c;

	public HeaderMenu() {
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		
		add(new JLabel("Header"), c);
		
	}

}
