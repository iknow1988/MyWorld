import java.util.ArrayList;
import java.util.HashMap;

class Collection {
	private ArrayList<Document> documentList = new ArrayList<Document>();
	private HashMap<Integer, Integer> dictionary = new HashMap<Integer, Integer>();
	private HashMap<String, Integer> id_lookup = new HashMap<String, Integer>();

	public ArrayList<Document> getDocumentList() {
		return this.documentList;
	}

	public HashMap<Integer, Integer> getDictionary() {
		return this.dictionary;
	}

	public HashMap<String, Integer> getIdLookup() {
		return this.id_lookup;
	}
}