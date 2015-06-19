package controllers.visualizations;

import model.data.DataColumn;
import model.data.DataModel;
import model.data.DataRow;
import model.data.DataTable;
import model.data.describer.RowValueDescriber;
import model.process.SortProcess;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for State Transition Matrix.
 * Created by Chris on 9-6-2015.
 */
public class MatrixController extends GraphImageController {
	private DataModel model;
	private Map<DataTable, Set<String>> codes;
	private Logger logger = Logger.getLogger("MatrixController");

	/**
	 * Construct new MatrixController.
	 *
	 * @param model DataModel of the application
	 */
	public MatrixController(DataModel model) {
		this.model = model;
		codes = new HashMap<>();
	}

	/**
	 * Collect the codes from all the rows in the complete DataModel and add them to the codes set.
	 */
	private void collectCodes() {
		logger.log(Level.INFO, "Tables in model = " + model.getObservableList());
		Set<String> codeset;
		for (DataTable table : model.getObservableList()) {
			logger.log(Level.INFO, "checking table " + table);
			codeset = new HashSet<>();
			for (DataRow row : table.getRows()) {
				codeset.addAll(row.getCodes());
			}
			codes.put(table, codeset);
		}
	}

	/**
	 * checks if any code exists in the model.
	 *
	 * @return true if no code exists.
	 */
	public boolean noCodes() {
		for (DataTable table : model) {
			if (!codes.get(table).isEmpty()) {
				return false;
			}
		}
		return true;
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
	public Map<DataTable, Set<String>> getCodes() {
		initialize();
		return codes;
	}


	/**
	 * Create a TableView object.
	 *
	 * @param codes the list of codes for the right axis.
	 * @return createImage of the tableView
	 */
	public int[][] create(List<String> codes, DataTable table, DataColumn column) {
		Map<String, Integer> codeMap = new HashMap<>(codes.size());
		int[][] matrix = createMatrix(codes.size());
		for (int i = 0; i < codes.size(); i++) {
			String code = codes.get(i);
			codeMap.put(code, i);
		}
		String currentCode = "";
		boolean init = true;
		SortProcess sortProcess = new SortProcess(
				new RowValueDescriber<>(column),
				SortProcess.Order.ASCENDING);
		sortProcess.setInput(table);
		DataTable sortedTable = (DataTable) sortProcess.process();
		for (DataRow row : sortedTable.getRows()) {
			for (String code : row.getCodes()) {
				if (codeMap.containsKey(code)) {
					if (init) {
						currentCode = code;
						init = false;
					} else {
						matrix[codeMap.get(currentCode)][codeMap.get(code)] += 1;
						currentCode = code;
					}
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
