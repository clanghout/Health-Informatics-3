package controllers.visualizations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.Chart;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import model.data.DataColumn;
import model.data.DataTable;

/**
 * Created by Chris on 4-6-2015.
 */
public abstract class ChartController {

	public abstract void initialize();
	public abstract boolean axesSet();
	public abstract Chart create();
	/**
	 * Set the items of a comboBox to the columns of the dataTable.
	 *
	 * @param inputBox  the comboBox that specifies the axis of the graph
	 * @param dataTable the dataTable used for the graph
	 */
	public void setColumnDropDown(ComboBox<DataColumn> inputBox, DataTable dataTable) {
		inputBox.setDisable(false);
		ObservableList<DataColumn> columns =
				FXCollections.observableArrayList(dataTable.getColumns());
		inputBox.setItems(columns);
		inputBox.setConverter(new StringConverter<DataColumn>() {
			@Override
			public String toString(DataColumn object) {
				return object.getName();
			}

			@Override
			public DataColumn fromString(String string) {
				return dataTable.getColumn(string);
			}
		});
	}
}
