package model.data.process.functions;

import model.data.DataModel;
import model.data.DataRow;
import model.data.DataValue;

/**
 * A class for finding the row with the maximum value for the specified column in a model
 * @author Louis Gosschalk 
 * @date 11-05-2015
 */
public class Maximum {
	public DataRow MaximumCheck(DataModel model, String columnName) {
		//return new DataRow();
		DataRow row = null;
		if(model.getRowCount() != 0)
			row = model.getRow(0);
		else
			return row; //throw exception "empty datamodel"
		
		//current row is the only row
		if(model.getRowCount() == 1) {
			return row;
		}
		//loop through datamodel rows, begin at index 1 because index 0 is the comparison
		for(int i=1; i< model.getRowCount(); i++){
			DataValue current = row.getValue(columnName);
			//getvalue int van current
			//int currentInt = current.getValue();
			DataValue compare = model.getRow(i).getValue(columnName);
			//getvalue int van compare
			//int compareInt = compare.getValue();
			//if(currentInt < compareInt)
				//row = compare;
		}
		return row;
	}
}
