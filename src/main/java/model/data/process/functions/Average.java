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
	
	public Average(DataTable model, DataDescriber<NumberValue> argument) {
		super(model, argument);
		this.table = model;
		this.argument = argument;
	}

	@Override
	public DataValue calculate() {
		initialize();
		float value = 0f;
		DataValue sum = new Sum(table, argument).calculate();
		float total = (float) sum.getValue();
		total = total/table.getRowCount();
		DataValue result = new FloatValue(total);
		return result;
	}
}
