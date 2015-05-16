package model.data.process.functions;

import model.data.DataRow;
import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.DataValue;
import model.data.value.FloatValue;
import model.data.value.NumberValue;

public class Average extends ValueFunction {
	
	private DataTable table;
	private DataDescriber<NumberValue> argument;
	private DataRow row;
	
	public Average(DataTable model, DataDescriber<NumberValue> argument) {
		super(model, argument);
		this.table = model;
		this.argument = argument;
	}

	@Override
	public DataValue calculate() {
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
