package model.language;

/**
 * Identifies a column within a table.
 *
 * Created by Boudewijn on 21-5-2015.
 */
public class ColumnIdentifier {

	private String table;
	private String column;

	public ColumnIdentifier(Identifier table, Identifier column) {
		this.table = table.getName();
		this.column = column.getName();
	}

	public String getTable() {
		return table;
	}

	public String getColumn() {
		return column;
	}
}
