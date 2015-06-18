package model.process.analysis.operations;

import model.data.*;
import model.data.describer.DataDescriber;
import model.data.describer.TableValueDescriber;
import model.data.value.DataValue;
import model.language.ColumnIdentifier;
import model.language.Identifier;
import model.process.DataProcess;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class that can construct a new table based on an old table.
 *
 * Created by jens on 6/11/15.
 */
public class ColumnComputation extends DataProcess {


	private String name;
	private LinkedHashMap<String, DataDescriber<? extends DataValue>> describers;
	private boolean addAllColumns;

	/**
	 * Create a columnComputation object.
	 * Specify the name of the new table.
	 * @param name name of the new table.
	 * @param addAllColumns true if all the columns of the input table
	 *                         should be added to the result.
	 */
	public ColumnComputation(String name, boolean addAllColumns) {
		this.describers = new LinkedHashMap<>();
		this.name = name;
		this.addAllColumns = addAllColumns;
	}

	/**
	 * Add a column that should end up in the result.
	 * @param describer describer for the value of the column
	 * @param name name of the column
	 */
	public void addColumn(DataDescriber<? extends DataValue> describer, String name) {
		describers.put(name, describer);
	}

	/**
	 * Create a new table, based on the input table.
	 * The table contains the columns specified with the describers.
	 * @return a new table.
	 */
	@Override
	protected Table doProcess() {
		Table input = getInput();
		processColumns();

		Iterator<? extends Row> iterator = input.iterator();
		if (!iterator.hasNext()) {
			throw new IllegalStateException("Table has no rows");
		}

		Row row = iterator.next();
		DataTableBuilder builder = constructBuilder(row);

		builder.createRow(getValues(row));
		while (iterator.hasNext()) {
			builder.createRow(getValues(iterator.next()));
		}

		DataTable res = builder.build();;
		getDataModel().add(res);
		return res;
	}

	/**
	 * Add all the existing columns to the result table.
	 */
	private void processColumns() {
		if (addAllColumns) {
			LinkedHashMap<String, DataDescriber<? extends DataValue>> col = new LinkedHashMap<>();
			for (DataColumn column : getInput().getColumns()) {
				col.put(column.getName(),
						new TableValueDescriber<>(
								getDataModel(),
								new ColumnIdentifier(
										new Identifier(column.getTable().getName()),
										new Identifier(column.getName()))));

			}
			col.putAll(describers);
			describers = col;
		}
	}

	/**
	 * Construct the builder and add the columns to the builder.
	 * @param row a row that can be used to get the types for the column.
	 *               Is needed to resolve the describers.
	 * @return a builder.
	 */
	private DataTableBuilder constructBuilder(Row row) {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName(name);

		for (Map.Entry<String, DataDescriber<? extends DataValue>> entry : describers.entrySet()) {
			builder.createColumn(entry.getKey(), entry.getValue().resolve(row).getClass());
		}

		return builder;
	}

	/**
	 * Get the values of a new row that should be added to the builder.
	 * @param row row used by the describers.
	 * @return the results of the describers resolved with the row.
	 */
	private DataValue[] getValues(Row row) {
		DataValue[] values = new DataValue[describers.size()];
		int i = 0;
		for (Map.Entry<String, DataDescriber<? extends DataValue>> entry : describers.entrySet()) {
			values[i] = entry.getValue().resolve(row);
			i++;
		}
		return values;
	}

}
