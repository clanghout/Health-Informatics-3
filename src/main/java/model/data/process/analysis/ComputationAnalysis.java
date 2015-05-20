package model.data.process.analysis;

import model.data.*;
import model.data.process.analysis.operations.computations.Computation;

import java.util.ArrayList;
import java.util.Iterator;
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
	public Table analyse(Table input) {
		List<? extends Row> out;
		if (input instanceof DataTable) {
			out = new ArrayList<DataRow>();
		} else {
			out = new ArrayList<CombinedDataRow>();
		}
		Iterator<? extends Row> rows = input.iterator();
// TODO: Add code which specifies what analysis should be computed on which data.
//		for (DataRow row: rows) {
//		}

		if (input instanceof DataTable) {
			DataTable dataInput = (DataTable) input;
			return new DataTable(dataInput.getName(), (ArrayList<DataRow>) out, new ArrayList<>(dataInput.getColumns().values()));
		} else {
			//TODO ...
			CombinedDataTable combInput = (CombinedDataTable) input;
			return null;
		}
	}
}
