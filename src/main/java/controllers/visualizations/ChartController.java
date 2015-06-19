package controllers.visualizations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import model.data.DataColumn;
import model.data.DataTable;
import model.data.describer.DataDescriber;
import model.data.describer.RowValueDescriber;
import model.data.value.DataValue;
import model.data.value.NumberValue;
import model.exceptions.InputMismatchException;
import model.process.functions.Maximum;
import model.process.functions.Minimum;
import javafx.scene.paint.Color;
import model.data.DataColumn;
import model.data.DataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Abstract ChartController specifying the controller for a javaFX chart.
 * Created by Chris on 4-6-2015.
 */
public abstract class ChartController extends GraphImageController {
	private DataColumn yCol;

	public static final int YAXIS_SEPARATION = 5;
	public static final int SIZE = 420;

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


	public void setColumnDropDown(ComboBox<ColumnWrapper> inputBox, DataTable dataTable) {
		inputBox.setDisable(false);
		inputBox.setItems(wrapColumns(dataTable.getColumns()));
	}

	public void setYAxisEventListener(ComboBox<ColumnWrapper> yAxisBox, DataTable table, Label yAxisErrorLabel) {
		yAxisBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			yCol = newValue.getColumn();
			DataDescriber<DataValue<?>> yColDescriber = new RowValueDescriber<>(yCol);
			try {
				NumberValue maxValue = (NumberValue) new Maximum(table, yColDescriber).calculate();
				NumberValue minValue = (NumberValue) new Minimum(table, yColDescriber).calculate();
				float max = Float.valueOf(maxValue.getValue().toString());
				float min = Float.valueOf(minValue.getValue().toString()) - 1;
				int sep = computeSeparatorValue(max, min);
				createYaxis(min, max, sep);
			} catch (ClassCastException e) {
				setErrorLabel(yAxisErrorLabel, "Something went wrong with the numbers.");
			} catch (InputMismatchException e) {
				setErrorLabel(yAxisErrorLabel, "Please select a column containing just numbers.");
			}
		});
	}

	/**
	 * create the Y axis that sets the class variable.
	 * void because bar and box use different axis.
	 */
	public abstract void createYaxis(float min, float max, int sep);

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

	protected ObservableList<ColumnWrapper> wrapColumns(List<DataColumn> columns) {
		return FXCollections.observableArrayList(new ArrayList<>(
				columns.stream().map(ColumnWrapper::new).collect(Collectors.toList())
		));
	}

	/**
	 * The class is a simple wrapper for the DataColumn.
	 */
	protected final class ColumnWrapper {
		private DataColumn column;

		private ColumnWrapper(DataColumn column) {
			this.column = column;
		}

		@Override
		public String toString() {
			return column.getName();
		}

		protected DataColumn getColumn() {
			return column;
		}
	}

	public DataColumn getyCol() {
		return yCol;
	}
}
