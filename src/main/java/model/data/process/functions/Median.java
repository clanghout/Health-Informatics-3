package model.data.process.functions;

import java.util.ArrayList;
import java.util.Collections;

import model.data.DataRow;
import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.FloatValue;
import model.data.value.NumberValue;

public class Median extends ValueFunction {
	
	private DataTable table;
	private DataDescriber<NumberValue> argument;
	private DataRow row;
	
	public Median(DataTable model, DataDescriber<NumberValue> argument) {
		super(model, argument);
		this.table = model;
		this.argument = argument;
	}

	@Override
	public FloatValue calculate() {
		initialize();
		ArrayList<Float> list = new ArrayList<Float>();
		//rows ordenen klein naar groot
		for(int i=0; i<table.getRowCount(); i++){
			row = table.getRow(i);
			if(argument.resolve(row).getClass().equals(FloatValue.class))
				list.add((float) argument.resolve(row).getValue());
			else
				list.add((float) 1.0 * (int) argument.resolve(row).getValue());
		}
		Collections.sort(list);
		//aantal rows = even -> middelste 2 /2 = median
		if((table.getRowCount() & 1) == 0) {//even
			int middle = table.getRowCount()/2;
			int middle2 = middle;
			float median = list.get(middle-1);
			float median2 = list.get(middle2);
			median = (median2+median)/2;
			return new FloatValue(median);
		//aantal rows = oneven -> middelste row = median
		} else { //odd
			int middle = (int) Math.ceil(table.getRowCount()/2.0);
			return new FloatValue(list.get(middle-1));
		}
	}
}
