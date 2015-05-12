package model.data.process.analysis.computations;

import model.data.DataRow;
import model.data.describer.DataDescriber;
import model.data.value.NumberValue;

/**
 * Created by Chris on 12-5-2015.
 */
public class Power extends Computation {

	public Power(DataDescriber leftSide, DataDescriber rightSide) {
		super(leftSide, rightSide);
	}

	@Override
	public NumberValue compute(DataRow row) {
		return null;
	}
}
