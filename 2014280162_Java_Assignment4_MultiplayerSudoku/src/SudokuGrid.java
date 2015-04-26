import java.util.Random;

public class SudokuGrid implements SudokuConstants {
	private int[][] partialSudoku;
	private int[][] sudoku;
	private int difficulty;
	private Random random;

	public SudokuGrid(int difficulty) {
		random = new Random();
		this.difficulty = difficulty;
		partialSudoku = new int[BOARD_SIZE][BOARD_SIZE];
		sudoku = new int[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; ++i) {
			for (int j = 0; j < BOARD_SIZE; ++j) {
				partialSudoku[i][j] = -1;
				sudoku[i][j] = -1;
			}
		}
		generateNumbers();
	}

	public int[][] getNumbers() {
		return partialSudoku;
	}

	public int getDifficulty() {
		return this.difficulty;
	}

	public boolean validate(int x, int y, int input) {
		boolean result = false;
		if (sudoku[x][y] == input)
			result = true;
		this.partialSudoku[x][y] = input;

		return result;
	}

	public boolean validateFull() {
		boolean result = true;
		for (int i = 0; i < BOARD_SIZE; ++i) {
			for (int j = 0; j < BOARD_SIZE; ++j) {
				if (partialSudoku[i][j] != sudoku[i][j]) {
					result = false;
					break;
				}
			}
			if (!result)
				break;
		}

		return result;
	}

	private void generateNumbers() {
		SudokuGenerator sudGen = new SudokuGenerator();
		this.sudoku = sudGen.nextBoard(0);
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				this.partialSudoku[i][j] = this.sudoku[i][j];
			}
		}
		for (int i = 0; i < 81 - 9* difficulty; i++) {
			partialSudoku[random.nextInt(9 - 0) + 0][random.nextInt(9 - 0) + 0] = -1;
		}
	}
}
