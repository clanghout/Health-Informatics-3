package model.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A container for the various data tables.
 * Created by Boudewijn on 12-5-2015.
 */
public class DataModel implements Iterable<DataTable> {

	private List<DataTable> dataTables = new ArrayList<>();

	/**
	 * Construct a new empty DataModel.
	 */
	public DataModel() { }

	/**
	 * Construct a new DataModel containing the given tables.
	 * @param tables The tables you want to add to this data model.
	 */
	public DataModel(Collection<DataTable> tables) {
		dataTables.addAll(tables);
	}

	/**
	 * Get the iterator for this DataModel.
	 * @return An iterator over this DataModel.
	 */
	public Iterator<DataTable> iterator() {
		return dataTables.iterator();
	}

	/**
	 * Add the given table to this DataModel.
	 * @param table The table you want to add
	 */
	public void add(DataTable table) {
		dataTables.add(table);
	}

	/**
	 * Add the given tables to this DataModel.
	 * @param tables The tables you want to add
	 */
	public void addAll(Collection<DataTable> tables) {
		dataTables.addAll(tables);
	}

	/**
	 * Returns the size of this DataModel.
	 * @return The number of tables in this DataModel.
	 */
	public int size() {
		return dataTables.size();
	}

	/**
	 * Check to see if this DataModel contains a table.
	 * @param table The table you want to check the containiness of.
	 * @return True if the DataModel contains the table, false if not.
	 */
	public boolean contains(DataTable table) {
		return dataTables.contains(table);
	}

	/**
	 * Get the index of the given table in the DataModel.
	 * @param table The table you want to get the index of.
	 * @return The index or -1 if the table isn't contained in this DataModel.
	 */
	public int indexOf(DataTable table) {
		return dataTables.indexOf(table);
	}

	/**
	 * Get the DataTable at the given index.
	 * @param index The index of the DataTable you want to get.
	 * @throws IndexOutOfBoundsException If the given index is out of bounds.
	 * @return The DataTable at the given index.
	 */
	public DataTable get(int index) {
		return dataTables.get(index);
	}
}
