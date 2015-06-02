package model.process.setOperations;

import model.data.DataRow;
import model.data.DataTable;
import model.data.DataTableConversionBuilder;
import model.process.DataProcess;

/**
 * Perform a union on two dataTables.
 * Two rows are considered equal if all their values are the same.
 * The codes of the two rows does not have to be the same.
 * If two rows are equals, than the codes of both rows are added to the output row.
 *
 * Created by jens on 5/28/15.
 */
public class Union extends DataProcess {
	private DataTable table;
	private DataTable table2;

	/**
	 * Create an union operation.
	 * The tables must have the same structure.
	 * @param table the first table of the union.
	 * @param table2 the second table of the union.
	 */
	public Union(DataTable table, DataTable table2) {
		if (!table.equalStructure(table2)) {
			throw new IllegalArgumentException("the tables must have the same structure");
		}
		this.table = table;
		this.table2 = table2;
	}


	@Override
	protected DataTable doProcess() {
		DataTableConversionBuilder builder = new DataTableConversionBuilder(table, table.getName());
		builder.addRowsFromTable(table);
		for (DataRow row : table2.getRows()) {
			boolean sameRow = false;
			for (DataRow rowInBuilder : builder.getRows()) {
				if (row.equalsSoft(rowInBuilder)) {
					rowInBuilder.addCodes(row.getCodes());
					sameRow = true;
					break;
				}
			}

			if (!sameRow) {
				builder.createRow(row);
			}
		}
		return builder.build();
	}
}
