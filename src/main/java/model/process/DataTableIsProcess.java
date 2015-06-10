package model.process;

import model.data.DataTable;
import model.data.Table;
import model.language.Identifier;

/**
 * Represents the process of saving a single table to the DataModel.
 * Created by jens on 6/6/15.
 */
public class DataTableIsProcess extends DataProcess {

	private final Identifier<DataTable> original;
	private final Identifier<DataTable> save;

	/**
	 * Create a process that saves a specific table from the set of table in the analysis.
	 * @param original the table that must be saved.
	 * @param save save the table to this.
	 */
	public DataTableIsProcess(Identifier<DataTable> original, Identifier<DataTable> save) {
		this.original = original;
		this.save = save;
	}

	/**
	 * Save the table specified in the constructor as a new table.
	 * @return the new table.
	 */
	@Override
	protected Table doProcess() {
		DataTable res = getInput().getTable(original.getName()).export(save.getName());
		getDataModel().add(res);
		return getInput();
	}
}
