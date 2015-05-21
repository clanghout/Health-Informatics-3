package model.data.describer;

import model.data.Row;
import model.data.process.analysis.operations.constraints.Constraint;
import model.data.value.BoolValue;

/**
 * A DataDescriber for the output of a constraint.
 * Created by Boudewijn on 11-5-2015.
 */
public final class ConstraintDescriber extends DataDescriber<BoolValue> {

	private Constraint constraint;

	/**
	 * Construct a new ConstraintDescriber.
	 * @param constraint The constraint this describer is describing.
	 */
	public ConstraintDescriber(Constraint constraint) {
		this.constraint = constraint;
	}

	/**
	 * Resolves the value of this constraint.
	 * @param row The row the value should be resolved from
	 * @return A BoolValue containing the result of the constraint on the row
	 */
	@Override
	public BoolValue resolve(Row row) {
		return new BoolValue(constraint.check(row));
	}
}
