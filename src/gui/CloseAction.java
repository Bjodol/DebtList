package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

//ActionListener for closing the frame
class CloseAction implements ActionListener{
	
	JFrame f;
	
	public CloseAction(JFrame frame){
		f = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		f.dispose();
		
	}
	
}