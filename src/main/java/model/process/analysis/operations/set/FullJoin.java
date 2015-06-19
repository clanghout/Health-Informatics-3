package model.process.analysis.operations.set;

import model.data.*;
import model.data.value.DataValue;
import model.language.Identifier;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class to join two table. Option to perform a full/left/right join.
 * Created by jens on 6/10/15.
 */
public class FullJoin extends Join {
	private Identifier<DataTable> leftId;
	private Identifier<DataTable> rightId;
	private DataTable left;
	private DataTable right;

	private Join join;

	/**
	 * Specify the type of join.
	 * Full, all the rows of both tables should exist in the result.
	 * Left, all the rows of the left table should exist in the result.
	 * Right, all the rows of the right table should exist in the result.
	 * Join, only the combined rows are added to the result.
	 */
	public enum Join {
		FULL, LEFT, RIGHT, JOIN
	}

	private FullJoin(String name, Join join) {
		super(name);
		this.join = join;
	}
	/**
	 * JOIN two tables.
	 * @param left left table
	 * @param right right table
	 * @param join type of join, FULL, LEFT, RIGHT or JOIN
	 */
	public FullJoin(String name, Identifier<DataTable> left,
					Identifier<DataTable> right,
					Join join) {
		this(name, join);
		this.leftId = left;
		this.rightId = right;
	}

	/**
	 * JOIN two tables.
	 * @param left left table
	 * @param right right table
	 * @param join type of join, FULL, LEFT, RIGHT or JOIN
	 */
	public FullJoin(String name, DataTable left,
					DataTable right,
					Join join) {
		this(name, join);
		this.left = left;
		this.right = right;
	}


	@Override
	protected DataTable joinTable() {
		processFullJoin(getLeft(), getRight());
		DataTable res = getBuilder().build();
		getDataModel().add(res);
		return res;
	}

	@Override
	protected Table getTable() {
		return new CombinedDataTable(getLeft(), getRight());
	}

	/**
	 * Get the left table.
	 * @return the left table.
	 */
	private DataTable getLeft() {
		if (left == null) {
			left =  getDataModel().getByName(leftId.getName()).get();
		}
		return left;
	}

	/**
	 * Get the right table.
	 * @return the right table.
	 */
	private DataTable getRight() {
		if (right == null) {
			right =  getDataModel().getByName(rightId.getName()).get();
		}
		return right;
	}

	/**
	 * Create the rows for the join.
	 * @param leftRow row in the left table
	 * @param rightRow row in the right table
	 */
	private void createRow(DataRow leftRow, DataRow rightRow) {
		DataRow newRow = new DataRow();
		for (Map.Entry<DataColumn, DataColumn> entry : getMappingColumns().entrySet()) {
			if (leftRow.hasColumn(entry.getKey())) {
				checkValue(newRow, entry.getValue(), leftRow.getValue(entry.getKey()));
			} else {
				checkValue(newRow, entry.getValue(),
						DataValue.getNullInstance(entry.getValue().getType()));
			}
			if (rightRow.hasColumn(entry.getKey())) {
				checkValue(newRow, entry.getValue(), rightRow.getValue(entry.getKey()));
			}
		}
		newRow.addCodes(leftRow.getCodes());
		newRow.addCodes(rightRow.getCodes());
		getBuilder().addRow(newRow);
	}

	/**
	 * Perform a LEFT/right/both full join, kind of join is based in fullLeft and fullRight boolean.
	 */
	private void processFullJoin(DataTable left, DataTable right) {
		Set<Row> rightAdd = new HashSet<>();
		for (DataRow leftRow : (Iterable<DataRow>) left) {
			boolean leftAdd = false;

			for (DataRow rightRow : (Iterable<DataRow>) right) {
				CombinedDataRow comb = new CombinedDataRow();
				comb.addDataRow(leftRow);
				comb.addDataRow(rightRow);
				if (getConstraint().resolve(comb).getValue()) {
					createRow(leftRow, rightRow);
					leftAdd = true;
					rightAdd.add(rightRow);
				}
			}
			if (!leftAdd && (join == Join.LEFT || join == Join.FULL)) {
				createRow(leftRow, new DataRow());
			}
		}
		if (join == Join.RIGHT || join == Join.FULL) {
			addNotAddedRighRows(rightAdd);
		}
	}

	/**
	 * Add the rows from the right table that are not added to the result.
	 * @param rightAdd set of rows that are set in the result table.
	 */
	private void addNotAddedRighRows(Set<Row> rightAdd) {
		for (DataRow rightRow : (Iterable<DataRow>) right) {
			if (!rightAdd.contains(rightRow)) {
				createRow(new DataRow(), rightRow);
			}
		}
	}

}
