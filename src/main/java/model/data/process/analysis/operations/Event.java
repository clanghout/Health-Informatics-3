package model.data.process.analysis.operations;

import model.data.Table;
import model.data.process.analysis.ConstraintAnalysis;
import model.data.process.analysis.operations.constraints.Constraint;

/**
 * Here I intend to create a specification for events.
 * 
 * @author Louis Gosschalk 21-05-2015
 */
public class Event {

	private Table table;
	private Constraint constraint;

	public Event(Table table, Constraint constraint) {
		this.table = table;
		this.constraint = constraint;
	}

	public Table create() {
		ConstraintAnalysis constr = new ConstraintAnalysis(constraint);
		Table event = constr.analyse(table);
		return event;
	}
}
