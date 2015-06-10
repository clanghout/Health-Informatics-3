package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.Chart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import model.data.DataColumn;
import model.data.DataModel;
import model.data.DataTable;
import view.GraphCreationDialog;
import view.MatrixCreationDialog;

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

	public void drawMatrix(TableView matrix) {
		visualizationGraph.getChildren().add(matrix);
		saveButton.setDisable(true);
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
	 * Set the items of a comboBox to the columns of the dataTable.
	 *
	 * @param inputBox  the comboBox that specifies the axis of the graph
	 * @param dataTable the dataTable used for the graph
	 */
	public void setColumnDropDown(ComboBox<DataColumn> inputBox, DataTable dataTable) {
		inputBox.setDisable(false);
		ObservableList<DataColumn> columns =
				FXCollections.observableArrayList(dataTable.getColumns());
		inputBox.setItems(columns);
		inputBox.setConverter(new StringConverter<DataColumn>() {
			@Override
			public String toString(DataColumn object) {
				return object.getName();
			}

			@Override
			public DataColumn fromString(String string) {
				return dataTable.getColumn(string);
			}
		});
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

	@FXML
	protected void handlePopupMatrixButtonAction() {
		visualizationGraph.getChildren().clear();
		saveButton.setDisable(true);
		try {
			MatrixCreationDialog matrixCreationDialog =
					new MatrixCreationDialog();
			matrixCreationDialog.show();
			PopupMatrixController matrixController =
					matrixCreationDialog.getFxml().getController();
			matrixController.initializeView(model, matrixCreationDialog, this);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "FXML could not be loaded");
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