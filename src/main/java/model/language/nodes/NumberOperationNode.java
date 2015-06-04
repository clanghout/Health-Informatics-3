package model.language.nodes;

import model.data.DataModel;
import model.data.describer.ComputationDescriber;
import model.data.describer.DataDescriber;
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
		switch (getOperation()) {
			case "+": return new Addition<>(left().resolve(model), right().resolve(model));
			case "-": return new Subtraction<>(left().resolve(model), right().resolve(model));
			case "*": return new Multiplication<>(left().resolve(model), right().resolve(model));
			case "/": return new Division<>(left().resolve(model), right().resolve(model));
			case "^": return new Power<>(left().resolve(model), right().resolve(model));
			case "SQRT": return new SquareRoot<>(left().resolve(model));
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
		return new ComputationDescriber<>(resolveComputation(model));
	}
}
