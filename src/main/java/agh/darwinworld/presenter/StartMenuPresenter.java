package agh.darwinworld.presenter;

import agh.darwinworld.Simulation;
import agh.darwinworld.control.IntField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
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
            Stage newStage = new Stage();
            Image appIcon = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("Icon.png")));
            newStage.getIcons().add(appIcon);
            newStage.setTitle("Simulation");
            newStage.setScene(new Scene(root));
            newStage.setMinWidth(root.minWidthProperty().getValue());
            newStage.setMinHeight(root.minHeightProperty().getValue());
            newStage.show();
        } catch (Exception e) {
        }
    }
}
