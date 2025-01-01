package agh.darwinworld.presenters;

import agh.darwinworld.Simulation;
import agh.darwinworld.controls.IntField;
import agh.darwinworld.helpers.AlertHelper;
import agh.darwinworld.helpers.StageHelper;
import agh.darwinworld.models.*;
import agh.darwinworld.models.exceptions.UserFriendlyException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class StartMenuPresenter implements Initializable {
    @FXML
    private Button deleteCsvButton;
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
    @FXML
    private BorderPane rootBorderPane;

    private CsvPrinter csvListener;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateSeed();
        Platform.runLater(this::updateLayout);
    }

    public void onMapTypeSelectionChanged(ActionEvent ignored) {
        updateLayout();
    }

    public void onSimulationStart(ActionEvent actionEvent) {
        Stage currentStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        assert currentStage != null;
        try {
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
                    seedIntField,
                    mapType.equals("Fire map") ? MapType.FIRE : MapType.WORLD,
                    AnimalType.AGEING_ANIMAL
            );
            Simulation simulation = new Simulation(params);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
            BorderPane root = loader.load();
            SimulationPresenter presenter = loader.getController();
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
            StageHelper.bindMinSize(simulationStage, root);
            StageHelper.setDarkMode(simulationStage, true);
            simulationStage.show();
        } catch (UserFriendlyException e) {
            AlertHelper.showUserFriendlyExceptionAlert(currentStage, e);
        } catch (Exception e) {
            AlertHelper.showExceptionAlert(currentStage, e);
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

    public void onSaveToCSV(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files", "*.csv"));
        fileChooser.setInitialFileName("simulation.csv");
        File file = fileChooser.showSaveDialog(((Node) actionEvent.getSource()).getScene().getWindow());
        csvButton.setText(file != null ? file.getName() : "Choose");
        if (file != null) {
            csvButton.getStyleClass().add("success");
            csvListener = new CsvPrinter(file.getAbsolutePath());
        }
    }

    public void onDelete(ActionEvent actionEvent) {
        csvButton.setText("Choose");
        csvButton.getStyleClass().remove("success");
        csvListener = null;
    }

}