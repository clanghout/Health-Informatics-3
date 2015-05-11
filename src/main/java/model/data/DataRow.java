package model.data;

import exceptions.ColumnValueMismatchException;
import exceptions.ColumnValueTypeMismatchException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Class that represents a row of data.
 */
public class DataRow {
	private Logger log = Logger.getLogger("DataRow");

	private Map<String, DataValue> values = new HashMap<String, DataValue>();

	private List<DataRow> causedBy = new ArrayList<>();
	private List<DataRow> resultsIn = new ArrayList<>();

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
				values.put(columnArray[i].getName(), valueArray[i]);
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
		values.put(column.getName(), value);
	}

	/**
	 * Add the column with the value to the row.
	 * @param column The column the value belongs to
	 * @param value The value you want to set
	 */
	public void setValue(String column, DataValue value) {
		values.put(column, value);
	}


	/**
	 * Get the value of a column.
	 *
	 * @param column the column where you want the value from
	 * @return the value of the column of this row
	 */
	public DataValue getValue(DataColumn column) {
		return values.get(column.getName());
	}
	public DataValue getValue(String column) {
		return values.get(column);
	}

	/**
	 * Get all the rows that are influenced by this row,
	 * @return A list of rows that are influenced by this row.
	 */
	public List<DataRow> getResultsIn() {
		return resultsIn;
	}

	/**
	 * Add a row that is influeced by this row.
	 * @param resultIn the row that is influenced by this row.
	 */
	public void addCauses(DataRow resultIn) {
		resultsIn.add(resultIn);
	}

	/**
	 * Get all the rows that influence this row.
	 * @return A list of all the rows that influence this row.
	 */
	public List<DataRow> getCausedBy() {
		return causedBy;
	}

	/**
	 * Add a row that influences this row
	 * @param origin a row that influences this row.
	 */
	public void setCausedBy(DataRow origin) {
		causedBy.add(origin);
	}


}
