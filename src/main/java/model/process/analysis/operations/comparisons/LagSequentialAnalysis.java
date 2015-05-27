package model.process.analysis.operations.comparisons;

import model.data.*;
import model.data.describer.DataDescriber;
import model.data.value.DateValue;
import model.process.analysis.operations.Event;

/**
 * This class will determine a relation between events.
 * Relation will be shown by chronologically sorting events.
 * 
 * @author Louis Gosschalk 26-05-2015
 */
public class LagSequentialAnalysis {

	private Table table;
	private DataDescriber<DateValue> col;

	public LagSequentialAnalysis(Event eventA, Event eventB) {
	}

	/**
	 * This function calculates the difference between the date values.
	 * 
	 * @return Table of events and their lag.
	 */
	public Table compare() {
		return null;
	}
}
