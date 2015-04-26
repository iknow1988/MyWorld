import java.math.BigInteger;

public class IntegerPartition {
	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
		int n = Integer.parseInt(args[0]);
		BigInteger[][] a = new BigInteger[n + 1][n + 1];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				a[i][j] = new BigInteger("0");
			}
		}
		int i, j, k;
		for (i = 0; i < a.length; i++) {
			a[0][i] = new BigInteger("1");
		}
		for (i = 1; i < a.length; i++) {
			a[i][1] = new BigInteger("1");
			for (j = 2; j < a[0].length; j++) {
				k = i - j;
				if (k < 0)
					a[i][j] = a[i][j - 1];
				else {
					a[i][j] = a[i][j - 1].add(a[k][j]);
				}
			}
		}
		i--;
		System.out.println(a[i][i - 1].add(new BigInteger("1")));
		long endTime = System.currentTimeMillis();
		System.out.println("Took " + (endTime - startTime) / 1000 + " s");
	}
}