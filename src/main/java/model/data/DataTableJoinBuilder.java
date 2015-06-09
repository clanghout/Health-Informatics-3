package model.data;

import model.data.describer.ConstantDescriber;
import model.data.describer.DataDescriber;
import model.data.value.BoolValue;
import model.data.value.DataValue;
import model.process.analysis.ConstraintAnalysis;

import java.util.*;

/**
 * Created by jens on 6/9/15.
 */
public class DataTableJoinBuilder extends DataTableBuilder{
	private Table table;
	private DataTable left;
	private DataTable right;
	private DataDescriber<BoolValue> constrain;
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
	 * Create a DataTableJoinBuilder builder. This uses a table to create the columns.
	 * @param table table from where the columns should be copied.
	 */
	public DataTableJoinBuilder(Table table) {
		this.table = table;
		this.fullLeft = false;
		this.fullRight = false;
		this.combineColumns = new LinkedHashMap<>();
		this.constrain = new ConstantDescriber<>(new BoolValue(true));
	}


	/**
	 * join two table.
	 * @param left left table
	 * @param right right table
	 * @param fullLeft true if all left rows should exist in the result
	 * @param fullRight true if all right rows should exist in the result
	 */
	public DataTableJoinBuilder(DataTable left,
								DataTable right,
								boolean fullLeft,
								boolean fullRight) {
		this(new CombinedDataTable(left, right));
		this.left = left;
		this.right = right;
		this.fullLeft = fullLeft;
		this.fullRight = fullRight;
	}

	public void setConstraint( DataDescriber<BoolValue> constraint) {
		this.constrain = constraint;
	}

	/**
	 * add two columns that must get combined
	 * @param col1
	 * @param col2
	 */
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
	@Override
	public DataTable build() throws InstantiationException, IllegalAccessException {
		simplifyCombinedColumns();
		addColumns();

		if (fullLeft || fullRight) {
			processFullJoin();
		} else {
			processRows();
		}

		return super.build();
	}

	/**
	 * check if each column gets a unique name
	 * @param mappingNewNameToOldColumns map that links the names with the original columns
	 * @param forbidden set of column names that already exist
	 *                  or names for which their was a name collision before.
	 * @param column original column
	 * @param nameColumn possible name for the column.
	 */
	private void checkNameCollision(Map<String, DataColumn> mappingNewNameToOldColumns,
									Map<DataColumn, String> mappingColumnsToName,
									Set<String> forbidden,
									DataColumn column,
									String nameColumn) {
		if (mappingNewNameToOldColumns.containsKey(nameColumn)) {
			forbidden.add(nameColumn);
			String newName = mappingNewNameToOldColumns.get(nameColumn).getTable().getName()
					+ "_" + nameColumn;
			DataColumn tempColumn = mappingNewNameToOldColumns.remove(nameColumn);
			checkNameCollision(mappingNewNameToOldColumns, mappingColumnsToName, forbidden, tempColumn, newName);
		}

		if (forbidden.contains(nameColumn)) {
			String newName = column.getTable().getName() + "_" + nameColumn;
			checkNameCollision(mappingNewNameToOldColumns, mappingColumnsToName, forbidden, column, newName);
		} else {
			mappingNewNameToOldColumns.put(nameColumn, column);
			mappingColumnsToName.put(column, nameColumn);
		}
	}

	/**
	 * assign to each column a unique name
	 * @return a map that maps unique names to columns
	 */
	private Map<DataColumn, String> addColumnToNameMapping() {
		Map<String, DataColumn> mappingNewNameToOldColumns = new LinkedHashMap<>();
		Map<DataColumn, String> mappingOldColumnsToNewName = new LinkedHashMap<>();
		Set<String> forbidden = new HashSet<>();

		for (DataColumn column : table.getColumns()) {
			if (!combineColumns.containsKey(column)) {
				checkNameCollision(mappingNewNameToOldColumns, mappingOldColumnsToNewName, forbidden, column, column.getName());
			}
		}
		return mappingOldColumnsToNewName;
	}

	/**
	 * Create the columns for the DataTable.
	 * @return a mapping of old columns to new columns
	 */
	private Map<DataColumn, DataColumn> addColumns() {
		Map<DataColumn, String> mappingOldColumnsToNewName = addColumnToNameMapping();
		mappingColumns = new LinkedHashMap<>();
		generateMappingColumns(mappingOldColumnsToNewName);

		generateMappingCombinedCollumns();

		return mappingColumns;
	}

	/**
	 * generate the mapping between the old and new column.
	 * map the columns that should combined with another column to the new column.
	 */
	private void generateMappingCombinedCollumns() {
		for (Map.Entry<DataColumn, DataColumn> entry : combineColumns.entrySet()) {
			if (mappingColumns.containsKey(entry.getValue())) {
				mappingColumns.put(entry.getKey(), mappingColumns.get(entry.getValue()));
			}
		}
	}

	/**
	 * create the new columns and generate the mapping bewteen the old and new columns.
	 * @param mappingNewNameToOldColumns mapping between the new names and the old columns
	 */
	private void generateMappingColumns(Map<DataColumn, String> mappingNewNameToOldColumns) {
		for (Map.Entry<DataColumn, String> entry : mappingNewNameToOldColumns.entrySet()) {
			DataColumn newColumn = new DataColumn(
					entry.getValue(),
					null,
					entry.getKey().getType());

			this.addColumn(newColumn);
			mappingColumns.put(entry.getKey(), newColumn);
		}
	}


	/**
	 * Add the rows to the dataTable.
	 */
	private void processRows() {
		for (Row row : (Iterable<Row>) table) {
			DataRow newRow = new DataRow();
			if (constrain.resolve(row).getValue()) {
				for (Map.Entry<DataColumn, DataColumn> entry : mappingColumns.entrySet()) {
					newRow.setValue(entry.getValue(), row.getValue(entry.getKey()));
					newRow.addCodes(row.getCodes());
				}

				this.addRow(newRow);
			}
		}
	}

	/**
	 * Create the rows for the join
	 * @param leftRow row in the left table
	 * @param rightRow row in the right table
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private void createRow(DataRow leftRow, DataRow rightRow) throws IllegalAccessException, InstantiationException {
		DataRow newRow = new DataRow();
		for (Map.Entry<DataColumn, DataColumn> entry : mappingColumns.entrySet()) {
			if (leftRow.hasColumn(entry.getKey())) {
				checkValue(newRow, entry.getValue(), leftRow.getValue(entry.getKey()));
			} else {
				//TODO set value to null
				checkValue(newRow, entry.getValue(), entry.getValue().getType().newInstance());
			}
			if (rightRow.hasColumn(entry.getKey())) {
				checkValue(newRow, entry.getValue(), leftRow.getValue(entry.getKey()));
			}
		}
		newRow.addCodes(leftRow.getCodes());
		newRow.addCodes(rightRow.getCodes());
		this.addRow(newRow);
	}

	/**
	 * perform a Left/right/both full join
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private void processFullJoin() throws IllegalAccessException, InstantiationException {
		Set<Row> rightAdd = new HashSet<>();
		for (DataRow leftRow : (Iterable<DataRow>) left) {
			boolean leftAdd = false;

			for (DataRow rightRow : (Iterable<DataRow>) right) {
				CombinedDataRow comb = new CombinedDataRow();
				comb.addDataRow(leftRow);
				comb.addDataRow(rightRow);
				if (constrain.resolve(comb).getValue()) {
					createRow(leftRow, rightRow);
					leftAdd = true;
					rightAdd.add(rightRow);
				}
			}
			if (!leftAdd && fullLeft) {
				createRow(leftRow, new DataRow());
			}
			if (fullRight) {
				addNotAddedRighRows(rightAdd);
			}
		}

	}

	/**
	 * add the rows from the right table that are not added to the result.
	 * @param rightAdd set of rows that are set in the result table.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private void addNotAddedRighRows(Set<Row> rightAdd) throws InstantiationException, IllegalAccessException {
		for (DataRow rightRow : (Iterable<DataRow>) right) {
			if (!rightAdd.contains(right)) {
				createRow(new DataRow(), rightRow);
			}
		}
	}

	/**
	 * Check if the value van added to the result table.
	 * @param row row where the value should be set
	 * @param column column that belongs to the value
	 * @param newValue new value
	 */
	private void checkValue(DataRow row, DataColumn column, DataValue newValue) {
		if (newValue == null) { //todo use the null values
			return;
		} else if (row.getValue(column) == null ) { //compare on isNull()
			row.setValue(column, newValue);
			return;
		} else if (row.getValue(column).equals(newValue)) {
			return;
		} else {
			//TODO ik gooi nu een exceptie, maar je zou ook kunnen stellen dat de join gewoon doorgaat maar dat deze row niet wordt toegevoegd.
			throw new IllegalStateException("conflicting values for column: " + column.toString());
		}
	}




}
