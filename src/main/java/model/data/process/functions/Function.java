package model.data.process.functions;

import java.util.ArrayList;
import java.util.List;

import model.data.DataRow;
import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.NumberValue;

/**
 * A class for finding the row with the minimum or maximum value for the specified column in a model
 * @author Louis Gosschalk 
 * @date 11-05-2015
 */
public abstract class Function {
	
	public Function(DataTable model, DataDescriber<NumberValue> argument){}
	
//	public abstract List<DataRow> calculate();
}
