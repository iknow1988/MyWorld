import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class HostGraphics extends JFrame implements SudokuConstants,
		ActionListener, Runnable {
	private JLabel label;
	private JButton newGameButton;
	private JTextField difficultyTextbox;
	private SudokuPanel sudokuPanel;
	private SudokuGrid grid;
	private Player hostPlayer;
	private Game game;

	public HostGraphics(SudokuGrid grid, Player player, Game game) {
		this.grid = grid;
		this.hostPlayer = player;
		this.game = game;
		initUI();
		initializeListeners();
	}

	private void initializeListeners() {
		newGameButton.addActionListener(this);
	}

	private void initUI() {
		this.setTitle("Sudoku");
		this.setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				HostGraphics gg = (HostGraphics) we.getSource();
				try {
					gg.game.exit();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		label = new JLabel("Level {1 Hardest, 8 Simplest} ");
		difficultyTextbox = new JTextField(10);
		difficultyTextbox.setText("5");
		newGameButton = new JButton("New Game");
		JPanel northPanel = new JPanel();
		northPanel.add(label);
		northPanel.add(difficultyTextbox);
		northPanel.add(newGameButton);
		this.getContentPane().add("North", northPanel);
		this.sudokuPanel = new SudokuPanel(this.hostPlayer, this.grid,
				this.game);
		this.getContentPane().add("Center", sudokuPanel.getSudokuPanel());
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newGameButton) {
			if (!difficultyTextbox.getText().isEmpty()) {
				try {
					int diff = Integer.parseInt(difficultyTextbox.getText());
					if (diff >= 1 && diff <= 8) {
						this.grid = new SudokuGrid(diff);
						this.sudokuPanel.newGrid(this.grid);
						// this.sudokuPanel = new SudokuPanel(this.hostPlayer,
						// this.grid);
						// this.getContentPane().add("Center",
						// sudokuPanel.getSudokuPanel());
						this.sudokuPanel.newGrid(this.grid);
						if (this.game.getGuestPlayer() != null) {
							this.game.getGuestGraphics().getPanel()
									.newGrid(this.grid);
						}
					} else {
						Utility.showErrorDialog("Please enter number 1 to 8");
					}
				} catch (NumberFormatException ex) {
					Utility.showErrorDialog("Please enter number 1 to 8");
				}
			} else {
				Utility.showErrorDialog("Empty Difficulty Box");
			}
		}
	}

	public void run() {
		new Thread(this.sudokuPanel).start();
	}

	public SudokuPanel getHostSudokuPanel() {
		return this.sudokuPanel;
	}
}
