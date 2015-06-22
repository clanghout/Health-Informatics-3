package model.process.analysis.operations.dates.computations;

import model.data.Row;
import model.process.describer.DataDescriber;
import model.data.value.IntValue;
import model.data.value.TemporalValue;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

/**
 * Gets the difference of time in the given unit between the given dates.
 *
 * Created by Boudewijn on 9-6-2015.
 */
public class Relative extends DateComputation<TemporalValue<?>, IntValue> {

	private ChronoUnit unit;

	/**
	 * Construct a new Relative.
	 * @param left The left side operand of this computation.
	 * @param right The right side operand of this computation.
	 * @param unit The unit the result should be in.
	 */
	public Relative(
			DataDescriber<? extends TemporalValue<?>> left,
			DataDescriber<TemporalValue<?>> right,
			ChronoUnit unit) {
		super(left, right);
		this.unit = unit;
	}

	@Override
	protected IntValue compute(Row row) {
		Temporal leftValue = getLeft().resolve(row).getValue();
		Temporal rightValue = getRight().resolve(row).getValue();
		return new IntValue((int) unit.between(leftValue, rightValue));
	}
}
