package model.data;


import exceptions.ColumnValueMismatchException;
import exceptions.ColumnValueTypeMismatchException;
import model.data.value.DataValue;

import java.util.ArrayList;
import java.util.List;



/**
 * Builder used to build a DataTable.
 */
public class DataTableBuilder {
	private List<DataRow> rows;
	private List<DataColumn> columns;

	/**
	 * Create a new builder.
	 */
	public DataTableBuilder() {
		 rows =  new ArrayList<DataRow>();
		 columns = new ArrayList<DataColumn>();
	}

	/**
	 * Return the DataTable build by the builder.
	 *
	 * @return The DataTable that is build by the builder
	 */
	public DataTable build() {
		return new DataTable(rows, columns);
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
	 * Construct a DataColumn. This is not added to the model
	 *
	 * @param name name of the column
	 * @param type type of the column
	 * @return the constructed DataColumn
	 */
	public DataColumn createColumn(String name, Class<? extends DataValue> type) {
		return new DataColumn(name, type);
	}

	/**
	 * Construct a DataRow. This is not added to the model
	 *
	 * @param values array of new values
	 * @return the new constructed DataRow
	 * @throws ColumnValueMismatchException Thrown when the number of columns is not equal to
	 * the number of values
	 * @throws ColumnValueTypeMismatchException Thrown when the value has a different type from
	 * what the columns expects
	 */
	public DataRow createRow(DataValue... values) {
		return new DataRow(columns.toArray(new DataColumn[columns.size()]), values);
	}
}
