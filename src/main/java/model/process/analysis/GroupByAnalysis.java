package model.process.analysis;


import model.data.DataTable;
import model.data.DataTableBuilder;
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
	private LinkedHashMap<? extends DataValue, ConstraintAnalysis> constraints;
	private DataTableBuilder builder;
	private String name;
	private List<Function> functions;
	private List<String> columnNames;

	protected GroupByAnalysis(String name, List<Function> functions, List<String> columnNames) {
		this.name = name;
		this.functions = functions;
		this.columnNames = columnNames;
	}

	/**
	 * Set the constraints for the chunks.
	 * @param constraints a linkedHashMap that contains all the constraints for the chunks.
	 */
	public void setConstraints(LinkedHashMap<? extends DataValue, ConstraintAnalysis> constraints) {
		this.constraints = constraints;
	}

	/**
	 * Add a chunk column to the builder.
	 */
	protected void addChunkColumn() {
		if (constraints.size() == 0) {
			builder.createColumn("Chunk", StringValue.class);
		} else {
			builder.createColumn("Chunk",
					(Class<? extends DataValue>) constraints.keySet().toArray()[0].getClass());
		}
	}

	/**
	 * Add the columns for the function results to the builder.
	 */
	protected void addFunctionColumns() {
		for (int i = 0; i < functions.size(); i++) {
			try {
				builder.createColumn(columnNames.get(i),
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
	 * Create a DataTable builder for the return table.
	 */
	protected void constructBuilder() {
		
		builder = new DataTableBuilder();
		builder.setName(name);
		if (functions.size() != columnNames.size()) {
			throw new IllegalArgumentException(
					"number of columns does not correspond to the number of functions.");
		}

		addChunkColumn();
		addFunctionColumns();

	}

	/**
	 * Perform the specified functions on the chunks in the input table.
	 * @param input the table to perform the groupBy on
	 * @return a table that contains, for each chunk, the results of the functions.
	 */
	protected DataTable groupBy(DataTable input) {
		constructBuilder();
		for (Map.Entry<? extends DataValue, ConstraintAnalysis> entry : constraints.entrySet()) {
			DataTable chunk = input.copy();
			chunk = (DataTable) entry.getValue().analyse(chunk);

			DataValue[] values = new DataValue[functions.size() + 1];
			values[0] = entry.getKey();

			int i = 1;
			for (Function function : functions) {
				function.setTable(chunk);
				values[i] = function.calculate();
				i++;
			}

			builder.createRow(values);
		}
		DataTable res = builder.build();
		getDataModel().add(res);
		return res;

	}
}
