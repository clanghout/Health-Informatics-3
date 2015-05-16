package model.data.process.functions;

import exceptions.FunctionInputMismatchException;
import model.data.DataRow;
import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.NumberValue;

/**
 * This class will provide a framework for functions resulting single data values
 * @author louisgosschalk
 *13-05-2015
 */
public abstract class ValueFunction extends Function {
	protected DataRow row;
	private DataTable table;
	private DataDescriber<NumberValue> argument;
	
	public ValueFunction(DataTable model, DataDescriber<NumberValue> argument) {
		super(model,argument);
		this.table = model;
		this.argument = argument;
	}
	/**
	 * This abstract function will execute various arithmetic calculations
	 * @return DataValue
	 */
	public abstract DataValue calculate();
	
	/**
	 * initialize class checks if specified column is eligible
	 * @return DataValue because it calls calculate
	 */
	public DataValue initialize() {
		if(table.getRowCount() == 0)
			throw new FunctionInputMismatchException("Calculation of nothing does not exist"); 
		
		row = table.getRow(0);
		try {
			argument.resolve(row).getClass();
		} catch (Exception e) {
			throw new FunctionInputMismatchException("Cannot resolve class of column");
		}
		
		if(argument.resolve(row).getClass().equals(FloatValue.class) || argument.resolve(row).getClass().equals(IntValue.class)) {
			return calculate();
		}
		else 
			throw new FunctionInputMismatchException("Specified column is neither float nor int");
	}
	
}
