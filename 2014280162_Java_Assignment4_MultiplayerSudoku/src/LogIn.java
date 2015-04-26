import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class LogIn extends JFrame implements SudokuConstants, ActionListener {
	private JButton launchButton;
	private JButton joinButton;
	private JTextField portTextbox;
	private JTextField ipAddressTextbox;
	private HashMap<Integer, Game> games = new HashMap<Integer, Game>();

	public LogIn() {
		initUI();
		launchButton.addActionListener(this);
		joinButton.addActionListener(this);
	}

	private void initUI() {
		this.setSize(450, 150);
		this.setLayout(new GridLayout(2, 3));
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		portTextbox = new JTextField(DEFAULT_PORT);
		ipAddressTextbox = new JTextField("127.0.0.1");
		joinButton = new JButton("Join!");
		launchButton = new JButton("Launch!");
		JLabel label = new JLabel("Start a new game. Port: ");
		this.getContentPane().add(label);
		this.getContentPane().add(portTextbox);
		this.getContentPane().add(launchButton);
		JLabel label1 = new JLabel("Join a game. Address: ");
		this.getContentPane().add(label1);
		this.getContentPane().add(ipAddressTextbox);
		this.getContentPane().add(joinButton);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == launchButton) {
			String port = this.portTextbox.getText();
			if (!port.isEmpty()) {
				Player hostPlayer;
				try {
					hostPlayer = new Player(Integer.parseInt(port));
					Game sudokuGame = new Game(hostPlayer);
					games.put(Integer.parseInt(port), sudokuGame);
					sudokuGame.singlePlay();
				} catch (NumberFormatException | IOException e1) {
					Utility.showErrorDialog("Invalid Port Number:" + e1);
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		if (e.getSource() == joinButton) {
			String port = this.portTextbox.getText();
			String IP = this.ipAddressTextbox.getText();
			if (!port.isEmpty() && !IP.isEmpty()) {
				Player guestPlayer;
				try {
					if (games.containsKey(Integer.parseInt(port))) {
						Game sudokuGame = games.get(Integer.parseInt(port));
						if (sudokuGame.getGuestPlayer() == null) {
							guestPlayer = new Player(Integer.parseInt(port), IP);
							sudokuGame.setGuestPlayer(guestPlayer);
							sudokuGame.setGuestGraphics();
							sudokuGame.doublePlay();
						} else {
							Utility.showErrorDialog("Can not play now. Host player is busy now.");
						}
					} else {
						Utility.showErrorDialog("No host Player found.");
					}
				} catch (NumberFormatException e1) {
					Utility.showErrorDialog("Invalid Port Number:" + e1);
					e1.printStackTrace();
				} catch (IOException e1) {
					Utility.showErrorDialog("Error: in client socket" + e1);
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
}
