package model.data.process.analysis.operations.constraints;

import model.data.Row;
import model.data.describer.DataDescriber;
import model.data.value.NumberValue;

/**
 * The check if a value is greater than another.
 *
 * Created by Boudewijn on 11-5-2015.
 * @param <T> The type of DataValue this check will operate on.
 */
public final class GreaterThanCheck<T extends NumberValue>
		extends CompareCheck<T> {

	public GreaterThanCheck(DataDescriber<T> leftSide, DataDescriber<T> rightSide) {
		super(leftSide, rightSide);
	}

	@Override
	public boolean check(Row row) {
		return compare(row) > 0;
	}
}
