package model.process.analysis.operations.dates.constraint;

import model.process.describer.DataDescriber;
import model.data.value.TemporalValue;

/**
 * Represents a Before check.
 *
 * Created by Boudewijn on 8-6-2015.
 */
public class Before extends DateComparison {

	/**
	 * Construct a new Before comparison.
	 * @param left The left side operand of this operation.
	 * @param right The right side operand of this operation.
	 */
	public Before(
			DataDescriber<? extends TemporalValue<?>> left,
			DataDescriber<? extends TemporalValue<?>> right) {
		super(left, right);
	}

	/**
	 * Checks if the amount of time between the given dates is greater than 0.
	 * @param amount The amount of time between the left and right side operands in seconds.
	 * @return True if the amount is positive, false otherwise.
	 */
	@Override
	protected boolean compare(long amount) {
		return amount > 0;
	}
}