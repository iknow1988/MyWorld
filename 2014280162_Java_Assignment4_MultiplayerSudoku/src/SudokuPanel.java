import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class SudokuPanel extends Thread implements SudokuConstants, KeyListener {
	private JTextField gridTextBox[][];
	private SudokuGrid grid;
	private Player player;
	private JPanel panel;
	private Game game;

	public SudokuPanel(Player player, SudokuGrid grid, Game game) {
		this.grid = grid;
		this.panel = new JPanel();
		this.player = player;
		this.game = game;
		initUI();
	}

	public SudokuPanel(Player player, HostGraphics hostGraphics, Game game) {
		this.grid = hostGraphics.getHostSudokuPanel().grid;
		this.panel = new JPanel();
		this.player = player;
		this.game = game;
		JTextField[][] hostTextBoxes = hostGraphics.getHostSudokuPanel()
				.getTextBoxes();
		gridTextBox = new JTextField[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; ++i) {
			for (int j = 0; j < BOARD_SIZE; ++j) {
				gridTextBox[i][j] = new JTextField();
				gridTextBox[i][j].setHorizontalAlignment(JTextField.CENTER);
				gridTextBox[i][j].setFont(new Font("SansSerif", Font.BOLD, 25));
				gridTextBox[i][j].addKeyListener(this);
				gridTextBox[i][j].setName("Numbers_" + Integer.toString(i)
						+ "_" + Integer.toString(j));
				gridTextBox[i][j].setBackground(hostTextBoxes[i][j]
						.getBackground());
				gridTextBox[i][j].setForeground(hostTextBoxes[i][j]
						.getForeground());
			}
		}
		this.panel.setLayout(new GridLayout(9, 9));
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				this.panel.add(gridTextBox[i][j]);
			}
		}
		this.setGrid(grid.getNumbers());
	}

	private void initUI() {
		gridTextBox = new JTextField[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				gridTextBox[i][j] = new JTextField();
				gridTextBox[i][j].setHorizontalAlignment(JTextField.CENTER);
				gridTextBox[i][j].setFont(new Font("SansSerif", Font.BOLD, 25));
				gridTextBox[i][j].addKeyListener(this);
				gridTextBox[i][j].setName("Numbers_" + Integer.toString(i)
						+ "_" + Integer.toString(j));
				if ((j == 3 || j == 4 || j == 5)
						&& (i != 3 && i != 4 && i != 5)) {
					gridTextBox[i][j].setBackground(new Color(33, 122, 168));
				} else if ((i == 3 || i == 4 || i == 5)
						&& (j == 0 || j == 1 || j == 2 || j == 6 || j == 7 || j == 8)) {
					gridTextBox[i][j].setBackground(new Color(33, 122, 168));
				} else {
					gridTextBox[i][j].setBackground(new Color(31, 190, 214));
				}
			}
		}
		this.panel.setLayout(new GridLayout(9, 9));
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				this.panel.add(gridTextBox[i][j]);
			}
		}
		this.setGrid(grid.getNumbers());
	}

	public void run() {
		String response;
		JTextField field;
		while (!grid.validateFull()) {
			try {
				response = player.input.readLine();
				System.out.println(this.player.isHost() + " read " + response);
				String[] values = response.split("_");
				field = this.getTextBox(Integer.parseInt(values[1]),
						Integer.parseInt(values[2]));
				if (values[3].equals("-1")) {
					field.setText("");
				} else {
					field.setText(values[3]);
				}
				field.setForeground(this.player.isHost() ? Color.pink
						: Color.YELLOW);
				boolean result = grid.validate(Integer.parseInt(values[1]),
						Integer.parseInt(values[2]),
						Integer.parseInt(values[3]));
				if (!result) {
					field.setBackground(Color.RED);
				} else {
					field.setBackground(this.getColor(
							Integer.parseInt(values[1]),
							Integer.parseInt(values[2])));
				}
			} catch (IOException e) {
				e.printStackTrace();
				break;
			} catch (NumberFormatException ex) {
				Utility.showErrorDialog("Invalid Input");
			}

			if (grid.validateFull()) {
				Utility.showSuccessDialog("Game ended");
				this.game.exit();
			}
		}
	}

	public JPanel getSudokuPanel() {
		return this.panel;
	}

	public void newGrid(SudokuGrid grid) {
		this.grid = grid;
		for (int i = 0; i < BOARD_SIZE; ++i) {
			for (int j = 0; j < BOARD_SIZE; ++j) {
				gridTextBox[i][j].setText("");
				gridTextBox[i][j].setForeground(Color.BLACK);
				if ((j == 3 || j == 4 || j == 5)
						&& (i != 3 && i != 4 && i != 5)) {
					gridTextBox[i][j].setBackground(new Color(33, 122, 168));
				} else if ((i == 3 || i == 4 || i == 5)
						&& (j == 0 || j == 1 || j == 2 || j == 6 || j == 7 || j == 8)) {
					gridTextBox[i][j].setBackground(new Color(33, 122, 168));
				} else {
					gridTextBox[i][j].setBackground(new Color(31, 190, 214));
				}
			}
		}
		this.setGrid(this.grid.getNumbers());
	}

	public void setGrid(int numbers[][]) {
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				if (numbers[i][j] != -1)
					gridTextBox[i][j].setText(Integer.toString(numbers[i][j]));
			}
		}
	}

	public JTextField[][] getTextBoxes() {
		return gridTextBox;
	}

	public void setTextBoxes(JTextField[][] hostTextBoxes) {
		for (int i = 0; i < BOARD_SIZE; ++i) {
			for (int j = 0; j < BOARD_SIZE; ++j) {
				this.gridTextBox[i][j] = hostTextBoxes[i][j];
				gridTextBox[i][j].setBackground(hostTextBoxes[i][j]
						.getBackground());
				gridTextBox[i][j].setForeground(hostTextBoxes[i][j]
						.getForeground());
			}
		}
	}

	public JTextField getTextBox(int x, int y) {
		return gridTextBox[x][y];
	}

	public SudokuGrid getSudokuGrid() {
		return this.grid;
	}

	public void keyReleased(KeyEvent e) {
		JTextField field = (JTextField) e.getSource();
		field.setForeground(this.player.isHost() ? Color.YELLOW : Color.pink);
		if (!field.getText().isEmpty()) {
			try {
				int value = Integer.parseInt(field.getText());
				String[] values = field.getName().split("_");
				if (player.output != null) {
					player.output.println(field.getName() + "_" + value);
					player.output.flush();
					// System.out.println(this.player.isHost() + " print "+
					// field.getName() + "_" + value);
				}
				boolean result = grid.validate(Integer.parseInt(values[1]),
						Integer.parseInt(values[2]), value);
				if (!result) {
					field.setBackground(Color.RED);
				} else {
					field.setBackground(this.getColor(
							Integer.parseInt(values[1]),
							Integer.parseInt(values[2])));
				}
			} catch (NumberFormatException ex) {
				Utility.showErrorDialog("Invalid Input");
				field.setText("");
			}
		} else {
			field.setBackground(Color.RED);
			player.output.println(field.getName() + "_" + -1);
			player.output.flush();
		}
		if (player.output == null && grid.validateFull()) {
			Utility.showSuccessDialog("Game ended");
			this.game.exit();
		}
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	private Color getColor(int i, int j) {
		Color color = new Color(33, 122, 168);
		if ((j == 3 || j == 4 || j == 5) && (i != 3 && i != 4 && i != 5)) {
		} else if ((i == 3 || i == 4 || i == 5)
				&& (j == 0 || j == 1 || j == 2 || j == 6 || j == 7 || j == 8)) {
		} else {
			color = new Color(31, 190, 214);
		}
		return color;
	}

	public JPanel getPanel() {
		return this.panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}
}
