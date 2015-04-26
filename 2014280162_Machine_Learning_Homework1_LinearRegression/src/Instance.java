import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

public class Instance {
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	private Collection featureCollection;
	private String directoryName;

	public Instance(String directoryName) {
		featureCollection = new Collection();
		this.directoryName = directoryName;
		createDictionary(directoryName);
	}

	/**
	 * Process raw input to create feature file
	 */
	public void processInput(String featureDirectoryName) {
		File folder = new File(featureDirectoryName);
		if (!folder.exists()) {
			createFeatureFile(directoryName);
			writeToFile();
		}
	}

	/**
	 * Creates a global dictionary of all words
	 */
	private void createDictionary(String directoryName) {
		File directory = new File(directoryName);
		File[] fList = directory.listFiles();
		int id = 1;
		for (File file : fList) {
			if (file.isFile() && !file.getName().endsWith("DS_Store")) {
				try (Scanner scanner = new Scanner(file, ENCODING.name())) {
					while (scanner.hasNextLine()) {
						String aLine = scanner.nextLine();
						if (aLine.startsWith("From:")) {
							continue;
						}
						String[] tokenize = aLine.split(" ");
						for (String words : tokenize) {
							String word = words.replaceAll(
									"[^A-Za-z0-9\\[\\]]", "");
							if (Stopwords.isStopword(word)) {
								continue;
							}
							if (!featureCollection.getIdLookup().containsKey(
									word)) {
								featureCollection.getIdLookup().put(word, id);
								id++;
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (file.isDirectory()) {
				createDictionary(file.getAbsolutePath());
			}
		}
	}

	/**
	 * Creates a collection of documents
	 */
	private void createFeatureFile(String directoryName) {
		createDictionary(directoryName);
		File directory = new File(directoryName);
		File[] fList = directory.listFiles();
		int game = 0;
		if (directory.getName().contains("hockey")) {
			game = 0;
		} else if (directory.getName().contains("baseball")) {
			game = 1;
		}
		for (File file : fList) {
			if (file.isFile() && !file.getName().endsWith("DS_Store")) {
				try (Scanner scanner = new Scanner(file, ENCODING.name())) {
					Document doc = new Document(file.getParentFile()
							.getParentFile().getName()
							+ "_" + file.getName(), file.getParentFile()
							.getParentFile().getName(), game);
					while (scanner.hasNextLine()) {
						String aLine = scanner.nextLine();
						if (aLine.startsWith("From:")) {
							continue;
						}
						String[] tokenize = aLine.split(" ");
						for (String words : tokenize) {
							String word = words.replaceAll(
									"[^A-Za-z0-9\\[\\]]", "");
							if (Stopwords.isStopword(word))
								continue;
							int id = featureCollection.getIdLookup().get(word);
							// //
							if (doc.getDictionary().containsKey(id)) {
								doc.getDictionary().put(id,
										doc.getDictionary().get(id) + 1);
							} else {
								doc.getDictionary().put(id, 1);
							}
							// ///
						}
					}
					featureCollection.getDocumentList().add(doc);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (file.isDirectory()) {
				createFeatureFile(file.getAbsolutePath());
			}
		}
	}

	/**
	 * Loads feature from input and test file
	 */
	public Collection loadInstance(String directoryName) {
		Collection collection = new Collection();
		File directory = new File(directoryName);
		File[] fList = directory.listFiles();
		for (File file : fList) {
			try (Scanner scanner = new Scanner(file, ENCODING.name())) {
				while (scanner.hasNextLine()) {
					String aLine = scanner.nextLine();
					String[] tokenize = aLine.split(" ", 2);
					Document doc = new Document(Integer.parseInt(tokenize[0]));
					String[] part = tokenize[1].split(" ");
					for (String word : part) {
						String[] in = word.split(":");
						int value = 0;
						int key = 0;
						try {
							key = Integer.parseInt(in[0]);
							value = Integer.parseInt(in[1]);
						} catch (NumberFormatException ex) {
							continue;
						}
						if (collection.getDictionary().containsKey(key)) {
							collection.getDictionary()
									.put(key,
											collection.getDictionary().get(key)
													+ value);
						} else {
							collection.getDictionary().put(key, value);
						}
						doc.getDictionary().put(key, value);
					}
					collection.getDocumentList().add(doc);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(file.getAbsolutePath());
		}

		return collection;
	}

	/**
	 * Writes feature files in the directory
	 */
	private void writeToFile() {
		for (Document doc : getFeatureCollection().getDocumentList()) {
			StringBuilder fileName = new StringBuilder();
			fileName.append("feature\\");
			fileName.append(doc.getFolderName());
			fileName.append(".txt");
			File folder = new File("feature");
			if (!folder.exists()) {
				folder.mkdir();
			}
			File file = new File(fileName.toString());
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try (PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(file, true)))) {
				out.print(doc.getLabel() + " ");
				Iterator<Entry<Integer, Integer>> it = doc.getDictionary()
						.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<Integer, Integer> pairs = it.next();
					out.print(pairs.getKey() + ":" + pairs.getValue() + " ");
				}
				out.println();
			} catch (IOException e) {
			}
		}
	}

	private Collection getFeatureCollection() {
		return this.featureCollection;
	}
}