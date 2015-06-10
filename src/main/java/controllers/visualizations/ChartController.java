package controllers.visualizations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import model.data.DataColumn;
import model.data.DataTable;

/**
 * Abstract ChartController specifying the controller for a javaFX chart.
 * Created by Chris on 4-6-2015.
 */
public abstract class ChartController {
	public static final int YAXIS_SEPARATION = 5;
	public static final int SIZE = 420;

	/**
	 * Creation of the controller.
	 */
	public abstract void initialize();

	/**
	 * Checks if all data Axes are set and contain valid information.
	 * Used to check before building the actual graph.
	 *
	 * @return true if all axes are well defined.
	 */
	public abstract boolean axesSet();

	/**
	 * Create WritableImage to draw.
	 */
	public abstract WritableImage createImage();

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

	/**
	 * Set the message to an error label.
	 *
	 * @param label   the label wich will show the error.
	 * @param message the message for in the label.
	 */
	public void setErrorLabel(Label label, String message) {
		label.setTextFill(Color.RED);
		label.setText(message);
	}

	/**
	 * Compute approximately a tenth of the range of the axis.
	 *
	 * @param max the maximum value of the axis.
	 * @param min the minimum value of the axis.
	 * @return the computed seperator value as int.
	 */
	public int computeSeparatorValue(float max, float min) {
		return Math.round((max - min) / YAXIS_SEPARATION);
	}
}
