package model.data.process.analysis.operations.comparisons;

import model.data.process.analysis.ComparisonAnalysis;

/**
 * This class will initialize events and call its comparison.
 * 
 * @author Louis Gosschalk 21-05-2015
 */
public class Comparison extends ComparisonAnalysis {
	
	private Comparison compare;
	
	public Comparison(Comparison comparison) {
		super(comparison);
		this.compare = comparison;
		// TODO Auto-generated constructor stub
	}

}
