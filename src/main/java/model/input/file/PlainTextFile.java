package model.input.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import model.data.value.*;

import model.data.DataTable;
import model.data.DataTableBuilder;

/**
 * Class to specify a .txt file.
 *
 * @author Paul
 */
public class PlainTextFile extends DataFile {

	private int counter;
	private String delimiter = ",";

	/**
	 * Creates a new PlainTextFile.
	 * @param path The path to the plain text file
	 * @throws FileNotFoundException when the file can not be found
	 */
	public PlainTextFile(String path) throws FileNotFoundException {
		super(path);
	}

	@Override
	public DataTable createDataTable() throws IOException {
		counter = 1;
		InputStream stream = new FileInputStream(getFile());

		try (Scanner scanner = new Scanner(stream, "UTF-8")) {
			scanner.useDelimiter("\\A");
			skipToStartLine(scanner);
			if (hasFirstRowAsHeader()) {
				String headers = scanner.nextLine();
				String[] sections = headers.split(",");
				for (int i = 0; i < getColumnTypes().length; i++) {
					getColumns().put(sections[i], getColumnTypes()[i]);
				}
			} else {
				for (Map.Entry<
						String,
						Class<? extends DataValue>
						> entry : getColumns().entrySet()) {
					getBuilder().createColumn(entry.getKey(), entry.getValue());
				}
			}
			addMetaDataFileColumn();
			readLines(scanner);
		}

		return getBuilder().build();
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
	 * Reads the lines from a scanner and inserts rows into the table.
	 * @param scanner The scanner
	 */
	private void readLines(Scanner scanner) {
		while (counter <= getEndLine() && scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] sections = line.split(delimiter);
			List<Class<? extends DataValue>> columns = getColumnList();
			DataValue[] values = new DataValue[getColumns().size() + 1];
			for (int i = 0; i < getColumns().size(); i++) {
				values[i] = toDataValue(sections[i].trim(), columns.get(i));
			}
			values[values.length - 1] = new FileValue(this);
			getBuilder().createRow(values);

			counter++;
		}
	}

	/**
	 * Creates a DataValue from a string.
	 * @param value The string that will be converted
	 * @param type The type of the column in which the value will be inserted.
	 * @return The DataValue
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