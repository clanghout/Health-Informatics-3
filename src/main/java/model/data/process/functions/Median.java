package model.data.process.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.FloatValue;
import model.data.value.NumberValue;

/**
 * This class calculates the median of a column.
 * @author louisgosschalk
 *16-05-2015
 */
public class Median extends Function {
	
	private DataTable table;
	private DataDescriber<NumberValue> argument;
	
	public Median(DataTable model, DataDescriber<NumberValue> argument) {
		super(model, argument);
		this.table = model;
		this.argument = argument;
	}
	
	/**
	 * This function calculates the median.
	 */
	@Override
	public FloatValue calculate() {
		initialize();
		List<Float> list = createList();
		//Amount of rows is even -> add two middle rows and divide by 2
		if ((table.getRowCount() & 1) == 0) {
			int middle = table.getRowCount() / 2;
			float median = list.get(middle - 1);
			float median2 = list.get(middle);
			median = (median2 + median) / 2;
			return new FloatValue(median);
		//Amount of rows is odd -> median is middle row
		} else { 
			int middle = (int) Math.ceil(table.getRowCount() / 2.0);
			return new FloatValue(list.get(middle - 1));
		}
	}
	/**
	 * This function takes all values from the specified column and puts it in an ascending list.
	 * @return List<Float> ascending list of floats 
	 */
	public List<Float> createList() {
		List<Float> list = new ArrayList<Float>();
		for (int i = 0; i < table.getRowCount(); i++) {
			list.add(intOrFloat(argument, table.getRow(i)));
		}
		Collections.sort(list);
		return list;
	}
}
