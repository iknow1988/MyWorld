import java.util.HashMap;

class Document {
	private String name;
	private String folderName;
	private int label;
	private HashMap<Integer, Integer> dictionary;

	public Document(String name, String folderName, int game) {
		this.name = name;
		this.folderName = folderName;
		this.label = game;
		dictionary = new HashMap<Integer, Integer>();
	}

	public Document(int game) {
		this.label = game;
		dictionary = new HashMap<Integer, Integer>();
	}

	public String getDocumentName() {
		return this.name;
	}

	public String getFolderName() {
		return this.folderName;
	}

	public int getLabel() {
		return this.label;
	}

	public HashMap<Integer, Integer> getDictionary() {
		return this.dictionary;
	}
}