package model.process;

import java.util.ArrayList;
import java.util.Collections;

import model.data.DataTable;
import model.data.Table;
import model.data.describer.RowValueDescriber;
import model.data.value.DataValue;

/**
 * This class sorts a DataTable on whatever column you want it to sort on, but
 * REVERSED.
 * 
 * @author Louis Gosschalk 10-06-2015
 */
public class ReversedSortProcess extends DataProcess {
	private DataTable table;
	private RowValueDescriber<DataValue> column;

	public ReversedSortProcess(DataTable table,
			RowValueDescriber<DataValue> column) {
		this.table = table;
		this.column = column;
	}

	/**
	 * This method first sorts the table based on the specified column and then
	 * reverses its order.
	 * 
	 * @return table with reversed order
	 */
	public Table doProcess() {
		DataTable sortedTable = new SortProcess(table, column).doProcess()
				.getTable(table.getName());
		ArrayList sortlist = new ArrayList<>(sortedTable.getRows());
		Collections.reverse(sortlist);
		return clearAndCreate(sortedTable, sortlist);
	}
}
