package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//A window for the purpose of giving the user a message

public class MessageDialog extends JFrame {

	//Public variables
	
	public JPanel p;
	public CustomButton okButton;
	public String message;
	public ImageIcon messageIcon;

	//Private variables
	private GridBagConstraints c;
	
	
	//Defaults constructor
	public MessageDialog(String dialogMessage) {
		
		//initialization of components and variables
		p = new JPanel();
		c = new GridBagConstraints();
		okButton = new CustomButton("Ok");
		okButton.addActionListener(new CloseAction(this));
		message = dialogMessage;
		messageIcon = new ImageIcon("mediaFiles/pictures/questionMark.png");
		
		//Placement of layout
		p.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		p.add(new JLabel(messageIcon), c);
		c.gridx++;
		p.add(new JLabel(message), c);
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 2;
		p.add(okButton, c);
		
		//Finitilization of the frame
		this.setType(Type.UTILITY);
		this.add(p);
		this.pack();	
	}	
	
	
	
}