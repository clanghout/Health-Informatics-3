package model.input.file;

import model.data.DataTable;
import model.data.value.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
	 */
	public PlainTextFile(String path) {
		super(path);
	}

	@Override
	public DataTable createDataTable() throws IOException {
		counter = 1;
		InputStream stream = new FileInputStream(getFile());
		getBuilder().setName(getFile().getName().replace(".", ""));
		Scanner scanner = new Scanner(stream, "UTF-8");
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

		scanner.close();

		return getBuilder().build();
	}

	@Override
	public String getFileTypeAsString() {
		return "plaintext";
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