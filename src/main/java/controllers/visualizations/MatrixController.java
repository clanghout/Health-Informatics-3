package controllers.visualizations;

import javafx.scene.image.WritableImage;
import model.data.DataModel;
import model.data.DataRow;
import model.data.DataTable;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller for State Transition Matrix.
 * Created by Chris on 9-6-2015.
 */
public class MatrixController extends ChartController {
	private DataModel model;
	private Set<String> codes;

	public MatrixController(DataModel model) {
		this.model = model;
		codes = new HashSet<>();
		initialize();
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
				collectCodes();
			}
		}
	}

	public Set<String> getCodes() {
		return codes;
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
