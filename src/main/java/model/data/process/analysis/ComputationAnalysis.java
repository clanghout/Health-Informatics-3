package model.data.process.analysis;

import model.data.DataRow;
import model.data.DataTable;
import model.data.process.analysis.operations.computations.Computation;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the analysis for computations.
 */
public class ComputationAnalysis extends DataAnalysis {

	private final Computation computation;

	public ComputationAnalysis(Computation computation) {
		this.computation = computation;
	}

	@Override
	public DataTable analyse(DataTable input) {
		List<DataRow> out = new ArrayList<>();
		List<DataRow> rows = input.getRows();
// TODO: Add code which specifies what analysis should be computed on which data.
//		for (DataRow row: rows) {
//		}
		return new DataTable(out, new ArrayList<>(input.getColumns().values()));
	}
}
