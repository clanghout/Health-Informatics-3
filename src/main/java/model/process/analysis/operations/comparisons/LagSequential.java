package model.process.analysis.operations.comparisons;

import model.data.DataColumn;
import model.data.DataModel;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.Table;
import model.data.describer.DataDescriber;
import model.data.describer.RowValueDescriber;
import model.data.describer.TableValueDescriber;
import model.data.value.DataValue;
import model.exceptions.InputMismatchException;
import model.language.ColumnIdentifier;
import model.language.Identifier;
import model.process.DataProcess;
import model.process.SortProcess;
import model.process.SortProcess.Order;
import model.process.analysis.operations.Connection;
import model.process.analysis.operations.Event;
import model.process.setOperations.FullJoin;
import model.process.setOperations.Join;

/**
 * This class will determine a relation between events. Relation will be shown
 * by chronologically sorting events.
 * 
 * @author Louis Gosschalk 26-05-2015
 */
public class LagSequential {

	private DataTable tableA;
	private DataTable tableB;
	private DataTable result;

	private Identifier<DataColumn> colA;
	private Identifier<DataColumn> colB;

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
	public LagSequential(Event eventA,
			Identifier<DataColumn> dateA, Event eventB,
			Identifier<DataColumn> dateB) {
		tableA = checkTable(eventA.create());
		tableB = checkTable(eventB.create());
		this.colA = dateA;
		this.colB = dateB;

		if (tableA.getRowCount() == 0 || tableB.getRowCount() == 0) {
			throw new InputMismatchException("Empty event input.");
		}
		
		Identifier<DataTable> a = new Identifier<DataTable>(tableA.getName());
		Identifier<DataColumn> c = new Identifier<DataColumn>(colA.toString());
		ColumnIdentifier columnid = new ColumnIdentifier(a,c);
		
		Identifier<DataTable> a2 = new Identifier<DataTable>(tableB.getName());
		Identifier<DataColumn> c2 = new Identifier<DataColumn>(colB.toString());
		ColumnIdentifier columnid2 = new ColumnIdentifier(a,c);
		
		DataModel model = new DataModel();
		model.add(tableA);
		model.add(tableB);
		
		
		Order order = Order.ASCENDING;
		DataDescriber datadesc = new TableValueDescriber<DataValue>(model, columnid);
		DataDescriber datadesc2 = new TableValueDescriber<DataValue>(model, columnid2);
		
		
		
		SortProcess sortA = new SortProcess(datadesc, order);
		sortA.setInput(tableA);
		tableA = (DataTable) sortA.process();
		
		SortProcess sortB = new SortProcess(datadesc2, order);
		sortB.setInput(tableB);
		tableB = (DataTable) sortB.process();
		
		model.add(tableA);
		model.add(tableB);
		
		System.out.println("tabel a "+tableA.getRowCount() + " tabel b "+tableB.getRowCount());

		// join the tables and sort chrono
		
		Connection con = new Connection("con", a, columnid, a2, columnid2);
		con.setDataModel(model);
		result = (DataTable) con.process();
		
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
	 * This class returns the created table.
	 * 
	 * @return result the built Table
	 */
	public DataTable getResult() {
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
