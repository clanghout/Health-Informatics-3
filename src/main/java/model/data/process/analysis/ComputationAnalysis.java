package model.data.process.analysis;

import model.data.DataModel;
import model.data.DataRow;
import model.data.process.analysis.computations.Computation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 12-5-2015.
 */
public class ComputationAnalysis extends DataAnalysis {

	private final Computation computation;

	public ComputationAnalysis(Computation computation) {
		this.computation = computation;
	}

	@Override
	public DataModel analyse(DataModel input) {
		List<DataRow> out = new ArrayList<>();
		List<DataRow> rows = input.getRows();
		for (DataRow row: rows) {
//          TODO: Doe dingen
		}
		return new DataModel(out, new ArrayList<>(input.getColumns().values()));
	}
}
