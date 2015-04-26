import java.util.HashMap;

public class NameSurferEntry implements INameSurferEntry {
	private String name;
	private String gender;
	private HashMap<Integer, Integer> rankings;

	public NameSurferEntry(String name, String gender, int year, int rank) {
		this.name = name;
		this.gender = gender;
		rankings = new HashMap<Integer, Integer>();
		rankings.put(year, rank);
	}

	public String getName() {

		return this.name;
	}

	/**
	 * Sets the name associated with this entry.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the gender associated with this entry.
	 */
	public String getGender() {

		return this.gender;
	}

	/**
	 * Sets the gender associated with this entry.
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * Returns the rank associated with an entry for a particular year. If a
	 * name is not among the first 1000 names of the particular year, the method
	 * should return 0.
	 */
	public int getRank(int year) {

		return (rankings.containsKey(year)) ? rankings.get(year) : 0;
	}

	public void setRank(int year, int rank) {
		rankings.put(year, rank);
	}

	public String toString() {
		String str = "{";
		str += this.getName() + ", ";
		str += this.getGender() + ", ";
		str += this.rankings.toString() + "}\n";

		return str;
	}
}
