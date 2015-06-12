package controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.Chart;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.data.DataModel;
import view.GraphCreationDialog;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Control visualisation.
 * Created by Chris on 26-5-2015.
 */
public class VisualizationController {
	@FXML
	private Parent root;
	@FXML
	private VBox visualizationGraph;
	@FXML
	private Button makeGraphButton;
	@FXML
	private Button clearViewButton;
	@FXML
	private Button saveButton;
	private DataModel model;
	private Logger logger = Logger.getLogger("VisualizationController");
	private WritableImage image;

	/**
	 * Constructor for Visualization controller.
	 */
	public VisualizationController() {
	}

	/**
	 * Initialization module for the visualisation controller.
	 * This method is automatically called by javaFX on initialization.
	 * The dropDown menu's are disabled while no DataModel is loaded.
	 */
	public void initialize() {
		makeGraphButton.setDisable(true);
		clearViewButton.setDisable(true);
		saveButton.setDisable(true);
	}

	/**
	 * Init method after a model is read.
	 */
	public void initializeVisualisation() {
		makeGraphButton.setDisable(false);
		clearViewButton.setDisable(false);
	}

	/**
	 * Draw a javaFX chart object.
	 *
	 * @param chart the object drawn.
	 */
	public void drawGraph(Chart chart) {
		visualizationGraph.getChildren().add(chart);
		this.image = visualizationGraph.snapshot(new SnapshotParameters(), null);
		saveButton.setDisable(false);
	}

	/**
	 * Draw an image.
	 *
	 * @param image WritableImage to be drawn.
	 */
	public void drawImage(WritableImage image) {
		this.image = image;
		Node node = new ImageView(image);
		node.maxWidth(Double.MAX_VALUE);
		node.maxHeight(Double.MAX_VALUE);
		visualizationGraph.getChildren().add(node);
		saveButton.setDisable(false);
	}

	/**
	 * Sets the model that will be observed.
	 *
	 * @param model The model
	 */
	public void setModel(DataModel model) {
		this.model = model;
	}


	/**
	 * Create a popupWindow and add the model to the controller.
	 */
	@FXML
	protected void handlePopupButtonAction() {
		visualizationGraph.getChildren().clear();
		saveButton.setDisable(true);
		try {
			GraphCreationDialog graphCreationDialog = new GraphCreationDialog();
			graphCreationDialog.show();

			PopupVisualizationController popupController =
					graphCreationDialog.getFxml().getController();
			popupController.initializeView(model, this, graphCreationDialog);

		} catch (NullPointerException e) {
			logger.log(Level.SEVERE, "No controller present");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "FXML file cannot be loaded");
		}
	}

	/**
	 * Save the graph as image.
	 */
	@FXML
	protected void handleSaveButtonAction() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Image");
		fileChooser.getExtensionFilters()
				.add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
		File file = fileChooser.showSaveDialog(root.getScene().getWindow());
		if (file != null) {
			if (!file.getAbsolutePath().endsWith(".png")) {
				file = new File(file.getAbsolutePath() + ".png");
			}
			RenderedImage renderedImage = SwingFXUtils.fromFXImage(image, null);
			try {
				ImageIO.write(renderedImage, "png", file);
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	/**
	 * Clear the graph.
	 */
	@FXML
	protected void handleClearButtonAction() {
		saveButton.setDisable(true);
		visualizationGraph.getChildren().clear();
	}
}