package model.data.process;

import language.Identifier;
import model.data.DataTable;

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
	protected DataTable doProcess() {
		return getDataModel().getByName(identifier.getName());
	}
}
