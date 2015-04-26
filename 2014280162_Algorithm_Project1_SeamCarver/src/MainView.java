import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;


public class MainView  extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton file;
	private JButton image;
	public MainView(){
		 file = new JButton("Read from file");
		 image = new JButton("Read from image");
			this.setLayout(new GridLayout(2,1));
			this.setSize(200, 200);
			file.addActionListener(this);
			image.addActionListener(this);
			this.getContentPane().add(file);
			this.getContentPane().add(image);
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==file){
			JFrame frame = new FileApplication();
			frame.setVisible(true);
		}
		else{
			JFrame frame = new ImageApplication();
			frame.setVisible(true);
		}
	}
}
