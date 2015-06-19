package model.process.analysis.operations.set;

import model.data.DataRow;
import model.data.DataTable;
import model.data.DataTableConversionBuilder;
import model.language.Identifier;
import model.process.DataProcess;

import java.util.Optional;


/**
 * Class used to crate a table that is the difference between the two table.
 * So it does: Table1-Table2
 *
 * Created by jens on 6/4/15.
 */
public class Difference extends DataProcess {
	private Identifier<DataTable> tableIdentifier;
	private Identifier<DataTable> table2Identifier;

	/**
	 * Create an Difference operation.
	 * The tables must have the same structure.
	 * @param table the first table of the Difference.
	 * @param table2 that contains the rows that must be removed from the other table.
	 */
	public Difference(Identifier<DataTable> table, Identifier<DataTable> table2) {
		this.tableIdentifier = table;
		this.table2Identifier = table2;
	}

	//TODO define a common parent for set-operations
	@Override
	protected DataTable doProcess() {
		Optional<DataTable> tableOptional = getDataModel().getByName(tableIdentifier.getName());
		Optional<DataTable> table2Optional = getDataModel().getByName(table2Identifier.getName());

		if (!(tableOptional.isPresent() && table2Optional.isPresent())) {
			throw new IllegalArgumentException(
					String.format("Not all identifiers refer to tables: %s, %s",
							tableIdentifier.getName(), table2Identifier.getName())
			);
		}

		return difference(tableOptional.get().copy(), table2Optional.get().copy());
	}

	/**
	 * Calculate the difference between the tables.
	 * @param table the base table
	 * @param table2 all rows from thi table are remove from the other table.
	 * @return the difference between table and table2, table - table2.
	 */
	private DataTable difference(DataTable table, DataTable table2) {
		if (!table.equalStructure(table2)) {
			throw new IllegalArgumentException("the tables must have the same structure");
		}

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
