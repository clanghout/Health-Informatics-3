package model.data.process.functions;

import java.util.ArrayList;
import java.util.List;

import exceptions.FunctionInputMismatchException;
import model.data.DataRow;
import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import model.data.value.IntValue;
import model.data.value.NumberValue;

public class Average extends ValueFunction {
	
	private DataTable table;
	private DataDescriber<NumberValue> argument;
	private DataRow row;
	
	public Average(DataTable model, DataDescriber<NumberValue> argument) {
		super(model, argument);
		this.table = model;
		this.argument = argument;
		this.row = null;
	}

	@Override
	public DataValue calculate() {
		if(table.getRowCount() == 0)
			throw new FunctionInputMismatchException("Average of nothing does not exist"); 
		
		row = table.getRow(0);
		try {
			argument.resolve(row).getClass();
		} catch (Exception e) {
			throw new FunctionInputMismatchException("Cannot resolve class of column");
		}
		
		if(argument.resolve(row).getClass().equals(FloatValue.class) || argument.resolve(row).getClass().equals(IntValue.class)) {
			return avrg();
		}
		else 
			throw new FunctionInputMismatchException("Specified column is neither float nor int");
	}
	public DataValue avrg() {
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
		total = total/table.getRowCount();
		DataValue result = new FloatValue(total);
		return result;
	}
}
