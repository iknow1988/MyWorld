import java.util.Random;
import java.util.Scanner;

class MemoryBandwidth {
	private static final int MINBYTES = (1 << 18);
	private static final int MAXBYTES = (1 << 27);
	private static final int MAXSTRIDE = 1;
	private static final int MAXELEMS = MAXBYTES * 8 / Integer.SIZE;
	private static int data[] = new int[MAXELEMS];
	private static volatile int sink;
	private static final int CORES = 2;
	private static SumThread[] st;
	private static Step2Thread[] st2;

	public static void main(String[] args) {
		Compiler.disable();
		System.out
				.println("Please enter your choice.\n1.Single Thread Sum.\t2.Multi Thread Sum.\t3.Single Thread Step 2.\t4.Multi Thread Step 2.");
		Scanner in = new Scanner(System.in);
		int choice = in.nextInt();
		if (choice == 1 || choice == 2) {
			initData();
		}
		System.out.print("Memory Bandwidth (MB/sec)\nSize\t");
		for (int stride = 1; stride <= MAXSTRIDE; stride = stride * 2)
			System.out.print("s" + stride + "\t");
		System.out.println();
		for (int size = MAXBYTES; size >= MINBYTES; size >>= 1) {
			if (size > (1 << 20))
				System.out.print(size / (1 << 20) + "m\t");
			else
				System.out.print(size / 1024 + "k\t");

			for (int stride = 1; stride <= MAXSTRIDE; stride = stride * 2) {
				if (choice != 1 && choice != 2) {
					initRandData();
				}
				System.out.print(run(size, stride, choice) + "\t");
			}
			System.out.println();
		}
	}

	private static void initData() {

		for (int i = 0; i < MAXELEMS; i++)
			data[i] = i;
	}

	private static void initRandData() {
		Random r = new Random();
		int Low = 0;
		int High = 100000000;
		for (int i = 0; i < MAXELEMS; i++)
			data[i] = r.nextInt(High - Low) + Low;
	}

	private static void testThread(int elems, int stride) {
		for (int i = 0; i < CORES; i++) {
			st[i].start();
		}
		/* wait for the threads to finish! */
		try {
			for (int i = 0; i < CORES; i++) {
				st[i].join();
			}
		} catch (InterruptedException e) {
			System.out.println("Interrupted");
		}
	}

	private static void prepareThread(int elems, int stride) {
		st = new SumThread[CORES];
		for (int i = 0; i < CORES; i++) {
			st[i] = new SumThread(i * (elems / CORES),
					(i * (elems / CORES) + (elems / CORES)), stride, elems,
					data);
		}
	}

	private static void testThreadStep2(int elems, int stride) {
		for (int i = 0; i < CORES; i++) {
			st2[i].start();
		}
		/* wait for the threads to finish! */
		try {
			for (int i = 0; i < CORES; i++) {
				st2[i].join();
			}
		} catch (InterruptedException e) {
			System.out.println("Interrupted");
		}
	}

	private static void prepareThreadStep2(int elems, int stride) {
		st2 = new Step2Thread[CORES];
		for (int i = 0; i < CORES; i++) {
			st2[i] = new Step2Thread(i * (elems / CORES),
					(i * (elems / CORES) + (elems / CORES)), stride, elems,
					data);
		}
	}

	private static void test(int elems, int stride) {
		int i;
		int result = 0;
		sink = 0;
		for (i = 0; i < elems; i += stride) {
			result += data[i];
		}
		sink = result;
	}

	private static void testStep2(int elems, int stride) {
		double sum = 0.0;
		double square_sum = 0.0;
		for (int i = 0; i < elems; i += stride) {
			double x = data[i];
			sum += x;
			square_sum += x * x;
		}
		double mean = sum * 1.0 / elems;
		double diff_square_sum = square_sum + elems * mean * mean - 2 * sum
				* mean;
		// double deviation = Math.sqrt(diff_square_sum);
	}

	private static double run(int size, int stride, int choice) {
		int elems = size * 8 / Integer.SIZE;
		long startTime = 0, endTime = 0;
		switch (choice) {
		case 1:
			test(elems, stride);
			startTime = System.nanoTime();
			test(elems, stride);
			endTime = System.nanoTime();
			break;
		case 2:
			prepareThread(elems, stride);
			testThread(elems, stride);
			prepareThread(elems, stride);
			startTime = System.nanoTime();
			testThread(elems, stride);
			endTime = System.nanoTime();
			break;
		case 3:
			testStep2(elems, stride);
			startTime = System.nanoTime();
			testStep2(elems, stride);
			endTime = System.nanoTime();
			break;
		case 4:
			prepareThreadStep2(elems, stride);
			testThreadStep2(elems, stride);
			prepareThreadStep2(elems, stride);
			startTime = System.nanoTime();
			testThreadStep2(elems, stride);
			endTime = System.nanoTime();
			break;
		default:
			break;
		}
		return (size / (stride)) / ((endTime - startTime) / 1000);
	}
}

class SumThread extends Thread {
	public int from, to, sum, stride, elems;
	public int data[];

	public SumThread() {

	}

	public SumThread(int from, int to, int stride, int elems, int[] data) {
		this.from = from;
		this.to = to;
		this.elems = elems;
		this.stride = stride;
		this.data = new int[elems];
		for (int i = from; i < to; i += stride) {
			this.data[i] = data[i];
		}
		sum = 0;
	}

	public void run() {
		for (int i = 0; i <= elems; i += 1) {
			sum += i;
		}
	}

	public int getSum() {
		return sum;
	}
}

class Step2Thread extends Thread {
	public int from, to, sum, stride, elems;
	public int data[];
	public double mean, deviation;

	public Step2Thread() {

	}

	public Step2Thread(int from, int to, int stride, int elems, int[] data) {
		this.from = from;
		this.to = to;
		this.elems = elems;
		this.data = new int[elems];
		this.stride = stride;
		for (int i = from; i < to; i += stride) {
			this.data[i] = data[i];
		}
		sum = 0;
		mean = 0.0;
		deviation = 0.0;
	}

	public void run() {
		double sum = 0.0;
		double square_sum = 0.0;
		for (int i = 0; i < elems; i += 1) {
			double x = data[i];
			sum += x;
			square_sum += x * x;
		}
		mean = sum * 1.0 / elems;
		double diff_square_sum = square_sum + elems * mean * mean - 2 * sum
				* mean;
		deviation = Math.sqrt(diff_square_sum);
	}

	public int getSum() {
		return sum;
	}
}