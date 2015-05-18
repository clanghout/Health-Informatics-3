package model.data;

import javax.xml.crypto.Data;
import java.lang.reflect.Array;
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
							new CombinedDataRow(table.getRow(index), combinedIterator.next());
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

