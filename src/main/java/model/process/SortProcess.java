package model.process;

import java.util.ArrayList;
import java.util.Collections;

import model.data.DataRow;
import model.data.DataTable;
import model.data.DataTableConversionBuilder;
import model.data.Table;
import model.data.describer.RowValueDescriber;
import model.data.value.DataValue;

/**
 * This class sorts a DataTable on whatever column you want it to sort on.
 * 
 * @author Louis Gosschalk 09-06-2015
 */
public class SortProcess extends DataProcess {
	private DataTable table;
	private RowValueDescriber<DataValue> column;

	public SortProcess(DataTable table, RowValueDescriber<DataValue> column) {
		this.table = table;
		this.column = column;
	}

	/**
	 * Input table will be sorted according to input column.
	 */
	@Override
	protected Table doProcess() {
		ArrayList sorted = new ArrayList<>(table.getRows());
		Collections.sort(
				sorted,
				(DataRow row1, DataRow row2) -> column.resolve(row1).compareTo(
						column.resolve(row2)));
		//remove rows from table
		table.clearRows();
		//add sorted to table
		DataTableConversionBuilder builder = new DataTableConversionBuilder(table, table.getName());
		builder.addRowsFromArray(sorted);
		table = builder.build();
		return table;
	}

	/**
	 * This method first sorts the table based on the specified column and then
	 * reverses its order.
	 * 
	 * @return table with reversed order
	 */
	private Table reverseDoProcess() {
		doProcess();
		Collections.reverse(table.getRows());
		return table;
	}
}
