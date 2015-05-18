package model.data.process.functions;

import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.NumberValue;

/**
 * A class for specifying the input of functions
 * @author Louis Gosschalk 
 * @date 11-05-2015
 */
public abstract class Function {
	
	public Function(DataTable model, DataDescriber<NumberValue> argument){}
	
}
