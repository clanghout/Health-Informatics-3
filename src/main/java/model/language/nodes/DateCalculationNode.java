package model.language.nodes;

import model.data.DataModel;
import model.process.describer.DataDescriber;
import model.process.describer.OperationDescriber;
import model.data.value.PeriodValue;
import model.data.value.TemporalValue;
import model.process.analysis.operations.dates.computations.Add;
import model.process.analysis.operations.dates.computations.DateCalculation;
import model.process.analysis.operations.dates.computations.Min;

/**
 * Represents an calculation on a Date or DateTime.
 *
 * Created by Boudewijn on 9-6-2015.
 */
public class DateCalculationNode extends OperationNode<TemporalValue<?>> {

	private ValueNode<PeriodValue> period;

	/**
	 * Construct a new DateCalculationNode.
	 * @param left The date of which to operate.
	 * @param operation The operation to perform.
	 * @param period The period to work with.
	 */
	public DateCalculationNode(
			ValueNode<TemporalValue<?>> left,
			String operation,
			ValueNode<PeriodValue> period) {
		super(left, operation, null);
		this.period = period;
	}

	@Override
	public DataDescriber<TemporalValue<?>> resolve(DataModel model) {
		return new OperationDescriber<>(resolveCalculation(model));
	}

	private DateCalculation resolveCalculation(DataModel model) {
		switch (getOperation()) {
			case "ADD": return new Add(left().resolve(model), period.resolve(model));
			case "MIN": return new Min(left().resolve(model), period.resolve(model));
			default: throw new UnsupportedOperationException("This operation isn't supported");
		}
	}
}
