package model.process.analysis;

import model.data.Table;
import model.process.analysis.operations.comparisons.Comparison;

/**
 * Implements comparison analysis.
 * 
 * @author Louis Gosschalk 26-05-2015
 */
public class ComparisonAnalysis extends DataAnalysis {
	
	private final Comparison comparison;
	
	public ComparisonAnalysis(Comparison comparison) {
		this.comparison = comparison;
	}

	@Override
	public Table analyse(Table input) {
		// TODO Auto-generated method stub
		return null;
	} 
	
}
