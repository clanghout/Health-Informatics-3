package model.data.process.analysis;

import model.data.DataModel;
import model.data.DataRow;
import model.data.process.analysis.constraints.Constraint;

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

	public ConstraintAnalysis(Constraint constraint) {
		this.constraint = constraint;
	}

	@Override
	public DataModel analyse(DataModel input) {
		List<DataRow> out = new ArrayList<>();
		Iterator<DataRow> iterator = input.getRows();
		while (iterator.hasNext()) {
			DataRow row = iterator.next();
			if (constraint.check(row)) {
				out.add(row);
			}
		}
		return new DataModel(out, new ArrayList<>(input.getColumns().values()));
	}
}
