package model.input.file;

import model.data.value.DataValue;

/**
 * Contains the information of a column how the values in it's cells should be parsed.
 * @author Paul.
 */
public class ColumnInfo {

	private String name;
	private Class<? extends DataValue> type;
	private String format;

	/**
	 * Create a new ColumnInfo.
	 * @param name The name of the column
	 * @param type The type of the elements in the column
	 * @param format The pattern how dates and times should be parsed
	 */
	public ColumnInfo(String name,
					  Class<? extends DataValue> type,
					  String format) {
		this.name = name;
		this.type = type;
		this.format = format;
	}

	/**
	 * Create a new ColumnInfo.
	 * @param name The name of the column
	 * @param type The type of the elements in the column
	 */
	public ColumnInfo(String name, Class<? extends DataValue> type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Create a new ColumnInfo.
	 * @param type The type of the elements in the column
	 */
	public ColumnInfo(Class<? extends DataValue> type) {
		this.type = type;
	}

	/**
	 * Create a new ColumnInfo.
	 * @param type The type of the elements in the column
	 * @param format The pattern how dates and times should be parsed
	 */
	public ColumnInfo(Class<? extends DataValue> type, String format) {
		this.type = type;
		this.format = format;
	}

	/**
	 * Returns the name of the column.
	 * @return The name of the column
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the column.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the type of the elements in the column.
	 * @return The type
	 */
	public Class<? extends DataValue> getType() {
		return type;
	}

	/**
	 * Returns the format how dates and times should be parsed.
	 * @return The format
	 */
	public String getFormat() {
		return format;
	}
}
