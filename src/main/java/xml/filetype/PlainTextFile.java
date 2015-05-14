package xml.filetype;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to specify a .txt file.
 * @author Paul
 *
 */
public class PlainTextFile extends DataFile {

	private String headerPattern;
	
	public PlainTextFile(String path) {
		super(path);
	}
	
	@Override
	public InputStream getDataStream() throws FileNotFoundException {
		InputStream stream = new FileInputStream(path);
		Scanner scanner = new Scanner(stream, "UTF-8");
		scanner.useDelimiter("\\A");
		String convertedStream = scanner.hasNext() ? scanner.next() : "";
		scanner.close();

		Pattern pattern = Pattern.compile(headerPattern);
		Matcher matcher = pattern.matcher(convertedStream);
		convertedStream = matcher.replaceAll("");

		InputStream newStream = new ByteArrayInputStream(
				convertedStream.getBytes(StandardCharsets.UTF_8)
		);
		return newStream;
	}

}