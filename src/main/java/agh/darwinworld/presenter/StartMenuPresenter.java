package agh.darwinworld.presenter;

import agh.darwinworld.Simulation;
import agh.darwinworld.control.IntField;
import agh.darwinworld.helper.AlertHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

public class StartMenuPresenter {
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
    private IntField seedIntField;

    public void onSimulationStart(ActionEvent actionEvent) {
        Stage currentStage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
            BorderPane root = loader.load();
            SimulationPresenter presenter = loader.getController();
            Integer width = widthIntField.getValue();
            Integer height = heightIntField.getValue();
            Integer startingPlantAmount = startingPlantAmountIntField.getValue();
            Integer plantGrowingAmount = plantGrowingAmountIntField.getValue();
            Integer plantEnergyAmount = plantEnergyAmountIntField.getValue();
            Integer startingAnimalAmount = startingAnimalAmountIntField.getValue();
            Integer startingEnergyAmount = startingEnergyAmountIntField.getValue();
            Integer minimumBreedingEnergy = minimumBreedingEnergyIntField.getValue();
            Integer breedingEnergyCost = breedingEnergyCostIntField.getValue();
            Integer minimumMutationAmount = minimumMutationAmountIntField.getValue();
            Integer maximumMutationAmount = maximumMutationAmountIntField.getValue();
            Integer animalGenomeLength = animalGenomeLengthIntField.getValue();
            Integer fireFrequency = fireFrequencyIntField.getValue();
            Integer fireLength = fireLengthIntField.getValue();
            Integer seedInt = seedIntField.getValue();
            Simulation simulation = new Simulation(width, height, startingPlantAmount,
                    plantGrowingAmount, plantEnergyAmount, startingAnimalAmount,
                    startingEnergyAmount, minimumBreedingEnergy, breedingEnergyCost,
                    minimumMutationAmount, maximumMutationAmount, animalGenomeLength,
                    fireFrequency, fireLength, seedInt);
            presenter.setSimulation(simulation);
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
        } catch (Exception e) {
            // TODO: Walidacja inputu i ładniejszy alert, poniższy alert tylko dla błędów!
            AlertHelper.ShowExceptionAlert(currentStage, e);
        }
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

    public void seedGenerate(ActionEvent actionEvent) {
        seedIntField.setValue((int) (Math.random() * Integer.MAX_VALUE));
    }
}
