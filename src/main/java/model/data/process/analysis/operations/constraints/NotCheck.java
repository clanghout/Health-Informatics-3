package model.data.process.analysis.operations.constraints;

import model.data.Row;
import model.data.describer.DataDescriber;
import model.data.value.BoolValue;

/**
 * The class represents the not operation.
 * Created by Boudewijn on 12-5-2015.
 */
public class NotCheck extends Constraint {

	private final DataDescriber<BoolValue> operand;

	/**
	 * Construct a new NotCheck.
	 * @param operand The operand to be notted
	 */
	public NotCheck(DataDescriber<BoolValue> operand) {
		this.operand = operand;
	}

	/**
	 * Performs the not operation.
	 * @param row The row the check should be performed on.
	 * @return The inverse of the operand
	 */
	@Override
	public boolean check(Row row) {
		boolean value = operand.resolve(row).getValue();
		return !value;
	}
}
