package output;

import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataRow;
import model.data.value.DataValue;
import model.data.value.StringValue;

import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Writes a DataTable to a file.
 */
public class DataTableWriter {
	private Logger logger = Logger.getLogger("DataTableWriter");

	// Options for user
	private boolean quotationmarks;

	/**
	 * Constructor for DataTableWriter.
	 */
	public DataTableWriter() {
	}

	/**
	 * Creates a PrintWriter that prints the DataTable to a file.
	 *
	 * @param dataTable The table you want to output.
	 * @param file The file you want to write to.
	 * @param delimiter The delimiter you want to use between fields.
	 */
	public void write(DataTable dataTable, File file, String delimiter) throws IOException {
		List<DataColumn> columns = readColumns(dataTable);
		List<DataRow> rows = dataTable.getRows();
		try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
			for (DataRow row : rows) {
				if (columns.size() > 0) {
					writer.print(row.getValue(columns.get(0)).toString());
				} else {
					logger.warning("Zero columns specified");
				}
				for (int i = 1; i < columns.size(); i++) {
					DataColumn col = columns.get(i);
					if (quotationmarks) {
						String value = addQuotes(row.getValue(col));
						writer.print(delimiter + value);
					} else {
						writer.print(delimiter + row.getValue(col).toString());
					}
				}
				writer.println();
			}
			logger.info("data written");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			logger.log(Level.SEVERE, "Error writing file", e);
			throw e;
		}
	}

	public List<DataColumn> readColumns(DataTable in) {
		return in.getColumns();
	}

	public String addQuotes(DataValue value) {
		String val = value.toString();
		if (value instanceof StringValue) {
			return '"' + val + '"';
		} else {
			return val;
		}
	}
}


