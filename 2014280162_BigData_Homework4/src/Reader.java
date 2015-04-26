import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Reader {
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	private HashMap<String, Float> map = new HashMap<String, Float>();
	private HashMap<String, String> attackType = new HashMap<String, String>();
	private ArrayList<DataPoint> trainDataPoint = new ArrayList<DataPoint>();
	private ArrayList<DataPoint> testDataPoint = new ArrayList<DataPoint>();
	private float[] max = new float[41];
	private float[] min = new float[41];
	private int id = 0;
	private String trainingtDataFileName;
	private String testingDataFileName;

	public Reader(String trainingFile, String testingFile) {
		this.trainingtDataFileName = trainingFile;
		this.testingDataFileName = testingFile;
		attackType.put("normal", "normal");
		getDataPoints();
		getTestDataPoints();
		getAttackTypes();
	}

	private void getDataPoints() {
		File file = new File(trainingtDataFileName);
		try (Scanner scanner = new Scanner(file, ENCODING.name())) {
			while (scanner.hasNextLine()) {
				ArrayList<Float> point = new ArrayList<Float>();
				String aLine = scanner.nextLine();
				String[] inputs = aLine.split(",");
				float val = 0.0f;
				int i = 0;
				String label = null;
				for (String input : inputs) {
					try {
						val = Float.parseFloat(input);
					} catch (NumberFormatException ex) {
						input = input.trim().replace(".", "");
						if (map.containsKey(input)) {
							val = map.get(input);
						} else {
							val = (float) id;
							map.put(input, val);
							id++;
						}
					}
					if (i < 41) {
						if (val > max[i]) {
							max[i] = val;
						}
						if (val < min[i]) {
							min[i] = val;
						}
					} else {
						label = input;
					}
					point.add(val);
					i++;
				}
				trainDataPoint.add(new DataPoint(point, label));
			}
		} catch (FileNotFoundException e) {
			System.out.println("Input File not found");
		}
	}

	private void getTestDataPoints() {
		File file = new File(testingDataFileName);
		try (Scanner scanner = new Scanner(file, ENCODING.name())) {
			while (scanner.hasNextLine()) {
				ArrayList<Float> point = new ArrayList<Float>();
				String aLine = scanner.nextLine();
				String[] inputs = aLine.split(",");
				float val = 0.0f;
				int i = 0;
				String label = null;
				for (String input : inputs) {
					try {
						val = Float.parseFloat(input);
					} catch (NumberFormatException ex) {
						input = input.trim().replace(".", "");
						if (map.containsKey(input)) {
							val = map.get(input);
						} else {
							val = (float) id;
							map.put(input, val);
							id++;
						}
					}
					if (i < 41) {
						if (val > max[i]) {
							max[i] = val;
						}
						if (val < min[i]) {
							min[i] = val;
						}
					} else {
						label = input;
					}
					point.add(val);
					i++;
				}
				testDataPoint.add(new DataPoint(point, label));
			}
		} catch (FileNotFoundException e) {
			System.out.println("Input File not found");
		}
	}

	private void getAttackTypes() {
		File file = new File("training_attack_types");
		try (Scanner scanner = new Scanner(file, ENCODING.name())) {
			while (scanner.hasNextLine()) {
				String aLine = scanner.nextLine();
				String[] inputs = aLine.split(" ");
				attackType.put(inputs[0].trim(), inputs[1].trim());
			}
		} catch (FileNotFoundException e) {
			System.out.println("Input File not found");
		}
	}

	public ArrayList<DataPoint> getAllTrainingData() {
		return this.trainDataPoint;
	}

	public ArrayList<DataPoint> getAllTestingData() {
		return this.testDataPoint;
	}

	public HashMap<String, String> getAllAttackTypes() {
		return this.attackType;
	}

	public float[] getMaxArray() {
		return this.max;
	}

	public float[] getMinArray() {
		return this.min;
	}
}
