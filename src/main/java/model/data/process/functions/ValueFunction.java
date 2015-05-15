package model.data.process.functions;

import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.DataValue;
import model.data.value.NumberValue;

/**
 * This class will provide a framework for functions resulting single data values
 * @author louisgosschalk
 *13-05-2015
 */
public abstract class ValueFunction extends Function {
	public ValueFunction(DataTable model, DataDescriber<NumberValue> argument) {
		super(model,argument);
	}
	/**
	 * This abstract function will execute various arithmetic calculations
	 * @return DataValue
	 */
	public abstract DataValue calculate();
	
}
