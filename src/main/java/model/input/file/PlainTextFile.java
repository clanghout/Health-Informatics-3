package model.input.file;

import model.data.DataTable;
import model.data.value.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
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
				String headers = scanner.nextLine();
				String[] sections = headers.split(delimiter);
				for (int i = 0; i < getColumns().size(); i++) {
					getColumns().get(i).setName(sections[i]);;
				}
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
		DataValue[] values;
		if (hasMetaData()) {
			values = new DataValue[getColumns().size() + 1];
		} else {
			values = new DataValue[getColumns().size()];
		}
		for (int i = 0; i < getColumns().size(); i++) {
			values[i] = toDataValue(sections[i].trim(), getColumns().get(i).getType());
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
		} else if(type.equals(DateValue.class)) {
			return parseDateValue(value);
		} else if(type.equals(TimeValue.class)) {
			return parseTimeValue(value);
		}
		else {
			throw new UnsupportedOperationException(
					String.format("Class %s not yet supported", type)
			);
		}
	}

	private TimeValue parseTimeValue(String value) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hhmm");
		LocalDateTime dateTime = LocalDateTime.from(formatter.parse(value));
		return new TimeValue(dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
	}

	private DateValue parseDateValue(String value) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yymmdd");
		LocalDateTime dateTime = LocalDateTime.from(formatter.parse(value));
		return new DateValue(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth());
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