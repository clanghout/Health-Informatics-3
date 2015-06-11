package model.process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.data.DataRow;
import model.data.DataTable;
import model.data.DataTableConversionBuilder;
import model.data.Table;
import model.data.describer.DataDescriber;
import model.data.value.DataValue;

/**
 * This class sorts a DataTable on whatever column you want it to sort on.
 * 
 * @author Louis Gosschalk 09-06-2015
 */
public class SortProcess extends DataProcess {
	private DataDescriber<DataValue> column;
	private Order order;

	public SortProcess(DataDescriber<DataValue> column, Order order) {
		this.column = column;
		this.order = order;
	}

	/**
	 * Input table will be sorted according to input column.
	 */
	@Override
	protected Table doProcess() {
		ArrayList<DataRow> sortlist = new ArrayList<>(((DataTable) getInput()).getRows());
		Collections.sort(sortlist, (DataRow row1, DataRow row2) -> {
			int orderValue = column.resolve(row1).compareTo(column.resolve(row2));
			return order == Order.DESCENDING ? -1 * orderValue : orderValue;
		});

		return clearAndCreate(sortlist);
	}

	/**
	 * Contains the different order you can use to sort.
	 */
	public enum Order {
		ASCENDING,
		DESCENDING
	}
	
	/**
	 * Function used to rebuild the table after sorting.
	 * 
	 * @author Louis Gosschalk
	 * @param sortlist
	 *            sorted rows
	 * @return table remade
	 */
	private Table clearAndCreate(List<DataRow> sortlist) {
		DataTable input = (DataTable) getInput();
		DataTableConversionBuilder builder = new DataTableConversionBuilder(
				input, input.getName());
		builder.addRowsFromList(sortlist);
		return builder.build();
	}
}
