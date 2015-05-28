package model.process;

import model.language.Identifier;
import model.data.DataTable;
import model.data.Table;

/**
 * This process simply reads a table from memory and outputs it.
 *
 * Created by Boudewijn on 20-5-2015.
 */
public class FromProcess extends DataProcess {

	private Identifier<DataTable> identifier;

	public FromProcess(Identifier<DataTable> identifier) {
		this.identifier = identifier;
	}

	@Override
	protected Table doProcess() {
		return getDataModel().getByName(identifier.getName()).copy();
	}
}
