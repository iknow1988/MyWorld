import java.util.ArrayList;

public class DistanceThread extends Thread {

	private int from, to;
	private ArrayList<DataPoint> testData;
	private ArrayList<DataPoint> trainData;
	private ArrayList<ArrayList<DataPoint>> distances = new ArrayList<ArrayList<DataPoint>>();
	private static final float INF = 9999999999.00f;

	public DistanceThread(int from, int to, ArrayList<DataPoint> test_Data,
			ArrayList<DataPoint> trainData) {
		this.trainData = trainData;
		this.from = from;
		this.to = to;
		this.testData = test_Data;
	}

	private Double distance(ArrayList<Float> testData,
			ArrayList<Float> trainData) {
		double distance = 0.0;
		for (int i = 0; i < testData.size() - 1; i++) {
			float testPoint = testData.get(i);
			float trainPoint = trainData.get(i);
			double dist = Math.abs(testPoint - trainPoint);
			distance += dist * dist;
		}
		distance = Math.sqrt(distance);

		return distance;
	}

	public void run() {
		for (int i = from; i < to; i++) {
			DataPoint test = testData.get(i);
			ArrayList<DataPoint> bestPoints = new ArrayList<DataPoint>();
			DataPoint bestPoint = null;
			DataPoint secondBestPoint = null;
			DataPoint thirdBestPoint = null;
			double best_distance = INF;
			double second_best_distance = INF;
			double third_best_distance = INF;
			for (DataPoint train : trainData) {
				ArrayList<Float> testData = test.getData();
				ArrayList<Float> trainData = train.getData();
				double distance = 0.0;
				distance = distance(testData, trainData);
				if (distance < best_distance) {
					thirdBestPoint = secondBestPoint;
					third_best_distance = second_best_distance;
					secondBestPoint = bestPoint;
					second_best_distance = best_distance;
					bestPoint = train;
					best_distance = distance;
				} else {
					if (distance < second_best_distance) {
						thirdBestPoint = secondBestPoint;
						third_best_distance = second_best_distance;
						secondBestPoint = train;
						second_best_distance = distance;
					} else if (distance < third_best_distance) {
						thirdBestPoint = train;
						third_best_distance = distance;
					}
				}
			}
			bestPoints.add(bestPoint);
			bestPoints.add(secondBestPoint);
			bestPoints.add(thirdBestPoint);
			distances.add(bestPoints);
		}
	}

	public ArrayList<ArrayList<DataPoint>> getAllBestDistances() {
		return this.distances;
	}
}