package model.output;

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
	private boolean quotationMarks;
	private String delimiter;

	/**
	 * Constructor for DataTableWriter.
	 */
	public DataTableWriter() {
	}

	/**
	 * Creates a PrintWriter that prints the DataTable to a file.
	 *
	 * @param dataTable The table you want to output.
	 * @param file      The file you want to write to.
	 * @param delimiter The delimiter you want to use between fields.
	 */
	public void write(DataTable dataTable, File file, String delimiter, String nullVal) throws IOException {
		this.delimiter = delimiter;
		List<DataColumn> columns = readColumns(dataTable);
		List<DataRow> rows = dataTable.getRows();
		try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
			for (DataRow row : rows) {
				if (columns.size() > 0) {
					print(writer, row, columns.get(0), "", nullVal);
				} else {
					logger.warning("Zero columns specified");
				}
				for (int i = 1; i < columns.size(); i++) {
					DataColumn col = columns.get(i);
					print(writer, row, col, delimiter, nullVal);
				}
				writer.println();
			}
			logger.info("data written");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			logger.log(Level.SEVERE, "Error writing file", e);
			throw e;
		}
	}

	/**
	 * print the value to the file.
	 * all the properties can be set here.
	 *
	 * @param writer    the file writer
	 * @param row       the row currently printing
	 * @param col       the column of the row
	 * @param delimiter the delimiter to use
	 */
	private void print(PrintWriter writer,
	                   DataRow row,
	                   DataColumn col,
	                   String delimiter,
	                   String nullVal) {
		String value = delimiter;
		DataValue dataValue = row.getValue(col);
		if (dataValue.isNull()) {
			value += nullVal;
		} else {
			if (quotationMarks) {
				value += addQuotes(dataValue);
			} else {
				value += dataValue.toString();
			}
		}
		writer.print(value);
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

	public void setQuotationMarks(boolean quotationMarks) {
		this.quotationMarks = quotationMarks;
	}
}


