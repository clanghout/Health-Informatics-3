package view;

import java.io.IOException;

/**
 * Create a Dialog for the graph creation.
 * Using the fxml popup_graph_create and name "Setup graph data".
 * Created by Chris on 2-6-2015.
 */
public class GraphCreationDialog extends Dialog {

	public GraphCreationDialog() throws IOException {
		super("/popup_graph_create.fxml", "Setup graph data");
	}
}
