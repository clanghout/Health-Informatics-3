package model.data.process.analysis;

import model.data.DataRow;
import model.data.DataTable;
import model.data.Row;
import model.data.Table;
import model.data.process.analysis.operations.constraints.Constraint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implements the constraints analysis.
 *
 * Created by Boudewijn on 5-5-2015.
 */
public class ConstraintAnalysis extends DataAnalysis {

	private final Constraint constraint;

	/**
	 * Construct a new ConstraintsAnalysis.
	 * @param constraint The constraint you want to use for this analysis.
	 */
	public ConstraintAnalysis(Constraint constraint) {
		this.constraint = constraint;
	}

	@Override
	public Table analyse(Table input) {
		List<Row> out = new ArrayList<>();
		Iterator<? extends Row> rows = input.iterator();
		while (rows.hasNext()) {
			Row row = rows.next();
			if (constraint.check(row)) {
				out.add(row);
			}
		}
		//TODO some solution for the name
	return null;
	//	return new DataTable(input.getName(), out, new ArrayList<>(input.getColumns().values()));
	}
}
