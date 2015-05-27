package model.data;

import java.util.*;

/**
 * Class that represents the data that should be analysed.
 */
public class DataTable extends Table {
	private List<DataRow> rows;
	/**
	 * flaggedRows is used to flag a row that will get a code or will not get filtered out.
	 * This is needed to allow certain operations to be performed on a combinedDataTable
	 */
	private Set<DataRow> flaggedRows;
	private Map<String, DataColumn> columns;
	private String name;

	/**
	 * Create a new empty DataTable.
	 */
	public DataTable() {
		rows = new ArrayList<>();
		columns = new HashMap<>();
		flaggedRows = new HashSet<>();
	}

	/**
	 * Create an new empty DataTable with the given name.
	 * @param name The name you want to give to this DataTable
	 */
	public DataTable(String name) {
		this();
		this.name = name;
	}

	/**
	 * Create a new DataTable.
	 * @param rows rows of the DataTable
	 * @param columns columns of the DataTable
	 */
	public DataTable(String name, List<DataRow> rows, List<DataColumn> columns) {
		this();
		this.rows = new ArrayList<>(rows);
		for (DataColumn c : columns) {
			this.columns.put(c.getName(), c);
		}
		this.name = name;
	}

	/**
	 * Get a specific row.
	 *
	 * @param i index of the row
	 * @return index of the requested row
	 */
	public DataRow getRow(int i) {
		return rows.get(i);
	}

	/**
	 * Return an unmodifiable list containing the rows.
	 *
	 * @return An unmodifiable list over the rows
	 */
	public List<DataRow> getRows() {
		return Collections.unmodifiableList(rows);
	}


	/**
	 * Returns the amounts of rows.
	 * @return The amount of rows
	 */
	public int getRowCount() {
		return rows.size();
	}

	@Override
	public List<DataColumn> getColumns() {
		return new ArrayList<>(columns.values());
	}

	/**
	 * get the column with the name columnName.
	 * @param columnName the name of the column
	 * @return the column with the name columnName
	 */
	public DataColumn getColumn(String columnName) {
		return columns.get(columnName);
	}
	/**
	 * Get the name of the table.
	 * @return the name of the table.
	 */
	public String getName() {
		return name;
	}

	@Override
	public Iterator<DataRow> iterator() {
		return rows.iterator();
	}

	@Override
	public boolean flagRow(Row row) {
		if (row instanceof DataRow && rows.contains(row)) {
			flaggedRows.add((DataRow) row);
			return true;
		} else {
			return false;
		}

	}

	@Override
	public void resetFlags() {
		flaggedRows.clear();
	}

	@Override
	public void deleteNotFlagged() {
		List<DataRow> newRows = new ArrayList<>();
		for (DataRow row : rows) {
			if (flaggedRows.contains(row)) {
				newRows.add(row);
			}
		}
		rows = newRows;
		resetFlags();
	}

	/**
	 * return the flagged rows.
	 * @return the rows that are flagged
	 */
	public Set<DataRow> getFlaggedRows() {
		return flaggedRows;
	}


	@Override
	public DataTable copy() {
		return export(this.name);
	}

	@Override
	public boolean equalsSoft(Object obj) {
		if (!(obj instanceof DataTable) || !(this.equalStructure(obj))) {
			return false;
		}
		DataTable other = (DataTable) obj;

		for (DataRow row : rows) {
			boolean res = false;
			for (DataRow rowOther : other.rows) {
				if (row.equalsSoft(rowOther)) {
					res = true;
					break;
				}
			}
			if (!res) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DataTable) || !(this.equalStructure(obj))) {
			return false;
		}
		DataTable other = (DataTable) obj;

		//first if is to enable that name is null, else there would be a nullpointer
		if (this.name != other.name && (this.name == null
				|| !other.name.equals(this.name))) {
				return false;
		}

		for (DataRow row : rows) {
			boolean same = false;
			for (DataRow rowOther : other.rows) {
				if (row.equals(rowOther)) {
					same = true;
					break;
				}
			}
			if (!same) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		int res = 0;
		for (DataColumn column : columns.values()) {
			res += column.hashCode();
		}
		return res;
	}

}
