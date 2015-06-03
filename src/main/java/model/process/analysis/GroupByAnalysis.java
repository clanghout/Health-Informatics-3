package model.process.analysis;


import model.data.CombinedDataTable;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.Table;
import model.data.value.DataValue;
import model.data.value.StringValue;
import model.process.functions.Function;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jens on 6/3/15.
 */
public class GroupByAnalysis extends DataAnalysis {
	private Map<String, ConstraintAnalysis> constraints;
	private DataTableBuilder builder;
	private List<Function> functionsList;

	public GroupByAnalysis(String name, List<ConstraintAnalysis> constrainList, List<String> groupNames,
						   List<Function> functions, List<String> columnNames) {
		if (constrainList.size() != groupNames.size()) {
			throw new IllegalArgumentException("number of groups does not correspond "
					+ "to the number of group names.");
		}

		constraints = new HashMap<>();
		for(int i = 0; i < constrainList.size(); i++) {
			constraints.put(groupNames.get(i), constrainList.get(i));
		}

		constructBuilder(name, functions, columnNames);
	}

	protected void constructBuilder(String name, List<Function> functions, List<String> columnsNames){
		builder = new DataTableBuilder();
		builder.setName(name);
		if (functions.size() != columnsNames.size()) {
			throw new IllegalArgumentException("number of columns does not correspond "
					+ "to the number of functions.");
		}

		this.functionsList = functions;

		for(int i = 0; i < functions.size(); i++) {
			try {
				builder.createColumn(columnsNames.get(i), (Class<? extends DataValue>) functions.get(i).getClass().getMethod("calculate").getReturnType());
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("function calculate does not exist.")
			}
		}

	}

	@Override
	public DataTable analyse(Table input) {
		if (input instanceof CombinedDataTable) {
			throw new IllegalArgumentException("group by work only on datatable");
		}
		for (Map.Entry<String, ConstraintAnalysis> entry : constraints.entrySet()) {
			DataTable chunk = (DataTable) input.copy();
			chunk = (DataTable) entry.getValue().analyse(chunk);

			DataValue[] values = new DataValue[functionsList.size() + 1];
			values[0] = new StringValue(entry.getKey());

			int i = 1;
			for(Function function : functionsList) {
				function.setTable(chunk);
				values[i] = function.calculate();
			}

			builder.createRow(values);

			return builder.build();

		}


	}
}
