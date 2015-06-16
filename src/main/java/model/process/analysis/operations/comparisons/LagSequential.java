package model.process.analysis.operations.comparisons;

import model.data.DataColumn;
import model.data.DataModel;
import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.describer.TableValueDescriber;
import model.data.value.DataValue;
import model.language.ColumnIdentifier;
import model.language.Identifier;
import model.process.SortProcess;
import model.process.SortProcess.Order;
import model.process.analysis.operations.Connection;

/**
 * This class will determine a relation between events. Relation will be shown
 * by chronologically sorting events.
 * 
 * @author Louis Gosschalk 26-05-2015
 */
public class LagSequential {

	private DataTable tableA;
	private DataTable result;

	private DataModel model;

	private Identifier<DataColumn> colA;
	private Identifier<DataColumn> colB;

	private Identifier<DataTable> a;
	private Identifier<DataTable> a2;

	private ColumnIdentifier columnid;
	private ColumnIdentifier columnid2;

	/**
	 * Construct LSA resulting combined table of events sorted chronologically.
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
	public LagSequential(DataTable tableA, Identifier<DataColumn> dateA,
			DataTable tableB, Identifier<DataColumn> dateB, String name) {
		this.tableA = tableA;
		this.colA = dateA;
		this.colB = dateB;
		this.model = new DataModel();

		tableA = sort(tableA, colA);
		tableB = sort(tableB, colB);

		model.add(tableA);
		model.add(tableB);

		Connection con = new Connection(name, a, columnid, a2, columnid2);
		con.setDataModel(model);
		result = (DataTable) con.process();

	}

	/**
	 * Calls sorting on the table.
	 * 
	 * @param table
	 *            The table to sort
	 * @param column
	 *            The column to sort on
	 * @return
	 */
	private DataTable sort(DataTable table, Identifier<DataColumn> column) {

		model.add(table);

		Order order = Order.ASCENDING;

		Identifier<DataColumn> c = new Identifier<DataColumn>(column.getName());

		DataDescriber datadesc = null;

		if (table.getName().equals(tableA.getName())) {
			a = new Identifier<DataTable>(table.getName());
			columnid = new ColumnIdentifier(a, c);
			datadesc = new TableValueDescriber<DataValue>(model, columnid);
		} else {
			a2 = new Identifier<DataTable>(table.getName());
			columnid2 = new ColumnIdentifier(a2, c);
			datadesc = new TableValueDescriber<DataValue>(model, columnid2);
		}

		SortProcess sort = new SortProcess(datadesc, order);
		sort.setDataModel(model);
		sort.setInput(table);
		table = (DataTable) sort.process();

		return table;
	}

	/**
	 * This class returns the created table.
	 * 
	 * @return result the built Table
	 */
	public DataTable getResult() {
		return result;
	}
}
