import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadWithScanner {
	private static List<Edge> edges = new ArrayList<Edge>();

	public ReadWithScanner(String aFileName) {
		fFilePath = Paths.get(aFileName);
	}

	public final void processLineByLine() throws IOException {
		try (Scanner scanner = new Scanner(fFilePath, ENCODING.name())) {
			while (scanner.hasNextLine()) {
				processLine(scanner.nextLine());
			}
		}
	}

	public List<Edge> AdjacentList() {
		return edges;
	}

	protected void processLine(String aLine) {
		Scanner scanner = new Scanner(aLine);
		if (scanner.hasNext()) {
			Object from = scanner.next().substring(1);
			Object to = scanner.next();
			edges.add(new Edge(from, to));
		}
		scanner.close();
	}

	public void log(Object aObject) {
		System.out.println(String.valueOf(aObject));
	}

	// PRIVATE
	private final Path fFilePath;
	private final static Charset ENCODING = StandardCharsets.UTF_8;
}