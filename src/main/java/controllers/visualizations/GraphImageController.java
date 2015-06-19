package controllers.visualizations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import model.data.DataColumn;
import model.data.DataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An abstract class about the graphs as well as the matrix.
 * Created by Chris on 19-6-2015.
 */
public abstract class GraphImageController {


	public abstract void initialize();

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
	 * The class is a simple wrapper for the DataColumn.
	 */
	public final class ColumnWrapper {
		private DataColumn column;

		private ColumnWrapper(DataColumn column) {
			this.column = column;
		}

		@Override
		public String toString() {
			return column.getName();
		}

		public DataColumn getColumn() {
			return column;
		}
	}

	public void setColumnDropDown(ComboBox<ColumnWrapper> inputBox, DataTable dataTable) {
		inputBox.setDisable(false);
		inputBox.setItems(wrapColumns(dataTable.getColumns()));
	}

	protected ObservableList<ColumnWrapper> wrapColumns(List<DataColumn> columns) {
		return FXCollections.observableArrayList(new ArrayList<>(
				columns.stream().map(ColumnWrapper::new).collect(Collectors.toList())
		));
	}
}
