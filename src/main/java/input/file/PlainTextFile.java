package input.file;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Class to specify a .txt file.
 * @author Paul
 *
 */
public class PlainTextFile extends DataFile {
	
	private Scanner scanner;
	private StringBuilder builder;
	private int counter;
	
	public PlainTextFile(String path) {
		super(path);
	}
	
	@Override
	public InputStream getDataStream() throws IOException {
		builder = new StringBuilder();
		counter = 1;
		InputStream stream = new FileInputStream(getFile());
		scanner = new Scanner(stream, "UTF-8");
		scanner.useDelimiter("\\A");
		skipToStartLine();
		readLines();
		scanner.close();
		
		InputStream newStream = new ByteArrayInputStream(
				builder.toString().getBytes(StandardCharsets.UTF_8)
		);
		
		return newStream;
	}
	
	private void skipToStartLine() throws IOException {
		if (!scanner.hasNextLine()) {
			scanner.close();
			throw new IOException("Nothing to read");
		}
		
		while (counter != getStartLine() && scanner.hasNextLine()) {
			scanner.nextLine();
			counter++;
		}
	}
	
	private void readLines() {
		while (counter <= getEndLine() && scanner.hasNextLine()) {
			builder.append(scanner.nextLine() + "\n");
			counter++;
		}
	}
}