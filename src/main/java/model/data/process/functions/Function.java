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
	
	private DataTable model;
	private DataDescriber<NumberValue> argument;
	private List<DataRow> rowlist;
	private DataRow row;
	
	public Function(DataTable model, DataDescriber<NumberValue> argument) {
		this.model = model;
		this.argument = argument;
		this.rowlist = new ArrayList<DataRow>();
		this.row = null;
	}
	
	public List<DataRow> calculate() {
		if(model.getRowCount() == 0)
			return rowlist; 

		if(model.getRowCount() == 1) {
			rowlist.add(model.getRow(0));
			return rowlist;
		}
		row = model.getRow(0);
		/**
		 * Check type of specified column
		 * if it's a string column, getting its class throws an exception
		 * for some reason...
		 */
		try {
			argument.resolve(row).getClass();
		} catch (Exception e) {
			return rowlist;
		}
		if(argument.resolve(row).getClass() == (FloatValue.class)){
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
	
	protected abstract List<DataRow> floatCompare();
	
	protected abstract List<DataRow> intCompare();
}
