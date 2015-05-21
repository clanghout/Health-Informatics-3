package model.data;

import model.data.value.DataValue;

/**
 * The interface that defines a tableRow.
 *
 * Created by jens on 5/19/15.
 */
public abstract class Row {
	/**
	 * Get the value of the column in this row.
	 * @param column the column that should be looked up
	 * @return the value of the column
	 */
	public abstract DataValue getValue(DataColumn column);

	/**
	 * Set the value of the column in this row.
	 * @param column the column that should be set
	 * @param value the new value
	 */
	public abstract void setValue(DataColumn column, DataValue value);

	/**
	 * Check if the row contains the column.
	 * @param column the column
	 * @return true if the row contains the column
	 */
	public abstract boolean hasColumn(DataColumn column);

	/**
	 * Return a copy of this row
	 * @return a copy of this row
	 */
	public abstract Row copy();

	@Override
	public abstract boolean equals(Object obj);
	public abstract boolean equalsSoft(Object obj);
	public abstract int hashCode();



}
