package model.process.setOperations;

import model.data.*;
import model.language.Identifier;

import java.util.Map;

/**
 * Created by jens on 6/10/15.
 */
public class SimpleJoin extends Join {
	private  Identifier<DataTable> table;

	public SimpleJoin(String name,  Identifier<DataTable> table) {
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
		return getDataModel().getByName(table.getName()).get();
	}
}
