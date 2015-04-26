import java.util.ArrayList;
import java.util.HashMap;

public class Test {
	private static int CORES;
	private static ArrayList<DataPoint> trainDataPoints;
	private static ArrayList<DataPoint> testDataPoints;
	private static ArrayList<ArrayList<DataPoint>> allDistances = new ArrayList<ArrayList<DataPoint>>();
	private static float[][][] accuracyMatrix = new float[2][6][6];
	private static DistanceThread[] distanceThread;
	private static NormalizeThread[] normalizeThreadTraining;
	private static NormalizeThread[] normalizeThreadTesting;
	private static AccuracyThread[] accuracyThread;
	private static Reader reader;
	private static HashMap<Integer, String> idMap = new HashMap<Integer, String>();

	private static void init(String trainFile, String testFile) {
		reader = new Reader(trainFile, testFile);
		System.out.println("All Testing and Training Data loaded");
		CORES = Runtime.getRuntime().availableProcessors();
		trainDataPoints = reader.getAllTrainingData();
		testDataPoints = reader.getAllTestingData();
		idMap.put(0, "normal");
		idMap.put(1, "u2r");
		idMap.put(2, "r2l");
		idMap.put(3, "probe");
		idMap.put(4, "dos");
	}

	private static void prepareAllThreads() {
		distanceThread = new DistanceThread[CORES];
		normalizeThreadTraining = new NormalizeThread[CORES];
		normalizeThreadTesting = new NormalizeThread[CORES];
		accuracyThread = new AccuracyThread[CORES];
		int testingElems = testDataPoints.size();
		int trainingElems = trainDataPoints.size();
		int testingElemsPerThread = testingElems / CORES;
		int countTesting = 0;
		int trainingElemsPerThread = trainingElems / CORES;
		int countTraining = 0;
		for (int i = 0; i < CORES; i++) {
			if (i == CORES - 1) {
				distanceThread[i] = new DistanceThread(
						i * testingElemsPerThread,
						(i * testingElemsPerThread + (testingElems - countTesting)),
						testDataPoints, trainDataPoints);
				normalizeThreadTraining[i] = new NormalizeThread(
						i * trainingElemsPerThread,
						(i * trainingElemsPerThread + (trainingElems - countTraining)),
						trainDataPoints, reader.getMaxArray(), reader
								.getMinArray());
				normalizeThreadTesting[i] = new NormalizeThread(
						i * testingElemsPerThread,
						(i * testingElemsPerThread + (testingElems - countTesting)),
						testDataPoints, reader.getMaxArray(), reader
								.getMinArray());
				accuracyThread[i] = new AccuracyThread(
						i * testingElemsPerThread,
						(i * testingElemsPerThread + (testingElems - countTesting)),
						testDataPoints, reader.getAllAttackTypes());
			} else {
				distanceThread[i] = new DistanceThread(i
						* testingElemsPerThread,
						(i * testingElemsPerThread + testingElemsPerThread),
						testDataPoints, trainDataPoints);
				normalizeThreadTraining[i] = new NormalizeThread(i
						* trainingElemsPerThread,
						(i * trainingElemsPerThread + trainingElemsPerThread),
						trainDataPoints, reader.getMaxArray(),
						reader.getMinArray());
				normalizeThreadTesting[i] = new NormalizeThread(i
						* testingElemsPerThread,
						(i * testingElemsPerThread + testingElemsPerThread),
						testDataPoints, reader.getMaxArray(),
						reader.getMinArray());
				accuracyThread[i] = new AccuracyThread(i
						* testingElemsPerThread,
						(i * testingElemsPerThread + testingElemsPerThread),
						testDataPoints, reader.getAllAttackTypes());
			}
			countTesting += testingElemsPerThread;
			countTraining += trainingElemsPerThread;
		}
	}

	private static void calculateBestDistances() {
		for (int i = 0; i < CORES; i++) {
			distanceThread[i].start();
		}
		try {
			for (int i = 0; i < CORES; i++) {
				distanceThread[i].join();
			}
			for (int i = 0; i < CORES; i++) {

				allDistances.addAll(distanceThread[i].getAllBestDistances());
			}
		} catch (InterruptedException e) {
			System.out.println("Interrupted");
		}
	}

	private static void normalizeAllData() {
		for (int i = 0; i < CORES; i++) {
			normalizeThreadTraining[i].start();
			normalizeThreadTesting[i].start();
		}
		try {
			for (int i = 0; i < CORES; i++) {
				normalizeThreadTraining[i].join();
				normalizeThreadTesting[i].join();
			}
		} catch (InterruptedException e) {
			System.out.println("Interrupted");
		}
	}

	private static void calculateAccuracy() {
		for (int i = 0; i < CORES; i++) {
			accuracyThread[i].setResult(allDistances);
			accuracyThread[i].start();
		}
		// Checking whether test data is Unlabelled or not. If labelled print
		// Accuracy matrix otherwise only prediction
		if (reader.getAllTestingData().get(0).getLabel() == null) {
			try {
				System.out.println("For\tK=1\tK=3");
				for (int i = 0; i < CORES; i++) {
					accuracyThread[i].join();
				}
			} catch (InterruptedException e) {
				System.out.println("Interrupted");
			}
		} else {
			try {
				for (int i = 0; i < CORES; i++) {
					accuracyThread[i].join();
					for (int j = 0; j < 5; j++) {
						for (int k = 0; k < 5; k++) {
							int[][][] temp = accuracyThread[i]
									.getAccuracyMatrix();
							accuracyMatrix[0][j][k] += temp[0][j][k];
							accuracyMatrix[1][j][k] += temp[1][j][k];
						}
					}
				}
			} catch (InterruptedException e) {
				System.out.println("Interrupted");
			}
			prepareAccuracyMatrix();
			printAccuracy();
		}
	}

	private static void prepareAccuracyMatrix() {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 5; j++) {
				float t0 = 0;
				float r0 = 0;
				for (int k = 0; k < 5; k++) {
					t0 += accuracyMatrix[i][j][k];
					if (j == k) {
						r0 = accuracyMatrix[i][j][k];
					}
				}
				if (t0 == 0.0)
					accuracyMatrix[i][j][5] = 0;
				else
					accuracyMatrix[i][j][5] = r0 / t0;
			}
		}
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 5; j++) {
				float t0 = 0;
				float r0 = 0;
				for (int k = 0; k < 5; k++) {
					t0 += accuracyMatrix[i][k][j];
					if (j == k) {
						r0 = accuracyMatrix[i][k][j];
					}
				}
				if (t0 == 0)
					accuracyMatrix[i][5][j] = 0f;
				else
					accuracyMatrix[i][5][j] = r0 / t0;
			}
		}
	}

	private static void printAccuracy() {
		for (int o = 0; o < 2; o++) {
			if (o == 0)
				System.out.println("FOR K = 1");
			else
				System.out.println("FOR K = 3");
			System.out.print("\tActual\t");
			for (int i = 0; i <= 5; i = i + 1) {
				if (i == 5)
					System.out.print("accuracy" + "\t");
				else
					System.out.print(idMap.get(i) + "\t");
			}
			System.out.println();
			System.out.print("Predicted" + "\t");
			System.out
					.println("________________________________________________________");
			for (int j = 0; j <= 5; j++) {
				if (j == 5)
					System.out.print("Correct\t\t");
				else
					System.out.print(idMap.get(j) + "\t\t");
				for (int k = 0; k <= 5; k++) {
					if (j == 5 || k == 5)
						System.out.print(accuracyMatrix[o][j][k] * 100 + "%\t");
					else
						System.out.print(accuracyMatrix[o][j][k] + "\t");
				}
				System.out.println();
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		init(args[0], args[1]);
		prepareAllThreads();
		normalizeAllData();
		System.out.println("Data Normalized");
		System.out.println("Now calculating Distance");
		calculateBestDistances();
		System.out.println("Now calculating Accuracy");
		calculateAccuracy();
		long endTime = System.currentTimeMillis();
		System.out.println("Finished. Time took"
				+ ((endTime - startTime) * 1.0 / 1000));
	}
}