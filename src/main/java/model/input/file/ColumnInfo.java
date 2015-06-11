package model.input.file;

import model.data.value.DataValue;

/**
 * Contains the information of a column.
 * @author Paul.
 */
public class ColumnInfo {

	private String name;
	private Class<? extends DataValue> type;

	public ColumnInfo(String name, Class<? extends DataValue> type) {
		this.name = name;
		this.type = type;
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

	public void setType(Class<? extends DataValue> type) {
		this.type = type;
	}
}