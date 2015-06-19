package controllers.popup;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import model.data.DataModel;
import model.data.DataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract class for the popups in the visualisation tab.
 * Created by Chris on 19-6-2015.
 */
public abstract class PopupController {
	private boolean tableSet = false;
	private DataTable table;

	void initComboBox(DataModel model, ComboBox<TableWrapper> tableComboBox) {
		List<TableWrapper> tables = model.getTables().stream()
				.map(TableWrapper::new)
				.collect(Collectors.toList());
		List<TableWrapper> asArrayList = new ArrayList<>(tables);
		tableComboBox.setItems(FXCollections.observableArrayList(asArrayList));
		tableComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			table = newValue.getTable();
			tableSet = true;
			tableEvent();
		});
	}

	/**
	 * Called in the event handler of the table combo box.
	 */
	public abstract void tableEvent();

	public boolean isTableSet() {
		return tableSet;
	}

	public DataTable getTable() {
		return table;
	}

	/**
	 * This class is a simple wrapper for the DataTable.
	 */
	final class TableWrapper {

		private DataTable table;

		private TableWrapper(DataTable table) {
			this.table = table;
		}

		@Override
		public String toString() {
			return table.getName();
		}

		private DataTable getTable() {
			return table;
		}
	}
}
