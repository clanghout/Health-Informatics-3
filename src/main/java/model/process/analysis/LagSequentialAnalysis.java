package model.process.analysis;

import model.data.DataColumn;
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
	private Event eventA;
	private Event eventB;
	private Identifier<DataColumn> dateA;
	private Identifier<DataColumn> dateB;

	/**
	 * Construct a new Lag Sequential analysis.
	 * 
	 * @param eventA
	 * @param dateA
	 * @param eventB
	 * @param dateB
	 */
	public LagSequentialAnalysis(Event eventA, Identifier<DataColumn> dateA,
			Event eventB, Identifier<DataColumn> dateB) {
		this.eventA = eventA;
		this.eventB = eventB;
		this.dateA = dateA;
		this.dateB = dateB;
	}

	@Override
	public Table analyse(Table input) {
		LagSequential lsa = new LagSequential(eventA, dateA, eventB,
				dateB);
		input = lsa.getResult();
		return input;
	}
}
