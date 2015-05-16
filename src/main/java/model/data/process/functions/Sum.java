package model.data.process.functions;

import model.data.DataRow;
import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.DataValue;
import model.data.value.FloatValue;
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
	public FloatValue calculate() {
		initialize();
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
		FloatValue result = new FloatValue(total);
		return result;
	}
}
