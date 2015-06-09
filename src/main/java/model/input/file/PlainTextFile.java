package model.input.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import model.data.value.FloatValue;
import model.data.value.IntValue;

import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.value.DataValue;
import model.data.value.StringValue;

/**
 * Class to specify a .txt file.
 *
 * @author Paul
 */
public class PlainTextFile extends DataFile {

	private DataTableBuilder builder;
	private int counter;
	private String delimiter = ",";

	public PlainTextFile(String path) {
		super(path);
	}

	@Override
	public DataTable createDataTable() throws IOException {
		builder = new DataTableBuilder();
		builder.setName(this.getFile().getName().replace(".", ""));
		counter = 1;
		InputStream stream = new FileInputStream(getFile());

		try (Scanner scanner = new Scanner(stream, "UTF-8")) {
			scanner.useDelimiter("\\A");
			skipToStartLine(scanner);
			if (hasFirstRowAsHeader()) {
				String headers = scanner.nextLine();
				String[] sections = headers.split(delimiter);
				for (int i = 0; i < getColumnTypes().length; i++) {
					getColumns().put(sections[i], getColumnTypes()[i]);
				}
			} else {
				for (Map.Entry<
						String,
						Class<? extends DataValue>
						> entry : getColumns().entrySet()) {
					builder.createColumn(entry.getKey(), entry.getValue());
				}
			}
			List<String> lines = readLines(scanner);
			addRowsToBuilder(filterLastRows(lines));
		}

		return builder.build();
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
	 * Reads the lines from a scanner and inserts them into a list
	 * @param scanner The scanner
	 * @return The list containing all lines read from the start line
	 */
	private ArrayList readLines(Scanner scanner) {
		ArrayList<String> result = new ArrayList<>();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			result.add(line);
		}
		return result;
	}

	private DataValue[] createValues(String line) {
		String[] sections = line.split(delimiter);
		List<Class<? extends DataValue>> columns = getColumnList();
		DataValue[] values = new DataValue[getColumns().size()];

		for (int i = 0; i < getColumns().size(); i++) {
			values[i] = toDataValue(sections[i].trim(), columns.get(i));
		}

		return values;
	}

	private void addRowsToBuilder(List<String> lines) {
		for (String line : filterLastRows(lines)) {
			DataValue[] values = createValues(line);
			builder.createRow(values);
		}
	}

	private List<String> filterLastRows(List<String> lines) {
		return lines.subList(0, lines.size() - getEndLine());
	}

	/**
	 * Creates a DataValue from a string.
	 * @param value The string that will be converted
	 * @param type The type of the column in which the value will be inserted.
	 * @return The datavalue
	 */
	private DataValue toDataValue(String value, Class<? extends DataValue> type) {
		if (type.equals(StringValue.class)) {
			return new StringValue(value);
		} else if (type.equals(IntValue.class)) {
			if (tryParseInt(value)) {
				return new IntValue(Integer.parseInt(value));
			} else {
				// TODO: throw appropriate exception
				return null;
			}
		} else if (type.equals(FloatValue.class)) {
			if (tryParseFloat(value)) {
				return new FloatValue(Float.parseFloat(value));
			} else {
				// TODO: throw appropriate exception
				return null;
			}
		} else {
			throw new UnsupportedOperationException(
					String.format("Class %s not yet supported", type)
			);
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