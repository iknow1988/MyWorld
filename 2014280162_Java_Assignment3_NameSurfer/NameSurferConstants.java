/*
 * File: NameSurferConstants.java
 * ------------------------------
 * This file declares several constants that are shared by the
 * different modules in the NameSurfer application.  Any class
 * that implements this interface can use these constants.
 */

public interface NameSurferConstants {

/** The width of the application window */
	public static final int APPLICATION_WIDTH = 800;

/** The height of the application window */
	public static final int APPLICATION_HEIGHT = 600;

/** The name of the file containing the data */
	public static final String NAMES_DATA_FOLDER = "names";

/** The first year in the database */
	public static final int START_YEAR = 1901;

/** The first year in the database */
	public static final int END_YEAR = 2010;
	
/** The number of years between adjacent grid lines*/
	public static final int INTERVAL = 10;

/** The maximum rank in the database */
	public static final int MAX_RANK = 1000;

/** The number of pixels to reserve at the top and bottom */
	public static final int GRAPH_MARGIN_SIZE = 20;
	
	public static final int XOffset = 5;
	public static final int N_YEARS = 110;
	public static final int N_SECTIONS = 11;
	public static final String GENDER_MALE = "Male";
	public static final String GENDER_FEMALE = "Female";	
	/** The word delimiter in file */	
	public static final String WORD_DELIMITER = ",";

}
