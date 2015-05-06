package xml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for a datafile.
 * @author Paul
 *
 */
public class DataFile {

	private static final long serialVersionUID = 1L;

	private String type;
	private String headerPattern;
	private String filename;

	/**
	 * Creates a new DataFile.
	 * @param filename The path to the file.
	 * @param type The type of the DataFile.
	 * @param headerPattern The RegEx pattern specifying the header of the DataFile.
	 */
	public DataFile(String filename, String type, String headerPattern) {
		this.filename = filename;
		this.type = type;
		this.headerPattern = headerPattern;
	}

	/**
	 * Returns the type of the DataFile.
	 * @return The type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the headerPattern.
	 */
	public String getHeaderPattern() {
		return headerPattern;
	}

	/**
	 * @param headerPattern The headerPattern to set.
	 */
	public void setHeaderPattern(String headerPattern) {
		this.headerPattern = headerPattern;
	}

	/**
	 * Filters out the header of the file using regEx.
	 * @return A stream containing the contents of the file excluding the header
	 * @throws FileNotFoundException
	 */
	public InputStream filterHeader() throws FileNotFoundException {
		InputStream stream = new FileInputStream(filename);
		Scanner scanner = new Scanner(stream, "UTF-8");
		scanner.useDelimiter("\\A");
		String convertedStream = scanner.hasNext() ? scanner.next() : "";
		scanner.close();
		
		Pattern pattern = Pattern.compile(headerPattern);
		Matcher matcher = pattern.matcher(convertedStream);
		convertedStream = matcher.replaceAll("");

		InputStream newStream = new ByteArrayInputStream(convertedStream.getBytes(StandardCharsets.UTF_8));
		return newStream;
	}

	public File getFile() throws FileNotFoundException{
		return new File(filename);
	}

	public String getFilename() {
		return filename;
	}

	/**
	 * Returns a string representation of the datafile.
	 * @return The string representing the file.
	 */
	@Override
	public String toString() {
		return "[" + super.toString() + ", type=" + type + ", header=" + headerPattern + "]";
	}
}
