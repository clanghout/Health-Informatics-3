package model.language.nodes;

import model.data.DataModel;
import model.data.describer.DataDescriber;
import model.data.describer.OperationDescriber;
import model.data.value.NumberValue;
import model.process.analysis.operations.computations.*;

/**
 * Represents an operation between two (or one) numbers.
 *
 * The supported operations are:
 * <ul>
 *     <li>+</li>
 *     <li>-</li>
 *     <li>*</li>
 *     <li>/</li>
 *     <li>^</li>
 *     <li>SQRT</li>
 * </ul>
 *
 * Since SQRT is a unary operation, only the left side operand is used.
 * Created by Boudewijn on 27-5-2015.
 */
public class NumberOperationNode extends OperationNode<NumberValue> {


	/**
	 * Construct a new NumberOperationNode.
	 * @param left The left side operand of the operation.
	 * @param operation The operation you want to perform.
	 * @param right The right side operand of the operation.
	 */
	public NumberOperationNode(
			ValueNode<NumberValue> left,
			String operation,
			ValueNode<NumberValue> right) {
		super(left, operation, right);
	}

	private Computation resolveComputation(DataModel model) {
		DataDescriber<NumberValue> leftValue = left().resolve(model);
		DataDescriber<NumberValue> rightValue = right() != null ? right().resolve(model) : null;
		switch (getOperation()) {
			case "+": return new Addition<>(leftValue, rightValue);
			case "-": return new Subtraction<>(leftValue, rightValue);
			case "*": return new Multiplication<>(leftValue, rightValue);
			case "/": return new Division<>(leftValue, rightValue);
			case "^": return new Power<>(leftValue, rightValue);
			case "%": return new Modulo<>(leftValue, rightValue);
			case "SQRT": return new SquareRoot<>(leftValue);
			default:
				throw new UnsupportedOperationException(
						String.format("Operation %s not supported", getOperation())
				);
		}
	}

	/**
	 * Resolves the NumberOperation to a DataDescriber.
	 * @param model The model to be used.
	 * @return A DataDescriber describing the operation.
	 */
	public DataDescriber<NumberValue> resolve(DataModel model) {
		return new OperationDescriber<>(resolveComputation(model));
	}
}
