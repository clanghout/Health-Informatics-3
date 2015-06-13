package model.input.file;

import model.data.value.DataValue;

/**
 * Contains the information of a column.
 * @author Paul.
 */
public class ColumnInfo {

	private String name;
	private Class<? extends DataValue> type;
	private String format;

	public ColumnInfo(String name,
					  Class<? extends DataValue> type,
					  String format) {
		this.name = name;
		this.type = type;
		this.format = format;
	}

	public ColumnInfo(String name, Class<? extends DataValue> type) {
		this.name = name;
		this.type = type;
	}

	public ColumnInfo(Class<? extends DataValue> type) {
		this.type = type;
	}

	public ColumnInfo(Class<? extends DataValue> type, String format) {
		this.type = type;
		this.format = format;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<? extends DataValue> getType() {
		return type;
	}

	public String getFormat() {
		return format;
	}

	public void print() {
		System.out.println(getName() + ", " + getType() + ", " + getFormat());
	}
}