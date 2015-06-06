package model.process.analysis.operations.comparisons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.data.DataRow;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.Row;
import model.data.Table;
import model.data.describer.DataDescriber;
import model.data.value.DateTimeValue;
import model.exceptions.EmptyEventException;
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

	private DataDescriber<DateTimeValue> dateA;
	private DataDescriber<DateTimeValue> dateB;

	private int positionA;
	private int positionB;

	private DateTimeValue compareA;
	private DateTimeValue compareB;

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
			DataDescriber<DateTimeValue> dateA, Event eventB,
			DataDescriber<DateTimeValue> dateB) {
		Table tabA = eventA.create();
		Table tabB = eventB.create();
		this.dateA = dateA;
		this.dateB = dateB;
		this.tableC = new DataTableBuilder();

		positionA = 0;
		positionB = 0;

		tableA = checkTable(tabA);
		tableB = checkTable(tabB);

		order = new ArrayList<String>();

		if (tableA.getRow(0) == null || tableB.getRow(0) == null) {
			throw new EmptyEventException("Empty event in the input.");
		} else {
			compareA = dateA.resolve(tableA.getRow(positionA));
			compareB = dateB.resolve(tableB.getRow(positionB));
		}
		chronoAdd();
		tableC.setName("result");
		result = tableC.build();
	}

	/**
	 * This class will put the events (rows) in one table, sorted
	 * chronologically.
	 */
	public void chronoAdd() {
		while (positionA < tableA.getRowCount()
				&& positionB < tableB.getRowCount()) {
			System.out.println("compareA "
					+ compareA.getValue().getTime().toString() + " compareB "
					+ compareB.getValue().getTime().toString());
			if (compareA.getValue().compareTo(compareB.getValue()) < 0) {
				order.add("A");
				System.out.println("chrono A " + order.toString());
				tableC.addRow(tableA.getRow(positionA));
				next(tableA);
			} else {
				order.add("B");
				System.out.println("chrono B " + order.toString());
				tableC.addRow(tableB.getRow(positionB));
				next(tableB);
			}
		}
		System.out.println("DONE " + order.toString());

	}

	/**
	 * 
	 * ZORG DAT DE TWEE TABELLEN EERST BEIDEN CHRONOLOGISCH GESORTEERD ZIJN!!
	 * 
	 * 
	 */

	public void next(DataTable table) {
		if (table.getName().equals(tableA.getName())) {
			next(tableA, positionA, compareA, dateA, tableB, positionB);
		} else {
			next(tableB, positionB, compareB, dateB, tableA, positionB);
		}
	}

	public void next(DataTable table, int position, DateTimeValue compare,
			DataDescriber<DateTimeValue> date, DataTable table2, int position2) {
		boolean tableone = table.getName().equals(tableA.getName());
		if (position < table.getRowCount() - 1) {
			System.out.println("here");
			position++;
			compare = date.resolve(table.getRow(position));
			if (tableone) {
				positionA = position;
				compareA = compare;
			} else {
				positionB = position;
				compareB = compare;
			}
		} else {
			while (position2 < table2.getRowCount()) {
				tableC.addRow(table2.getRow(position2));
				position2++;
				if (tableone) {
					positionB = position2;
					order.add("B");
					System.out.println("add B");
				} else {
					positionA = position2;
					order.add("A");
					System.out.println("add A");
				}
			}
		}
	}

	/**
	 * Make sure input is a datatable.
	 * 
	 * @param table
	 * @return the datatable
	 */
	public DataTable checkTable(Table table) {
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
	public Table getTable() {
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
