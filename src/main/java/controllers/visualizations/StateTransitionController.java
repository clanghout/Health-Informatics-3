package controllers.visualizations;

import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import model.data.DataModel;
import model.data.DataRow;
import model.data.DataTable;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller for State Transition Matrix.
 * Created by Chris on 9-6-2015.
 */
public class StateTransitionController extends ChartController {
	private DataModel model;
	private VBox vBox;
	private Set<String> codes;

	public StateTransitionController(DataModel model, VBox vBox) {
		this.model = model;
		this.vBox = vBox;
	}

	/**
	 * Collect the codes from all the rows in the complete DataModel and add them to the codes set.
	 */
	private void collectCodes() {
		for (DataTable table: model.getObservableList()) {
			for (DataRow row : table.getRows()) {
					codes.addAll(row.getCodes().stream().collect(Collectors.toList()));
			}
		}
	}

	@Override
	public void initialize() {
		// fill up complete codes set
		if (model.size() > 0) {
			DataTable table = model.get(0);
			if (table.getRowCount() > 0) {
				DataRow row = table.getRow(0);
				codes = row.getCodes();
				collectCodes();
			}
		}

	}

	@Override
	public boolean axesSet() {
		return false;
	}

	@Override
	public WritableImage createImage() {
		return null;
	}
}
