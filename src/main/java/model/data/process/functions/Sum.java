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
 * A class for calculating the sum of a specified column over all rows
 * @author louisgosschalk
 * 16-05-2015
 */
public class Sum extends ValueFunction {
	
	private DataTable table;
	private DataDescriber<NumberValue> argument;
	private DataRow row;
	
	public Sum(DataTable model, DataDescriber<NumberValue> argument) {
		super(model, argument);
		this.table = model;
		this.argument = argument;
	}

	@Override
	public DataValue calculate() {
		if(table.getRowCount() == 0)
			throw new FunctionInputMismatchException("Sum of nothing does not exist"); 
		
		row = table.getRow(0);
		try {
			argument.resolve(row).getClass();
		} catch (Exception e) {
			throw new FunctionInputMismatchException("Cannot resolve class of column");
		}
		
		if(argument.resolve(row).getClass().equals(FloatValue.class) || argument.resolve(row).getClass().equals(IntValue.class)) {
			return sum();
		}
		else 
			throw new FunctionInputMismatchException("Specified column is neither float nor int");
	}
	public DataValue sum() {
		float total = 0f;
		float value = 0f;
		for(int i = 0; i<table.getRowCount(); i++) {
			row = table.getRow(i);
			if(argument.resolve(row).getClass().equals(FloatValue.class))
				value = (float) argument.resolve(row).getValue();
			else
				value = (float) 1.0 * (int) argument.resolve(row).getValue();
			total += value;
		}
		DataValue result = new FloatValue(total);
		return result;
	}
}
