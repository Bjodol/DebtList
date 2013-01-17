package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import session.Session;

public class SingelDebtButton extends JButton {
	
	private String infoString;
	
	public SingelDebtButton(logic.Debt debt) {
		super();
		infoString = "";
		
		switch (debt.getStatus()) {
			case REQUESTED:
				if (Session.session.getUser().equals(debt.getTo())) {
					infoString = "Request sent to " + debt.getFrom().getUsername() + ": Owes you ";
				} else if (Session.session.getUser().equals(debt.getFrom())) {
					infoString = "Request from " + debt.getTo().getUsername() + ": You owe " ;				
				} else {
					MessageDialog eDialog = new MessageDialog("Debt undecided");
				}
				setBackground(new Color(0xFFFF66));
				setPreferredSize(new Dimension(400, 30));
				break;
			case CONFIRMED:
				
				//Decides if it is a good or a bad debt
				if (Session.session.getUser().equals(debt.getTo())) {
					setBackground(new Color(0x99FF66)); //Green
					infoString = debt.getFrom().getUsername() + ": ";
				} else if (Session.session.getUser().equals(debt.getFrom())) {
					setBackground(new Color(0xFF4D4D)); //Red
					infoString = debt.getTo().getUsername() + ": ";
				} else {
					MessageDialog eDialog = new MessageDialog("Debt undecided");
				}
				setPreferredSize(new Dimension(430, 30));
			break;
	
			default:
				MessageDialog eDialog = new MessageDialog("Debt error");
				break;
		}
	
		infoString += debt.getAmount() + " " + debt.getWhat() + ", " +'"' + debt.getComment() + '"' + "."; 
		setText(infoString);
		setHorizontalAlignment(LEFT);
	}

}
