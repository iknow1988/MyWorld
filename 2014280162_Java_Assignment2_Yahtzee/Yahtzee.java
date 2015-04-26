/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import java.util.ArrayList;

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {

	public static void main(String[] args) {
		new Yahtzee().start(args);
	}

	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt(Messages.ENTER_NO_OF_PLAYERS);
		while (true) {
			if (nPlayers <= MAX_PLAYERS)
				break;
			nPlayers = dialog.readInt(Messages.VALID_NO_OF_PLAYERS);
		}
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine(String.format(
					Messages.ENTER_PLAYER_NAME, i));
		}

		display = new YtzDisplay();
		display.init(getGCanvas(), playerNames);

		scores = new int[nPlayers + 1][N_CATEGORIES + 1];
		categorySelected = new boolean[nPlayers + 1][N_CATEGORIES + 1];

		playGame();
	}

	private void playGame() {
		for (int i = 0; i < N_SCORING_CATEGORIES; i++) {
			for (int j = 1; j <= nPlayers; j++) {
				play(j);
			}
		}
		calculateFinalResult();
		showWinner();
	}

	private void play(int player) {
		rollDices(player);
		selectCategory(player);
	}

	private void rollDices(int player) {
		for (int i = 0; i < N_DICE; i++) {
			dieValues[i] = rgen.nextInt(1, 6);
		}

		display.printMessage(String.format(Messages.ROLL_THE_DICE,
				playerNames[player - 1]));
		display.waitForPlayerToClickRoll(player);
		display.displayDice(dieValues);

		for (int i = 0; i < 2; i++) {
			display.printMessage(Messages.ROLL_AGAIN);
			display.waitForPlayerToSelectDice();
			for (int j = 0; j < N_DICE; j++) {
				if (display.isDieSelected(j) == true) {
					dieValues[j] = rgen.nextInt(1, 6);
				}
			}
			display.displayDice(dieValues);
		}
	}

	private void selectCategory(int player) {
		while (true) {
			display.printMessage(Messages.SELECT_CATEGORY);
			category = display.waitForPlayerToSelectCategory();

			if (categorySelected[player][category] == false) {

				categorySelected[player][category] = true;
				int score = 0;

				if (checkCategory(dieValues, category) == true) {
					setCategoryScore(player, category);
					score = scores[player][category];
				} else {
					scores[player][category] = 0;
				}

				display.updateScorecard(category, player, score);
				calculateTotalScores(player);
				int totalScore = scores[player][TOTAL];
				display.updateScorecard(TOTAL, player, totalScore);
				break;
			}
		}
	}

	private boolean checkCategory(int[] dice, int category) {
		boolean categoryMatch = false;

		ArrayList<Integer> ones = new ArrayList<Integer>();
		ArrayList<Integer> twos = new ArrayList<Integer>();
		ArrayList<Integer> threes = new ArrayList<Integer>();
		ArrayList<Integer> fours = new ArrayList<Integer>();
		ArrayList<Integer> fives = new ArrayList<Integer>();
		ArrayList<Integer> sixes = new ArrayList<Integer>();

		for (int i = 0; i < N_DICE; i++) {
			if (dice[i] == 1) {
				ones.add(1);
			} else if (dice[i] == 2) {
				twos.add(1);
			} else if (dice[i] == 3) {
				threes.add(1);
			} else if (dice[i] == 4) {
				fours.add(1);
			} else if (dice[i] == 5) {
				fives.add(1);
			} else if (dice[i] == 6) {
				sixes.add(1);
			}
		}

		switch (category) {
		case ONES:
		case TWOS:
		case THREES:
		case FOURS:
		case FIVES:
		case SIXES:
		case CHANCE:
			categoryMatch = true;
			break;
		case THREE_OF_A_KIND:
			if (ones.size() >= 3 || twos.size() >= 3 || threes.size() >= 3
					|| fours.size() >= 3 || fives.size() >= 3
					|| sixes.size() >= 3) {
				categoryMatch = true;
			}
			break;
		case FOUR_OF_A_KIND:
			if (ones.size() >= 4 || twos.size() >= 4 || threes.size() >= 4
					|| fours.size() >= 4 || fives.size() >= 4
					|| sixes.size() >= 4) {
				categoryMatch = true;
			}
			break;
		case YAHTZEE:
			if (ones.size() == 5 || twos.size() == 5 || threes.size() == 5
					|| fours.size() == 5 || fives.size() == 5
					|| sixes.size() == 5) {
				categoryMatch = true;
			}
			break;
		case FULL_HOUSE:
			if (ones.size() == 3 || twos.size() == 3 || threes.size() == 3
					|| fours.size() == 3 || fives.size() == 3
					|| sixes.size() == 3) {
				if (ones.size() == 2 || twos.size() == 2 || threes.size() == 2
						|| fours.size() == 2 || fives.size() == 2
						|| sixes.size() == 2) {
					categoryMatch = true;
				}
			}
			break;
		case LARGE_STRAIGHT:
			if (ones.size() == 1 && twos.size() == 1 && threes.size() == 1
					&& fours.size() == 1 && fives.size() == 1) {
				categoryMatch = true;
			} else if (twos.size() == 1 && threes.size() == 1
					&& fours.size() == 1 && fives.size() == 1
					&& sixes.size() == 1) {
				categoryMatch = true;
			}
			break;
		case SMALL_STRAIGHT:
			if (ones.size() >= 1 && twos.size() >= 1 && threes.size() >= 1
					&& fours.size() >= 1) {
				categoryMatch = true;
			} else if (twos.size() >= 1 && threes.size() >= 1
					&& fours.size() >= 1 && fives.size() >= 1) {
				categoryMatch = true;
			} else if (threes.size() >= 1 && fours.size() >= 1
					&& fives.size() >= 1 && sixes.size() >= 1) {
				categoryMatch = true;
			}
			break;
		}

		return categoryMatch;
	}

	private void setCategoryScore(int player, int category) {
		int score = 0;
		switch (category) {
		case ONES:
		case TWOS:
		case THREES:
		case FOURS:
		case FIVES:
		case SIXES:
			for (int i = 0; i < N_DICE; i++) {
				if (dieValues[i] == category) {
					score += category;
				}
			}
			break;
		case THREE_OF_A_KIND:
		case FOUR_OF_A_KIND:
		case CHANCE:
			for (int i = 0; i < N_DICE; i++) {
				score += dieValues[i];
			}
			break;
		case FULL_HOUSE:
			score = FULL_HOUSE_SCORE;
			break;
		case SMALL_STRAIGHT:
			score = SMALL_STRAIGHT_SCORE;
			break;
		case LARGE_STRAIGHT:
			score = LARGE_STRAIGHT_SCORE;
			break;
		case YAHTZEE:
			score = YAHTZEE_SCORE;
			break;
		}

		scores[player][category] = score;
	}

	private void calculateTotalScores(int playerNumber) {
		int upperScore = 0;
		int lowerScore = 0;
		int totalScore = 0;
		for (int i = ONES; i <= SIXES; i++) {
			upperScore += scores[playerNumber][i];
		}
		for (int i = THREE_OF_A_KIND; i <= CHANCE; i++) {
			lowerScore += scores[playerNumber][i];
		}
		totalScore = upperScore + lowerScore;
		scores[playerNumber][UPPER_SCORE] = upperScore;
		scores[playerNumber][LOWER_SCORE] = lowerScore;
		scores[playerNumber][TOTAL] = totalScore;
	}

	private void calculateFinalResult() {
		for (int i = 1; i <= nPlayers; i++) {
			display.updateScorecard(UPPER_SCORE, i, scores[i][UPPER_SCORE]);
			display.updateScorecard(LOWER_SCORE, i, scores[i][LOWER_SCORE]);
			if (scores[i][UPPER_SCORE] >= BONUS_MINIMUM_SCORE) {
				scores[i][UPPER_BONUS] = BONUS;
			}
			display.updateScorecard(UPPER_BONUS, i, scores[i][UPPER_BONUS]);
			scores[i][TOTAL] = scores[i][TOTAL] + scores[i][UPPER_BONUS];
			display.updateScorecard(TOTAL, i, scores[i][TOTAL]);
		}
	}

	private void showWinner() {
		int score = 0;
		int player = 0;
		for (int i = 1; i <= nPlayers; i++) {
			int x = scores[i][TOTAL];
			if (x > score) {
				score = x;
				player = i - 1;
			}
		}
		display.printMessage(String.format(Messages.WINNER,
				playerNames[player], score));
	}

	/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YtzDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
	private int[][] scores;
	private int category;
	private boolean[][] categorySelected;
	private int[] dieValues = new int[N_DICE];

}
