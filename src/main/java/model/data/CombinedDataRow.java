package model.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is able to store multiple rows.
 * This is used for analyses over multiple tables.
 *
 * Created by jens on 5/18/15.
 */
public class CombinedDataRow {
	private Map<String, DataRow> rows;

	public CombinedDataRow() {
		this.rows = new HashMap<>();

	}

	/**
	 * add a row to the combine datarow
	 * @param row the row to add
	 * @param name the name of the added tow
	 */
	public void addDataRow(DataRow row, String name) {
		rows.put(name, row);
	}

	/**
	 * return the datarow with the name name.
	 * @param name name of the datarow
	 * @return the datarow with the name name.
	 */
	public DataRow getRow(String name) {
		return rows.get(name);
	}
}
