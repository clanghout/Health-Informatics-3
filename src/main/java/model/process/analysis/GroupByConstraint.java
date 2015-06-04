package model.process.analysis;

import model.data.CombinedDataTable;
import model.data.DataTable;
import model.data.Table;
import model.process.functions.Function;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Specify the chunks as constraints and perform a groupBy on them.
 *
 * Created by jens on 6/3/15.
 */
public class GroupByConstraint extends GroupByAnalysis {

	/**
	 * Create a group by. The chunks are specified by constraints and the columns by functions.
	 * @param name name of the new table
	 * @param constrainList list of constrainst for the chunks
	 * @param groupNames name for the chunks
	 * @param functions functions for the columns
	 * @param columnNames name for the columns
	 */
	public GroupByConstraint(
			String name,
			List<ConstraintAnalysis> constrainList,
			List<String> groupNames,
			List<Function> functions,
			List<String> columnNames) {

		constructConstraintList(groupNames, constrainList);
		constructBuilder(name, functions, columnNames);
	}

	/**
	 * Construct the list of constraints for the chunks.
	 * @param groupNames name of the chunks
	 * @param constrainList constraints that specifies the chunks
	 */
	private void constructConstraintList(
			List<String> groupNames,
			List<ConstraintAnalysis> constrainList) {

		if (constrainList.size() != groupNames.size()) {
			throw new IllegalArgumentException(
					"number of groups does not correspond to the number of group names.");
		}

		LinkedHashMap<String, ConstraintAnalysis> constraints = new LinkedHashMap<>();
		for (int i = 0; i < constrainList.size(); i++) {
			constraints.put(groupNames.get(i), constrainList.get(i));
		}
		setConstraints(constraints);
	}

	@Override
	public Table analyse(Table input) {
		if (!(input instanceof DataTable)) {
			throw new IllegalArgumentException("group by work only on datatable");
		}
		return groupBy((DataTable) input);
	}
}
