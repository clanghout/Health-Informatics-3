package model.data.process.analysis.operations.constraints;

import model.data.DataRow;
import model.data.describer.DataDescriber;
import model.data.value.NumberValue;

/**
 * The check if a value is lesser than or equal to another.
 * Created by Boudewijn on 11-5-2015.
 * @param <T> The type of the values being compared.
 */
public final class LesserEqualsCheck<T extends NumberValue>
		extends CompareCheck<T> {

	public LesserEqualsCheck(DataDescriber<T> leftSide, DataDescriber<T> rightSide) {
		super(leftSide, rightSide);
	}

	@Override
	public boolean check(DataRow row) {
		return compare(row) <= 0;
	}
}
