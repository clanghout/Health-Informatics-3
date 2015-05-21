package language;

/**
 * Identifies a column within a table.
 *
 * Created by Boudewijn on 21-5-2015.
 */
class ColumnIdentifier {

	private String table;
	private String column;

	ColumnIdentifier(Identifier table, Identifier column) {
		this.table = table.getName();
		this.column = column.getName();
	}

	String getTable() {
		return table;
	}

	String getColumn() {
		return column;
	}
}
