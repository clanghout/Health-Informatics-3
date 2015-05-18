package model.data.process.functions;

import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.NumberValue;

/**
 * A class for finding the row with the minimum value for the specified column in a model
 * @author Louis Gosschalk 
 * @date 11-05-2015
 */
public class Minimum extends RowFunction {
	
	public Minimum(DataTable model, DataDescriber<NumberValue> argument) {
		super(model, argument);
	}

	public Boolean check(int comparison) {
		if(comparison > 0)
			return true;
		return false;
	}
}
