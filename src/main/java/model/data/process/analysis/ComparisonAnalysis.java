package model.data.process.analysis;

import model.data.Table;
import model.data.process.analysis.operations.comparisons.Comparison;

public class ComparisonAnalysis extends DataAnalysis {
	
	private final Comparison compare;
	
	public ComparisonAnalysis(Comparison compare) {
		this.compare = compare;
	}

	@Override
	public Table analyse(Table input) {
		// TODO Auto-generated method stub
		return null;
	} 
	
}
