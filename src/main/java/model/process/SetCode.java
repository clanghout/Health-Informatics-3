package model.process;

import model.data.*;
import model.process.setOperations.Union;

/**
 * Class that is used to set code on the input table. In needs a string for the code.
 * And in needs a table that contains all the rows that must get a code.
 *
 * Created by jens on 6/1/15.
 */
public class SetCode extends DataProcess {
	private String code;
	private Table codeTable;

	/**
	 * Set the code on the rows of the input table that also exist in the codeTable.
	 * @param code code that must be set
	 * @param codeTable rows on which the code must be set
	 */
	public SetCode(String code, Table codeTable) {
		this.code = code;
		this.codeTable = codeTable;
	}

	/**
	 * Set the code to the input table and return the result.
	 * @return the input code with the codes set.
	 */
	@Override
	public final Table doProcess() {
		Table input = getInput();

		if (input == null) {
			throw new IllegalStateException("Input is not set");
		}
		if (input instanceof CombinedDataTable) {
			CombinedDataTable comb = (CombinedDataTable) input;
			comb.getTables().forEach(this::setCodes);
		} else if (input instanceof DataTable) {
			DataTable inputTable = (DataTable) input;
			input = setCodes(inputTable);
		}

		return input;
	}


	/**
	 * Set the codes on the input table.
	 * @param input table that must get the code
	 * @return the input table with the set codes
	 */
	private Table setCodes(DataTable input) {
		if (codeTable instanceof CombinedDataTable) {
			CombinedDataTable comb = (CombinedDataTable) codeTable;
			for (DataTable table : comb.getTables()) {
				if (table.equalStructure(input)) {
					setCode(input, table);
				}
			}
		} else if (codeTable instanceof DataTable) {
			DataTable table = (DataTable) codeTable;
			setCode(input, table);
		}
		return input;
	}

	private void setCode(DataTable input, DataTable codeTable) {
		for (DataRow row : input.getRows()) {
			for (DataRow rowInodes : codeTable.getRows()) {
				if (row.equalsSoft(rowInodes)) {
					row.addCode(code);
					break;
				}
			}
		}
	}
}
