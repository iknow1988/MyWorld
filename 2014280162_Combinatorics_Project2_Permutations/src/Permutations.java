import java.util.ArrayList;

public class Permutations {

	public static void lexographicAlgorithm(int[] n) {
		int i = n.length - 2;
		for (int j = 0; j < n.length; j++) {
			System.out.print(n[j]);
		}
		System.out.println();
		for (;;) {
			if (n[i] > n[i + 1]) {
				i = i - 1;
				if (i == -1)
					break;
				continue;
			} else {
				int j = i + 1;
				int smallest = n[j] - n[i];
				int index = j;
				for (; j <= n.length - 1; j++) {

					if (n[j] - n[i] > 0 && (n[j] - n[i]) <= smallest) {
						smallest = n[j] - n[i];
						index = j;
					}
				}
				int temp = n[i];
				n[i] = n[index];
				n[index] = temp;
				for (int k = 1; k <= (n.length - i) / 2; k++) {
					int temp1 = n[i + k];
					n[i + k] = n[n.length - k];
					n[n.length - k] = temp1;
				}
			}
			for (int j = 0; j < n.length; j++) {
				System.out.print(n[j]);
			}
			System.out.println();
			i = n.length - 2;

		}
	}

	public static void main(String[] args) {
		int[] a = { 1, 2, 3 };
		System.out.println("Using Lexographic Algorithm");
		lexographicAlgorithm(a);
		System.out.println("Finished");
		System.out.println("Now printing Using Recursive Algorithm");
		String x = "123";
		PermutationGenerator generator = new PermutationGenerator(x);
		ArrayList<String> permutations = generator.generate();
		for (String s : permutations) {
			System.out.println(s);
		}
		System.out.println("Finished");
	}
}

class PermutationGenerator {

	private String input;
	private ArrayList<String> validPermutations;

	public PermutationGenerator(String input) {
		this.validPermutations = new ArrayList<String>();
		this.input = input;
	}

	public ArrayList<String> generate() {
		permute("", this.input);
		return this.validPermutations;
	}

	private void permute(String prefix, String str) {
		int n = str.length();
		if (n == 0) {
			this.validPermutations.add(prefix);
		} else {
			for (int i = 0; i < n; i++) {
				permute(prefix + str.charAt(i),
						str.substring(0, i) + str.substring(i + 1, n));
			}
		}
	}
}