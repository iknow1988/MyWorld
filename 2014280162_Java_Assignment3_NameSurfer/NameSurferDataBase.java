import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class NameSurferDataBase implements INameSurferDataBase {

	private File root;
	private HashMap<String, NameSurferEntry> namesDataBase;

	public NameSurferDataBase(String dir) {
		this.root = new File(dir);
		namesDataBase = new HashMap<String, NameSurferEntry>();
		init();
	}

	private void init() {
		for (int year = START_YEAR; year <= END_YEAR; year++) {
			String line;
			File file = new File(root, String.valueOf(year) + ".csv");

			try (BufferedReader reader = new BufferedReader(
					new FileReader(file));) {
				while ((line = reader.readLine()) != null) {
					String[] tokenizer = line.split(",");
					int rank = Integer.parseInt(tokenizer[0]);
					for (int i = 1; i <= 2; i++) {
						String name = tokenizer[i];
						String gender = (i == 1) ? GENDER_MALE : GENDER_FEMALE;
						String id = name.toLowerCase() + gender;
						if (namesDataBase.containsKey(id)) {
							namesDataBase.get(id).setRank(year, rank);
						} else {
							namesDataBase.put(id, new NameSurferEntry(name,
									gender, year, rank));
						}
					}
				}
			} catch (IOException e) {
				Utility.showErrorDialog("Error with some file. \n" + "error:"
						+ e.toString());
			} catch (NumberFormatException e) {
				Utility.showErrorDialog("Error, unknown format in "
						+ file.getAbsolutePath() + "\n");
				continue;
			}
		}
	}

	public void setDir(String dir) {
		root = new File(dir);
	}

	public String getDir() {
		return root.getAbsolutePath();
	}

	public INameSurferEntry findEntry(String name, String gender) {
		if (namesDataBase.containsKey(name.toLowerCase()+ gender)) {
			return namesDataBase.get(name.toLowerCase()+ gender);
		} else {
			return null;
		}
	}

}
