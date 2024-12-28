package agh.darwinworld.presenter;

import agh.darwinworld.Simulation;
import agh.darwinworld.control.IntField;
import agh.darwinworld.helper.AlertHelper;
import agh.darwinworld.model.CsvPrinter;
import agh.darwinworld.model.UserFriendlyException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.File;

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
    private IntField fireFrequencyIntField;
    @FXML
    private IntField fireLengthIntField;
    @FXML
    private IntField refreshTimeIntField;
    @FXML
    private IntField seedIntField;

    CsvPrinter csvListener;

    @FXML
    public void initialize() {
        generateSeed();
    }

    public void onSimulationStart(ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        try {
            int width = getValidatedIntFieldValue(widthIntField, "Width", 1, 100);
            int height = getValidatedIntFieldValue(heightIntField, "Height", 1, 100);
            int startingPlantAmount = getValidatedIntFieldValue(startingPlantAmountIntField, "Amount of plants spawning at the start", 0, null);
            int plantGrowingAmount = getValidatedIntFieldValue(plantGrowingAmountIntField, "Amount of plants growing each day", 0, null);
            int plantEnergyAmount = getValidatedIntFieldValue(plantEnergyAmountIntField, "Plant energy profit", 0, null);
            int startingAnimalAmount = getValidatedIntFieldValue(startingAnimalAmountIntField, "Amount of animal spawning at the start ", 1, null);
            int startingEnergyAmount = getValidatedIntFieldValue(startingEnergyAmountIntField, "Animal start energy", 1, null);
            int minimumBreedingEnergy = getValidatedIntFieldValue(minimumBreedingEnergyIntField, "Minimum breeding energy", 1, null);
            int breedingEnergyCost = getValidatedIntFieldValue(breedingEnergyCostIntField, "Breeding energy cost", 1, null);
            int minimumMutationAmount = getValidatedIntFieldValue(minimumMutationAmountIntField, "Minimum mutation amount", 0, null);
            int maximumMutationAmount = getValidatedIntFieldValue(maximumMutationAmountIntField, "Maximum mutation amount", 1, null);
            int animalGenomeLength = getValidatedIntFieldValue(animalGenomeLengthIntField, "Animal genome length", 1, null);
            int fireFrequency = getValidatedIntFieldValue(fireFrequencyIntField, "Fire frequency", 1, null);
            int fireLength = getValidatedIntFieldValue(fireLengthIntField, "Fire length", 1, null);
            int refreshTime = getValidatedIntFieldValue(refreshTimeIntField, "RefreshTime", 10, null);
            int seedInt = getValidatedIntFieldValue(seedIntField, "Seed", null, null);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
            BorderPane root = loader.load();
            SimulationPresenter presenter = loader.getController();
            Simulation simulation = new Simulation(width, height, startingPlantAmount, plantGrowingAmount,
                    plantEnergyAmount, startingAnimalAmount, startingEnergyAmount, minimumBreedingEnergy,
                    breedingEnergyCost, minimumMutationAmount, maximumMutationAmount, animalGenomeLength,
                    fireFrequency, fireLength, refreshTime, seedInt);
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
                simulationStage.setMinWidth(root.getMinWidth() + windowInsets.getLeft() + windowInsets.getRight());
                simulationStage.setMinHeight(root.getMinHeight() + windowInsets.getTop() + windowInsets.getBottom());
                simulationStage.setWidth(root.getMinWidth() + windowInsets.getLeft() + windowInsets.getRight());
                simulationStage.setHeight(root.getMinHeight() + windowInsets.getTop() + windowInsets.getBottom());
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

    private int getValidatedIntFieldValue(IntField intField, String propertyName, Integer min, Integer max) throws UserFriendlyException {
        String recapitalizedPropertyName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1).toLowerCase();
        Integer value;
        try {
            value = intField.getValue();
        } catch (NumberFormatException e) {
            int minBounds = min == null ? Integer.MIN_VALUE : min;
            int maxBounds = max == null ? Integer.MAX_VALUE : max;
            throw new UserFriendlyException("Value too large!", recapitalizedPropertyName + " must be between " + minBounds + " and " + maxBounds + ".");
        }
        if (value == null)
            throw new UserFriendlyException("Missing value!", recapitalizedPropertyName + " has to be provided.");
        if (min != null && value < min)
            throw new UserFriendlyException("Value outside of boundaries!", recapitalizedPropertyName + " must be greater than or equal to " + min + ".");
        if (max != null && value > max)
            throw new UserFriendlyException("Value outside of boundaries!", recapitalizedPropertyName + " must be less than or equal to " + max + ".");
        return value;
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