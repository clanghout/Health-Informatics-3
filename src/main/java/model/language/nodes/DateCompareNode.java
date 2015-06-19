package model.language.nodes;

import model.data.DataModel;
import model.process.describer.DataDescriber;
import model.process.describer.OperationDescriber;
import model.data.value.BoolValue;
import model.data.value.TemporalValue;
import model.process.analysis.operations.dates.constraint.After;
import model.process.analysis.operations.dates.constraint.Before;
import model.process.analysis.operations.dates.constraint.DateConstraint;

/**
 * Represents a comparison between 2 dates.
 *
 * Created by Boudewijn on 9-6-2015.
 */
public class DateCompareNode extends OperationNode<BoolValue> {

	private ValueNode<? extends TemporalValue<?>> left;
	private ValueNode<? extends TemporalValue<?>> right;

	/**
	 * Construct a new DateCompareNode.
	 * @param left The left side operand of this comparison.
	 * @param operation The type of comparison.
	 * @param right The right side operand of this comparison.
	 */
	public DateCompareNode(
			ValueNode<? extends TemporalValue<?>> left,
			String operation,
			ValueNode<? extends TemporalValue<?>> right) {
		super(null, operation, null);
		this.left = left;
		this.right = right;
	}

	@Override
	public DataDescriber<BoolValue> resolve(DataModel model) {
		return new OperationDescriber<>(
				resolveConstraint(model)
		);
	}

	private DateConstraint resolveConstraint(DataModel model) {
		DataDescriber<? extends TemporalValue<?>> leftDescriber = left.resolve(model);
		DataDescriber<? extends TemporalValue<?>> rightDescriber = right.resolve(model);
		switch (getOperation()) {
			case "AFTER": return new After(leftDescriber, rightDescriber);
			case "BEFORE": return new Before(leftDescriber, rightDescriber);
			default: throw new UnsupportedOperationException(
					String.format("Operation %s not supported", getOperation())
			);
		}
	}
}
