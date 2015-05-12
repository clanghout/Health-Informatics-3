package model.data.describer;

import model.data.DataRow;
import model.data.process.analysis.computations.Computation;
import model.data.value.NumberValue;


/**
 * Created by Chris on 12-5-2015.
 */
public final class ComputationDescriber extends DataDescriber {
	private Computation computation;

	/**
	 * Construct a new ConstraintDescriber.
	 * @param computation The constraint this describer is describing.
	 */
	public ComputationDescriber(Computation computation) {
		this.computation = computation;
	}

	/**
	 * Resolves the value of this constraint.
	 * @param row The row the value should be resolved from
	 * @return A BoolValue containing the result of the constraint on the row
	 */
	@Override
	public NumberValue resolve(DataRow row) {
		return computation.compute(row);
	}
}
