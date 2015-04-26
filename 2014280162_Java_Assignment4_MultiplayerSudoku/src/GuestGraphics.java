import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GuestGraphics extends JFrame implements SudokuConstants {
	private SudokuPanel sudokuPanel;
	private Player guestPlayer;
	private HostGraphics hostGraphics;
	private Game game;

	public GuestGraphics(SudokuGrid grid, Player player,
			HostGraphics hostGraphics, Game game) {
		this.hostGraphics = hostGraphics;
		this.guestPlayer = player;
		this.game = game;
		initUI();
	}

	private void initUI() {
		this.setTitle("Sudoku");
		this.setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		this.sudokuPanel = new SudokuPanel(this.guestPlayer, this.hostGraphics, this.game);
		this.getContentPane().add("Center", sudokuPanel.getSudokuPanel());
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				GuestGraphics gg = (GuestGraphics) we.getSource();
				try {
					// gg.game.getGuestPlayer().output.close();
					// gg.game.getGuestPlayer().input.close();
					// gg.game.getGuestPlayer().getGuest().close();
					gg.game.setGuestPlayer(null);
					gg.setVisible(false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public void start() {
		this.sudokuPanel.start();
	}

	public SudokuPanel getPanel() {
		return this.sudokuPanel;
	}
}
