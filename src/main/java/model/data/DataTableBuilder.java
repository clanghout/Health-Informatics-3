package model.data;

import model.data.value.DataValue;

import java.util.ArrayList;
import java.util.List;



/**
 * Builder used to build a DataTable.
 */
public class DataTableBuilder {
	protected List<DataRow> rows;
	protected List<DataColumn> columns;
	private String name;

	/**
	 * Create a new builder.
	 */
	public DataTableBuilder() {
		rows =  new ArrayList<>();
		columns = new ArrayList<>();
	}

	/**
	 * Set the name of the table.
	 * @param name name of the table
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return the DataTable build by the builder.
	 *
	 * @return The DataTable that is build by the builder
	 */
	public DataTable build() {
		if (name == null) {
			throw new IllegalStateException("Name must be set");
		}
		DataTable result = new DataTable(name, rows, columns);
		for (DataColumn column : columns) {
			column.setTable(result);
		}

		return result;
	}

	/**
	 * Add a column to the DataTable.
	 *
	 * @param column The new column
	 */
	public void addColumn(DataColumn column) {
		columns.add(column);
	}

	/**
	 * Add a row to the DataTable.
	 *
	 * @param row the new row
	 */
	public void addRow(DataRow row) {
		rows.add(row);
	}

	/**
	 * Construct a DataColumn. And add it to the table
	 *
	 * @param name name of the column
	 * @param type type of the column
	 * @return the constructed DataColumn
	 */
	public DataColumn createColumn(String name, Class<? extends DataValue> type) {
		DataColumn column = new DataColumn(name, null, type);
		addColumn(column);
		return column;
	}

	/**
	 * Construct a DataRow. And add it to the table
	 *
	 * @param values array of new values
	 * @return the new constructed DataRow
	 * @throws ColumnValueMismatchException Thrown when the number of columns is not equal to
	 * the number of values
	 * @throws ColumnValueTypeMismatchException Thrown when the value has a different type from
	 * what the columns expects
	 */
	public DataRow createRow(DataValue... values) {
		DataRow row = new DataRow(columns.toArray(new DataColumn[columns.size()]), values);
		addRow(row);
		return row;
	}
}