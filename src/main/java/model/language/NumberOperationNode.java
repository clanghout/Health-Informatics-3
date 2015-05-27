package model.language;

import model.data.DataModel;
import model.data.describer.ComputationDescriber;
import model.data.describer.DataDescriber;
import model.data.value.NumberValue;
import model.process.analysis.operations.computations.*;

/**
 * Created by Boudewijn on 27-5-2015.
 */
class NumberOperationNode extends NumberNode {

	private final char operation;

	NumberOperationNode(NumberNode left, char operation, NumberNode right) {
		super(left, right);

		this.operation = operation;
	}

	private Computation resolveComputation(DataModel model) {
		switch (operation) {
			case '+': return new Addition<>(left().resolve(model), right().resolve(model));
			case '-': return new Subtraction<>(left().resolve(model), right().resolve(model));
			case '*': return new Multiplication<>(left().resolve(model), right().resolve(model));
			case '/': return new Division<>(left().resolve(model), right().resolve(model));
			default:
				throw new UnsupportedOperationException(
						String.format("Operation %c not supported", operation)
				);
		}
	}

	DataDescriber<NumberValue> resolve(DataModel model) {
		return new ComputationDescriber<>(resolveComputation(model));
	}
}
