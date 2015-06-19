package model.process.describer;

import model.data.Row;
import model.data.value.DataValue;
import model.process.analysis.operations.Operation;

/**
 * A DataDescriber describing the result of an operation.
 *
 * Created by Boudewijn on 9-6-2015.
 * @param <T> The type this Describer returns.
 */
public class OperationDescriber<T extends DataValue<?>> extends DataDescriber<T> {

	private Operation<T> operation;

	/**
	 * Construct a new OperationDescriber.
	 * @param operation The operation to describe.
	 */
	public OperationDescriber(Operation<T> operation) {
		this.operation = operation;
	}

	@Override
	public T resolve(Row row) {
		return operation.operate(row);
	}
}
