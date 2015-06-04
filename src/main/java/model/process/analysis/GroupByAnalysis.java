package model.process.analysis;


import model.data.CombinedDataTable;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.Table;
import model.data.value.DataValue;
import model.data.value.StringValue;
import model.process.functions.Function;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Perform a groupBy analyse on the table.
 * Specify the chunks and the functions.
 * Created by jens on 6/3/15.
 */
public abstract class GroupByAnalysis extends DataAnalysis {
	private LinkedHashMap<String, ConstraintAnalysis> constraints;
	private DataTableBuilder builder;
	private List<Function> functionsList;

	protected void constructConstraintList(
			List<String> groupNames,
			List<ConstraintAnalysis> constrainList) {

		if (constrainList.size() != groupNames.size()) {
			throw new IllegalArgumentException("number of groups does not correspond "
					+ "to the number of group names.");
		}

		constraints = new LinkedHashMap<>();
		for (int i = 0; i < constrainList.size(); i++) {
			constraints.put(groupNames.get(i), constrainList.get(i));
		}
	}

	protected void constructBuilder(
			String name,
			List<Function> functions,
			List<String> columnsNames) {

		builder = new DataTableBuilder();
		builder.setName(name);
		if (functions.size() != columnsNames.size()) {
			throw new IllegalArgumentException("number of columns does not correspond "
					+ "to the number of functions.");
		}

		this.functionsList = functions;
		builder.createColumn("Chunk", StringValue.class);

		for (int i = 0; i < functions.size(); i++) {
			try {
				builder.createColumn(columnsNames.get(i),
						(Class<? extends DataValue>) functions.get(i)
								.getClass()
								.getMethod("calculate")
								.getReturnType());
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("function calculate does not exist.");
			}
		}
	}

	/**
	 * Perform the specified functions on the chunks in the input table.
	 * @param input the table to perform the groupBy on
	 * @return a table that contains, for each chunk, the results of the functions.
	 */
	protected DataTable groupBy(Table input) {
		if (input instanceof CombinedDataTable) {
			throw new IllegalArgumentException("group by works only on datatable");
		}
		for (Map.Entry<String, ConstraintAnalysis> entry : constraints.entrySet()) {
			DataTable chunk = (DataTable) input.copy();
			chunk = (DataTable) entry.getValue().analyse(chunk);

			DataValue[] values = new DataValue[functionsList.size() + 1];
			values[0] = new StringValue(entry.getKey());

			int i = 1;
			for (Function function : functionsList) {
				function.setTable(chunk);
				values[i] = function.calculate();
				i++;
			}

			builder.createRow(values);
		}
		return builder.build();

	}
}
