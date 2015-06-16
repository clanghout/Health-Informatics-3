package model.process.analysis;

import model.data.DataColumn;
import model.data.DataTable;
import model.data.Table;
import model.language.Identifier;
import model.process.analysis.operations.Event;
import model.process.analysis.operations.comparisons.LagSequential;

/**
 * Implements the Lag Sequential analysis.
 *
 * @author Louis Gosschalk 11-06-2015
 */
public class LagSequentialAnalysis extends DataAnalysis {
	private Identifier<DataTable> tableA;
	private Identifier<DataTable> tableB;
	private Identifier<DataColumn> dateA;
	private Identifier<DataColumn> dateB;

	/**
	 * Construct a new Lag Sequential analysis to show chronological occurrence
	 * of two events.
	 */
	public LagSequentialAnalysis(Identifier<DataTable> tableA,
			Identifier<DataColumn> dateA, Identifier<DataTable> tableB,
			Identifier<DataColumn> dateB) {
		this.tableA = tableA;
		this.tableB = tableB;
		this.dateA = dateA;
		this.dateB = dateB;
	}

	@Override
	public Table analyse(Table input) {
		DataTable left;
		DataTable right;
		if (getDataModel().getByName(tableA.getName()).isPresent()
				&& getDataModel().getByName(tableB.getName()).isPresent()) {
			left = getDataModel().getByName(tableA.getName()).get();
			right = getDataModel().getByName(tableB.getName()).get();
		} else {
			throw new NullPointerException("One of the tables has not been set.");
		}
		LagSequential lsa = new LagSequential(left, dateA, right, dateB);
		input = lsa.getResult();
		return input;
	}
}
