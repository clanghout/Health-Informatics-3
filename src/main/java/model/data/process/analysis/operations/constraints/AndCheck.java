package model.data.process.analysis.operations.constraints;

import model.data.Row;
import model.data.describer.DataDescriber;
import model.data.value.BoolValue;

/**
 * The check for the and operation.
 * Created by Boudewijn on 11-5-2015.
 */
public final class AndCheck extends BinaryCheck<BoolValue> {

	/**
	 * Construct a new AndCheck.
	 * @param leftSide The left side operand of the and operation
	 * @param rightSide The right side operand of the and operation
	 */
	public AndCheck(DataDescriber<BoolValue> leftSide, DataDescriber<BoolValue> rightSide) {
		super(leftSide, rightSide);
	}

	/**
	 * Perform the and operation.
	 * @param row The row the check should be performed on.
	 * @return The result of and-ing the left and right side
	 */
	@Override
	public boolean check(Row row) {
		BoolValue left = getLeftSide().resolve(row);
		BoolValue right = getRightSide().resolve(row);
		return left.getValue() & right.getValue();
	}
}
