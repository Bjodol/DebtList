package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import logic.Debt;
import logic.DebtStatus;
import logic.User;

import network.Constants;

import session.Session;

public class LogInPanel extends JPanel {

	private GridBagConstraints c;
	private JTextField usernameField, passwordField;
	private JButton logInButton, registerButton;
	
	public LogInPanel() {
		super(new GridBagLayout());
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		add(new JLabel("Username: "), c);
		c.gridx++;
		usernameField = new JTextField(20);
		add(usernameField, c);
		c.gridx--;
		c.gridy++;
		add(new JLabel("Password: "), c);
		c.gridx++;
		passwordField = new JPasswordField(20);
		add(passwordField, c);
		c.gridy++;
		c.anchor = GridBagConstraints.EAST;
		registerButton = new JButton("Register");
		logInButton = new JButton("Log in");
		JPanel p = new JPanel();
		p.add(registerButton);
		p.add(logInButton);
		add(p, c);
		
		
		// Add listener
		ActionHandler handler = new ActionHandler();
		usernameField.addActionListener(handler);
		passwordField.addActionListener(handler);
		logInButton.addActionListener(handler);
		registerButton.addActionListener(handler);
	}
	
	private LogInPanel dis = this;
	
	class ActionHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			if(event.getSource() != registerButton) {
				Session.session.connect(Constants.SERVER_ADDRESS, Constants.STANDARD_SERVER_PORT);
//				Session.session.send(new LogInRequest(usernameField.getText(), passwordField.getText()).toXml());
//				LogInRequest resp = (LogInRequest)XMLParsable.toObject(Session.session.receive());
//				switch(resp.getStatus()) {
				switch(Session.session.logIn(usernameField.getText(), passwordField.getText())) {
				case UNHANDLED:
					System.out.println("LogInRequest was not handled by the server! Something is probably wrong with the connection!");
					break;
				case ACCEPTED:
					System.out.println("Log in OK!");
					Session.session.addPanel(new DebtDashboard());
					
					break;
				case WRONG_INFORMATION:
					System.out.println("Wrong username/password!");
					break;
				case ALREADY_LOGGED_ON:
					System.out.println("User already logged on!");
					break;
				}
	//			if(resp.isAccepted()) {
	//				System.out.println("LOG IN OK!");
	//			} else {
	//				System.out.println("LOG IN FAILED!");
	//				
	//			}
			} else {
				Session.session.addPanel(new CreateUserPanel(dis));
			}
		}
	}
}
