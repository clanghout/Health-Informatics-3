package model.language.nodes;

import model.data.DataModel;
import model.data.describer.DataDescriber;
import model.data.describer.OperationDescriber;
import model.data.value.IntValue;
import model.data.value.TemporalValue;
import model.process.analysis.operations.dates.computations.Relative;

import java.time.temporal.ChronoUnit;

/**
 * Represents a function performed on 2 dates and a unit.
 *
 * Created by Boudewijn on 9-6-2015.
 */
public class DateFunctionNode extends ValueNode<IntValue> {

	private ValueNode<? extends TemporalValue<?>> left;
	private ValueNode<TemporalValue<?>> right;
	private String unit;

	/**
	 * Construct a new DateFunctionNode.
	 * @param left The first argument of this function.
	 * @param right The second argument of this function.
	 * @param unit The unit this function should output.
	 */
	public DateFunctionNode(
			ValueNode<? extends TemporalValue<?>> left,
			ValueNode<TemporalValue<?>> right,
			String unit) {
		super(null, null);
		this.left = left;
		this.right = right;
		this.unit = unit;
	}

	@Override
	public DataDescriber<IntValue> resolve(DataModel model) {
		ChronoUnit chronoUnit = PeriodNode.resolveUnit(unit);

		return new OperationDescriber<>(new Relative(
				left.resolve(model),
				right.resolve(model),
				chronoUnit
		));
	}
}
