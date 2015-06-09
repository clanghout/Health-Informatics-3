package model.data.describer;

import model.data.Row;
import model.data.value.BoolValue;
import model.process.analysis.operations.dates.constraint.DateConstraint;

/**
 * Describes a DateConstraint.
 *
 * Created by Boudewijn on 9-6-2015.
 */
public class DateConstraintDescriber extends DataDescriber<BoolValue> {

	private DateConstraint constraint;

	public DateConstraintDescriber(DateConstraint constraint) {
		this.constraint = constraint;
	}

	@Override
	public BoolValue resolve(Row row) {
		return constraint.operate(row);
	}
}
