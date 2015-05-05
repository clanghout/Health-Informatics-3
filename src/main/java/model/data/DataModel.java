package model.data;

import java.util.*;

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
		rows = new ArrayList<DataRow>();
		columns = new HashMap<String, DataColumn>();
	}

	/**
	 * create a new DataModel
	 * @param rows rows of the dataModel
	 * @param columns columns of the dataModel
	 */
	public DataModel(List<DataRow> rows, Map<String, DataColumn> columns) {
		this.rows = new ArrayList<>(rows);
		this.columns = columns;
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
	public Iterator<DataRow> getRows() {
		return rows.iterator();
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
	 * @return a HashMap that contains all the columns
	 */
	public Map<String, DataColumn> getColumns() {
		return columns;
	}

}
