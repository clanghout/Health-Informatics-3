package model.process.analysis.operations.comparisons;

import java.util.Iterator;
import java.util.List;

import model.data.DataRow;
import model.data.DataTableBuilder;
import model.data.Row;
import model.data.Table;
import model.data.describer.DataDescriber;
import model.data.value.DateTimeValue;
import model.exceptions.EmptyEventException;
import model.process.analysis.operations.Event;

/**
 * This class will determine a relation between events. Relation will be shown
 * by chronologically sorting events.
 * 
 * @author Louis Gosschalk 26-05-2015
 */
public class LagSequentialAnalysis {

	private Table tableA;
	private Table tableB;
	private Table result;

	Iterator<? extends Row> a = tableA.iterator();
	Iterator<? extends Row> b = tableB.iterator();

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
	public LagSequentialAnalysis(
			Event eventA, DataDescriber<DateTimeValue> dateA, 
			Event eventB, DataDescriber<DateTimeValue> dateB) {
		this.tableA = eventA.create();
		this.tableB = eventB.create();
		this.tableC = new DataTableBuilder();

		if (a.hasNext() || b.hasNext()) {
			compareA = dateA.resolve(a.next());
			compareB = dateB.resolve(b.next());
			chronoAdd();
		} else {
			throw new EmptyEventException("Empty event in the input.");
		}
		do {
			chronoAdd();
		} while (a.hasNext() || b.hasNext());
		result = tableC.build();
	}

	/**
	 * This class will put the events (rows) in one table, sorted
	 * chronologically.
	 */
	public void chronoAdd() {
		if (compareA.getValue().before(compareB.getValue())) {
			order.add("A");
			tableC.addRow((DataRow) a);
			getNext(a, b);
		} else {
			order.add("B");
			tableC.addRow((DataRow) b);
			getNext(b, a);
		}
	}

	/**
	 * This class determines if the iterator has a next. If it doesn't, all
	 * elements of the other event have to be added to result.
	 * 
	 * @param x
	 *            The event to have a next element
	 * @param y
	 *            The event to add if there isn't a next
	 */
	public void getNext(Iterator<? extends Row> x, Iterator<? extends Row> y) {
		if (x.hasNext()) {
			x.next();
		} else {
			do {
				tableC.addRow((DataRow) y);
			} while (y.hasNext());
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
