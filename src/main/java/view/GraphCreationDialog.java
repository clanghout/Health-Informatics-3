package view;

import java.io.IOException;

/**
 * Created by Chris on 2-6-2015.
 */
public class GraphCreationDialog extends Dialog {

	public GraphCreationDialog() throws IOException {
		super("/popup_visualization.fxml", "Setup graph data");
	}
}
