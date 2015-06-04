package model.process.analysis;

import model.data.CombinedDataTable;
import model.data.DataRow;
import model.data.DataTable;
import model.data.Table;
import model.data.describer.ConstantDescriber;
import model.data.describer.ConstraintDescriber;
import model.data.describer.RowValueDescriber;
import model.data.value.BoolValue;
import model.data.value.DataValue;
import model.data.value.StringValue;
import model.process.analysis.operations.constraints.EqualityCheck;
import model.process.functions.Function;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by jens on 6/3/15.
 */
public class GroupByColumn extends GroupByAnalysis {

	RowValueDescriber column;

	/**
	 * Create a group by. The chunks are specified by distinct values in the column column.
	 * and the columns by functions.
	 * @param name name of the new table
	 * @param column each distinct value of the column is one chunk.
	 * @param functions functions for the columns
	 * @param columnNames name for the columns
	 */
	public GroupByColumn(String name, RowValueDescriber column, List<Function> functions,
						 List<String> columnNames) {
		this.column = column;
		constructBuilder(name, functions, columnNames);

	}

	@Override
	public Table analyse(Table input) {
		if (input instanceof CombinedDataTable) {
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
		LinkedHashMap<String, ConstraintAnalysis> constraints = new LinkedHashMap<>();

		for (DataRow row : table.getRows()) {
			DataValue value = column.resolve(row);
			constraints.put(value.toString(), new ConstraintAnalysis(
					new ConstraintDescriber(
							new EqualityCheck<>(
									column,
									new ConstantDescriber<>(
											value.copy())))));
		}
		setConstraints(constraints);
	}
}
