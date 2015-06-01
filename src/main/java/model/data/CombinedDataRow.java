package model.data;

import model.data.value.DataValue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is able to store multiple rows.
 * This is used for analyses over multiple tables.
 * So this simulates a join of multiple tables
 *
 * Created by jens on 5/18/15.
 */
public class CombinedDataRow extends Row {
	private List<DataRow> rows;

	public CombinedDataRow() {
		this.rows = new ArrayList<>();

	}

	/**
	 * add a row to the combine datarow.
	 * @param row the row to add
	 */
	public void addDataRow(DataRow row) {
		rows.add(row);
	}

	/**
	 * return the rows of this combinedDataRow.
	 * @return a list of the rows in the combinedDataRow
	 */
	public List<DataRow> getRows() {
		return rows;
	}

	@Override
	public DataValue getValue(DataColumn column) {
		for (DataRow row : rows) {
			if (row.hasColumn(column)) {
				return row.getValue(column);
			}
		}
		throw new IllegalArgumentException("no such column");
	}

	@Override
	public void setValue(DataColumn column, DataValue value) {
		for (DataRow row : rows) {
			if (row.hasColumn(column)) {
				row.setValue(column, value);
				return;
			}
		}
		throw new IllegalArgumentException("no such column");
	}

	@Override
	public boolean hasColumn(DataColumn column) {
		for (DataRow row : rows) {
			if (row.hasColumn(column)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public CombinedDataRow copy() {
		CombinedDataRow combRow = new CombinedDataRow();
		for (DataRow row : rows) {
			combRow.addDataRow(row.copy());
		}

		return combRow;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CombinedDataRow)) {
			return false;
		}
		CombinedDataRow other = (CombinedDataRow) obj;
		if (rows.size() != other.rows.size()) {
			return false;
		}
		for (DataRow row : rows) {
			if (!other.rows.contains(row)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean equalsSoft(Object obj) {
		if (!(obj instanceof CombinedDataRow)) {
			return false;
		}
		CombinedDataRow other = (CombinedDataRow) obj;
		if (rows.size() != other.rows.size()) {
			return false;
		}
		for (DataRow row : rows) {
			boolean same = false;
			for (DataRow rowOther : other.rows) {
				if (row.equalsSoft(rowOther)) {
					same = true;
					break;
				}
			}
			if (!same) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Set<String> getCodes() {
		Set<String> codes = new HashSet<>();
		for (DataRow row : rows) {
			codes.addAll(row.getCodes());
		}

		return codes;
	}

	@Override
	public int hashCode() {
		int res = 0;
		for (DataRow row : rows) {
			res += row.hashCode();
		}
		return res;
	}

	@Override
	public void addCode(String code) {
		for (DataRow row : rows) {
			row.addCode(code);
		}
	}

}
