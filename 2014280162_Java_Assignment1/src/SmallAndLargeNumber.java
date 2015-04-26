import acm.program.*;

public class SmallAndLargeNumber extends ConsoleProgram {
	private static final int Sentinel = 0;

	public void run() {
		print("This program finds the largest and the smallest numbers.\n?");
		
		int smallestNum = 0;//to save smallest number
		int highestNum = 0;//to save largest number
		int counter = 0;
		int read;
		
		while ((read = readInt()) != 0) {
			if (counter == 0) {
				highestNum = smallestNum = read;
			}
			if (read > highestNum) {
				highestNum = read;
			} else if (read < smallestNum) {
				smallestNum = read;
			}
			counter++;
			print("?");
		}
		
		if (counter == 0) {
			println("No integer was found");
		} else
			println("smallest: " + smallestNum + "\nlargest: " + highestNum);
	}
}
