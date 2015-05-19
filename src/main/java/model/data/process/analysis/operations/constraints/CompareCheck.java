package model.data.process.analysis.operations.constraints;

import model.data.DataRow;
import model.data.describer.DataDescriber;
import model.data.value.NumberValue;

/**
 * Framework class for comparing checks.
 *
 * Created by Boudewijn on 11-5-2015.
 * @param <T> The type of DataValue this check will operate on.
 */
public abstract class CompareCheck<T extends NumberValue>
		extends BinaryCheck<T> {

	/**
	 * Construct a new CompareCheck.
	 * @param leftSide The left side operand
	 * @param rightSide The right side operand
	 */
	public CompareCheck(DataDescriber<T> leftSide, DataDescriber<T> rightSide) {
		super(leftSide, rightSide);
	}

	/**
	 * Compare the values.
	 * @return The result of the Comparable#compareTo() of the operands
	 */
	public int compare(DataRow row) {
		T left = getLeftSide().resolve(row);
		T right = getRightSide().resolve(row);
		return left.compareTo(right);
	}
}
