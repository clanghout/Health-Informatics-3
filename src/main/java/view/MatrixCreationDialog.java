package view;

import java.io.IOException;

/**
 * Create a Dialog for the creation of the matrix.
 * Using popup_matrix_create fxml and name "Setup matrix data"
 * Created by Chris on 10-6-2015.
 */
public class MatrixCreationDialog extends Dialog {
	public MatrixCreationDialog() throws IOException {
		super("/popup_matrix_create.fxml", "Setup matrix data");
	}
}
