package model.input.file;

import model.data.DataTable;
import model.data.value.DataValue;
import model.data.value.DateTimeValue;
import model.data.value.DateValue;
import model.data.value.TimeValue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Class to specify a .txt file.
 *
 * @author Paul
 */
public class PlainTextFile extends DataFile {

	private Logger logger = Logger.getLogger("PlainTextFile");

	private int counter;
	private String delimiter = ",";

	/**
	 * Creates a new PlainTextFile.
	 * @param path The path to the plain text file
	 */
	public PlainTextFile(String path) {
		super(path);
	}

	@Override
	public DataTable createDataTable() throws IOException {
		counter = 1;
		InputStream stream = new FileInputStream(getFile());
		getBuilder().setName(getFile().getName().replace(".", ""));
		try (Scanner scanner = new Scanner(stream, "UTF-8")) {
			scanner.useDelimiter("\\A");
			skipToStartLine(scanner);

			if (hasFirstRowAsHeader()) {
				handleFirstRowHeader(scanner);

			} else {
				for (ColumnInfo column : getColumns()) {
					getBuilder().createColumn(column.getName(), column.getType());
				}
			}

			if (hasMetaData()) {
				getBuilder().createColumn(getMetaDataColumnName(), getMetaDataType());
			}

			List<String> lines = readLines(scanner);
			addRowsToBuilder(filterLastRows(lines));
		}

		return getBuilder().build();
	}

	private void handleFirstRowHeader(Scanner scanner) {
		String headers = scanner.nextLine();
		String[] sections = headers.split(delimiter);
		for (int i = 0; i < getColumns().size(); i++) {
			ColumnInfo column = getColumns().get(i);
			column.setName(sections[i].trim());
			getBuilder().createColumn(column.getName(), column.getType());
		}
	}

	/**
	 * Fast forwards the scanner to the actual content of the data.
	 * @param scanner The Scanner
	 * @throws IOException When there is nothing to read
	 */
	private void skipToStartLine(Scanner scanner) throws IOException {
		if (!scanner.hasNextLine()) {
			scanner.close();
			throw new IOException("Nothing to read");
		}

		while (counter != getStartLine() && scanner.hasNextLine()) {
			scanner.nextLine();
			counter++;
		}
	}

	/**
	 * Reads the lines from a scanner and inserts them into a list.
	 * @param scanner The scanner
	 * @return The list containing all lines read from the start line
	 */
	private ArrayList<String> readLines(Scanner scanner) {
		ArrayList<String> result = new ArrayList<>();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			result.add(line);
		}
		return result;
	}
	
	private DataValue[] createValues(String line) {
		String[] sections = line.split(delimiter, -1);
		DataValue[] values;
		if (hasMetaData()) {
			values = new DataValue[getColumns().size() + 1];
		} else {
			values = new DataValue[getColumns().size()];
		}
		for (int i = 0; i < getColumns().size(); i++) {
			values[i] = toDataValue(sections[i].trim(), getColumns().get(i));
		}
		if (hasMetaData()) {
			values[values.length - 1] = getMetaDataValue();
		}
		return values;
	}
	
	private void addRowsToBuilder(List<String> lines) {
		for (String line : lines) {
			DataValue[] values = createValues(line);
			getBuilder().createRow(values);
		}
	}
	
	private List<String> filterLastRows(List<String> lines) {
		return lines.subList(0, lines.size() - getEndLine());
	}

	/**
	 * Creates a DataValue from a string.
	 * @param value The string that will be converted
	 * @param columnInfo The info of the column to which the DataValue will be inserted
	 * @return The DataValue
	 */
	private DataValue toDataValue(String value, ColumnInfo columnInfo) {
		if (value.isEmpty()) {
			return DataValue.getNullInstance(columnInfo.getType());
		} else if (columnInfo.getType() == DateValue.class) {
			return new DateValue(parseLocalDate(value, columnInfo.getFormat()));
		} else if (columnInfo.getType() == DateTimeValue.class) {
			return new DateTimeValue(parseLocalDateTime(value, columnInfo.getFormat()));
		} else if (columnInfo.getType() == TimeValue.class) {
			return parseLocalTime(value, columnInfo.getFormat());
		} else {
			return parseSimpleDataValue(value, columnInfo.getType());
		}
	}

	/**
	 * Sets the delimiter that separates the values in the file.
	 * @param delimiter The delimiter to set
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * returns the delimiter that separates the values in the file.
	 * @return The delimiter
	 */
	public String getDelimiter() {
		return delimiter;
	}
}