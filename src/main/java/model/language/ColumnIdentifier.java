package model.language;

/**
 * Identifies a column within a table.
 *
 * Created by Boudewijn on 21-5-2015.
 */
public class ColumnIdentifier {

	private String table;
	private String column;

	/**
	 * Construct a new ColumnIdentifier.
	 * @param table The identifier for the table.
	 * @param column The identifier for the column.
	 */
	public ColumnIdentifier(Identifier table, Identifier column) {
		this.table = table.getName();
		this.column = column.getName();
	}

	/**
	 * Get the table name.
	 * @return The name of the table.
	 */
	public String getTable() {
		return table;
	}

	/**
	 * Get the column name.
	 * @return The name of the column.
	 */
	public String getColumn() {
		return column;
	}
}
