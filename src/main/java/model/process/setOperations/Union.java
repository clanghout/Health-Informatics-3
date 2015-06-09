package model.process.setOperations;

import model.data.DataRow;
import model.data.DataTable;
import model.data.DataTableConversionBuilder;
import model.language.Identifier;
import model.process.DataProcess;

import java.util.Optional;

/**
 * Perform a union on two dataTables.
 * Two rows are considered equal if all their values are the same.
 * The codes of the two rows does not have to be the same.
 * If two rows are equals, than the codes of both rows are added to the output row.
 *
 * Created by jens on 5/28/15.
 */
public class Union extends DataProcess {
	private Identifier<DataTable> tableIdentifier;
	private Identifier<DataTable> table2Identifier;

	/**
	 * Create an union operation.
	 * The tables must have the same structure.
	 * @param tableIdentifier the first table of the union.
	 * @param table2Identifier the second table of the union.
	 */
	public Union(Identifier<DataTable> tableIdentifier, Identifier<DataTable> table2Identifier) {
		this.tableIdentifier = tableIdentifier;
		this.table2Identifier = table2Identifier;
	}


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

		return union(tableOptional.get(), table2Optional.get());
	}

	private DataTable union(DataTable table1, DataTable table2) {
		if (!table1.equalStructure(table2)) {
			throw new IllegalArgumentException("the tables must have the same structure");
		}

		DataTableConversionBuilder builder = new DataTableConversionBuilder(
				table1, table1.getName()
		);
		builder.addRowsFromTable(table1);
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
		try {
			return builder.build();
		} catch (Exception e) {
			throw new RuntimeException("unexpected exception");
		}
	}
}
