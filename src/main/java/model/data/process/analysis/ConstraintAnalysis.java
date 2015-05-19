package model.data.process.analysis;

import model.data.DataTable;
import model.data.DataRow;
import model.data.process.analysis.constraints.Constraint;

import java.util.ArrayList;
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
	public DataTable analyse(DataTable input) {
		List<DataRow> out = new ArrayList<>();
		List<DataRow> rows = input.getRows();
		for (DataRow row: rows) {
			if (constraint.check(row)) {
				out.add(row);
			}
		}
		//TODO some solution for the name
		return new DataTable(input.getName(), out, new ArrayList<>(input.getColumns().values()));
	}
}
