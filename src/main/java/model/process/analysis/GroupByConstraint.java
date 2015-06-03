package model.process.analysis;

import model.data.Table;
import model.process.functions.Function;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jens on 6/3/15.
 */
public class GroupByConstraint extends GroupByAnalysis{

	public GroupByConstraint(String name, List<ConstraintAnalysis> constrainList, List<String> groupNames,
						   List<Function> functions, List<String> columnNames) {

		constructConstraintList(groupNames, constrainList);
		constructBuilder(name, functions, columnNames);
	}

	@Override
	public Table analyse(Table input) {
		return groupBy(input);
	}
}
