package model.data.process.functions;

import java.util.ArrayList;
import model.data.DataModel;
import model.data.DataRow;

/**
 * A class for finding the row with the minimum value for the specified column in a model
 * @author Louis Gosschalk 
 * @date 11-05-2015
 */
public class Maximum {
	
	private DataModel model;
	private String name;
	private ArrayList<DataRow> rowlist;
	private DataRow row;
	
	public Maximum(DataModel model, String columnName) {
		this.model = model;
		this.name = columnName;
		this.rowlist = null;
		this.row = null;
	}
	/**
	 * @param model datamodel containing the input
	 * @param columnName string specifying the 
	 * @return ArrayList of DataRow; because duplicate minimums are possible.
	 */
	public ArrayList<DataRow> MaximumCheck() {
		//return new DataRow();
//		ArrayList<DataRow> rowlist = null;
//		DataRow row = null;
		if(model.getRowCount() != 0)
			row = model.getRow(0);
		else
			return rowlist; 
			/**
			 * return null or throw exception if input is null?
			 */
		
		//current row is the only row
		if(model.getRowCount() == 1) {
			rowlist.add(row);
			return rowlist;
		}
		//loop through datamodel rows, begin at 1 because 0 is the comparison
		for(int i=1; i< model.getRowCount(); i++){
			/**
			 * check if specified column is int or float, else throw exception
			 */ 
			String klasse = CheckClass(row.getValue(name).getValue());
			Compare(i);
			else if(klass == float) FloatCompare(i)
			
//			if(row.getValue(name).getValue().getClass().isInstance(int.class))
		}
		return rowlist;
	}
	public ArrayList<DataRow> Compare(int i) {
		int currentInt = (int) row.getValue(name).getValue();
		DataRow compare = model.getRow(i);
		int compareInt = (int) compare.getValue(name).getValue();
		if(currentInt > compareInt){
			row = compare;
			rowlist.clear();
			rowlist.add(compare);
			return rowlist;
		}
		else if(currentInt == compareInt){
			rowlist.add(compare);
			return rowlist;
		}
		return rowlist;
	}
}
