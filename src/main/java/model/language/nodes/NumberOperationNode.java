package model.language.nodes;

import model.data.DataModel;
import model.data.describer.ComputationDescriber;
import model.data.describer.DataDescriber;
import model.data.value.NumberValue;
import model.process.analysis.operations.computations.*;

/**
 * Created by Boudewijn on 27-5-2015.
 */
public class NumberOperationNode extends OperationNode<NumberValue> {


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

	public DataDescriber<NumberValue> resolve(DataModel model) {
		return new ComputationDescriber<>(resolveComputation(model));
	}
}
