package model.data;

import model.data.value.DataValue;

/**
 * The interface that defines a tableRow
 *
 * Created by jens on 5/19/15.
 */
public interface Row {
	/**
	 * Get the value of the column in this row.
	 * @param column the column that should be looked up
	 * @return the value of the column
	 */
	DataValue getValue(DataColumn column);

	/**
	 * Set the value of the column in this row.
	 * @param column the column that should be set
	 * @param value the new value
	 */
	void setValue(DataColumn column, DataValue value);

	/**
	 * Check if the row contains the column.
	 * @param column the column
	 * @return true if the row contains the column
	 */
	boolean hasColumn(DataColumn column);

}
