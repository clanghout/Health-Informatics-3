package view;

import java.io.IOException;

/**
 * Dialog for the wizard for saving files.
 * Created by Chris on 11-6-2015.
 */
public class SaveDialog extends Dialog {
	/**
	 * Make a custom Dialog for the save file wizard.
	 *
	 * @throws IOException
	 */
	public SaveDialog() throws IOException {
		super("/wizard_save_file.fxml", "Save file");
	}
}
