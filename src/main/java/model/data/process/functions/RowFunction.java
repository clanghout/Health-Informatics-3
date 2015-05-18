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

/**
 * This class will provide a framework for functions resulting multiple rows of data
 * @author louisgosschalk
 *13-05-2015
 */
public abstract class RowFunction extends Function{
	private DataTable table;
	private DataDescriber<NumberValue> argument;
	private List<DataRow> rowlist;
	private DataRow row;
	
	public RowFunction(DataTable model, DataDescriber<NumberValue> argument) {
		super(model, argument);
		this.table = model;
		this.argument = argument;
		this.rowlist = new ArrayList<DataRow>();
		this.row = null;
	}
	/**
	 * this function checks restrictions for the calculation and calls its execution
	 * @return List<DataRow> the rows containing the minimum
	 */
	public List<DataRow> calculate() throws Exception {
		if(table.getRowCount() == 0)
			return rowlist; 
			
		row = table.getRow(0);
		rowlist.add(row);
		
		try {
			argument.resolve(row).getClass();
		} catch (Exception e) {
			throw new FunctionInputMismatchException("Input is not int or float");
		}
		Class<? extends DataValue> klasse = argument.resolve(row).getClass();
		if(klasse.equals(FloatValue.class) || klasse.equals(IntValue.class)) {
			return compare();
		}
		else 
			throw new FunctionInputMismatchException("Input is not int or float");
	}
	
    public List<DataRow> compare() {
		for(int i = 1; i<table.getRowCount(); i++){
			Comparable currentVal = (Comparable) argument.resolve(row).getValue();
		    DataRow compare = table.getRow(i);
		    Comparable compareVal = (Comparable) argument.resolve(compare).getValue();
		    // if there's a new minimum or there's a new maximum
		    int comparison = currentVal.compareTo(compareVal);
		    if(check(comparison)){
		        row = compare;
		        rowlist.clear();
		        rowlist.add(compare);
		    }
		    // if there's a duplicate minimum/maximum
		    else if(comparison == 0)
		        rowlist.add(compare);
		}
		return rowlist;
	}
    public abstract Boolean check(int comparison);
}
