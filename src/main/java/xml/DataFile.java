package xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for a datafile.
 * @author Paul
 *
 */
public class DataFile extends File {

	private static final long serialVersionUID = 1L;

	private String type;
	private String headerPattern;

	/**
	 * Creates a new DataFile.
	 * @param pathname The path to the file.
	 * @param t The type of the DataFile.
	 * @param hp The regEx pattern specifying the header of the DataFile.
	 */
	public DataFile(String pathname, String t, String hp) {
		super(pathname);
		this.type = t;
		this.headerPattern = hp;
	}

	/**
	 * Returns the type of the DataFile.
	 * @return The type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param t The type to set.
	 */
	public void setType(String t) {
		this.type = t;
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
	 * @return
	 * @throws FileNotFoundException
	 */
	public InputStream filterHeader() throws FileNotFoundException {
		InputStream stream = getClass().getResourceAsStream(this.getPath());
		Scanner scanner = new Scanner(stream, "UTF_8");
		scanner.useDelimiter("\\A");
		String convertedStream = scanner.hasNext() ? scanner.next() : "";
		scanner.close();
		
		Pattern pattern = Pattern.compile(headerPattern);
		Matcher matcher = pattern.matcher(convertedStream);
		convertedStream = matcher.replaceAll("");

		InputStream newStream = new ByteArrayInputStream(convertedStream.getBytes(StandardCharsets.UTF_8));
		return newStream;
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
