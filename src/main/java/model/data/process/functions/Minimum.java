package model.data.process.functions;

import java.util.ArrayList;
import model.data.DataModel;
import model.data.DataValue;
import model.data.DataRow;

/**
 * A class for finding the row with the minimum value for the specified column in a model
 * @author Louis Gosschalk 
 * @date 11-05-2015
 */
public class Minimum {
	
	/**
	 * 
	 * @param model datamodel containing the input
	 * @param columnName string specifying the 
	 * @return ArrayList of DataRow, in case there's duplicate minimums
	 */
	public ArrayList<DataRow> MinimumCheck(DataModel model, String columnName) {
		//return new DataRow();
		ArrayList<DataRow> rowlist = null;
		DataRow row = null;
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
			 * check if specified column is int, else throw exception
			 */
			int currentInt = (int) row.getValue(columnName).getValue();
			DataRow compare = model.getRow(i);
			int compareInt = (int) compare.getValue(columnName).getValue();
			if(currentInt > compareInt){
				row = compare;
				rowlist.clear();
				rowlist.add(compare);
			}
			else if(currentInt == compareInt)
				rowlist.add(compare);
		}
		return rowlist;
	}
}
