package model.data;

import java.util.List;

import model.data.value.DataValue;

/**
 * This datatable builder uses a table to build its structure.
 * It is possible to add a row from another table to this table.
 * That row must contain all the columns that the builder has.
 * If the rows has those columns, than its values will be added to a new row,
 * that new row will be added to this builder.
 *
 * Created by jens on 5/29/15.
 */
public class DataTableConversionBuilder extends DataTableBuilder {

	/**
	 * Create a datatableconversion builder. This uses a table to create the columns.
	 * @param table table from where the columns should be copied.
	 * @param name the name of the table.
	 */
	public DataTableConversionBuilder(DataTable table, String name) {
		this.setName(name);
		for (DataColumn column : table.getColumns()) {
			this.addColumn(column.copy());
		}
	}
	
	/**
	 * Adds a column to an existing table and sets a default DataValue as its value.
	 * 
	 * @param table the table to add the column to
	 * @param type the DataValue to fill in the new column
	 * @param columnName the name of the new column
	 */
	public void addColumn(DataTable table, DataValue type, String columnName) {
		addColumn(new DataColumn(columnName, table, type.getClass()));
		for (DataRow row : table.getRows()) {
			int i = 1;
			for (DataColumn col : table.getColumns()) {
				if (i == table.getColumns().size()) {
					row.setValue(col, type);
				} else {
				row.setValue(col, row.getValue(col));
				}
				i++;
			}
		}
	}

	/**
	 * Add all the rows from the table to the builder.
	 * @param table that contains the rows that must be added to the builder.
	 */
	public void addRowsFromTable(DataTable table) {
		for (DataRow row : table.getRows()) {
			createRow(row);
		}
	}
	
	/**
	 * Add all rows from a List to the builder.
	 * @param table list of datarows.
	 */
	public void addRowsFromList(List<DataRow> rowList) {
		for (DataRow row : rowList) {
			createRow(row);
		}
	}


	/**
	 * add a row to the table.
	 * the columns of the rows must have the same type and name as the columns in the table.
	 * @param row the row that must be added to the table.
	 */
	public DataRow createRow(DataRow row) {
		DataValue[] values = new DataValue[this.getColumns().size()];

		int i = 0;
		for (DataColumn column : this.getColumns()) {
			for (DataColumn colRow : row.getColumns()) {
				if (colRow.equalsExcludeTable(column)) {
					values[i] = row.getValue(colRow);
					break;
				}
			}
			i++;
		}
		DataRow res = createRow(values);
		res.addCodes(row.getCodes());

		return res;
	}
}
