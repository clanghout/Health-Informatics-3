package model.data.process.analysis.constraints;

import model.data.DataRow;
import model.data.describer.DataDescriber;
import model.data.value.DataValue;

/**
 * The check if a value is greater than another.
 *
 * Created by Boudewijn on 11-5-2015.
 * @param <T> The type of DataValue this check will operate on.
 */
public final class GreaterThanCheck<T extends DataValue & Comparable<DataValue>>
		extends CompareCheck<T> {

	public GreaterThanCheck(DataDescriber<T> leftSide, DataDescriber<T> rightSide) {
		super(leftSide, rightSide);
	}

	@Override
	public boolean check(DataRow row) {
		return compare(row) > 0;
	}
}
