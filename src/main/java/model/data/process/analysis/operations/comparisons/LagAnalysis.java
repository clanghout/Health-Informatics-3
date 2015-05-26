package model.data.process.analysis.operations.comparisons;

import model.data.Table;
import model.data.process.analysis.operations.constraints.Constraint;

/**
 * This class will calculate the lag between specified events.
 * 
 * @author Louis Gosschalk 26-05-2015
 */
public class LagAnalysis extends Comparison {

	private Table table;

	public LagAnalysis(Table table, Constraint constraint) {
		super(table, constraint);
		this.table = table;
	}

	/**
	 * This function calculates the difference between the datevalues.
	 * 
	 * @return
	 */
	@Override
	public Table compare() {
		// TODO Auto-generated method stub
		return null;
	}

}
