package model.data;

import model.data.value.DataValue;

/**
 * Class that specifies what the data in a column is.
 */
public class DataColumn {
	private String name;
	private Class<? extends DataValue> type;
	private DataTable table;

	/**
	 * Create a column.
	 * @param name name of the column
	 * @param type type of the column
	 */
	public DataColumn(String name, DataTable table, Class<? extends DataValue> type) {
		this.name = name;
		this.type = type;
		this.table = table;
	}

	/**
	 * Get the name of the column.
	 * @return the name of the column
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the table to which the column belongs to.
	 * @param table the table to which the column belongs to
	 */
	public void setTable(DataTable table) {
		this.table = table;
	}

	/**
	 * Get the table to which the column belongs to.
	 * @return te table that contains this column
	 */
	public DataTable getTable() {
		return table;
	}

	/**
	 * Get the type of the column.
	 * @return the type of the column
	 */
	public Class<? extends DataValue> getType() {
		return type;
	}

	/**
	 * create a copy of this column
	 * @return a copy of this column
	 */
	public DataColumn copy() {
		return new DataColumn(name, table, type);
	}
}
