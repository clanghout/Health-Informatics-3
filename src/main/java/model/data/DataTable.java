package model.data;

import java.util.*;

/**
 * Class that represents the data that should be analysed.
 */
public class DataTable implements Table, Iterable {
	private List<DataRow> rows;
	private Map<String, DataColumn> columns;
	private String name;

	/**
	 * Create a new empty DataTable.
	 */
	public DataTable() {
		rows = new ArrayList<>();
		columns = new HashMap<>();
	}

	/**
	 * Create an new empty DataTable with the given name.
	 * @param name The name you want to give to this DataTable
	 */
	public DataTable(String name) {
		this();
		this.name = name;
	}

	/**
	 * Create a new DataTable.
	 * @param rows rows of the DataTable
	 * @param columns columns of the DataTable
	 */
	public DataTable(String name, List<DataRow> rows, List<DataColumn> columns) {
		this();
		this.rows = new ArrayList<>(rows);
		for (DataColumn c : columns) {
			this.columns.put(c.getName(), c);
		}
		this.name = name;
	}

	/**
	 * Get a specific row.
	 *
	 * @param i index of the row
	 * @return index of the requested row
	 */
	public DataRow getRow(int i) {
		return rows.get(i);
	}

	/**
	 * Return an unmodifiable list containing the rows.
	 *
	 * @return An unmodifiable list over the rows
	 */
	public List<DataRow> getRows() {
		return Collections.unmodifiableList(rows);
	}

	/**
	 * Returns the amounts of rows.
	 * @return The amount of rows
	 */
	public int getRowCount() {
		return rows.size();
	}

	/**
	 * Get the columns of the DataTable.
	 *
	 * @return A Map that contains all the columns, the key is the column name.
	 */
	public Map<String, DataColumn> getColumns() {
		return columns;
	}

	public String getName() {
		return name;
	}

	@Override
	public Iterator<DataRow> iterator() {
		return rows.iterator();
	}
}
