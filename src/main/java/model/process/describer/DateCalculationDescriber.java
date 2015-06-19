package model.process.describer;

import model.data.Row;
import model.data.value.TemporalValue;
import model.process.analysis.operations.dates.computations.DateCalculation;

/**
 * Describes the operation of a DateComputation.
 *
 * Created by Boudewijn on 9-6-2015.
 */
public class DateCalculationDescriber extends DataDescriber<TemporalValue<?>> {

	private DateCalculation computation;

	/**
	 * Construct a new DateCalculationDescriber.
	 * @param computation The computation to describe.
	 */
	public DateCalculationDescriber(DateCalculation computation) {
		this.computation = computation;
	}

	/**
	 * Resolves the result of the DateComputation.
	 * @param row The row the value should be resolved from
	 * @return The result of the DateComputation.
	 */
	@Override
	public TemporalValue<?> resolve(Row row) {
		return computation.operate(row);
	}
}
