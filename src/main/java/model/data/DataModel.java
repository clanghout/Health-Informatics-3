package model.data;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

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
	public DataModel(ArrayList<DataRow> rows, Map<String, DataColumn> columns) {
		this.rows = rows;
		this.columns = columns;
	}

	/**
	 * Get a specific row
	 *
	 * @param i number of the row
	 * @return the requested row
	 */
//	public DataRow getRow(int i) {
//		return data.get(i);
//	}

	/**
	 * return an iterator of the rows
	 *
	 * @return an iterator that contains al the rows
	 */
	public Iterator<DataRow> getRows() {
		return rows.iterator();
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
