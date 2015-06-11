package model.process.setOperations;

import model.data.*;
import model.language.Identifier;

import java.util.Map;

/**
 * Created by jens on 6/10/15.
 */
public class SimpleJoin extends Join {
	private  Identifier<DataTable> tableId;
	private Table table;

	/**
	 * Create a join that uses a combined dataTable.
	 * @param name new name of the table
	 * @param table table used in the join
	 */
	public SimpleJoin(String name, Identifier<DataTable> table) {
		super(name);
		this.tableId = table;
	}

	/**
	 * Create a join that uses a combined dataTable.
	 * @param name new name of the table
	 * @param table table used in the join
	 */
	public SimpleJoin(String name, Table table) {
		super(name);
		this.table = table;
	}

	@Override
	protected void joinTable() {
		for (Row row : (Iterable<Row>) table) {
			DataRow newRow = new DataRow();
			if (getConstraint().resolve(row).getValue()) {
				for (Map.Entry<DataColumn, DataColumn> entry : getMappingColumns().entrySet()) {
					checkValue(newRow, entry.getValue(), row.getValue(entry.getKey()));
				}
				newRow.addCodes(row.getCodes());

				getBuilder().addRow(newRow);
			}
		}
	}

	@Override
	protected Table getTable() {
		if (table == null) {
			table = getDataModel().getByName(tableId.getName()).get();
		}
		return table;
	}
}
