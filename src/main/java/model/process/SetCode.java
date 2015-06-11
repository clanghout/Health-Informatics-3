package model.process;

import model.data.CombinedDataTable;
import model.data.DataRow;
import model.data.DataTable;
import model.data.Table;
import model.data.describer.DataDescriber;
import model.data.value.StringValue;
import model.language.Identifier;

/**
 * Class that is used to set code on the input table. It needs a string for the code.
 * It needs a able to set the codes on.
 * Furthermore it needs frome the pipe a table that contains all the rows that must get a code.
 *
 * Created by jens on 6/1/15.
 */
public class SetCode extends DataProcess {
	private DataDescriber<StringValue> code;
	private Identifier<DataTable> table;

	/**
	 * Set the code on the rows of the input table that also exist in the codeTable.
	 * @param code code that must be set
	 * @param table table that must get the codes.
	 */
	public SetCode(DataDescriber<StringValue> code, Identifier<DataTable> table) {
		this.code = code;
		this.table = table;
	}


	/**
	 * Set the code to the input table and return the result.
	 * @return the input code with the codes set.
	 */
	@Override
	public final Table doProcess() {
		Table codeTable = getInput();

		DataTable input = getDataModel().getByName(table.getName()).get();
		if (codeTable == null) {
			throw new IllegalStateException("Input is not set");
		}
		if (codeTable instanceof CombinedDataTable) {
			CombinedDataTable comb = (CombinedDataTable) codeTable;
			comb.getTables().forEach(x -> setCode(input, x));
		} else if (codeTable instanceof DataTable) {
			DataTable codeDataTable = (DataTable) codeTable;
			setCode(input, codeDataTable);
		}

		return input;
	}

	/**
	 * Set the code on the rows of the input table, where the row exist in the codeTable.
	 * @param input the table in which the rows must get the code.
	 * @param codeTable the table that specifies which rows must get the code.
	 */
	private void setCode(DataTable input, DataTable codeTable) {
		for (DataRow row : input.getRows()) {
			for (DataRow codeRows : codeTable.getRows()) {
				if (row.equalsSoft(codeRows)) {
					row.addCode(code.resolve(row).getValue());
					break;
				}
			}
		}
	}
}