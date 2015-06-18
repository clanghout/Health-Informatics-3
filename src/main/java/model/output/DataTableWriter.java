package model.output;

import model.data.DataColumn;
import model.data.DataTable;
import model.data.DataRow;
import model.data.value.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Writes a DataTable to a file.
 */
public class DataTableWriter {
	private Logger logger = Logger.getLogger("DataTableWriter");

	//Delimiter used in CSV file
	private static final String NEW_LINE_SEPARATOR = "\n";

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
	 * Write to csv file using the commons-csv library.
	 * @param table table to save.
	 * @param file file to save to.
	 * @throws IOException when save location is invalid.
	 */
	public void writeCSV(DataTable table, File file) throws IOException {
		logger.log(Level.INFO, "writing to CSV format");
		//Create the CSVFormat object with "\n" as a record delimiter
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

		try (FileWriter fileWriter = new FileWriter(file)) {
			try (CSVPrinter csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat)) {
				for (DataRow row : table.getRows()) {
					for (DataColumn col : row.getColumns()) {
						DataValue value = row.getValue(col);
						csvFilePrinter.print(formatValue(value));
					}
					csvFilePrinter.println();
				}
			}
		} catch (IOException e) {
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
	 *
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
			return buildDateTime((DateTimeValue) value);

		} else {
			return val;
		}
	}

	/**
	 * Build a string for the dateTimeValue.
	 *
	 * @param dateValue values
	 * @return formatted string
	 */
	private String buildDateTime(DateTimeValue dateValue) {
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
	}

	/**
	 * Create stringBuilder containing years, months and days.
	 *
	 * @param year  years
	 * @param month months
	 * @param day   days
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


