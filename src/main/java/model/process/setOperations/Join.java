package model.process.setOperations;

import model.data.*;
import model.data.describer.ConstantDescriber;
import model.data.describer.DataDescriber;
import model.data.value.BoolValue;
import model.data.value.DataValue;
import model.language.ColumnIdentifier;
import model.process.DataProcess;

import java.util.*;

/**
 * Abstract class for the join operations.
 *
 * Created by jens on 6/10/15.
 */
public abstract class Join extends DataProcess {

	private DataTableBuilder builder;
	private DataDescriber<BoolValue> constraint;
	/**
	 * Map the old the column to the new column.
	 * Key is the old column
	 * Value is net new column
	 */
	private Map<DataColumn, DataColumn> mappingColumns;
	/**
	 * Map the combined columns.
	 * Key is the origin
	 * Value is the column is should be mapped to.
	 */
	private Map<DataColumn, DataColumn> combineColumns;

	private Map<ColumnIdentifier, ColumnIdentifier> combineColumnsIdentifier;
	/**
	 * Initialize the mapping arrays ands set the constraint on true.
	 */
	public Join(String name) {
		this.builder = new DataTableBuilder();
		builder.setName(name);
		this.mappingColumns = new HashMap<>();
		this.combineColumnsIdentifier = new LinkedHashMap<>();
		this.combineColumns = new LinkedHashMap<>();
		this.constraint = new ConstantDescriber<>(new BoolValue(true));
	}

	/**
	 * Set the constraint.
	 * @param constraint the constraint.
	 */
	public void setConstraint(DataDescriber<BoolValue> constraint) {
		this.constraint = constraint;
	}

	/**
	 * Add two columns that must get combined.
	 * @param col1 column that should combined with the second one.
	 * @param col2 column that must get in the result.
	 */
	public void addCombineColumn(ColumnIdentifier col1, ColumnIdentifier col2) {
		combineColumnsIdentifier.put(col1, col2);
	}

	/**
	 * Resolve the identifiers for the combined columns.
	 */
	private void reslveCombineColumnsIdentifiers() {
		for (Map.Entry<ColumnIdentifier, ColumnIdentifier> entry
				: combineColumnsIdentifier.entrySet()) {
			DataColumn col1 = getDataModel().getByName(entry.getKey().getTable()).get()
					.getColumn(entry.getKey().getColumn());
			DataColumn col2 = getDataModel().getByName(entry.getValue().getTable()).get()
					.getColumn(entry.getValue().getColumn());
			if (!col1.getType().equals(col2.getType())) {
				throw new IllegalArgumentException("type of columns differ");
			}
			combineColumns.put(col1, col2);
		}
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
		while (changed) {
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
	 * Perform the join.
	 * @return a new DataTable
	 */
	@Override
	protected DataTable doProcess() {
		reslveCombineColumnsIdentifiers();
		simplifyCombinedColumns();
		addColumns();
		joinTable();
		return builder.build();
	}

	/**
	 * Get the new column.
	 * @param oldColumn oldcolumn
	 * @return the new instance of the oldColum
	 */
	public DataColumn getNewColumn(DataColumn oldColumn) {
		return mappingColumns.get(oldColumn);
	}
	/**
	 * Make sure that each column gets a unique name.
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
			checkNameCollision(mappingNewNameToOldColumns, mappingColumnsToName,
					forbidden, tempColumn, newName);
		}

		if (forbidden.contains(nameColumn)) {
			String newName = column.getTable().getName() + "_" + nameColumn;
			checkNameCollision(mappingNewNameToOldColumns, mappingColumnsToName,
					forbidden, column, newName);
		} else {
			mappingNewNameToOldColumns.put(nameColumn, column);
			mappingColumnsToName.put(column, nameColumn);
		}
	}

	/**
	 * Assign to each column a unique name.
	 * @return a map that maps unique names to columns
	 */
	private Map<DataColumn, String> addColumnToNameMapping() {
		Map<String, DataColumn> mappingNewNameToOldColumns = new LinkedHashMap<>();
		Map<DataColumn, String> mappingOldColumnsToNewName = new LinkedHashMap<>();
		Set<String> forbidden = new HashSet<>();

		for (DataColumn column : this.getTable().getColumns()) {
			if (!combineColumns.containsKey(column)) {
				checkNameCollision(mappingNewNameToOldColumns, mappingOldColumnsToNewName,
						forbidden, column, column.getName());
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

			builder.addColumn(newColumn);
			mappingColumns.put(entry.getKey(), newColumn);
		}
	}

	/**
	 * Join the tables.
	 */
	protected abstract void joinTable();

	/**
	 * Return a combined table that contains all the tables that should be joined.
	 * @return a combined table that contains all the tales that should be joined.
	 */
	protected abstract Table getTable();


	/**
	 * Return the constraint for the join.
	 * @return the constrain that should be used with the join.
	 */
	protected DataDescriber<BoolValue> getConstraint() {
		return constraint;
	}

	/**
	 * Return the builder used to build the table.
	 * @return the builder used to build the table.
	 */
	protected DataTableBuilder getBuilder() {
		return builder;
	}

	/**
	 * Return the mapping of the old columns to the new columns.
	 * @return a mapping of the old columns to the new columns.
	 */
	protected Map<DataColumn, DataColumn>  getMappingColumns() {
		return mappingColumns;
	}

	/**
	 * Check if the value is added to the result table.
	 * If it is added and it is not the same value as the new value, throw an exception.
	 * @param row row where the value should be set
	 * @param column column that belongs to the value
	 * @param newValue new value
	 */
	protected void checkValue(DataRow row, DataColumn column, DataValue newValue) {
		if (row.getValue(column) == null || row.getValue(column).isNull()) {
			row.setValue(column, newValue);
			return;
		} else if (newValue == null || newValue.isNull()) {
				return;
		} else if (row.getValue(column).equals(newValue)) {
			return;
		} else {
			/*TODO ik gooi nu een exceptie,
			  TODO maar je zou ook kunnen stellen dat de join gewoon doorgaat
			  TODO maar dat deze row niet wordt toegevoegd. */
			throw new IllegalStateException("conflicting values for column: " + column.toString());
		}
	}
}
