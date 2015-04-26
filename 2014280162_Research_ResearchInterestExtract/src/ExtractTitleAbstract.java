import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

public class ExtractTitleAbstract {
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	public static ReadWithScanner parser = new ReadWithScanner(
			"E:\\Tsinghua\\Research\\Social Networking\\Project\\AMiner-Paper\\AMiner-Paper.txt");

	public static void main(String[] args) {
//		 try {
//		 parser.processTitlesInFiles();
//		 System.out.println("Now writing to file");
//		 writeToFile();
//		
//		 System.out.println("End");
//		 } catch (IOException e) {
//		 }
		findKeyItems();
	}

	public static void writeToFile() {
		try (PrintWriter out = new PrintWriter(
				new BufferedWriter(
						new FileWriter(
								"E:\\Tsinghua\\Research\\Social Networking\\Project\\AMiner-Paper\\values.txt",
								true)))) {
			Iterator<Entry<String, StringBuilder>> it = parser.collection
					.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, StringBuilder> pairs = it.next();
				out.write(pairs.getKey() + " @ " + pairs.getValue() + "\n");
			}
		} catch (IOException e) {
		}
	}

	public static void findKeyItems() {
		KeyTermFindingService s = KeyTermFindingService.getInstance();
		try (Scanner scanner = new Scanner(
				Paths.get("E:\\Tsinghua\\Research\\Social Networking\\Project\\AMiner-Paper\\input.txt"))) {
			String title = new String();
			String author = new String();
			int count = 0;
			while (scanner.hasNextLine()) {
				String aLine = scanner.nextLine();
				String[] inputs = aLine.split("@");
				author = inputs[0];
				title = inputs[1];
				List<String> keyTermsByString = s.extractKeyTerms(1, 10, title);
				try (PrintWriter out = new PrintWriter(
						new BufferedWriter(
								new FileWriter(
										"E:\\Tsinghua\\Research\\Social Networking\\Project\\AMiner-Paper\\keywords.txt",
										true)))) {
					out.print(author + " @ ");
					for (String string : keyTermsByString) {
						out.print(string + " | ");
					}
					out.println();
				} catch (IOException e) {
				}
				System.out.println(++count);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
