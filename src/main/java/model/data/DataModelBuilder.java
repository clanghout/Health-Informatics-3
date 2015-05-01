package model.data;

import exceptions.ColumnValueMismatchException;
import exceptions.ColumnValueTypeMismatchException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Builder used to build a DataModel
 */
public class DataModelBuilder {
	private ArrayList<DataRow> rows;
	private ArrayList<DataColumn> columns;

	/**
	 * create a new builder
	 */
	public DataModelBuilder() {
		 rows =  new ArrayList<DataRow>();
		 columns = new ArrayList<DataColumn>();
	}

	/**
	 * return the dataModel build by the builder
	 *
	 * @return the DataModel that is build by the builder
	 */
	public DataModel build() {
		HashMap<String, DataColumn> columnsMap = new HashMap<String, DataColumn>();
		for (DataColumn column : columns) {
			columnsMap.put(column.getName(), column);
		}
		return new DataModel(rows, columnsMap);
	}

	/**
	 * add a column to the DataModel
	 *
	 * @param column the new column
	 */
	public void addColumn(DataColumn column) {
		columns.add(column);
	}

	/**
	 * Add a row to the DataModel
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
	 * @throws ColumnValueMismatchException thrown when the number of columns is not equal to the number of values
	 * @throws ColumnValueTypeMismatchException thrown when the value has a different type from what the columns expects
	 */
	public DataRow createRow(DataValue... values) {
		return new DataRow(columns.toArray(new DataColumn[columns.size()]), values);
	}
}
