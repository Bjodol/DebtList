package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

public class DebtDashboard extends JPanel {

	private GridBagConstraints c; 
	private HeaderMenu headerMenu;
	private DebtPanel debtPane;
	private BottomMenu bottomMenu;
	
	public DebtDashboard() {
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 1; 
		c.anchor = GridBagConstraints.WEST;
		
		headerMenu = new HeaderMenu();
		debtPane = new DebtPanel();
		bottomMenu = new BottomMenu();
		
		add(headerMenu, c);
		c.gridy++;
		add(debtPane, c);
		c.gridy++;
		add(bottomMenu, c);
		
		
	}
}
