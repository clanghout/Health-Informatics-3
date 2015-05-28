package model.input.file;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import model.data.DataTable;
import model.data.DataTableBuilder;

/**
 * Class to specify a .txt file.
 * @author Paul
 *
 */
public class PlainTextFile extends DataFile {
	
	private Scanner scanner;
	private DataTableBuilder builder;
	private int counter;
	
	public PlainTextFile(String path) {
		super(path);
	}
	
	@Override
	public DataTable createDataTable() throws IOException {
		builder = new DataTableBuilder();
		counter = 1;
		InputStream stream = new FileInputStream(getFile());
		scanner = new Scanner(stream, "UTF-8");
		scanner.useDelimiter("\\A");
		skipToStartLine();
		readLines();
		scanner.close();
		return builder.build();
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
			
			counter++;
		}
	}
}