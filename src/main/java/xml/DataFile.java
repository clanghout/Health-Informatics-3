package xml;

import java.io.File;

/**
 * Class for a datafile.
 * @author Paul
 *
 */
public class DataFile extends File {

	private static final long serialVersionUID = 1L;

	/**
	 * The type of the datafile.
	 */
	private String type;
	
	/**
	 * Creates a new datafile.
	 * @param pathname the path to the file.
	 * @param t the type of the datafile.
	 */
	public DataFile(String pathname, String t) {
		super(pathname);
		this.type = t;
	}

	/**
	 * returns the type of the datafile.
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param t the type to set
	 */
	public void setType(String t) {
		this.type = t;
	}
	
	/**
	 * returns a string representation of the datafile.
	 * @return the string representing the file.
	 */
	@Override
	public String toString() {
		return "[" + super.toString() + ", type=" + type + "]";
	}
}
