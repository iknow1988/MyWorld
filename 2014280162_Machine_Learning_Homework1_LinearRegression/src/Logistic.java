import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Logistic {

	private double rate;
	private int ITERATIONS = 1000;
	public HashMap<Integer, Double> weights = new HashMap<Integer, Double>();

	public Logistic() {
		this.rate = 0.0001;
	}

	private double sigmoid(double z) {
		return 1 / (1 + Math.exp(-z));
	}

	public void train(ArrayList<Document> instances) {
		for (int n = 0; n < ITERATIONS; n++) {
			for (int i = 0; i < instances.size(); i++) {
				HashMap<Integer, Integer> x = instances.get(i).getDictionary();
				double predicted = classify(x);
				int label = instances.get(i).getLabel();
				Iterator<Entry<Integer, Integer>> it = x.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<Integer, Integer> pairs = it.next();
					int featureValue = 0;
					double weight = 0.0;
					if (x.containsKey(pairs.getKey())) {
						featureValue = x.get(pairs.getKey());
					}
					if (weights.containsKey(pairs.getKey())) {
						weight = weights.get(pairs.getKey());
					}
					weight = weight + rate * (label - predicted) * featureValue;
					weights.put(pairs.getKey(), weight);
				}
			}
		}
	}

	public double classify(HashMap<Integer, Integer> x) {
		double logit = .0;
		Iterator<Entry<Integer, Integer>> it = x.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, Integer> pairs = it.next();
			int featureValue = 0;
			double weight = 0;
			if (x.containsKey(pairs.getKey())) {
				featureValue = x.get(pairs.getKey());
			}
			if (weights.containsKey(pairs.getKey())) {
				weight = weights.get(pairs.getKey());
			}
			logit += weight * featureValue;
		}
		return sigmoid(logit);
	}
}