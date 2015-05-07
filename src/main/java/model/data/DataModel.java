package model.data;

import java.util.*;

import static java.util.Collections.unmodifiableList;

/**
 * class that represents the data that should be analyzed
 */
public class DataModel {
	private ArrayList<DataRow> rows;
	private Map<String, DataColumn> columns;

	/**
	 * create a new empty DataModel
	 */
	public DataModel() {
		rows = new ArrayList<>();
		columns = new HashMap<>();
	}

	/**
	 * create a new DataModel
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
	 * Get a specific row
	 *
	 * @param i index of the row
	 * @return index of the requested row
	 */
	public DataRow getRow(int i) {
		return rows.get(i);
	}

	/**
	 * return an iterator of the rows
	 *
	 * @return an iterator over the rows
	 */
	public List<DataRow> getRows() {
		return unmodifiableList(rows);
	}

	/**
	 * Returns the amounts of rows
	 * @return The amount of rows
	 */
	public int getRowCount() {
		return rows.size();
	}

	/**
	 * get the columns of the dataModel
	 *
	 * @return a Map that contains all the columns
	 */
	public Map<String, DataColumn> getColumns() {
		return columns;
	}

}
