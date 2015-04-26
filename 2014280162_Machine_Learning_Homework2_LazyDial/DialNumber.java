import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DialNumber {
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	private static int[][] dialPad = new int[4][3];
	private static int[][] phoneNumbers;

	private static int distance(int source, int destination) {
		int distance = 0;
		
		// Source or destination can not be 3,6,9
		if (source != 0 && source % 3 == 0)
			source = source - 1;
		if (destination != 0 && destination % 3 == 0)
			destination = destination - 1;

		int s_i = 0;
		int s_j = 0;
		int d_i = 0;
		int d_j = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				if (dialPad[i][j] == source) {
					s_i = i;
					s_j = j;
				}
				if (dialPad[i][j] == destination) {
					d_i = i;
					d_j = j;
				}
			}
		}

		distance = Math.abs(d_i - s_i) + Math.abs(d_j - s_j);

		return distance;
	}

	private static boolean isSecondColumn(int x) {
		boolean result = false;

		if (x == 2 || x == 5 || x == 8 || x == 0)
			result = true;

		return result;
	}

	// This is my main function to calculate minimum movements
	private static int minimumMovements(int[] input, int n, int source) {
		if (n == input.length)
			return 0;
		else {
			int destination = input[n];

			if (isSecondColumn(destination)) {
				int d1 = destination;
				int d2 = destination - 1;
				int dist1 = distance(source, d1);
				int dist2 = distance(source, d2);
				if (dist1 > dist2) {
					System.out.print(dist2 + " ");
					return dist2 + minimumMovements(input, n + 1, d2);
				} else {
					System.out.print(dist1 + " ");
					return dist1 + minimumMovements(input, n + 1, d1);
				}
			} else {
				System.out.print(distance(source, destination) + " ");
				return distance(source, destination)
						+ minimumMovements(input, n + 1, destination);
			}
		}
	}

	private static void generateDial() {
		int d = 1;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				dialPad[i][j] = (char) d;
				d++;
			}
		}
		dialPad[3][0] = -1;
		dialPad[3][1] = 0;
		dialPad[3][2] = -2;
	}

	private static int[] convertStringToArray(String[] strings) {
		int[] input = new int[strings.length];
		for (int i = 0; i < strings.length; i++) {
			input[i] = Integer.parseInt(strings[i]);
		}

		return input;
	}

	private static void generateInput() {
		File file = new File("input.txt");
		try (Scanner scanner = new Scanner(file, ENCODING.name())) {
			int inputNum = Integer.parseInt(scanner.nextLine());
			phoneNumbers = new int[inputNum][];
			int count = 0;
			while (scanner.hasNextLine()) {
				String aLine = scanner.nextLine();
				int[] inputArray = convertStringToArray(aLine.split(""));
				phoneNumbers[count] = inputArray;
				count++;
			}
		} catch (FileNotFoundException e) {
			System.out.println("Input File not found");
		} catch (NumberFormatException e) {
			System.out.println("Invalid Number of test cases");
		}
	}

	public static void main(String[] args) {
		generateDial();
		generateInput();
		for (int i = 0; i < phoneNumbers.length; i++) {
			int result = minimumMovements(phoneNumbers[i], 0, 1);
			System.out.println(result);
		}
	}
}