package model.process.analysis.operations.constraints;

import model.data.Row;
import model.process.describer.DataDescriber;
import model.data.value.DataValue;

/**
 * A check for equality.
 * Created by Boudewijn on 5-5-2015.
 * @param <T> The type of the values we're checking.
 */
public class EqualityCheck<T extends DataValue> extends BinaryCheck<T> {

	/**
	 * Construct a new EqualityCheck.
	 * @param leftSide The left side operand of the equality check
	 * @param rightSide The right side operand of the equality check
	 */
	public EqualityCheck(DataDescriber<T> leftSide, DataDescriber<T> rightSide) {
		super(leftSide, rightSide);
	}

	/**
	 * Perform the EqualityCheck.
	 * @param row The row the check should be performed on.
	 * @return True if the values are equal, false if not
	 */
	@Override
	public boolean check(Row row) {
		return getLeftSide().resolve(row).equals(getRightSide().resolve(row));
	}
}
