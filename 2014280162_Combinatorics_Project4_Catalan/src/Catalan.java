import java.io.BufferedInputStream;
import java.math.BigInteger;
import java.util.Scanner;

public class Catalan {

	public static void main(String[] args) {
		int n;
		Scanner cin = new Scanner(new BufferedInputStream(System.in));
		while (cin.hasNext()) {
			n = cin.nextInt();
			if (n == -1)
				break;
			BigInteger a[] = new BigInteger[1000];
			int i;
			a[0] = a[1] = new BigInteger("1");
			for (i = 2; i <= n; i++) {
				a[i] = a[i - 1].multiply(BigInteger.valueOf((4 * i - 2)));
				a[i] = a[i].divide(BigInteger.valueOf(i + 1));
			}
			System.out.println(a[n]);
		}
		cin.close();
	}
}