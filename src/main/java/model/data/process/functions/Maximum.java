package model.data.process.functions;

import java.util.ArrayList;
import java.util.List;

import model.data.DataRow;
import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.value.NumberValue;

/**
 * A class for finding the row with the maximum value for the specified column in a model
 * @author Louis Gosschalk 
 * @date 11-05-2015
 */
public class Maximum extends Function{
	
	private DataTable model;
	private DataDescriber<NumberValue> argument;
	private ArrayList<DataRow> rowlist;
	private DataRow row;
	
	public Maximum(DataTable model, DataDescriber<NumberValue> argument) {
		super(model, argument);
		this.model = model;
		this.argument = argument;
		this.row = model.getRow(0);
		this.rowlist = new ArrayList<DataRow>();
		maximum = true;
	}
}
