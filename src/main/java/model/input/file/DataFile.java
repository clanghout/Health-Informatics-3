package model.input.file;

import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.value.*;
import model.exceptions.DataFileNotRecognizedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for a datafile. This class contains all the specification of a datafile such as the 
 * path to the file, the starting and ending line of the data in the file and the names of the
 * columns.
 *
 * @author Paul
 */
public abstract class DataFile {

	private Logger log = Logger.getLogger("DataFile");

	private String metaDataColumnName;
	private DataValue<?> metaDataValue;
	private Class<? extends DataValue> metaDataType;

	private String path;
	private int startLine;
	private int endLine;

	private List<ColumnInfo> columns;

	private DataTableBuilder builder = new DataTableBuilder();
	private boolean firstRowAsHeader;
	private boolean hasMetaData;

	/**
	 * Creates a new type of a DataFile. Sets the default range of lines to read
	 * from 1 to integer maxvalue.
	 * @param path The path to the DataFile
	 */
	public DataFile(String path) {
		this.path = path;
		this.setStartLine(1);
		this.setEndLine(0);
		this.setFirstRowAsHeader(false);
		this.columns = new ArrayList<>();
	}

	/**
	 * Returns the rows of the DataFile only. All redundant data is filtered out.
	 *
	 * @return A stream containing the data contents of the file
	 * @throws IOException When the file is not found or if the file is corrupt
	 */
	public abstract DataTable createDataTable() throws IOException;
	
	/**
	 * Gets a new File object directing to the dataFile on the system.
	 * @return The file
	 * @throws FileNotFoundException When the file is not found
	 */
	public File getFile() throws FileNotFoundException {
		return new File(path);
	}

	/**
	 * Returns the path of the DataFile.
	 * @return The path
	 */
	public String getPath() {
		return path;
	}
	
	/**
	* Creates a new DataFile type class based on the string specified.
	* @param path The path of the DataFile
	* @param type The type specifying what type of DataFile should be created
	* @return A new DataFile
	* @throws DataFileNotRecognizedException When the datafile is not recognized
	*/
	public static DataFile createDataFile(String path, String type)
			throws DataFileNotRecognizedException {
		switch (type) {
			case "plaintext": return new PlainTextFile(path);
			case "xls": return new XlsFile(path);
			case "xlsx": return new XlsxFile(path);
			default: throw new DataFileNotRecognizedException("Type " + type 
					+ " is not recognized");
		} 	
	}
	
	/**
	 * Returns the line at which the datafile will begin reading i.e. the 
	 * first line of actual data in the datafile.
	 * @return The startLine
	 */
	public int getStartLine() {
		return startLine;
	}

	/**
	 * Sets the line at which the datafile will begin reading i.e. the
	 * first line of actual data in the datafile.
	 * @param startLine the startLine to set
	 */
	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}

	/**
	 * Returns the line at which the datafile will stop reading i.e. the last
	 * line of actual data in the datafile.
	 * @return the endLine
	 */
	public int getEndLine() {
		return endLine;
	}

	/**
	 * Sets the line at which the datafile will stop reading i.e. the last
	 * line of actual data in the datafile.
	 * @param endLine the endLine to set
	 */
	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}

	/**
	 * Returns the type of the class based on a string describing the type.
	 * @param type The name of the type
	 * @return The classtype
	 */
	public static Class<? extends DataValue> getColumnType(String type) {
		switch (type) {
			case "int":
				return IntValue.class;
			case "float":
				return FloatValue.class;
			case "string":
				return StringValue.class;
			case "date":
				return DateValue.class;
			case "time" :
				return TimeValue.class;
			case "datetime" :
				return DateTimeValue.class;
			case "bool" :
				return BoolValue.class;
			default:
				throw new RuntimeException("The specified type of data is not supported");
		}
	}

	/**
	 * Returns a string describing the type of the class.
	 * @param type The class
	 * @return The type of class as a string
	 */
	public static String getStringColumnType(Class<? extends DataValue> type) {
		if (type == IntValue.class) {
			return "int";
		} else if (type == FloatValue.class) {
			return "float";
		} else if (type == StringValue.class) {
			return "string";
		} else if (type == DateValue.class) {
			return "date";
		} else if (type == TimeValue.class) {
			return "time";
		} else if (type == DateTimeValue.class) {
			return "datetime";
		} else if (type == BoolValue.class) {
			return "bool";
		} else {
			throw new RuntimeException("The specified type of data is not supported");
		}
	}

	/**
	 * Returns the TableBuilder that creates the table from the file.
	 * @return The TableBuilder
	 */
	protected DataTableBuilder getBuilder() {
		return builder;
	}

	/**
	 * Returns the List with the names and types of the columns.
	 * @return The List with the names and types of the columns
	 */
	public List<ColumnInfo> getColumns() {
		return columns;
	}

	/**
	 * Adds ColumnInfo for a new column to the list.
	 * @param info The ColumnInfo to add
	 */
	public void addColumnInfo(ColumnInfo info) {
		columns.add(info);
	}

	/**
	 * Returns true if the columns' headers are in the first row.
	 * @return The firstRowAsHeader
	 */
	public boolean hasFirstRowAsHeader() {
		return firstRowAsHeader;
	}

	/**
	 * Sets if the first row is the header.
	 * @param firstRowAsHeader The firstRowAsHeader to set
	 */
	public void setFirstRowAsHeader(boolean firstRowAsHeader) {
		this.firstRowAsHeader = firstRowAsHeader;
	}

	/**
	 * Returns the DataValue that is considered the metadata.
	 * @return The metadatavalue
	 */
	public DataValue<?> getMetaDataValue() {
		return metaDataValue;
	}

	/**
	 * Sets the class for the metadata column.
	 * @param metaDataType The class for the metadata
	 */
	public void setMetaDataType(Class<? extends DataValue> metaDataType) {
		this.metaDataType = metaDataType;
	}

	/**
	 * Returns a string representation of the datafile.
	 *
	 * @return The string representing the file.
	 */
	@Override
	public String toString() {
		return "[" + path + "]";
	}

	/**
	 * Sets the standard metadata value.
	 *
	 * @param name The name that the metadata column will get
	 * @param type The type of the column
	 */
	public void createMetaDataValue(String name, String type) {
		try {
			String fileName = this.getFile().getName();
			String metaValue = fileName.substring(0, fileName.lastIndexOf("."));
			Class<? extends DataValue> typeClass = DataFile.getColumnType(type);
			this.metaDataValue = parseSimpleDataValue(metaValue, typeClass);
			this.setMetaDataType(getColumnType(type));
			this.setMetaDataColumnName(name);
			hasMetaData = true;
		} catch (FileNotFoundException e) {
			log.log(Level.SEVERE, "The file could not be found", e);
		}
	}

	/**
	 * Parses a String or number to a DataValue of a given type.
	 * @param value The value to parse
	 * @param type The type of DataValue to parse to
	 * @return The new created DataValue
	 */
	protected DataValue parseSimpleDataValue(String value, Class<? extends DataValue> type) {
		if (type == IntValue.class) {
			return new IntValue(Integer.parseInt(value));
		} else if (type == FloatValue.class) {
			return new FloatValue(Float.parseFloat(value));
		} else if (type == StringValue.class) {
			return new StringValue(value);
		} else if (type == BoolValue.class) {
			return new BoolValue(Boolean.parseBoolean(value));
		} else {
			throw new RuntimeException("Class has not yet been implemented");
		}
	}

	/**
	 * Parses a string representing a localtime using the given format.
	 * @param value The value to parse
	 * @param format The format used to parse
	 * @return The new created TimeValue
	 */
	protected TimeValue parseLocalTime(String value, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		LocalTime localTime = LocalTime.parse(value, formatter);

		return new TimeValue(localTime.getHour(),
				localTime.getMinute(),
				localTime.getSecond());
	}

	/**
	 * Parses a String representing a LocalDateTime using a given format.
	 * @param value The value to parse
	 * @param format The format used to parse
	 * @return The new created LocalDateTime
	 */
	protected LocalDateTime parseLocalDateTime(String value, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return LocalDateTime.from(formatter.parse(value));
	}

	/**
	 * Parses a String representing a LocalDate using a given format.
	 * @param value The value to parse
	 * @param format The format used to parse
	 * @return The new created LocalDate
	 */
	protected LocalDate parseLocalDate(String value, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return LocalDate.parse(value, formatter);
	}

	/**
	 * Parses a String representing a TemporalValue using given parse information.
	 * @param value The value to parse
	 * @param columnInfo The information used to parse the TemporalValue
	 * @return The new created TemporalValue
	 */
	protected TemporalValue parseTemporalValue(String value, ColumnInfo columnInfo) {
		if (columnInfo.getType() == DateValue.class) {
			return new DateValue(parseLocalDate(value, columnInfo.getFormat()));
		} else if (columnInfo.getType() == DateTimeValue.class) {
			return new DateTimeValue(parseLocalDateTime(value, columnInfo.getFormat()));
		} else if (columnInfo.getType() == TimeValue.class) {
			return parseLocalTime(value, columnInfo.getFormat());
		}
		throw new RuntimeException("Type of TemporalValue : "
				+ columnInfo.getType()
				+ " is not recognized");
	}

	/**
	 * Returns true if a type is a TemporalValue.
	 * @param type The type to check
	 * @return True if type is a TemporalValue
	 */
	protected boolean isTemporalValue(Class<? extends DataValue> type) {
		return type == DateTimeValue.class
				|| type == DateValue.class
				|| type == TimeValue.class;
	}

	/**
	 * Returns true if the datafile has metadata.
	 * @return true if the datafile has metadata
	 */
	public boolean hasMetaData() {
		return hasMetaData;
	}

	/**
	 * Sets the name of the metadata column.
	 * @param metaDataColumnName The name to set
	 */
	public void setMetaDataColumnName(String metaDataColumnName) {
		this.metaDataColumnName = metaDataColumnName;
	}

	/**
	 * Returns the name of the metadata column.
	 * @return The name of the metadata column.
	 */
	public String getMetaDataColumnName() {
		return metaDataColumnName;
	}

	/**
	 * Returns the type of class of the metadata column.
	 * @return The type of class of the metadata column
	 */
	public Class<? extends DataValue> getMetaDataType() {
		return metaDataType;
	}
}