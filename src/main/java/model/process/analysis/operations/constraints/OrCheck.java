package model.process.analysis.operations.constraints;

import model.data.Row;
import model.process.describer.DataDescriber;
import model.data.value.BoolValue;

/**
 * The check for the or operation.
 * Created by Boudewijn on 11-5-2015.
 */
public final class OrCheck extends BinaryCheck<BoolValue> {

	/**
	 * Construct a new OrCheck.
	 * @param leftSide The left side operand of the or operation
	 * @param rightSide The right side operand of the or operation
	 */
	public OrCheck(DataDescriber<BoolValue> leftSide, DataDescriber<BoolValue> rightSide) {
		super(leftSide, rightSide);
	}

	/**
	 * Perform the or operation.
	 * @param row The row the check should be performed on.
	 * @return The result of or-ing the left and right side
	 */
	@Override
	public boolean check(Row row) {
		BoolValue left = getLeftSide().resolve(row);
		BoolValue right = getRightSide().resolve(row);
		return left.getValue() || right.getValue();
	}
}
