package model.data.process.analysis.operations;

import model.data.DataRow;
import model.data.Row;
import model.data.value.DataValue;

/**
 * A class representing an operation on a row, yielding a DataValue.
 * Created by Boudewijn on 12-5-2015.
 * @param <T> The type of DataValue this operation returns.
 */
public abstract class Operation<T extends DataValue> {

	/**
	 * Perform the operation.
	 * @param row The row you want to operate on.
	 * @return The result of the operation.
	 */
	public abstract T operate(Row row);
}
