/*
 * File: INameSurferDataBase.java
 * -----------------------------
 * This interface keeps track of the complete database of names.
 * The public method makes it possible to look up a
 * name and get back the corresponding NameSurferEntry.
 * Names are matched independent of case, so that "Eric"
 * and "ERIC" are the same names.
 */

public interface INameSurferDataBase extends NameSurferConstants {

	/**
	 * Set path of the directory where data files locate
	 */
	public void setDir(String dir);

	/**
	 * Return path of the directory where data files locate
	 */

	public String getDir();

	/* Method: findEntry(name) */
	/**
	 * Returns the NameSurferEntry associated with this name, if one exists. If
	 * the name does not appear in the database, this method returns null.
	 */
	public INameSurferEntry findEntry(String name, String gender);
}
