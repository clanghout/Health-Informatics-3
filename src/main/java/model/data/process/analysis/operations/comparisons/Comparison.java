package model.data.process.analysis.operations.comparisons;

import model.data.Table;
import model.data.process.analysis.operations.Event;
import model.data.process.analysis.operations.constraints.Constraint;

/**
 * This class will initialize events and call its comparison.
 * 
 * @author Louis Gosschalk 21-05-2015
 */
public abstract class Comparison {

	protected Event event;

	public Comparison(Table table, Constraint constraint) {
		event = new Event(table, constraint);
	}

	public abstract Table compare();
}
