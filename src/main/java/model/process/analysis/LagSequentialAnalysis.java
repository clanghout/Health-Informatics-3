package model.process.analysis;

import model.data.Row;
import model.data.Table;
import model.data.describer.DataDescriber;
import model.data.value.BoolValue;

import java.util.Iterator;

/**
 * Implements the constraints analysis.
 *
 * @author Louis Gosschalk 11-06-2015
 */
public class LagSequentialAnalysis extends DataAnalysis {


	/**
	 * Construct a new LagSequentialAnalysis.
	 * @param constraint The constraint you want to use for this analysis.
	 */
	public LagSequentialAnalysis() {
		
	}

	@Override
	public Table analyse(Table input) {
//		Iterator<? extends Row> rows = input.iterator();
//		while (rows.hasNext()) {
//			Row row = rows.next();
//			if (constraint.resolve(row).getValue()) {
//				input.flagNotDelete(row);
//			}
//		}
//		input.deleteNotFlagged();
//		return input;
		return input;
	}
}
