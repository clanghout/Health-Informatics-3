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
 * This class will provide a framework for functions resulting multiple rows of data
 * @author louisgosschalk
 *13-05-2015
 */
public class RowFunction extends Function{
	protected boolean minimum;
	protected boolean maximum;
	private DataTable table;
	private DataDescriber<NumberValue> argument;
	private List<DataRow> rowlist;
	private DataRow row;
	
	public RowFunction(DataTable model, DataDescriber<NumberValue> argument) {
		super(model, argument);
		this.table = model;
		this.argument = argument;
		this.minimum = false;
		this.maximum = false;
		this.rowlist = new ArrayList<DataRow>();
		this.row = null;
	}
	/**
	 * this function checks restrictions for the calculation and calls its execution
	 * @return List<DataRow> the rows containing the minimum
	 */
	public List<DataRow> calculate() {
		if(table.getRowCount() == 0)
			return rowlist; 
			
		if(table.getRowCount() == 1) {
			rowlist.add(table.getRow(0));
			return rowlist;
		}
		row = table.getRow(0);
		try {
			argument.resolve(row).getClass();
		} catch (Exception e) {
			return rowlist;
		}
			if(argument.resolve(row).getClass().equals(FloatValue.class)){
			return floatCompare();
		}
		else if(argument.resolve(row).getClass().equals(IntValue.class)) {
			return intCompare();
		}
		else {
			rowlist.clear();
			return rowlist;
		}
	}
	public List<DataRow> floatCompare() {
		for(int i = 0; i<table.getRowCount(); i++){
			float currentVal = (float) argument.resolve(row).getValue();
			DataRow compare = table.getRow(i);
			float compareVal = (float) argument.resolve(compare).getValue();
			// if there's a new minimum or there's a new maximum
			if((currentVal > compareVal && minimum) || (currentVal < compareVal && maximum)){
				row = compare;
				rowlist.clear();
				rowlist.add(compare);
			}
			// if there's a duplicate minimum/maximum
			else if(currentVal == compareVal)
				rowlist.add(compare);
		}
		return rowlist;
	}
	public List<DataRow> intCompare() {
		for(int i = 0; i<table.getRowCount(); i++){
			int currentVal = (int) argument.resolve(row).getValue();
			DataRow compare = table.getRow(i);
			int compareVal = (int) argument.resolve(compare).getValue();
			// new minimum or new maximum
			if((currentVal > compareVal && minimum) || (currentVal < compareVal && maximum)){
				row = compare;
				rowlist.clear();
				rowlist.add(compare);
			}
			// duplicate minimum/maximum
			else if(currentVal == compareVal)
				rowlist.add(compare);
		}
		return rowlist;
	}
}
