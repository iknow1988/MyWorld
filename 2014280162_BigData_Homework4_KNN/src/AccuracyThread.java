import java.util.ArrayList;
import java.util.HashMap;

public class AccuracyThread extends Thread {
	private int from, to;
	private ArrayList<DataPoint> testData;
	private ArrayList<ArrayList<DataPoint>> result;
	private HashMap<String, String> attackType;
	private HashMap<String, Integer> idMap = new HashMap<String, Integer>();
	private int[][][] accuracyMatrix = new int[2][5][5];// one for K=1 and other
														// for K=3

	public AccuracyThread(int from, int to, ArrayList<DataPoint> testData,
			HashMap<String, String> attackType) {
		this.testData = testData;
		this.from = from;
		this.to = to;
		this.attackType = attackType;
		idMap.put("normal", 0);
		idMap.put("u2r", 1);
		idMap.put("r2l", 2);
		idMap.put("probe", 3);
		idMap.put("dos", 4);
	}

	public void setResult(ArrayList<ArrayList<DataPoint>> result) {
		this.result = result;
	}

	private String getAttackType(String input) {
		String output = attackType.get(input);
		if (output == null) {
			output = input;
		}
		return output;
	}

	public void run() {
		String predicted = null;
		String actual = null;
		for (int i = from; i < to; i++) {
			ArrayList<DataPoint> tops = result.get(i);
			DataPoint test = testData.get(i);
			actual = getAttackType(test.getLabel());
			predicted = getAttackType(tops.get(0).getLabel());
			if (actual != null) {
				accuracyMatrix[0][idMap.get(actual)][idMap.get(predicted)] = accuracyMatrix[0][idMap
						.get(actual)][idMap.get(predicted)] + 1;
				HashMap<String, Integer> count = new HashMap<String, Integer>();
				for (DataPoint point : tops) {
					if (count.containsKey(getAttackType(point.getLabel()))) {
						predicted = getAttackType(point.getLabel());
						break;
					} else {
						count.put(getAttackType(point.getLabel()), 1);
					}
				}
				accuracyMatrix[1][idMap.get(actual)][idMap.get(predicted)] = accuracyMatrix[1][idMap
						.get(actual)][idMap.get(predicted)] + 1;
			} else {
				System.out.print("\t" + predicted + "\t");
				HashMap<String, Integer> count = new HashMap<String, Integer>();
				for (DataPoint point : tops) {
					if (count.containsKey(getAttackType(point.getLabel()))) {
						predicted = getAttackType(point.getLabel());
						break;
					} else {
						count.put(getAttackType(point.getLabel()), 1);
					}
				}
				System.out.print(predicted);
				System.out.println();
			}
		}
	}

	public int[][][] getAccuracyMatrix() {
		return this.accuracyMatrix;
	}

	public HashMap<String, Integer> getID() {
		return this.idMap;
	}
}
