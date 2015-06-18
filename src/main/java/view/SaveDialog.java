package view;

import controllers.SaveWizardController;
import model.data.DataModel;

import java.io.IOException;

/**
 * Dialog for the wizard for saving files.
 * Created by Chris on 11-6-2015.
 */
public class SaveDialog extends Dialog {
	private DataModel model;

	/**
	 * Make a custom Dialog for the save file wizard.
	 *
	 * @throws IOException
	 */
	public SaveDialog(DataModel model) throws IOException {
		super("/wizard_save_file.fxml", "Save file");
		this.model = model;
	}

	@Override
	public void show() {
		super.show();
		SaveWizardController controller = getFxml().getController();
				controller.initializeView(model, this);
	}
}
