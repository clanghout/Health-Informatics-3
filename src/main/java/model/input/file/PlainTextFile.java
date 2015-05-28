package model.input.file;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Row;

import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.value.DataValue;
import model.data.value.StringValue;

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
		builder.setName(this.getFile().getName());
		counter = 1;
		InputStream stream = new FileInputStream(getFile());
		scanner = new Scanner(stream, "UTF-8");
		scanner.useDelimiter("\\A");
		skipToStartLine();
		if(hasFirstRowAsHeader()) {
			String headers = scanner.nextLine();
			String[] sections = headers.split(",");
			for(int i = 0; i < getColumnTypes().length; i++) {
				getColumns().put(sections[i], getColumnTypes()[i]);
			}
		} else {			
			for (String key : getColumns().keySet()) {
				builder.createColumn(key, getColumns().get(key));
			}
		}
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
			String line = scanner.nextLine();
			String[] sections = line.split(",");
			Stream<DataValue> values = Arrays.stream(sections).map(StringValue::new);
			builder.createRow(values.toArray(DataValue[]::new));

			counter++;
		}
	}
}