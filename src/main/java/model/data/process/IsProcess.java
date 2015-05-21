package model.data.process;


import language.Identifier;
import model.data.DataTable;
import model.data.Table;

/**
 * Represents the process of saving a table to the DataModel.
 * Created by Boudewijn on 21-5-2015.
 */
public class IsProcess extends DataProcess {

	private final Identifier<DataTable> table;

	public IsProcess(Identifier<DataTable> table) {
		this.table = table;
	}

	@Override
	protected Table doProcess() {
		getDataModel().add((DataTable) getInput().copy());
		return getInput();
	}
}
