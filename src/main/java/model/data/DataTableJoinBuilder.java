package model.data;

import model.data.value.DataValue;

import java.util.*;

/**
 * Created by jens on 6/9/15.
 */
public class DataTableJoinBuilder extends DataTableBuilder{
	private Table table;
	/**
	 * Map the old the column to the new column to the new column.
	 * Key is the old column
	 * Value is net new column
	 */
	private Map<DataColumn, DataColumn> mappingColumns;
	/**
	 * Map the combined columns
	 * Key is the origin
	 * Value is the column is should be mapped to.
	 */
	private Map<DataColumn, DataColumn> combineColumns;
	private boolean fullLeft;
	private boolean fullRight;

	/**
	 * Create a datatableconversion builder. This uses a table to create the columns.
	 * @param table table from where the columns should be copied.
	 */
	public DataTableJoinBuilder(Table table) {
		this.table = table;
		this.fullLeft = false;
		this.fullRight = false;
		this.combineColumns = new HashMap<>();
	}

	public void addCombineColumn(DataColumn col1, DataColumn col2) {
		if (!col1.getType().equals(col2.getType())) {
			throw new IllegalArgumentException("type of columns differ");
		}
		combineColumns.put(col1, col2);
	}

	/**
	 * This function ensures that there is no value in map that is also a key in map.
	 *
	 * The user specifies which column must get combined.
	 * It is possible that the user wants to combine one column with multiple other column.
	 * So it is possible that the combineColumn map contains the following entry's: (1,2) (2,3).
	 * In this case (1,2) must be rewritten to (1,3).
	 * Furthermore it is possible that the combineColumn map contains: (1,2)(2,3)(3,1)
	 * It this case is must be rewritten to (1,3)(2,3).
	 */
	private void simplifyCombinedColumns() {
		boolean changed = true;
		while(changed) {
			changed = false;
			for (Map.Entry<DataColumn, DataColumn> columnEntry :  combineColumns.entrySet()) {
				if (combineColumns.containsKey(columnEntry.getValue())) {
					//check if the key and the value of the entry are not the same.
					if (combineColumns.get(columnEntry.getValue()).equals(columnEntry.getValue())) {
						combineColumns.remove(columnEntry.getValue());
					} else {
						combineColumns.put(columnEntry.getKey(),
								combineColumns.get(columnEntry.getValue()));
					}
					changed = true;
				}
			}
		}
	}

	/**
	 * Convert the table in a new DataTable.
	 * This clones a DataTable, a CombTable will be joined, so it becomes a DataTable
	 *
	 * @return a DataTable that represents the table
	 */
	public DataTable build() {
		simplifyCombinedColumns();
		addColumns();
		processRows();

		return super.build();
	}

	private void checkNameCollision(Map<String, DataColumn> mappingNewNameToOldColumns,
									Set<String> forbidden,
									DataColumn column,
									String nameColumn) {
		if (mappingNewNameToOldColumns.containsKey(nameColumn)) {
			forbidden.add(nameColumn);
			String newName = mappingNewNameToOldColumns.get(nameColumn).getTable().getName()
					+ "_" + nameColumn;
			DataColumn tempColumn = mappingNewNameToOldColumns.remove(nameColumn);
			checkNameCollision(mappingNewNameToOldColumns, forbidden, tempColumn, newName);
		}

		if (forbidden.contains(nameColumn)) {
			String newName = column.getTable().getName() + "_" + nameColumn;
			checkNameCollision(mappingNewNameToOldColumns, forbidden, column, newName);
		} else {
			mappingNewNameToOldColumns.put(nameColumn, column);
		}
	}

	private Map<String, DataColumn> addColumnToNameMapping() {
		Map<String, DataColumn> mappingNewNameToOldColumns = new HashMap<>();
		Set<String> forbidden = new HashSet<>();

		for (DataColumn column : table.getColumns()) {
			if (!combineColumns.containsKey(column)) {
				checkNameCollision(mappingNewNameToOldColumns, forbidden, column, column.getName());
			}
		}
		return mappingNewNameToOldColumns;
	}

	/**
	 * Create the columns for the DataTable.
	 * @return a mapping of old columns to new columns
	 */
	private Map<DataColumn, DataColumn> addColumns() {
		mappingColumns = new HashMap<>();
		Map<String, DataColumn> mappingNewNameToOldColumns = addColumnToNameMapping();
		generateMappingColumns(mappingNewNameToOldColumns);

		generateMappingCombinedCollumns();

		return mappingColumns;
	}

	private void generateMappingCombinedCollumns() {
		for (Map.Entry<DataColumn, DataColumn> entry : combineColumns.entrySet()) {
			if (mappingColumns.containsKey(entry.getValue())) {
				mappingColumns.put(entry.getKey(), mappingColumns.get(entry.getKey()));
			}
		}
	}

	private void generateMappingColumns(Map<String, DataColumn> mappingNewNameToOldColumns) {
		for (Map.Entry<String, DataColumn> entry : mappingNewNameToOldColumns.entrySet()) {
			DataColumn newColumn = new DataColumn(
					entry.getKey(),
					null,
					entry.getValue().getType());

			this.addColumn(newColumn);
			mappingColumns.put(entry.getValue(), newColumn);
		}
	}


	/**
	 * Add the rows to the dataTable.
	 */
	private void processRows() {
		for (Row row : (Iterable<Row>) table) {
			DataRow newRow = new DataRow();

			for (Map.Entry<DataColumn, DataColumn> entry : mappingColumns.entrySet()) {
				newRow.setValue(entry.getValue(), row.getValue(entry.getKey()));
				newRow.addCodes(row.getCodes());
			}
			this.addRow(newRow);
		}
	}




}
