package output;

import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataRow;
import model.data.value.DataValue;
import model.data.value.StringValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Write output after analysis is done.
 */
public class DataTableWriter {
	private Logger logger = Logger.getLogger("DataTableWriter");

	// Options for user
	private boolean quotationmarks;

	/**
	 * Constructor for DataTableWriter.
	 */
	public DataTableWriter() { }

	/**
	 * Creates a PrintWriter that prints the DataTable to a file.
	 */
	public void write(DataTable dataTableInput, File fileInput, String delimiter) {
		List<DataColumn> columns = readColumns(dataTableInput);
		List<DataRow> rows = dataTableInput.getRows();
		try (PrintWriter writer = new PrintWriter(fileInput, "UTF-8")) {
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
		}
	}

	public List<DataColumn> readColumns(DataTable in) {
		ArrayList<DataColumn> res = new ArrayList<>();
		Map<String, DataColumn> columnsData = in.getColumns();
		for (Object o : columnsData.entrySet()) {
			Map.Entry pair = (Map.Entry) o;
			DataColumn column = (DataColumn) pair.getValue();
			res.add(column);
		}
		return res;
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


