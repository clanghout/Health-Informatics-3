package model.process.setOperations;

import model.data.DataRow;
import model.data.DataTable;
import model.data.DataTableConversionBuilder;
import model.process.DataProcess;

/**
 * Created by jens on 6/4/15.
 */
public class Difference extends DataProcess {
	private DataTable table;
	private DataTable table2;

	/**
	 * Create an union operation.
	 * The tables must have the same structure.
	 * @param table the first table of the union.
	 * @param table2 the second table of the union.
	 */
	public Difference(DataTable table, DataTable table2) {
		if (!table.equalStructure(table2)) {
			throw new IllegalArgumentException("the tables must have the same structure");
		}
		this.table = table;
		this.table2 = table2;
	}

	//TODO define a common parent for set-operations
	@Override
	protected DataTable doProcess() {
		DataTableConversionBuilder builder = new DataTableConversionBuilder(table, table.getName());
		for (DataRow row : table.getRows()) {
			boolean sameRow = false;
			for (DataRow rowDif : table2.getRows()) {
				if (row.equalsSoft(rowDif)) {
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
