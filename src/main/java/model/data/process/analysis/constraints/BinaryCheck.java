package model.data.process.analysis.constraints;

import model.data.DataValue;
import model.data.describer.DataDescriber;

/**
 * The superclass for binary checks.
 * Created by Boudewijn on 11-5-2015.
 *
 * @param <T> The type of the left and right side DataDescribers
 */
public abstract class BinaryCheck<T extends DataValue> extends Constraint {

	private final DataDescriber<T> left;
	private final DataDescriber<T> right;

	/**
	 * Construct a new BinaryCheck.
	 * @param leftSide The DataDescriber for the left side operand
	 * @param rightSide The DataDescriber for the right side operand
	 */
	public BinaryCheck(DataDescriber<T> leftSide, DataDescriber<T> rightSide) {
		left = leftSide;
		right = rightSide;
	}

	/**
	 * Get the DataDescriber for the left side operand.
	 * @return The DataDescriber for the left side operand
	 */
	public DataDescriber<T> getLeftSide() {
		return left;
	}

	/**
	 * Get the DataDescriber for the right side operand.
	 * @return The DataDescriber for the right side operand
	 */
	public DataDescriber<T> getRightSide() {
		return right;
	}

}
