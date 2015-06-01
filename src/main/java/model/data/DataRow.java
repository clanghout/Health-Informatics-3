package model.data;

import model.data.value.DataValue;
import model.exceptions.ColumnValueMismatchException;
import model.exceptions.ColumnValueTypeMismatchException;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Class that represents a row of data.
 */
public class DataRow extends Row {
	private Logger log = Logger.getLogger("DataRow");

	private Set<String> codes = new HashSet<>();
	private Map<SoftColumn, DataValue> values = new HashMap<>();

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
				values.put(new SoftColumn(columnArray[i]), valueArray[i]);
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
		values.put(new SoftColumn(column), value);
	}

	@Override
	public boolean hasColumn(DataColumn column) {
		return values.containsKey(new SoftColumn(column));
	}

	@Override
	public DataRow copy() {
		DataRow row = new DataRow();
		for (Map.Entry<SoftColumn, DataValue> entry : values.entrySet()) {
			row.setValue(entry.getKey().getColumn(), values.get(entry.getKey()).copy());
			row.addCodes(this.codes);
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
		SoftColumn[] columns = values.keySet().toArray(new SoftColumn[ values.keySet().size()]);
		if (columns.length > 0 && table.equalStructure(columns[0].getColumn().getTable())) {
			for (DataColumn column : table.getColumns()) {
				for (Map.Entry<SoftColumn, DataValue> entry : values.entrySet()) {
					if (entry.getKey().getColumn().equalsExcludeTable(column)) {
						row.setValue(column,
								values.get(entry.getKey()).copy());
						row.addCodes(this.codes);
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

		Set<DataColumn> myColumns = this.values.keySet().stream()
				.map(SoftColumn::getColumn)
				.collect(Collectors.toSet());
		Set<DataColumn> otherColumns = other.values.keySet().stream()
				.map(SoftColumn::getColumn)
				.collect(Collectors.toSet());

		return myColumns.equals(otherColumns) && this.values.equals(other.values);
	}

	/**
	 * return the columns of this row.
	 * @return a set that contains the columns of this row.
	 */
	public List<DataColumn> getColumns() {
		return values.keySet().stream().map(x -> x.getColumn()).collect(Collectors.toList());
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

		for (SoftColumn column : values.keySet()) {
			boolean same = false;
			for (SoftColumn otherColumn : other.values.keySet()) {
				if (column.getColumn().equalsExcludeTable(otherColumn.getColumn())) {
					if (!this.getValue(column.getColumn())
							.equals(other.getValue(otherColumn.getColumn()))) {
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
		for (Map.Entry<SoftColumn, DataValue> entry : values.entrySet()) {
			DataColumn key = entry.getKey().getColumn();
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
	@Override
	public DataValue getValue(DataColumn column) {
		return values.get(new SoftColumn(column));
	}

	/**
	 * Add the code code to the row.
	 * @param code the code that must be added to the row.
	 */
	@Override
	public void addCode(String code) {
		codes.add(code);
	}


	/**
	 * Add the codes codes to the row.
	 * @param codes the codes that must be added to the row.
	 */
	public void addCodes(Set<String> codes) {
		this.codes.addAll(codes);
	}

	/**
	 * This class is a wrapper around DataColumn, which only checks for the equality of the table
	 * name and the type and name of the DataColumn.
	 *
	 * This is to ensure that DataColumn of a copied table can still be used on the original.
	 */
	private static final class SoftColumn {

		private final DataColumn column;

		private SoftColumn(DataColumn column) {
			if (column == null) {
				throw new IllegalArgumentException("column is null");
			}
			this.column = column;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			SoftColumn that = (SoftColumn) o;

			return column.equalsOnTableName(that.column);
		}

		@Override
		public int hashCode() {
			return
					column.getType().hashCode()
					+ column.getName().hashCode();
		}

		private DataColumn getColumn() {
			return column;
		}
	}
}