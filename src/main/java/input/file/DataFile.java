package input.file;

import java.io.*;

import exceptions.DataFileNotRecognizedException;
/**
 * Class for a datafile.
 *
 * @author Paul
 */
public abstract class DataFile {

	private String path;
	private int startLine;
	private int endLine;
	
	/**
	 * Creates a new type of a DataFile. Sets the default range of lines to read
	 * from 1 to integer maxvalue.
	 * @param path The path to the DataFile
	 */
	public DataFile(String path) {
		this.path = path;
		this.setStartLine(1);
		this.setEndLine(Integer.MAX_VALUE);
	}

	/**
	 * Returns the rows of the DataFile only. All redundant data is filtered out.
	 *
	 * @return A stream containing the data contents of the file
	 * @throws IOException When the file is not found or if the file is corrupt
	 */
	public abstract InputStream getDataStream() throws IOException;
	
	/**
	 * Gets a new File object directing to the dataFile on the system.
	 * @return The file
	 * @throws FileNotFoundException When the file is not found
	 */
	public File getFile() throws FileNotFoundException {
		return new File(path);
	}

	/**
	 * Returns the path of the DataFile.
	 * @return The path
	 */
	public String getPath() {
		return path;
	}
	
	/**
	* Creates a new DataFile type class based on the string specified.
	* @param path The path of the DataFile
	* @param type The type specifying what type of DataFile should be created
	* @return A new DataFile
	* @throws DataFileNotRecognizedException
	*/
	public static DataFile createDataFile(String path, String type) 
			throws DataFileNotRecognizedException {
		switch(type) {
			case "plaintext": return new PlainTextFile(path);
			case "xls": return new XlsFile(path);
			case "xlsx": return new XlsxFile(path);
			default: throw new DataFileNotRecognizedException("Type " + type 
					+ " is not recognized");
		} 	
	}
	
	/**
	 * Returns the line at which the datafile will begin reading i.e. the 
	 * first line of actual data in the datafile.
	 * @return The startLine
	 */
	public int getStartLine() {
		return startLine;
	}

	/**
	 * Sets the line at which the datafile will begin reading i.e. the
	 * first line of actual data in the datafile.
	 * @param startLine the startLine to set
	 */
	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}

	/**
	 * Returns the line at which the datafile will stop reading i.e. the last
	 * line of actual data in the datafile.
	 * @return the endLine
	 */
	public int getEndLine() {
		return endLine;
	}

	/**
	 * Sets the line at which the datafile will stop reading i.e. the last
	 * line of actual data in the datafile.
	 * @param endLine the endLine to set
	 */
	public void setEndLine(int endLine) {
		this.endLine = endLine;
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
