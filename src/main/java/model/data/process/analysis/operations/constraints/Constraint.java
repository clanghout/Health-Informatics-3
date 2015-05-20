package model.data.process.analysis.operations.constraints;

import model.data.Row;
import model.data.process.analysis.operations.Operation;
import model.data.value.BoolValue;

/**
 * A class for declaring constraints to be placed on rows.
 * Created by Boudewijn on 5-5-2015.
 */
public abstract class Constraint extends Operation<BoolValue> {

	/**
	 * The check subclasses should implement.
	 *
	 * @param row The row the check should be performed on.
	 * @return True if the check passed, false if not
	 */
	public abstract boolean check(Row row);

	@Override
	public BoolValue operate(Row row) {
		return new BoolValue(check(row));
	}
}
