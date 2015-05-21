package model.data;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * The interface that defines a table.
 *
 * Created by jens on 5/19/15.
 */
public interface Table {

	/**
	 * Create an iterator that iterates over all rows of the table.
	 * @return an iterator that iterates over all rows
	 */
	Iterator<? extends Row> iterator();

	/**
	 * Flag the row that it should not be deleted.
	 * The flag is removed when delete is called.
	 * @param row the row that should not be deleted.
	 * @return true if the row has been found.
	 */
	boolean flagNotDelete(Row row);

	/**
	 * Delete all rows, except the rows that are flagged.
	 * Remove the flags from the remaining rows.
	 */
	void deleteNotFlagged();

	/**
	 * Return a copy of this table
	 * @return a copy of this row
	 */
	Table copy();

	@Override
	boolean equals(Object obj);

	/**
	 * Check if the columns are equals
	 * @param obj
	 * @return
	 */
	boolean equalStructure(Object obj);

	@Override
	int hashCode();

	/**
	 * Get the columns of the DataTable.
	 *
	 * @return A list that contains all the columns
	 */
	List<DataColumn> getColumns();

}
