package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import logic.Debt;
import session.Session;

public class DebtPanel extends JPanel {

	private GridBagConstraints c;
	private List<Debt> pendingDebts;
	private List<Debt> confirmedDebts;
	
	public DebtPanel() {
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 1; 
		c.anchor = GridBagConstraints.WEST;
		
		pendingDebts = Session.session.getUser().getPendingDebts();
		confirmedDebts = Session.session.getUser().getConfirmedDebts();
		if (confirmedDebts.isEmpty() && pendingDebts.isEmpty()) {
			add(new JLabel("No debts"), c);
		} else if (confirmedDebts.isEmpty() && !pendingDebts.isEmpty()) {
			for (int i = 0; i < pendingDebts.size(); i++) {
				add(new SingelDebtButton(pendingDebts.get(i)), c);
				c.gridy++;
			}
		} else if (!confirmedDebts.isEmpty() && pendingDebts.isEmpty()) {
			for (int i = 0; i < confirmedDebts.size(); i++) {
				add(new SingelDebtButton(confirmedDebts.get(i)), c);
				c.gridy++;
			}
		} else {
			for (int i = 0; i < pendingDebts.size(); i++) {
				add(new SingelDebtButton(pendingDebts.get(i)), c);
				c.gridy++;
			}
			for (int i = 0; i < confirmedDebts.size(); i++) {
				add(new SingelDebtButton(confirmedDebts.get(i)), c);
				c.gridy++;
			}
		}
		
	}

}
