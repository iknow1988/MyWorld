import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

public class ReadWithScanner {
	public static HashMap<String, StringBuilder> collection = new HashMap<String, StringBuilder>();
	public static HashMap<String, Boolean> address = new HashMap<String, Boolean>();
	
	// PRIVATE
	private final Path fFilePath;
	private final static Charset ENCODING = StandardCharsets.UTF_8;

	public ReadWithScanner(String aFileName) {
		fFilePath = Paths.get(aFileName);
	}

	public final void processTitlesInMap() throws IOException {
		try (Scanner scanner = new Scanner(fFilePath, ENCODING.name())) {
			String title = new String();
			String[] authors = null;
			int count = 0;
			while (scanner.hasNextLine()) {
				String aLine = scanner.nextLine();
				if (!aLine.isEmpty()) {
					String[] inputs = aLine.split(" ", 2);
					if (inputs[0].equals("#@")) {
						authors = inputs[1]
								.trim()
								.replaceAll("[^a-zA-Z0-9 ,;@#$%^&()_~+{}.-]",
										".").split(";");

					} else if (inputs[0].equals("#*")) {
						title = inputs[1];
					}
				} else {
					if (!title.isEmpty() && authors.length != 0) {
						for (String author : authors) {
							String auth = author.trim();
							StringBuilder fileName = new StringBuilder();
							fileName.append("E:\\Tsinghua\\Research\\Social Networking\\Project\\AMiner-Paper\\Authors\\");
							fileName.append(auth);
							fileName.append(".txt");
							if (collection.containsKey(auth)) {
								collection.put(auth, collection.get(auth)
										.append(". ").append(title));

							} else {
								StringBuilder sb = new StringBuilder();
								sb.append(title);
								collection.put(auth, sb);
								System.out.println(++count);
							}
						}
					}
					title = new String();
					authors = null;
				}
			}
		}
	}

	public final void processTitlesInFiles() throws IOException {
		try (Scanner scanner = new Scanner(fFilePath, ENCODING.name())) {
			String title = new String();
			String[] authors = null;
			int count = 0;
			while (scanner.hasNextLine()) {
				String aLine = scanner.nextLine();
				if (!aLine.isEmpty()) {
					String[] inputs = aLine.split(" ", 2);
					if (inputs[0].equals("#@")) {
						authors = inputs[1]
								.trim()
								.replaceAll("[^a-zA-Z0-9 ,;@#$%^&()_~+{}.-]",
										".").split(";");

					} else if (inputs[0].equals("#*")) {
						title = inputs[1];
					}
				} else {
					if (!title.isEmpty() && authors.length != 0) {
						for (String author : authors) {
							String auth = author.trim();
							StringBuilder fileName = new StringBuilder();
							fileName.append("E:\\Tsinghua\\Research\\Social Networking\\Project\\AMiner-Paper\\Authors\\");
							fileName.append(auth);
							fileName.append(".txt");
							if (address.containsKey(auth)) {
								File file = new File(fileName.toString());
								try (PrintWriter out = new PrintWriter(
										new BufferedWriter(new FileWriter(file,
												true)))) {
									out.println(title);
								} catch (IOException e) {
								}
								address.put(auth, true);

							} else {
								try (PrintWriter out = new PrintWriter(
										new BufferedWriter(new FileWriter(
												fileName.toString(), true)))) {
									out.println(title);
									address.put(auth, true);
								} catch (IOException e) {
								}
								System.out.println(++count);
							}
						}
					}
					title = new String();
					authors = null;
				}
			}
		}
	}

	public final void processAbstractInMap() throws IOException {
		collection.clear();
		try (Scanner scanner = new Scanner(fFilePath, ENCODING.name())) {
			String paperAbstract = new String();
			String[] authors = null;
			int count = 0;
			while (scanner.hasNextLine()) {
				String aLine = scanner.nextLine();
				if (!aLine.isEmpty()) {
					String[] inputs = aLine.split(" ", 2);
					if (inputs[0].equals("#@")) {
						authors = inputs[1]
								.trim()
								.replaceAll("[^a-zA-Z0-9 ,;@#$%^&()_~+{}.-]",
										".").split(";");

					} else if (inputs[0].equals("#!")) {
						paperAbstract = inputs[1];
					}
				} else {
					if (authors.length != 0) {
						for (String author : authors) {
							String auth = author.trim();
							StringBuilder fileName = new StringBuilder();
							fileName.append("E:\\Tsinghua\\Research\\Social Networking\\Project\\AMiner-Paper\\Authors\\");
							fileName.append(auth);
							fileName.append(".txt");
							if (collection.containsKey(auth)) {
								collection.put(auth, collection.get(auth)
										.append(". ").append(paperAbstract));

							} else {
								StringBuilder sb = new StringBuilder();
								sb.append(". ");
								sb.append(paperAbstract);
								collection.put(auth, sb);
								System.out.println(++count);
							}
						}
					}
					paperAbstract = new String();
					authors = null;
				}
			}
		}
	}
}