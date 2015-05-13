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
	}
	
	public List<DataRow> floatCompare() {
		for(int i = 0; i<model.getRowCount(); i++){
			float currentVal = (float) argument.resolve(row).getValue();
			DataRow compare = model.getRow(i);
			float compareVal = (float) argument.resolve(compare).getValue();
			if(currentVal < compareVal){
				row = compare;
				rowlist.clear();
				rowlist.add(compare);
			}
			else if(currentVal == compareVal)
				rowlist.add(compare);
		}
		return rowlist;
	}
	
	/**
	 * Get values and cast to int, determine maximum values
	 * @return List of datarows which contain the maximum values of the column
	 */
	public List<DataRow> intCompare() {
		for(int i = 0; i<model.getRowCount(); i++){
			int currentVal = (int) argument.resolve(row).getValue();
			DataRow compare = model.getRow(i);
			int compareVal = (int) argument.resolve(compare).getValue();
			if(currentVal < compareVal){
				row = compare;
				rowlist.clear();
				rowlist.add(compare);
			}
			else if(currentVal == compareVal){
				rowlist.add(compare);
			}
		}
		return rowlist;
	}
}
