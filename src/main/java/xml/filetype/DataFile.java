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
	protected int startLine;
	protected int endLine;
	
	public DataFile(String path) {
		this.path = path;
		this.startLine = 1;
		this.endLine = Integer.MAX_VALUE;
	}

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
			case "plaintext": return new PlainTextFile(path);
			case "xls": return new XlsFile(path);
			case "xlsx": return new XlsxFile(path);
		} 
		throw new DataFileNotRecognizedException("Type " + type + " is not recognized");		
	}
	
	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}
	
	public int getStartLine() {
		return startLine;
	}
	
	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}
	
	public int getEndLine() {
		return endLine;
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
