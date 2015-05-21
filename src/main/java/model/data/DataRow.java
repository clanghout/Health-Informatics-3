package model.data;

import exceptions.ColumnValueMismatchException;
import exceptions.ColumnValueTypeMismatchException;
import model.data.value.DataValue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Class that represents a row of data.
 */
public class DataRow implements Row {
	private Logger log = Logger.getLogger("DataRow");

	private Map<DataColumn, DataValue> values = new HashMap<>();

	/**
	 * Create an empty row.
	 */
	public DataRow() {

	}

	/**
	 * Create a row and set for the columns and the values.
	 *
	 * @param columnArray columns that the row should have
	 * @param valueArray value of the columns
	 * @throws ColumnValueMismatchException    thrown when the number of columns is not equal to
	 * the number of values
	 * @throws ColumnValueTypeMismatchException thrown when the value has a different type from
	 * what the columns expects
	 */
	public DataRow(DataColumn[] columnArray, DataValue[] valueArray) {
		if (columnArray.length != valueArray.length) {
			throwValueMismatchException();
		}
		for (int i = 0; i < columnArray.length; i++) {
			if (columnArray[i].getType().isInstance(valueArray[i])) {
				values.put(columnArray[i], valueArray[i]);
			} else {
				throwTypeMismatchException();
			}
		}
	}

	private void throwValueMismatchException() {
		ColumnValueMismatchException e = new ColumnValueMismatchException(
				"Number of columns is not equal to the number of values"
		);
		log.throwing(this.getClass().getSimpleName(), "constructor", e);
		throw e;
	}

	private void throwTypeMismatchException() {
		ColumnValueTypeMismatchException e = new ColumnValueTypeMismatchException(
				"Type of value is not a subtype of column type"
		);
		log.throwing(this.getClass().getSimpleName(), "constructor", e);
		throw e;
	}

	/**
	 * Add the column with the value to the row.
	 *
	 * @param column The column where the value belongs to
	 * @param value The value of the added column
	 */
	public void setValue(DataColumn column, DataValue value) {
		values.put(column, value);
	}

	@Override
	public boolean hasColumn(DataColumn column) {
		return values.containsKey(column);
	}

	@Override
	public DataRow copy() {
		DataRow row = new DataRow();
		Iterator<DataColumn> columns = values.keySet().iterator();
		while (columns.hasNext()) {
			DataColumn column = columns.next();
			row.setValue(column.copy(), values.get(column).copy());
		}

		return row;
	}

	@Override
	public Row copyForTable(Table table) {
		DataColumn[] columns = values.keySet().toArray(new DataColumn[ values.keySet().size()]);
		if (columns.length > 0 && table.equalStructure(columns[0].getTable()));
			table.getColumns();
	}

	@Override
	public DataRow copy(Table table) {
		return null;
	}


	/**
	 * Get the value of a column.
	 *
	 * @param column the column where you want the value from
	 * @return the value of the column of this row
	 */
	public DataValue getValue(DataColumn column) {
		return values.get(column);
	}

}
