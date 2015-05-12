package model.data.process.functions;

import java.util.ArrayList;
import java.util.List;

import model.data.DataModel;
import model.data.DataRow;
import model.data.value.FloatValue;
import model.data.value.IntValue;

/**
 * A class for finding the row with the minimum value for the specified column in a model
 * @author Louis Gosschalk 
 * @date 11-05-2015
 */
public class Maximum {
	
	private DataModel model;
	private String name;
	private List<DataRow> rowlist;
	private DataRow row;
	
	public Maximum(DataModel model, String columnName) {
		this.model = model;
		this.name = columnName;
		this.rowlist = new ArrayList<>();
		this.row = null;
	}
	/**
	 * @param model datamodel containing the input
	 * @param columnName string specifying the 
	 * @return List of DataRow; because duplicate minimums are possible.
	 */
	public List<DataRow> maximumCheck() {
		if(model.getRowCount() != 0)
			row = model.getRow(0);
		else
			return rowlist; 

		if(model.getRowCount() == 1) {
			rowlist.add(row);
			return rowlist;
		}
		/**
		 * Check type of specified column
		 */
		if(model.getColumns().get(name).getType().equals(FloatValue.class))
			 compare();
		else if(model.getColumns().get(name).getType().equals(IntValue.class))
			 compare();
		else rowlist.clear();
		
		return rowlist;
	}
	/**
	 * Get values and cast to Float, determine maximum values
	 * @return List of datarows which contain the maximum values of the column
	 */
	public List<DataRow> compare() {
		for(int i = 0; i<model.getRowCount(); i++){
			float currentVal = (float) row.getValue(name).getValue();
			DataRow compare = model.getRow(i);
			float compareVal = (float) compare.getValue(name).getValue();
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
