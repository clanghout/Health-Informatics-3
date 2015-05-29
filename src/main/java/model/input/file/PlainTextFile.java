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
				String[] sections = headers.split(",");
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
			readLines(scanner);
		}

		return builder.build();
	}

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

	private void readLines(Scanner scanner) {
		while (counter <= getEndLine() && scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] sections = line.split(delimiter);
			List<Class<? extends DataValue>> columns = getColumnList();
			DataValue[] values = new DataValue[getColumns().size()];
			for (int i = 0; i < getColumns().size(); i++) {
				values[i] = toDataValue(sections[i].trim(), columns.get(i));
			}
			builder.createRow(values);

			counter++;
		}
	}

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

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getDelimiter() {
		return delimiter;
	}
}