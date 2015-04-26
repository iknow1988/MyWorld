import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Application {
	//private static final String directoryName = "data 1";
	private static final String inputDirectoryName = "input";
	private static final String testDirectoryName = "Test";
	private static final String featureDirectoryName = "feature";
	private static final int iteration = 5;
	private static Random gen = new Random();
	private static int[] result = new int[5];
	private static Set<Integer> used = new HashSet<Integer>();
	private static int n = 5;
	private static int maxRange = 4;
	private static File inputDirectory;
	private static File testDirectory;
	private static double precision[] = new double[iteration];
	private static double recall[] = new double[iteration];
	private static double f1[] = new double[iteration];
	private static final int inputFile = 4;

	public static void main(String[] args) {
		File rawInputDirectory = new File(args[0]);
		if (!rawInputDirectory.exists()) {
			System.out.println("Invalid Data folder");
			return;
		}
		Instance application = new Instance(args[0]);

		System.out.println("Creating Feature File");
		application.processInput(featureDirectoryName);
		System.out.println("Feature file creation Completed");

		for (int start = 0; start < iteration; start++) {
			System.out.println("Iteration : " + start);
			System.out.println("Selecting input and test file");
			fileSelection();
			System.out.println("Reading from Feature File");

			Collection trainingCollection = application
					.loadInstance(inputDirectoryName);
			ArrayList<Document> instances = trainingCollection
					.getDocumentList();

			Logistic logistic = new Logistic();

			System.out.println("Training Started");
			logistic.train(instances);

			System.out.println("Testing Started");
			Collection testCollection = application
					.loadInstance(testDirectoryName);
			ArrayList<Document> testInstances = testCollection
					.getDocumentList();

			int tp = 0;
			int tn = 0;
			int fp = 0;
			int fn = 0;
			for (Document doc : testInstances) {
				double prediction = logistic.classify(doc.getDictionary());
				if (Math.round(prediction) < 0.50) {
					if (doc.getLabel() == 0)
						tn++;
					else
						fn++;
				} else {
					if (doc.getLabel() == 1)
						tp++;
					else
						fp++;
				}
			}
			System.out.println("tp : " + tp + ", tn :" + tn + " ,fp" + fp
					+ " , fn:" + fn);
			precision[start] = 1.00 * tp / (tp + fp);
			recall[start] = 1.00 * tp / (tp + fn);
			f1[start] = 2.0 * precision[start] * recall[start]
					/ (precision[start] + recall[start]);
			System.out.println("Precision : " + precision[start]);
			System.out.println("Recall : " + recall[start]);
			System.out.println("F1 : " + f1[start]);

			for (File file : inputDirectory.listFiles())
				file.delete();
			for (File file : testDirectory.listFiles())
				file.delete();
			initializeRandomGenerator();
		}

		double avgPrecision = 0.0;
		double avgRecall = 0.0;
		double avgF1 = 0.0;
		for (int i = 0; i < iteration; i++) {
			avgPrecision += precision[i];
			avgRecall += recall[i];
			avgF1 += f1[i];
		}

		System.out.println("_____________________-");
		System.out.println("Average Precision : " + avgPrecision / iteration);
		System.out.println("Average Recall : " + avgRecall / iteration);
		System.out.println("Average F1 : " + avgF1 / iteration);

		// Removes file of input and test folder
		for (File files : inputDirectory.listFiles())
			files.delete();
		for (File files : testDirectory.listFiles())
			files.delete();

		System.out.println("Finished");
	}

	private static void copyFileUsingFileChannels(File source, File dest)
			throws IOException {
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			inputChannel = new FileInputStream(source).getChannel();
			outputChannel = new FileOutputStream(dest.toString() + "\\"
					+ source.getName()).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		} finally {
			inputChannel.close();
			outputChannel.close();
		}
	}

	private static void initializeRandomGenerator() {
		gen = new Random();
		result = new int[5];
		used = new HashSet<Integer>();
	}

	private static int[] generateRandomNumbers() {
		assert n <= maxRange : "cannot get more unique numbers than the size of the range";
		for (int i = 0; i < n; i++) {

			int newRandom;
			do {
				newRandom = gen.nextInt(maxRange + 1);
			} while (used.contains(newRandom));
			result[i] = newRandom;
			used.add(newRandom);
		}
		return result;
	}

	private static void fileSelection() {
		File directory = new File("feature");
		inputDirectory = new File(inputDirectoryName);
		testDirectory = new File(testDirectoryName);
		if (!inputDirectory.exists()) {
			inputDirectory.mkdir();
		}
		if (!testDirectory.exists()) {
			testDirectory.mkdir();
		}
		File[] fList = directory.listFiles();
		int[] rand = generateRandomNumbers();
		int count = 0;
		for (int i = 0; i < 5; i++) {
			File f = fList[rand[i]];
			try {
				if (count < inputFile) {
					copyFileUsingFileChannels(f, inputDirectory);
					count++;
				} else {
					copyFileUsingFileChannels(f, testDirectory);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
