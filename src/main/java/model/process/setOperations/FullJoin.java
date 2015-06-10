package model.process.setOperations;

import model.data.*;
import model.language.Identifier;

import javax.xml.crypto.Data;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by jens on 6/10/15.
 */
public class FullJoin extends Join {
	private Identifier<DataTable> left;
	private Identifier<DataTable> right;
	private boolean fullLeft;
	private boolean fullRight;

	/**
	 * Join two tables.
	 * @param left left table
	 * @param right right table
	 * @param fullLeft true if all left rows should exist in the result
	 * @param fullRight true if all right rows should exist in the result
	 */
	public FullJoin(String name, Identifier<DataTable> left,
					Identifier<DataTable> right,
					boolean fullLeft,
					boolean fullRight) {
		super(name);
		this.left = left;
		this.right = right;
		this.fullLeft = fullLeft;
		this.fullRight = fullRight;
	}


	@Override
	protected void joinTable() {
		try {
			processFullJoin(getLeft(), getRight());
		} catch (Exception e) {
			throw new RuntimeException("join error");
		}
	}

	@Override
	protected Table getTable() {
		return new CombinedDataTable(getLeft(), getRight());
	}

	private DataTable getLeft() {
		return getDataModel().getByName(left.getName()).get();
	}

	private DataTable getRight() {
		return getDataModel().getByName(right.getName()).get();
	}

	/**
	 * Create the rows for the join
	 * @param leftRow row in the left table
	 * @param rightRow row in the right table
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private void createRow(DataRow leftRow, DataRow rightRow) throws IllegalAccessException, InstantiationException {
		DataRow newRow = new DataRow();
		for (Map.Entry<DataColumn, DataColumn> entry : getMappingColumns().entrySet()) {
			if (leftRow.hasColumn(entry.getKey())) {
				checkValue(newRow, entry.getValue(), leftRow.getValue(entry.getKey()));
			} else {
				//TODO set value to null
				checkValue(newRow, entry.getValue(), entry.getValue().getType().newInstance());
			}
			if (rightRow.hasColumn(entry.getKey())) {
				checkValue(newRow, entry.getValue(), leftRow.getValue(entry.getKey()));
			}
		}
		newRow.addCodes(leftRow.getCodes());
		newRow.addCodes(rightRow.getCodes());
		getBuilder().addRow(newRow);
	}

	/**
	 * perform a Left/right/both full join
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private void processFullJoin(DataTable left, DataTable right) throws IllegalAccessException, InstantiationException {
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
			if (!leftAdd && fullLeft) {
				createRow(leftRow, new DataRow());
			}
			if (fullRight) {
				addNotAddedRighRows(rightAdd);
			}
		}

	}

	/**
	 * add the rows from the right table that are not added to the result.
	 * @param rightAdd set of rows that are set in the result table.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private void addNotAddedRighRows(Set<Row> rightAdd) throws InstantiationException, IllegalAccessException {
		for (DataRow rightRow : (Iterable<DataRow>) right) {
			if (!rightAdd.contains(right)) {
				createRow(new DataRow(), rightRow);
			}
		}
	}

}
