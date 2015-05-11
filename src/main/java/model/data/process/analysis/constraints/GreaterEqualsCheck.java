package model.data.process.analysis.constraints;

import model.data.DataRow;
import model.data.describer.DataDescriber;
import model.data.value.NumberValue;

/**
 * The check if a value is greater than or equal to another.
 * Created by Boudewijn on 11-5-2015.
 * @param <T> The type of the values being compared.
 */
public final class GreaterEqualsCheck<T extends NumberValue>
		extends CompareCheck<T> {

	public GreaterEqualsCheck(DataDescriber<T> leftSide, DataDescriber<T> rightSide) {
		super(leftSide, rightSide);
	}

	@Override
	public boolean check(DataRow row) {
		return compare(row) >= 0;
	}
}
