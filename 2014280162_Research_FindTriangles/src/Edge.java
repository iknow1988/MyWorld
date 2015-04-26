public class Edge {
	private final Object from;
	private final Object to;

	public Edge(Object from, Object to) {
		this.from = from;
		this.to = to;
	}

	public Object fromNode() {

		return from;
	}

	public Object toNode() {

		return to;
	}
}