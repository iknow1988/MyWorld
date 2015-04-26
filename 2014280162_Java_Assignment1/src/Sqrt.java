import acm.program.*;

public class Sqrt extends ConsoleProgram {

	public void run() {
		println("Enter values to compute Pythagorean theorem.");
		
		print("a:");
		int a = readInt();
		
		print("b:");
		int b = readInt();
		
		double c = Math.sqrt(a * a + b * b);
		println("c = " + c);

	}
}
