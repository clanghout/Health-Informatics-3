package model.data;


import java.util.*;

/**
 * This class simulates the join of the tables.
 * In this way we can perform constrains over multiple tables.
 * But we can also separate the tables in the end.
 *
 * Created by jens on 5/18/15.
 */
public class CombinedDataTable implements Iterable {
	private DataTable table;
	private CombinedDataTable combined;

	public CombinedDataTable(DataTable table, DataTable... tables) {
		this.table = table;
		if (tables.length != 0) {
			combined = new CombinedDataTable(tables[0], Arrays.copyOfRange(tables, 1, tables.length));
		}
	}

	@Override
	public Iterator<CombinedDataRow> iterator() {
		if (combined != null) {
			return combinedIterator();
		} else {
			return rowIterator();
		}
	}

	private Iterator<CombinedDataRow> rowIterator() {
		return new Iterator<CombinedDataRow>() {
			private int index = 0;

			@Override
			public boolean hasNext() {
				return index < table.getRowCount();
			}

			@Override
			public CombinedDataRow next() {
				if (hasNext()) {
					CombinedDataRow result = new CombinedDataRow();
					result.addDataRow(table.getRow(index), table.getName());
					index++;
					return result;
				} else {
					throw new NoSuchElementException();
				}
			}
		};
	}


	private Iterator<CombinedDataRow> combinedIterator() {
		return new Iterator<CombinedDataRow>() {
			private int index = 0;
			private Iterator<CombinedDataRow> combinedIterator = combined.iterator();

			@Override
			public boolean hasNext() {
				return index < table.getRowCount();
			}

			@Override
			public CombinedDataRow next() {
				if (hasNext()) {
					CombinedDataRow result =
							combinedIterator.next();
					result.addDataRow(table.getRow(index), table.getName());
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


