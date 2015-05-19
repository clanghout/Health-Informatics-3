package model.data;

import exceptions.ColumnValueMismatchException;
import exceptions.ColumnValueTypeMismatchException;
import model.data.value.DataValue;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Class that represents a row of data.
 */
public class DataRow implements Row {
	private Logger log = Logger.getLogger("DataRow");

	private Map<DataColumn, DataValue> values = new HashMap<>();
	private Map<String, DataColumn> columns = new HashMap<>();

	private Set<DataConnection> connections = new HashSet<>();


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
				columns.put(columnArray[i].getName(), columnArray[i]);
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

	/**
	 * Add the column with the value to the row.
	 * @param column The column the value belongs to
	 * @param value The value you want to set
	 */
	public void setValue(String column, DataValue value) {
		values.put(columns.get(column), value);
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
	public DataValue getValue(String column) {
		return values.get(columns.get(column));
	}

	/**
	 * Add a connection to this row.
	 * @param connection a connection that this row is involved in.
	 */
	public void addConnection(DataConnection connection) {
		connections.add(connection);
	}

	/**
	 * Get all the connections that influence this row.
	 * @return A Set of all the connections that influence this row.
	 */
	public Set<DataConnection> getCausedBy() {
		return connections.stream()
				.filter(c ->
				        c.getResultsIn().contains(this))
				.collect(Collectors.toSet());
	}

	/**
	 * Get all the connections that are influenced by this row.
	 * @return A set of connections that are influenced by this row.
	 */
	public Set<DataConnection> getResultsIn() {
		return connections.stream()
				.filter(c ->
				        c.getCausedBy().contains(this))
				.collect(Collectors.toSet());
	}




}
