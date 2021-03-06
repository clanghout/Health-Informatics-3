package model.data;


import java.util.*;

/**
 * This class simulates the join of the tables.
 * In this way we can perform constrains over multiple tables.
 * But we can also separate the tables in the end.
 *
 * Created by jens on 5/18/15.
 */
public class CombinedDataTable extends Table {
	private DataTable table;
	private CombinedDataTable combined;

	public CombinedDataTable(DataTable table, DataTable... tables) {
		this.table = table;
		if (tables.length != 0) {
			combined = new CombinedDataTable(tables[0],
					Arrays.copyOfRange(tables, 1, tables.length));
		}
	}

	@Override
	public Iterator<? extends Row> iterator() {
		if (combined != null) {
			return combinedIterator();
		} else {
			return table.iterator();
		}
	}

	@Override
	public boolean flagNotDelete(Row row) {
		boolean result = true;
		if (row instanceof CombinedDataRow) {
			CombinedDataRow combRow = (CombinedDataRow) row;
			List<DataRow> rows = combRow.getRows();
			for (DataRow dataRow : rows) {
				result = result && flagNotDelete(dataRow);
			}
		}
		return result;

	}

	@Override
	public CombinedDataTable copy() {
		CombinedDataTable combined = null;
		if (this.combined != null) {
			combined = this.combined.copy();
		}
		CombinedDataTable result = new CombinedDataTable(table.copy());
		result.combined = combined;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CombinedDataTable)) {
			return false;
		}
		CombinedDataTable other = (CombinedDataTable) obj;
		if (this.combined != null) {
			return table.equals(other.table) && this.combined.equals(other.combined);
		}
		return table.equals(other.table) && other.combined == null;
	}

	@Override
	public boolean equalsSoft(Object obj) {
		if (!(obj instanceof CombinedDataTable)) {
			return false;
		}
		CombinedDataTable other = (CombinedDataTable) obj;
		if (this.combined != null) {
			return table.equalsSoft(((CombinedDataTable) obj).table)
					&& this.combined.equalsSoft(other.combined);
		}
		return table.equalsSoft(((CombinedDataTable) obj).table) && other.combined == null;

	}

	@Override
	public int hashCode() {
		int res = 0;
		if (combined != null) {
			res += combined.hashCode();
		}
		return table.hashCode() + res;
	}

	/**
	 * flag a datarRow for no delete in the correct dataTable of the combined dataTable.
	 * @param row row that must not be delete
	 * @return true if there is a table found that had this row.
	 */
	public boolean flagNotDelete(DataRow row) {
		return table.flagNotDelete(row)
				|| combined != null && combined.flagNotDelete(row);
	}

	@Override
	public void deleteNotFlagged() {
		table.deleteNotFlagged();
		if (combined != null) {
			combined.deleteNotFlagged();
		}
	}

	@Override
	public List<DataColumn> getColumns() {
		if (combined != null) {
			ArrayList res = new ArrayList(table.getColumns());
			res.addAll(combined.getColumns());
			return res;
		} else {
			return table.getColumns();
		}
	}

	@Override
	public DataTable getTable(String name) {
		if (table.getName().equals(name)) {
			return table;
		} else if (combined != null) {
			return combined.getTable(name);
		} else {
			throw new NoSuchElementException("table " + name + " not found");
		}
	}


	private Iterator<CombinedDataRow> combinedIterator() {
		return new Iterator<CombinedDataRow>() {
			private int index = 0;
			private Iterator<? extends Row> combinedIterator = combined.iterator();

			@Override
			public boolean hasNext() {
				return index < table.getRowCount();
			}

			@Override
			public CombinedDataRow next() {
				if (hasNext()) {
					CombinedDataRow result;
					Row row = combinedIterator.next();
					if (row instanceof CombinedDataRow) {
						result = (CombinedDataRow) row;
					} else {
						result = new CombinedDataRow();
						result.addDataRow((DataRow) row);
					}
					result.addDataRow(table.getRow(index));
					if (!combinedIterator.hasNext()) {
						index++;
						combinedIterator = combined.iterator();
					}
					return result;
				} else {
					throw new NoSuchElementException();
				}
			}
		};
	}

	/**
	 * Return a list that contains all the tables in the combined dataTable.
	 * @return a list that contains all the tables.
	 */
	public List<DataTable> getTables() {
		List<DataTable> list = getReversedTablesList();
		Collections.reverse(list);
		return list;
	}

	/**
	 * Return a list that contains all the tables in the combined dataTable.
	 * The table are in reversed order
	 * @return a list that contains all the tables.
	 */
	private List<DataTable> getReversedTablesList() {
		List<DataTable> res = new ArrayList<>();
		if (combined != null) {
			res = combined.getReversedTablesList();
		}
		res.add(table);
		return res;
	}
}


