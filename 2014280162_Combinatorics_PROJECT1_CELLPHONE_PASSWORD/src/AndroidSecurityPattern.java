public class AndroidSecurityPattern {
	public static int rules[][] = { { 0 }, { 1, 2, 4, 5, 6, 8 },
			{ 1, 2, 3, 4, 5, 6, 7, 9 }, { 2, 3, 4, 5, 6, 8 },
			{ 1, 2, 3, 4, 5, 7, 8, 9 }, { 1, 2, 3, 4, 5, 6, 7, 8, 9 },
			{ 1, 2, 3, 5, 6, 7, 8, 9 }, { 2, 4, 5, 6, 7, 8 },
			{ 1, 3, 4, 5, 6, 7, 8, 9 }, { 2, 4, 5, 6, 8, 9 } };

	public static boolean validPattern(int[] pattern) {
		boolean result = false;
		for (int i = 0; i < pattern.length - 1; i++) {
			int digit = pattern[i];
			int nextDigit = pattern[i + 1];
			for (int j = 0; j < rules[digit].length; j++) {
				int rule = rules[digit][j];
				if (rule == nextDigit) {
					result = true;
					break;
				} else {
					if (i != 0) {
						for (int k = i - 1; k >= 0; k--) {
							int lastDigit = pattern[k];
							if (lastDigit == (digit + nextDigit) / 2) {
								result = true;
								break;
							} else {
								result = false;
							}
						}
					} else {
						result = false;
					}
				}
			}
			if (!result)
				break;
		}
		return result;
	}

	public static void validPasscodes() {
		int counter = 0;
		for (int i = 4; i <= 9; i++) {
			int levelCounter = 0;
			Permutation p = new Permutation(9, i);
			while (p.hasNext()) {
				int[] a = p.next();
				if (validPattern(a)) {
					// System.out.println("Valid - " + Arrays.toString(a));
					counter++;
					levelCounter++;
				} else {
					// System.out.println(Arrays.toString(a));
				}
			}
			System.out.println("Valid pattern for " + i
					+ " Digit passwords are: " + levelCounter);
		}
		System.out.println("Total number of valid passwords are: " + counter);
	}

	public static void main(String[] args) {
		validPasscodes();
	}
}

class Permutation {
	private int n, r;
	private int[] index;
	private boolean hasNext = true;

	public Permutation(int n, int r) {
		this.n = n;
		this.r = r;
		index = new int[n];
		for (int i = 0; i < n; i++)
			index[i] = i + 1;
		reverseAfter(r - 1);
	}

	public boolean hasNext() {
		return hasNext;
	}

	private void moveIndex() {
		int i = rightmostDip();
		if (i < 0) {
			hasNext = false;
			return;
		}

		int smallestToRightIndex = i + 1;
		for (int j = i + 2; j < n; j++)
			if ((index[j] < index[smallestToRightIndex])
					&& (index[j] > index[i]))
				smallestToRightIndex = j;

		swap(index, i, smallestToRightIndex);

		if (r - 1 > i) {
			// reverse the elements to the right of the dip
			reverseAfter(i);
			// reverse the elements to the right of r - 1
			reverseAfter(r - 1);
		}
	}

	public int[] next() {
		if (!hasNext)
			return null;
		int[] result = new int[r];
		for (int i = 0; i < r; i++)
			result[i] = index[i];
		moveIndex();
		return result;
	}

	private void reverseAfter(int i) {
		int start = i + 1;
		int end = n - 1;
		while (start < end) {
			swap(index, start, end);
			start++;
			end--;
		}
	}

	private int rightmostDip() {
		for (int i = n - 2; i >= 0; i--)
			if (index[i] < index[i + 1])
				return i;
		return -1;
	}

	private void swap(int[] a, int i, int j) {
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
}