package model.data;

import java.util.Iterator;
import java.util.List;


/**
 * The interface that defines a table.
 *
 * Created by jens on 5/19/15.
 */
public abstract class Table implements Iterable{

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
	 * Return a copy of this table
	 * @return a copy of this row
	 */
	public abstract Table copy();

	@Override
	public abstract boolean equals(Object obj);
	public abstract boolean equalsSoft(Object obj);
	/**
	 * Check if the columns are equals
	 * @param obj
	 * @return
	 */
	public boolean equalStructure(Object obj) {
		if (! (obj instanceof Table)) {
			return false;
		}

		Table table = (Table) obj;
		List<DataColumn> otherColumns = table.getColumns();
		List<DataColumn> columns = table.getColumns();


		if (otherColumns.size() == columns.size()) {
			for (int i = 0; i < columns.size(); i++) {
				boolean result = false;
				for (int j = 0; j < otherColumns.size(); j++) {
					if (otherColumns.get(j).equalsExcludeTable(columns.get(i))) {
						result = true;
					}
				}
				if(!result) {
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
	 * Get the columns of the DataTable.
	 *
	 * @return A list that contains all the columns
	 */
	public abstract List<DataColumn> getColumns();


}
