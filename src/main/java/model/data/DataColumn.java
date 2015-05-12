package model.data;

import model.data.value.DataValue;

import java.util.List;

/**
 * Class that specified what the data in a column is.
 */
public class DataColumn {
	private String name;
	private Class<? extends DataValue> type;

	/**
	 * Create a column.
	 * @param name name of the column
	 * @param type type of the column
	 */
	public DataColumn(String name, Class<? extends DataValue> type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Get the name of the column.
	 * @return the name of the column
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the type of the column.
	 * @return the type of the column
	 */
	public Class<? extends DataValue> getType() {
		return type;
	}
}
