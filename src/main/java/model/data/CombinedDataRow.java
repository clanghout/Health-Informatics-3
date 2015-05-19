package model.data;

import model.data.value.DataValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is able to store multiple rows.
 * This is used for analyses over multiple tables.
 * So this simulates a join of multiple tables
 *
 * Created by jens on 5/18/15.
 */
public class CombinedDataRow implements Row {
	private List<DataRow> rows;

	public CombinedDataRow() {
		this.rows = new ArrayList<>();

	}

	/**
	 * add a row to the combine datarow
	 * @param row the row to add
	 */
	public void addDataRow(DataRow row) {
		rows.add(row);
	}

	@Override
	public DataValue getValue(DataColumn column) {
		for (DataRow row : rows) {
			if (row.getValue(column) != null) {
				return row.getValue(column);
			}
		}
		return null;
	}

	@Override
	public void setValue(DataColumn column, DataValue value) {
		for (DataRow row : rows) {
			if (row.getValue(column) != null) {
				row.setValue(column, value);
				return;
			}
		}
	}

}
