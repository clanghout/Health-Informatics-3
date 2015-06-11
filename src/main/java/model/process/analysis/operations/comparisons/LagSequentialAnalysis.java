package model.process.analysis.operations.comparisons;

import java.util.*;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.Table;
import model.data.describer.RowValueDescriber;
import model.data.value.DataValue;
import model.exceptions.InputMismatchException;
import model.process.SortProcess;
import model.process.SortProcess.Order;
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

	private RowValueDescriber<DataValue> colA;
	private RowValueDescriber<DataValue> colB;

	private DataTableBuilder tableC;

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
			RowValueDescriber<DataValue> dateA, Event eventB,
			RowValueDescriber<DataValue> dateB) {
		tableA = checkTable(eventA.create());
		tableB = checkTable(eventB.create());
		this.colA = dateA;
		this.colB = dateB;
		this.tableC = new DataTableBuilder();

		if (tableA.getRowCount() == 0 || tableB.getRowCount() == 0) {
			throw new InputMismatchException("Empty event input.");
		} else if (!tableA.getColumns().contains(colA)
				|| !tableB.getColumns().contains(colB)) {
			throw new IllegalArgumentException(
					"Specified date column is not an instance of specified table.");
		}

		Order order = Order.ASCENDING;

		SortProcess sortA = new SortProcess(colA, order);
		SortProcess sortB = new SortProcess(colB, order);

		tableA = (DataTable) sortA.getOutput();
		tableB = (DataTable) sortB.getOutput();

		// join the tables and sort chrono

		tableC.setName("LSA");
		result = tableC.build();
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
	 * This class returns the first input table (when sorted). Required for
	 * PatternDetection.
	 */
	public DataTable getTableInputOne() {
		return tableA;
	}

	/**
	 * This class returns the second input table (when sorted). Required for
	 * PatternDetection.
	 */
	public DataTable getTableInputTwo() {
		return tableB;
	}
}
