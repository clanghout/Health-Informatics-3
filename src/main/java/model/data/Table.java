package model.data;

import java.util.Iterator;
import java.util.List;


/**
 * The interface that defines a table.
 *
 * Created by jens on 5/19/15.
 */
public abstract class Table implements Iterable {

	/**
	 * Create an iterator that iterates over all rows of the table.
	 * @return an iterator that iterates over all rows
	 */
	public abstract Iterator<? extends Row> iterator();

	/**
	 * Flag the row that it should not be deleted.
	 * The flag is removed when delete is called.
	 * @param row the row that should not be deleted.
	 * @return true if the row has been found.
	 */
	public abstract boolean flagNotDelete(Row row);

	/**
	 * Delete all rows, except the rows that are flagged.
	 * Remove the flags from the remaining rows.
	 */
	public abstract void deleteNotFlagged();

	/**
	 * Return a copy of this table.
	 * @return a copy of this row
	 */
	public abstract Table copy();

	@Override
	public abstract boolean equals(Object obj);

	/**
	 * Check if two objects have the same values
	 *
	 * Equals wants that the references to the attributes are the same.
	 * equalsSoft want that the value of the attributes are the same.
	 * So equals is more strict than equalsSoft.
	 * @param obj the table that should be compared
	 * @return true if object is the same as this.
	 */
	public abstract boolean equalsSoft(Object obj);
	/**
	 * Check if the columns has the same name and type.
	 * @param obj the table that should be compared
	 * @return true if the tables have the same structure
	 */
	public boolean equalStructure(Object obj) {
		if (!(obj instanceof Table)) {
			return false;
		}

		Table table = (Table) obj;
		List<DataColumn> otherColumns = table.getColumns();
		List<DataColumn> columns = table.getColumns();


		if (otherColumns.size() == columns.size()) {
			for (DataColumn column : columns) {
				boolean same = false;
				for (DataColumn otherColumn : otherColumns) {
					if (otherColumn.equalsExcludeTable(column)) {
						same = true;
						break;
					}
				}
				if (!same) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public abstract int hashCode();

	/**
	 * Convert the table in a new DataTable.
	 * This clones a DataTable, a CombTable will be joined, so it becomes a DataTable
	 *
	 * @param name name of the new table
	 * @return a DataTable that represents the table
	 */
	public abstract DataTable export(String name);

	/**
	 * Get the columns of the DataTable.
	 *
	 * @return A list that contains all the columns
	 */
	public abstract List<DataColumn> getColumns();


}
