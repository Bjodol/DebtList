package gui;

import java.awt.Color;
import java.awt.Dimension;
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
	private JPanel outgoingReqLabel;
	private JPanel incommingReqLabel;
	
	public DebtPanel() {
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 1; 
		c.anchor = GridBagConstraints.WEST;
		
		outgoingReqLabel = new JPanel();
		incommingReqLabel = new JPanel();
		outgoingReqLabel.setBackground(new Color(0xFF4D4D));
		incommingReqLabel.setBackground(new Color(0x99FF66));
		outgoingReqLabel.setPreferredSize(new Dimension(30, 30));
		incommingReqLabel.setPreferredSize(new Dimension(30, 30));
		
		pendingDebts = Session.session.getUser().getPendingDebts();
		confirmedDebts = Session.session.getUser().getConfirmedDebts();
		if (confirmedDebts.isEmpty() && pendingDebts.isEmpty()) {
			add(new JLabel("No debts"), c);
		} else if (confirmedDebts.isEmpty() && !pendingDebts.isEmpty()) {
			c.gridwidth = 1;
			for (int i = 0; i < pendingDebts.size(); i++) {
				if (pendingDebts.get(i).getTo().equals(Session.session.getUser())) {
					add(incommingReqLabel, c);
					c.gridx++;
				} else {
					add(outgoingReqLabel, c);
					c.gridx++;
				}
				
				add(new SingelDebtButton(pendingDebts.get(i)), c);
				c.gridx = 0;
				c.gridy++;
			}
		} else if (!confirmedDebts.isEmpty() && pendingDebts.isEmpty()) {
			c.gridwidth = 2;
			for (int i = 0; i < confirmedDebts.size(); i++) {
				add(new SingelDebtButton(confirmedDebts.get(i)), c);
				c.gridy++;
			}
		} else {
			c.gridwidth = 1;
			for (int i = 0; i < pendingDebts.size(); i++) {
				if (pendingDebts.get(i).getTo().equals(Session.session.getUser())) {
					add(incommingReqLabel, c);
					c.gridx++;
				} else {
					add(outgoingReqLabel, c);
					c.gridx++;
				}
				
				add(new SingelDebtButton(pendingDebts.get(i)), c);
				c.gridx = 0;
				c.gridy++;
			}
			c.gridwidth = 2;
			for (int i = 0; i < confirmedDebts.size(); i++) {
				add(new SingelDebtButton(confirmedDebts.get(i)), c);
				c.gridy++;
			}
		}
		
	}

}
