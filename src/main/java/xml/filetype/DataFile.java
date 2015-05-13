package xml.filetype;

import java.io.*;

import exceptions.DataFileNotRecognizedException;
/**
 * Class for a datafile.
 *
 * @author Paul
 */
public abstract class DataFile {

	protected String path;
	
	public DataFile(String path) {
		this.path = path;
	}
	
	/**
	 * Returns the type of the DataFile.
	 *
	 * @return The type
	 */
	public abstract String getType();
	
	/**
	 * Returns the rows of the DataFile only. All redundant data is filtered out.
	 *
	 * @return A stream containing the contents of the file excluding the header
	 * @throws FileNotFoundException
	 */
	public abstract InputStream getDataStream() throws FileNotFoundException;
	
	public File getFile() throws FileNotFoundException {
		return new File(path);
	}

	public String getPath() {
		return path;
	}
	
	public static DataFile createDataFile(String path, String type) throws DataFileNotRecognizedException {
		switch(type) {
			case "statsensor": return new StatsensorFile(path);
			case "admireinput": return new XlsFile(path);
		} 
		throw new DataFileNotRecognizedException("Type " + type + " is not recognized");		
	}
	
	/**
	 * Returns a string representation of the datafile.
	 *
	 * @return The string representing the file.
	 */
	@Override
	public String toString() {
		return "[" + path + "]";
	}
}
