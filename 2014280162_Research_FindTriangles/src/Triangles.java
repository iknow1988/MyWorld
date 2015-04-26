import java.io.IOException;
import java.util.*;

public class Triangles {
	
	public static int numberOfTriangles(List<Edge> edges) {
		Map<Object, Set<Object>> graph = getAdjacencyMatrix(edges);

		int trianglesCount = 0;
		for (Set<Object> neighbors : graph.values()) {
			for (Object n2 : neighbors) {
				for (Object n3 : neighbors) {
					if ((!n2.equals(n3)) && (graph.get(n2).contains(n3))) {
						trianglesCount++;
					}
				}
			}
		}

		return (trianglesCount / 6);
	}
	
	private static Map<Object, Set<Object>> getAdjacencyMatrix(List<Edge> edges) {
		Map<Object, Set<Object>> graph = new HashMap<>();
		
		if ((edges == null) || (edges.isEmpty())) {
			return Collections.<Object, Set<Object>> emptyMap();
		}

		for (Edge e : edges) {
			if (!graph.containsKey(e.fromNode())) {
				graph.put(e.fromNode(), new HashSet<Object>());
			}
			if (!graph.containsKey(e.toNode())) {
				graph.put(e.toNode(), new HashSet<Object>());
			}
			graph.get(e.fromNode()).add(e.toNode());
			graph.get(e.toNode()).add(e.fromNode());
		}

		return graph;
	}

	public static void main(String... aArgs) throws IOException {
		ReadWithScanner parser = new ReadWithScanner("test1.txt");
		//ReadWithScanner parser = new ReadWithScanner("AMiner-Coauthor.txt");
		parser.processLineByLine();
		List<Edge> edges = parser.AdjacentList();
		System.out.println(numberOfTriangles(edges));
		System.exit(0);
	}
}
