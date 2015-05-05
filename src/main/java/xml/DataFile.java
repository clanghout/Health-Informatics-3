package xml;

import java.io.File;

/**
 * Class for a datafile.
 * @author Paul
 *
 */
public class DataFile extends File {

	private static final long serialVersionUID = 1L;

	private String type;
	
	public DataFile(String pathname, String type) {
		super(pathname);
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	public String toString() {
		return "[" + super.toString() + ", type=" + type + "]";
	}
}
