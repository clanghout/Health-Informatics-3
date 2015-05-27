package model.language;

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
 */
final class CompareNode extends BooleanNode {

	private Object left;
	private Object right;
	private String operator;

	CompareNode(Object left, String operator, Object right) {
		super(null, null);
		this.left = left;
		this.right = right;
		this.operator = operator;
	}

	DataDescriber<BoolValue> resolve(DataModel model) {
		DataDescriber<NumberValue> leftSide = resolveNode(model, left);
		DataDescriber<NumberValue> rightSide = resolveNode(model, right);

		return new ConstraintDescriber(resolveConstraint(leftSide, operator, rightSide));
	}

	private DataDescriber<NumberValue> resolveNode(DataModel model, Object node) {
		if (node instanceof NumberNode) {
			NumberNode number = (NumberNode) node;

			return number.resolve(model);
		} else {
			throw new UnsupportedOperationException("Code not yet implemented");
		}
	}

	private Constraint resolveConstraint(
			DataDescriber<NumberValue> left,
			String operator,
			DataDescriber<NumberValue> right) {

		switch (operator) {
			case "=": return new EqualityCheck<>(left, right);
			default:
				return resolveComparison(left, operator, right);
		}
	}

	private Constraint resolveComparison(DataDescriber left, String operator, DataDescriber right) {
		DataDescriber<NumberValue> leftNumber =
				left;
		DataDescriber<NumberValue> rightNumber =
				right;
		switch (operator) {
			case ">": return new GreaterThanCheck<>(leftNumber, rightNumber);
			case ">=": return new GreaterEqualsCheck<>(leftNumber, rightNumber);
			case "<": return new LesserThanCheck<>(leftNumber, rightNumber);
			case "<=": return new LesserEqualsCheck<>(leftNumber, rightNumber);
			default: throw new UnsupportedOperationException("Code not yet implemented");
		}
	}
}