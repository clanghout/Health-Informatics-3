package model.process.analysis.operations.dates.computations;

import model.data.Row;
import model.data.describer.DataDescriber;
import model.data.value.PeriodValue;
import model.data.value.TemporalValue;

import java.time.Period;
import java.time.temporal.Temporal;

/**
 * Add a certain amount of time to a time.
 *
 * Created by Boudewijn on 8-6-2015.
 */
public class Add extends DateComputation {

	/**
	 * Construct a new Add operation.
	 * @param left The left side operand of this operation.
	 * @param right The right side operand of this operation.
	 */
	public Add(DataDescriber<? extends TemporalValue<?>> left, DataDescriber<PeriodValue> right) {
		super(left, right);
	}

	@Override
	protected Temporal compute(Row row) {
		Temporal time = getLeft().resolve(row).getValue();
		Period timeAmount = getRight().resolve(row).getValue();
		return timeAmount.addTo(time);
	}
}
