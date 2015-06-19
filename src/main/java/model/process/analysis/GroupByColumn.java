package model.process.analysis;

import model.data.DataRow;
import model.data.DataTable;
import model.data.Table;
import model.process.describer.ConstantDescriber;
import model.process.describer.ConstraintDescriber;
import model.process.describer.DataDescriber;
import model.data.value.DataValue;
import model.process.analysis.operations.constraints.EqualityCheck;
import model.process.functions.Function;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Class that groups a table by a column.
 * Created by jens on 6/3/15.
 */
public class GroupByColumn extends GroupByAnalysis {

	private DataDescriber<?> column;

	/**
	 * Create a group by. The chunks are specified by distinct values in the column column.
	 * and the columns by functions.
	 * @param name name of the new table
	 * @param column each distinct value of the column is one chunk.
	 * @param functions functions for the columns
	 * @param columnNames name for the columns
	 */
	public GroupByColumn(
			String name,
			DataDescriber<?> column,
			List<Function> functions,
			List<String> columnNames) {
		super(name, functions, columnNames);
		this.column = column;

	}

	@Override
	public Table analyse(Table input) {
		if (!(input instanceof DataTable)) {
			throw new IllegalArgumentException("group by work only on datatable");
		}
		DataTable table = (DataTable) input;

		createConstraintsFromColumn(table);

		return groupBy(table);
	}

	/**
	 * Create the constraint for the distinct columns values.
	 * @param table table to get the distinct values from.
	 */
	private void createConstraintsFromColumn(DataTable table) {
		LinkedHashMap<DataValue, ConstraintAnalysis> constraints = new LinkedHashMap<>();

		for (DataRow row : table.getRows()) {
			DataValue<?> value = column.resolve(row);
			constraints.put(value, new ConstraintAnalysis(
					new ConstraintDescriber(
							new EqualityCheck(
									column,
									new ConstantDescriber<>(
											value)))));
		}
		setConstraints(constraints);
	}
}
