package model.data;

import java.util.*;

/**
 * Class that represents the data that should be analysed.
 */
public class DataModel {
	private ArrayList<DataRow> rows;
	private Map<String, DataColumn> columns;

	/**
	 * Create a new empty DataModel.
	 */
	public DataModel() {
		rows = new ArrayList<>();
		columns = new HashMap<>();
	}

	/**
	 * Create a new DataModel.
	 * @param rows rows of the dataModel
	 * @param columns columns of the dataModel
	 */
	public DataModel(List<DataRow> rows, List<DataColumn> columns) {
		this();
		this.rows = new ArrayList<>(rows);
		for (DataColumn c : columns) {
			this.columns.put(c.getName(), c);
		}
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
	 * Get the columns of the dataModel.
	 *
	 * @return A Map that contains all the columns, the key is the column name.
	 */
	public Map<String, DataColumn> getColumns() {
		return columns;
	}

}
