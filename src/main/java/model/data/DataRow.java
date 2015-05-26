package model.data;

import exceptions.ColumnValueMismatchException;
import exceptions.ColumnValueTypeMismatchException;
import model.data.value.DataValue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Class that represents a row of data.
 */
public class DataRow extends Row {
	private Logger log = Logger.getLogger("DataRow");

	private Set<String> codes = new HashSet<>();
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
		for (Map.Entry<DataColumn, DataValue> entry : values.entrySet()) {
			row.setValue(entry.getKey(), values.get(entry.getKey()).copy());
		}
		return row;
	}

	/**
	 * Copy the dataRow to the table table.
	 * @param table the table this row should be copied to.
	 * @return a copy of this row in table table
	 */
	public DataRow copy(DataTable table) {
		DataRow row = new DataRow();
		DataColumn[] columns = values.keySet().toArray(new DataColumn[ values.keySet().size()]);
		if (columns.length > 0 && table.equalStructure(columns[0].getTable())) {
			for (DataColumn column : table.getColumns()) {
				for (Map.Entry<DataColumn, DataValue> entry : values.entrySet()) {
					if (entry.getKey().equalsExcludeTable(column)) {
						row.setValue(column,
								values.get(entry.getKey()).copy());
						break;
					}
				}
			}
			return row;
		} else {
			throw new IllegalArgumentException("table not the right structure");
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DataRow)) {
			return false;
		}
		DataRow other = (DataRow) obj;
		if (values.keySet().size() != other.values.keySet().size()) {
			return false;
		}

		for (DataColumn column : values.keySet()) {
			if (!this.getValue(column).equals(other.getValue(column))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean equalsSoft(Object obj) {
		if (!(obj instanceof DataRow)) {
			return false;
		}
		DataRow other = (DataRow) obj;
		if (values.keySet().size() != other.values.keySet().size()) {
			return false;
		}

		for (DataColumn column : values.keySet()) {
			boolean same = false;
			for (DataColumn otherColumn : other.values.keySet()) {
				if (column.equalsExcludeTable(otherColumn)) {
					if (!this.getValue(column).equals(other.getValue(otherColumn))) {
						return false;
					} else {
						same = true;
						break;
					}
				}
			}
			if (!same) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Set<String> getCodes() {
		return codes;
	}

	@Override
	public int hashCode() {
		int res = 0;
		for (Map.Entry<DataColumn, DataValue> entry : values.entrySet()) {
			DataColumn key = entry.getKey();
			DataValue value = entry.getValue();

			res += key.hashCode() * value.hashCode();
		}
		return res;
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

	/**
	 * Add the code code to the row.
	 * @param code the code that must be added to the row.
	 */
	public void addCode(String code) {
		codes.add(code);
	}
}