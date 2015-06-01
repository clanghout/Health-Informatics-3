package model.process;

import model.data.CombinedDataTable;
import model.data.DataTable;
import model.data.Row;
import model.data.Table;
import model.process.setOperations.Union;

import java.util.Iterator;

/**
 * Created by jens on 6/1/15.
 */
public class SetCodes extends DataProcess {
	private String code;
	private Table codeTable;

	public SetCodes(String code, Table codeTable) {
		this.code = code;
		this.codeTable = codeTable;
	}

	@Override
	public final Table doProcess() {
		Table input = getInput();
		if (input == null) {
			throw new IllegalStateException("Input is not set");
		}
		if (input instanceof CombinedDataTable) {
			CombinedDataTable comb = (CombinedDataTable) input;
			for (DataTable table : comb.getTables()) {
				setCodes(table);
			}
		} else if (input instanceof DataTable) {
			DataTable inputTable = (DataTable) input;
			setCodes(inputTable);
		}

		return input;
	}


	private Table setCodes(DataTable input) {
		if (codeTable instanceof CombinedDataTable) {
			CombinedDataTable comb = (CombinedDataTable) codeTable;
			for (DataTable table : comb.getTables()) {
				if (table.equalStructure(input)) {
					Union union = new Union(input, table);
					union.process();
					input = (DataTable) union.getOutput();
				}
			}
		} else if (codeTable instanceof DataTable) {
			DataTable table = (DataTable) codeTable;
			Union union = new Union(input, table);
			union.process();
			input = (DataTable) union.getOutput();
		}

		return input;

	}
}
