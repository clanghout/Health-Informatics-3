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
	 * Create the columns for the DataTable.
	 * @param builder builder used to create the datatable
	 * @return a mapping of old columns to new columns
	 */
	private Map<DataColumn, DataColumn> addColumns(DataTableBuilder builder) {
		Map<DataColumn, DataColumn> mappingColumns = new HashMap<>();
		Map<String, DataColumn> mappingNewNameToOldColumns = new HashMap<>();
		Set<String> forbidden = new HashSet<>();

		List<DataColumn> columns = getColumns();
		for (DataColumn column : columns) {
			String nameColumn = column.getName();
			if (mappingNewNameToOldColumns.containsKey(nameColumn)) {
				forbidden.add(nameColumn);
				String newName = mappingNewNameToOldColumns.get(nameColumn).getTable().getName()
						+ "." + nameColumn;
				DataColumn tempColumn = mappingNewNameToOldColumns.remove(nameColumn);
				mappingNewNameToOldColumns.put(newName, tempColumn);
			}
			if (forbidden.contains(nameColumn)) {
				nameColumn = column.getTable().getName() + "." + nameColumn;
			}
			mappingNewNameToOldColumns.put(nameColumn, column);
		}

		for (Map.Entry<String, DataColumn> entry: mappingNewNameToOldColumns.entrySet()) {
			DataColumn newColumn = new DataColumn(entry.getKey(), null, entry.getValue().getType());
			builder.addColumn(newColumn);
			mappingColumns.put(entry.getValue(), newColumn);
		}

		return mappingColumns;
	}

	/**
	 * Add the rows to the dataTable.
	 * @param mappingColumns the mapping of the pld columns to the new columns
	 * @param builder the dataBuilder used for constructing the table.
	 */
	private void processRows(Map<DataColumn, DataColumn> mappingColumns, DataTableBuilder builder) {
		Iterator<? extends Row> iterator = iterator();
		while (iterator.hasNext()) {
			DataRow newRow = new DataRow();
			Row row = iterator.next();

			for (Map.Entry<DataColumn, DataColumn> entry: mappingColumns.entrySet()) {
				newRow.setValue(entry.getValue(), row.getValue(entry.getKey()).copy());
			}
			builder.addRow(newRow);
		}
	}

	@Override
	public DataTable export(String nameTable) {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName(nameTable);

		Map<DataColumn, DataColumn> mappingColumns = addColumns(builder);
		processRows(mappingColumns, builder);

		return builder.build();
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


