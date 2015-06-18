package view;

import java.io.IOException;

/**
 * @author Paul.
 */
public class XMLCreationDialog extends Dialog {

	/**
	 * Creates a new dialog for the user to create a datafile specifying xml file.
	 * @throws IOException When the fxml file can not be located
	 */
	public XMLCreationDialog() throws IOException {
		super("/wizard_xml_builder.fxml", "XML wizard");
	}
}
