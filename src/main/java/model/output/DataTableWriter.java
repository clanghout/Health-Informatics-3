package model.output;

import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataRow;
import model.data.value.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
	private String delimiter = ", ";
	private String nullVal = "";

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
	 */
	public void write(DataTable dataTable,
	                  File file) throws IOException {
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
			value += formatValue(dataValue);
		}
		writer.print(value);
	}

	public List<DataColumn> readColumns(DataTable in) {
		return in.getColumns();
	}

	/**
	 * Format the value to write to file.
	 * @param value the value that is formatted.
	 * @return String containing formatted data.
	 */
	public String formatValue(DataValue value) {
		String val = value.toString();
		if (value instanceof StringValue && quotationMarks) {
			return '"' + val + '"';
		} else if (value instanceof DateValue) {
			DateValue dateValue = (DateValue) value;
			LocalDate date = dateValue.getValue();
			return writeDate(date.getYear(),
					date.getMonthValue(),
					date.getDayOfMonth()).toString();
		} else if (value instanceof DateTimeValue) {
			DateTimeValue dateValue = (DateTimeValue) value;
			LocalDateTime date = dateValue.getValue();
			StringBuilder builder = writeDate(date.getYear(),
					date.getMonthValue(),
					date.getDayOfMonth());
			builder.append(" ");
			builder.append(date.getHour());
			builder.append(':');
			builder.append(date.getMinute());
			builder.append(':');
			builder.append(date.getSecond());
			return builder.toString();
		} else {
			return val;
		}
	}

	/**
	 * Create stringBuilder containing years, months and days.
	 * @param year years
	 * @param month months
	 * @param day days
	 * @return appended StringBuilder
	 */
	private StringBuilder writeDate(int year, int month, int day) {
		StringBuilder builder = new StringBuilder();
		builder.append(year);
		builder.append('-');
		builder.append(month);
		builder.append('-');
		builder.append(day);
		return builder;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public void setNullVal(String nullVal) {
		this.nullVal = nullVal;
	}

	public void setQuotationMarks(boolean quotationMarks) {
		this.quotationMarks = quotationMarks;
	}
}


