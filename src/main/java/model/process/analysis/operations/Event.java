package model.process.analysis.operations;

import model.data.DataTable;
import model.data.Table;
import model.data.describer.DataDescriber;
import model.data.value.BoolValue;
import model.process.analysis.ConstraintAnalysis;

/**
 * Here I intend to create a specification for events.
 * @author Louis Gosschalk 21-05-2015
 */
public class Event {
	
	private Table table;
	private DataDescriber<BoolValue> constraint;
	
	public Event(Table table, DataDescriber<BoolValue> constraint) {
		this.table = table;
		this.constraint = constraint;
	}
	
	public DataTable create() {
		ConstraintAnalysis constr = new ConstraintAnalysis(constraint);
		DataTable event = (DataTable) constr.analyse(table);
		return event;
	}
}
