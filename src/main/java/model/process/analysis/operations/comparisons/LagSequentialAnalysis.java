package model.process.analysis.operations.comparisons;

import java.util.*;

import model.data.DataRow;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.Table;
import model.data.describer.DataDescriber;
import model.data.describer.RowValueDescriber;
import model.data.value.DateTimeValue;
import model.exceptions.InputMismatchException;
import model.process.analysis.operations.Event;

/**
 * This class will determine a relation between events. Relation will be shown
 * by chronologically sorting events.
 * 
 * @author Louis Gosschalk 26-05-2015
 */
public class LagSequentialAnalysis {

	private DataTable tableA;
	private DataTable tableB;
	private DataTable result;

	private DataDescriber<DateTimeValue> colA;
	private DataDescriber<DateTimeValue> colB;

	private DataTableBuilder tableC;

	private List<String> order;

	/**
	 * This class constructs the LSA.
	 * 
	 * @param eventA
	 *            The first event
	 * @param dateA
	 *            The column of the dateValues (first event)
	 * @param eventB
	 *            The second event
	 * @param dateB
	 *            The column of the dateValues (second event)
	 * @return
	 */
	public LagSequentialAnalysis(Event eventA,
			RowValueDescriber<DateTimeValue> dateA, Event eventB,
			RowValueDescriber<DateTimeValue> dateB) {
		tableA = checkTable(eventA.create());
		tableB = checkTable(eventB.create());
		this.colA = dateA;
		this.colB = dateB;
		this.tableC = new DataTableBuilder();

		if (tableA.getRowCount() == 0 || tableB.getRowCount() == 0) {
			throw new InputMismatchException("Empty event input.");
		}

		/**
		 * TODO replace sorting function for tables with global sorting function of
		 * DataTable
		 */
		tableA = sortTable(tableA);
		tableB = sortTable(tableB);

		order = new ArrayList<String>();

		chronoAdd();
		tableC.setName("LSA");
		result = tableC.build();
	}

	/**
	 * Sort the table chronologically.
	 * 
	 * @param table
	 *            the table to sort
	 * @return a new table, sorted with the very same content
	 */
	private DataTable sortTable(DataTable table) {
		DataTableBuilder res = new DataTableBuilder();
		res.setName(table.getName());

		DataDescriber<DateTimeValue> date = null;
		if (table.getName().equals(tableA.getName())) {
			date = colA;
		} else {
			date = colB;
		}

		List<Calendar> cal = new ArrayList<Calendar>();
		DataRow row = null;
		for (int i = 0; i < table.getRowCount(); i++) {
			row = table.getRow(i);
			cal.add(date.resolve(row).getValue());
		}
		Collections.sort(cal);

		for (Calendar x : cal) {
			for (int i = 0; i < table.getRowCount(); i++) {
				row = table.getRow(i);
				if (date.resolve(row).getValue().equals(x)) {
					res.addRow(row);
				}
			}
		}
		return res.build();
	}

	/**
	 * This class will put the events (rows) in one table, sorted
	 * chronologically.
	 */
	private void chronoAdd() {
		Iterator<DataRow> tableAIt = tableA.iterator();
		Iterator<DataRow> tableBIt = tableB.iterator();

		DataRow rowA = tableAIt.next();
		DataRow rowB = tableBIt.next();
		Calendar dataA = colA.resolve(rowA).getValue();
		Calendar dataB = colB.resolve(rowB).getValue();

		boolean nextA = true;
		boolean nextB = true;

		while (nextA && nextB) {
			if (dataA.compareTo(dataB) < 0) {
				order.add("A");
				tableC.addRow(rowA);
				if (tableAIt.hasNext()) {
					rowA = tableAIt.next();
					dataA = colA.resolve(rowA).getValue();
				} else {
					order.add("B");
					tableC.addRow(rowB);
					break;
				}
			} else {
				order.add("B");
				tableC.addRow(rowB);
				if (tableBIt.hasNext()) {
					rowB = tableBIt.next();
					dataB = colB.resolve(rowB).getValue();
				} else {
					order.add("A");
					tableC.addRow(rowA);
					break;
				}

			}
		}
		while (tableAIt.hasNext()) {

			order.add("A");
			tableC.addRow(rowA);
			rowA = tableAIt.next();

		}
		while (tableBIt.hasNext()) {

			order.add("B");
			tableC.addRow(rowB);
			rowB = tableBIt.next();

		}
	}

	/**
	 * Make sure input is a datatable.
	 * 
	 * @param table
	 * @return the datatable
	 */
	private DataTable checkTable(Table table) {
		if (table instanceof DataTable) {
			return (DataTable) table;
		} else {
			throw new InputMismatchException(
					"Table should be instance of DataTable");
		}
	}

	/**
	 * This class returns the actual created table.
	 * 
	 * @return result the built Table
	 */
	public DataTable getTable() {
		return result;
	}

	/**
	 * This class returns the order of the created table.Relevant for pattern
	 * detection.
	 * 
	 * @return List<String> containing A's and B's specifying ordering.
	 */
	public List<String> getOrder() {
		return order;
	}
}
