package model.data.process.analysis.operations.comparisons;

import model.data.DataColumn;
import model.data.Table;
import model.data.process.analysis.operations.constraints.Constraint;

/**
 * This class will calculate the lag between specified events.
 * 
 * @author Louis Gosschalk 26-05-2015
 */
public class LagAnalysis extends Comparison {

	private Table table;
	private DataColumn col;

	public LagAnalysis(Table table, Constraint constraint) {
		super(table, constraint);
		this.table = table;
	}

	/**
	 * This function calculates the difference between the date values.
	 * 
	 * @return Table of events and their lag.
	 */
	public Table compare() {
		return null;
	}

	/**
	 * This function checks if the given table contains a date.
	 * 
	 * @return DataColumn if the given table contains a dataValue column.
	 */
	public DataColumn findDate() {
		return null;
	}
}
