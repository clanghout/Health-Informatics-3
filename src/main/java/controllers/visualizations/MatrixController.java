package controllers.visualizations;

import model.data.DataModel;
import model.data.DataRow;
import model.data.DataTable;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for State Transition Matrix.
 * Created by Chris on 9-6-2015.
 */
public class MatrixController {
	private DataModel model;
	private Set<String> codes;
	private Logger logger = Logger.getLogger("MatrixController");

	/**
	 * Construct new MatrixController.
	 *
	 * @param model DataModel of the application
	 */
	public MatrixController(DataModel model) {
		this.model = model;
	}

	/**
	 * Collect the codes from all the rows in the complete DataModel and add them to the codes set.
	 */
	private void collectCodes() {
		codes = new HashSet<>();
		logger.log(Level.INFO, "Tables in model = " + model.getObservableList());
		for (DataTable table : model.getObservableList()) {
			logger.log(Level.INFO, "checking table " + table);
			for (DataRow row : table.getRows()) {
				codes.addAll(row.getCodes());
			}
		}
	}

	/**
	 * Check if data exists and then call to collectCodes.
	 */
	public void initialize() {
		if (model.size() > 0) {
			DataTable table = model.get(0);
			if (table.getRowCount() > 0) {
				collectCodes();
			}
		}
	}

	/**
	 * Returns the set of codes of the model.
	 *
	 * @return Set of codes.
	 */
	public Set<String> getCodes() {
		initialize();
		return codes;
	}


	/**
	 * Create a TableView object.
	 *
	 * @param codes the list of codes for the right axis.
	 * @return createImage of the tableView
	 */
	public int[][] create(List<String> codes) {
		Map<String, Integer> codeMap = new HashMap<>(codes.size());
		int[][] matrix = createMatrix(codes.size());
		for (int i = 0; i < codes.size(); i++) {
			String code = codes.get(i);
			codeMap.put(code, i);
		}
		String currentCode = codes.get(0);
		for (DataTable table : model.getTables()) {
			for (DataRow row : table.getRows()) {
				for (String code : row.getCodes()) {
					matrix[codeMap.get(currentCode)][codeMap.get(code)] += 1;
					currentCode = code;
				}
			}
		}
		return matrix;
	}

	/**
	 * Create square matrix filled with 0.
	 *
	 * @param size size of sides.
	 * @return int[][] with all 0 values.
	 */
	private int[][] createMatrix(int size) {
		int[][] matrix = new int[size][size];
		for (int i = 0; i < matrix.length; i++) {
			for (int i1 = 0; i1 < matrix[0].length; i1++) {
				matrix[i][i1] = 0;
			}
		}
		return matrix;
	}
}
