package model.data;

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

	public DataTableConversionBuilder(DataTable table, String name) {
		this.setName(name);
		for (DataColumn column : table.getColumns()) {
			this.addColumn(column.copy());
		}
	}

	public void addRowsFromTable(DataTable table) {
		for (DataRow row : table.getRows()) {
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
