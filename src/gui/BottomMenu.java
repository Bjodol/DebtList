package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class BottomMenu extends JPanel{
	
	private GridBagConstraints c;

	public BottomMenu() {
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		
		add(new JLabel("BottomMenu"), c);
	}

}
