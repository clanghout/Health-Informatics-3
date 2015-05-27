package model.process.analysis;

import model.data.Row;
import model.data.Table;
import model.data.describer.DataDescriber;
import model.data.value.BoolValue;

import java.util.Iterator;

/**
 * Implements the constraints analysis.
 *
 * Created by Boudewijn on 5-5-2015.
 */
public class ConstraintAnalysis extends DataAnalysis {

	private final DataDescriber<BoolValue> constraint;

	/**
	 * Construct a new ConstraintsAnalysis.
	 * @param constraint The constraint you want to use for this analysis.
	 */
	public ConstraintAnalysis(DataDescriber<BoolValue> constraint) {
		this.constraint = constraint;
	}

	@Override
	public Table analyse(Table input) {
		Iterator<? extends Row> rows = input.iterator();
		while (rows.hasNext()) {
			Row row = rows.next();
			if (constraint.resolve(row).getValue()) {
				input.flagRow(row);
			}
		}
		input.deleteNotFlagged();
		return input;
	}
}
