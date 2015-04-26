import acm.program.*;

public class HailstoneSequence extends ConsoleProgram {

	public void run() {
		print("Enter a number: ");
		
		int counter = 0;
		int read = readInt();
		
		while (read != 1) {
			if (read % 2 == 0) {
				println(read + " is even so I take half:" + read / 2);
				read = read / 2;
			} else {
				int temp = 3 * read + 1;
				println(read + " is odd, so I make 3n+1: " + temp);
				read = temp;
			}
			counter++;
		}
		
		println("The process took "+counter+" to reach 1");
	}

}
