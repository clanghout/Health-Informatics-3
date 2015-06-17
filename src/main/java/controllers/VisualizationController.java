package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.Chart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.data.DataModel;
import view.GraphCreationDialog;
import view.MatrixCreationDialog;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
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

	public void drawMatrix(int[][] matrix, List<String> colNames) {
		TableView table = new TableView();
		fillTableHeaders(table, colNames);
		for (int i = 0; i < matrix.length; i++) {
			int[] matrixRow = matrix[i];
			ObservableList<StringProperty> row = FXCollections.observableArrayList();
			row.add(new SimpleStringProperty(colNames.get(i)));
			for(int matrixVal : matrixRow) {
				row.add(new SimpleStringProperty(String.valueOf(matrixVal)));
			}
			table.getItems().add(row);
		}


		visualizationGraph.getChildren().add(table);
		saveButton.setDisable(true);
	}

	/**
	 * Fills the headers of the table.
	 *
	 * @param columns A List containing the DataColumns
	 */
	private void fillTableHeaders(TableView matrix, List<String> columns) {
		System.out.println("fill headers called");
		matrix.getColumns().add(createColumn(0, ""));
		System.out.println("add column");
		for (int i = 0; i < columns.size(); i++) {
			matrix.getColumns().add(createColumn(i + 1, columns.get(i)));
		}
	}

	/**
	 * Creates a new TableColumn based on an observable list with StringProperties.
	 *
	 * @param index       The index of the column that will be created
	 * @param columnTitle The name of the column
	 * @return The created TableColumn
	 * @see <a href="https://docs.oracle.com/javafx/2/api/javafx/scene/control/TableView.html">
	 * The TableView Class</a>
	 */
	private TableColumn<ObservableList<StringProperty>, String> createColumn(int index, String columnTitle) {
		TableColumn<ObservableList<StringProperty>, String> column
				= new TableColumn<>(columnTitle);
		column.setCellValueFactory(
				cellDataFeatures -> cellDataFeatures.getValue().get(index));
		return column;
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