package model.input.file;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.value.*;
import model.exceptions.DataFileNotRecognizedException;
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
	private DataValue metaDataValue;
	private Class metaDataType;

	private String path;
	private int startLine;
	private int endLine;
	private Map<String, Class<? extends DataValue>> columns;
	private List<Class<? extends DataValue>> columnList;
	private boolean firstRowAsHeader;
	private Class[] columnTypes;
	private DataTableBuilder builder = new DataTableBuilder();
	private boolean hasMetaData;

	/**
	 * Creates a new type of a DataFile. Sets the default range of lines to read
	 * from 1 to integer maxvalue.
	 * @param path The path to the DataFile
	 * @throws FileNotFoundException When the file can not be found
	 */
	public DataFile(String path) throws FileNotFoundException {
		this.path = path;
		metaDataColumnName = path;
		this.setStartLine(1);
		this.setEndLine(Integer.MAX_VALUE);
		this.setFirstRowAsHeader(false);
		this.setColumns(new LinkedHashMap<>(), new ArrayList<>());
		this.builder.setName(getFile().getName().replace(".", ""));
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
	* @throws FileNotFoundException When the file is not found
	*/
	public static DataFile createDataFile(String path, String type)
			throws DataFileNotRecognizedException, FileNotFoundException {
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

	public void setColumnTypes(Class[] types) {
		this.columnTypes = types;
	}
	
	public Class[] getColumnTypes() {
		return this.columnTypes;
	}

	/**
	 * Returns the type of the class based on a string describing the type.
	 * @param type The name of the type
	 * @return The classtype
	 * @throws ClassNotFoundException When the class is not recognized
	 */
	public static Class getColumnType(String type) throws ClassNotFoundException {
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
			default:
				throw new ClassNotFoundException();
		}
	}

	/**
	 * Returns a string describing the type of the class.
	 * @param type The class
	 * @return The type of class as a string
	 * @throws ClassNotFoundException
	 */
	public static String getStringColumnType(Class type) throws ClassNotFoundException {
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
		} else {
			throw new ClassNotFoundException();
		}
	}

	/**
	 * Returns the TableBuilder that creates the table from the file.
	 * @return The TableBuilder
	 */
	protected DataTableBuilder getBuilder() {
		return this.builder;
	}

	/**
	 * Returns the array with the names of the columns.
	 * @return The array with the names of the columns
	 */
	public Map<String, Class<? extends DataValue>> getColumns() {
		return columns;
	}

	public List<Class<? extends DataValue>> getColumnList() {
		return columnList;
	}

	/**
	 * Sets the names of the columns.
	 * @param columns The columns to set
	 */
	public void setColumns(
			Map<String, Class<? extends DataValue>> columns,
			List<Class<? extends DataValue>> columnList) {
		this.columns = new LinkedHashMap<>(columns);
		this.columnList = columnList;
	}

	/**
	 * Tries to parse an integer.
	 * @param value The String to parse
	 * @return True if the value can be parsed as integer
	 */
	protected boolean tryParseInt(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Tries to parse a float.
	 * @param value The String to parse
	 * @return True if the value can be parsed as float
	 */
	protected boolean tryParseFloat(String value) {
		try {
			Float.parseFloat(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
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
	public DataValue getMetaDataValue() {
		return metaDataValue;
	}

	/**
	 * Sets the metadatavalue.
	 * @param metaDataValue The metadatavalue
	 */
	public void setMetaDataValue(DataValue metaDataValue) {
		this.metaDataValue = metaDataValue;
	}

	/**
	 * Sets the class for the metadata column.
	 * @param metaDataType The class for the metadata
	 */
	public void setMetaDataType(Class metaDataType) {
		this.metaDataType = metaDataType;
	}

	/**
	 * Sets the name that the metadatacolumn will get.
	 * @param name The name of the metadatacolumn
	 */
	public void setMetaDataColumnName(String name) {
		this.metaDataColumnName = name;
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
	public void createMetaDataValue(String name, String type) throws ClassNotFoundException {
		try {
			DataValue res = null;
			String metavalue = this.getFile().getName().substring(0, name.lastIndexOf("."));

			switch (type) {
				case "int":
					res = new IntValue(Integer.parseInt(metavalue));
				case "float":
					res = new FloatValue(Float.parseFloat(metavalue));
				case "string":
					res = new StringValue(metavalue);
//				case "date":
//					res = new DateValue();
//				case "time" :
//					res = new TimeValue();
//				case "datetime" :
//					res = new DateTimeValue();
			}
			this.metaDataValue = res;
			builder.createColumn(name, getColumnType(type));
			hasMetaData = true;
		} catch (FileNotFoundException e) {
			log.log(Level.SEVERE, "The file could not be found", e);
		}
	}

	/**
	 * returns true if the datafile has metadata.
	 * @return true if the datafile has metadata
	 */
	public boolean hasMetaData() {
		return hasMetaData;
	}
}
