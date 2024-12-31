package agh.darwinworld.presenters;

import agh.darwinworld.Simulation;
import agh.darwinworld.controls.IntField;
import agh.darwinworld.helpers.AlertHelper;
import agh.darwinworld.models.*;
import agh.darwinworld.models.exceptions.UserFriendlyException;
import agh.darwinworld.models.maps.FireMap;
import agh.darwinworld.models.maps.WorldMap;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Random;

public class StartMenuPresenter {
    @FXML
    private Button csvButton;
    @FXML
    private IntField heightIntField;
    @FXML
    private IntField widthIntField;
    @FXML
    private IntField startingPlantAmountIntField;
    @FXML
    private IntField plantGrowingAmountIntField;
    @FXML
    private IntField plantEnergyAmountIntField;
    @FXML
    private IntField startingAnimalAmountIntField;
    @FXML
    private IntField startingEnergyAmountIntField;
    @FXML
    private IntField minimumBreedingEnergyIntField;
    @FXML
    private IntField breedingEnergyCostIntField;
    @FXML
    private IntField minimumMutationAmountIntField;
    @FXML
    private IntField maximumMutationAmountIntField;
    @FXML
    private IntField animalGenomeLengthIntField;
    @FXML
    private IntField fireIntervalIntField;
    @FXML
    private IntField fireLengthIntField;
    @FXML
    private IntField refreshTimeIntField;
    @FXML
    private IntField seedIntField;
    @FXML
    private ComboBox<String> mapTypeComboBox;
    @FXML
    private Label fireLengthLabel;
    @FXML
    private Label fireIntervalLabel;

    private CsvPrinter csvListener;

    @FXML
    public void initialize() {
        generateSeed();
        Platform.runLater(this::updateLayout);
    }

    public void onMapTypeSelectionChanged(ActionEvent ignored) {
        updateLayout();
    }

    public void onSimulationStart(ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
            BorderPane root = loader.load();
            SimulationPresenter presenter = loader.getController();
            String mapType = mapTypeComboBox.getSelectionModel().getSelectedItem();
            SimulationParameters params = SimulationParameters.createFromIntField(
                    widthIntField,
                    heightIntField,
                    startingPlantAmountIntField,
                    plantGrowingAmountIntField,
                    plantEnergyAmountIntField,
                    startingAnimalAmountIntField,
                    startingEnergyAmountIntField,
                    minimumBreedingEnergyIntField,
                    breedingEnergyCostIntField,
                    minimumMutationAmountIntField,
                    maximumMutationAmountIntField,
                    animalGenomeLengthIntField,
                    fireIntervalIntField,
                    fireLengthIntField,
                    refreshTimeIntField,
                    new Random(seedIntField.getValue()),
                    mapType.equals("Fire map") ? new FireMap() : new WorldMap()
            );
            Simulation simulation = new Simulation(params);
            presenter.setSimulation(simulation);
            if (csvListener != null) {
                simulation.addStepListener(csvListener);
                presenter.addPauseListener(csvListener);
            }
            Stage simulationStage = new Stage();
            simulationStage.initOwner(currentStage);
            simulationStage.getIcons().addAll(currentStage.getIcons());
            simulationStage.setTitle("Simulation");
            simulationStage.setScene(new Scene(root));
            simulationStage.setOnShown(e -> {
                Insets windowInsets = getWindowInsets(simulationStage);
                simulationStage.minWidthProperty().bind(root.minWidthProperty().add(windowInsets.getLeft() + windowInsets.getRight()));
                simulationStage.minHeightProperty().bind(root.minHeightProperty().add(windowInsets.getTop() + windowInsets.getBottom()));
                simulationStage.setWidth(simulationStage.getMinWidth());
                simulationStage.setHeight(simulationStage.getMinHeight());
            });
            simulationStage.show();
        } catch (UserFriendlyException e) {
            AlertHelper.ShowUserFriendlyExceptionAlert(currentStage, e);
        } catch (Exception e) {
            AlertHelper.ShowExceptionAlert(currentStage, e);
        }
    }

    public void onSeedGenerate(ActionEvent ignored) {
        generateSeed();
    }

    private void generateSeed() {
        seedIntField.setValue((int) (Math.random() * Integer.MAX_VALUE));
    }

    private void updateLayout() {
        String mapType = mapTypeComboBox.getSelectionModel().getSelectedItem();
        boolean v = mapType.equals("Fire map");
        fireLengthIntField.setVisible(v);
        fireLengthIntField.setManaged(v);
        fireIntervalIntField.setVisible(v);
        fireIntervalIntField.setManaged(v);
        fireLengthLabel.setVisible(v);
        fireLengthLabel.setManaged(v);
        fireIntervalLabel.setVisible(v);
        fireIntervalLabel.setManaged(v);
    }

    private Insets getWindowInsets(Stage stage) {
        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();
        double contentWidth = stage.getScene().getWidth();
        double contentHeight = stage.getScene().getHeight();
        double horizontalInsets = (stageWidth - contentWidth) / 2;
        double verticalInsets = stageHeight - contentHeight - horizontalInsets;
        return new Insets(verticalInsets, horizontalInsets, verticalInsets, horizontalInsets);
    }

    public void onSaveToCSV(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files", "*.csv"));
        fileChooser.setInitialFileName("simulation.csv");
        File file = fileChooser.showSaveDialog(((Node) actionEvent.getSource()).getScene().getWindow());
        csvButton.setText(file != null ? file.getName() : "Choose");
        if (file != null) {
            csvListener = new CsvPrinter(file.getAbsolutePath());
        }
    }
}