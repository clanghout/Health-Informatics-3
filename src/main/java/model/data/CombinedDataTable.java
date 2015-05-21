package model.data;


import java.util.*;

/**
 * This class simulates the join of the tables.
 * In this way we can perform constrains over multiple tables.
 * But we can also separate the tables in the end.
 *
 * Created by jens on 5/18/15.
 */
public class CombinedDataTable implements Iterable, Table {
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

	/**
	 * flag a datarRow for no delete in the correct dataTable of the combined dataTable.
	 * @param row row that must not be delete
	 * @return true if there is a table found that had this row.
	 */
	public boolean flagNotDelete(DataRow row) {
		if (table.flagNotDelete(row)) {
			return true;
		} else if (combined != null) {
			return combined.flagNotDelete(row);
		} else {
			return false;
		}
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
			Set<DataColumn> columnSet = new HashSet<>();

			columnSet.addAll(combined.getColumns());
			columnSet.addAll(table.getColumns());

			return new ArrayList<>(columnSet);
		} else {
			return table.getColumns();
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
}


