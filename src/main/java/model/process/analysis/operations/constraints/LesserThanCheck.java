package model.process.analysis.operations.constraints;

import model.data.Row;
import model.process.describer.DataDescriber;
import model.data.value.NumberValue;

/**
 * The check if a value is lesser than another.
 *
 * Created by Boudewijn on 11-5-2015.
 * @param <T> The type of DataValue this check will operate on.
 */
public final class LesserThanCheck<T extends NumberValue>
		extends CompareCheck<T> {

	/**
	 * Construct a new LesserThanCheck.
	 * @param leftSide The left side operand.
	 * @param rightSide The right side operand.
	 */
	public LesserThanCheck(DataDescriber<T> leftSide, DataDescriber<T> rightSide) {
		super(leftSide, rightSide);
	}

	/**
	 * Perform the lesser than check.
	 * @param row The row the check should be performed on.
	 * @return True if the left side is lesser than the right side.
	 */
	@Override
	public boolean check(Row row) {
		return compare(row) < 0;
	}

}
