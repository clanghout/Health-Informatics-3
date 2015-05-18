package xml.filetype;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Class to specify a .txt file.
 * @author Paul
 *
 */
public class PlainTextFile extends DataFile {
	
	public PlainTextFile(String path) {
		super(path);
	}
	
	@Override
	public InputStream getDataStream() throws FileNotFoundException {
		InputStream stream = new FileInputStream(getFile());
		Scanner scanner = new Scanner(stream, "UTF-8");
		scanner.useDelimiter("\\A");
		StringBuilder builder = new StringBuilder();
		int i = 1;

		//Skip to beginline
		while (i != getStartLine() && scanner.hasNextLine()) {
			scanner.nextLine();
			i++;
		}
		
		while (i <= getEndLine() && scanner.hasNextLine()) {
			
			builder.append(scanner.nextLine() + "\n");
			i++;
		}
		
		scanner.close();
		
		InputStream newStream = new ByteArrayInputStream(
				builder.toString().getBytes(StandardCharsets.UTF_8)
		);
		
		return newStream;
	}

}