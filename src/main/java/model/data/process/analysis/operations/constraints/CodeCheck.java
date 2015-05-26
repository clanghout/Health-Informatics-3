package model.data.process.analysis.operations.constraints;

import model.data.Row;
import model.data.describer.DataDescriber;
import model.data.value.StringValue;


/**
 * Filter rows based in their codes.
 * Created by jens on 5/26/15.
 */
public class CodeCheck extends Constraint {

	private final DataDescriber<StringValue> code;

	/**
	 * Construct a new CodeCheck.
	 * @param code The code the rows should contain.
	 */
	public CodeCheck(DataDescriber<StringValue> code) {
		this.code = code;
	}


	/**
	 * Check if this row contains the code
	 * @param row The row the check should be performed on.
	 * @return true if the row contains the code.
	 */
	@Override
	public boolean check(Row row) {
		return row.containsCode(code.resolve(row).getValue());
	}
}
