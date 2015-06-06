package model.language.nodes;

import model.data.DataModel;
import model.data.describer.ConstraintDescriber;
import model.data.describer.DataDescriber;
import model.process.analysis.operations.constraints.*;
import model.data.value.BoolValue;
import model.data.value.NumberValue;

/**
 * This class represent a comparison.
 *
 * Created by Boudewijn on 21-5-2015.
 * @param <T> The type of the values entered into this comparison.
 */
public final class CompareNode<T extends NumberValue> extends OperationNode<BoolValue> {

	private ValueNode<T> left;
	private ValueNode<T> right;

	/**
	 * Construct a new CompareNode.
	 * @param left The left side operand of the comparison.
	 * @param operation The kind of comparison you want to perform.
	 * @param right The right side operand of the comparison.
	 */
	public CompareNode(
			ValueNode<T> left,
			String operation,
			ValueNode<T> right) {
		super(null, operation, null);
		this.left = left;
		this.right = right;
	}

	/**
	 * Resolves the ComparisonNode to a DataDescriber.
	 * @param model The model to be used.
	 * @return A DataDescriber describing the comparison.
	 */
	public DataDescriber<BoolValue> resolve(DataModel model) {
		DataDescriber<T> leftSide = left.resolve(model);
		DataDescriber<T> rightSide = right.resolve(model);

		return new ConstraintDescriber(resolveConstraint(leftSide, rightSide));
	}

	private Constraint resolveConstraint(
			DataDescriber<T> left,
			DataDescriber<T> right) {

		switch (getOperation()) {
			case ">": return new GreaterThanCheck<>(left, right);
			case ">=": return new GreaterEqualsCheck<>(left, right);
			case "<": return new LesserThanCheck<>(left, right);
			case "<=": return new LesserEqualsCheck<>(left, right);
			default: throw new UnsupportedOperationException("Code not yet implemented");
		}
	}
}