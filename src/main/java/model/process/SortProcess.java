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
		ArrayList sortlist = new ArrayList<>(table.getRows());
		Collections.sort(
				sortlist,
				(DataRow row1, DataRow row2) -> column.resolve(row1).compareTo(
						column.resolve(row2)));
		return clearAndCreate(table,sortlist);
	}
}
