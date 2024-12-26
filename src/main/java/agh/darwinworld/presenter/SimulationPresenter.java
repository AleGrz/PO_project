package agh.darwinworld.presenter;

import agh.darwinworld.Simulation;
import agh.darwinworld.model.SimulationStepListener;
import agh.darwinworld.model.Vector2D;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.Random;

public class SimulationPresenter implements SimulationStepListener {
    private Simulation simulation;
    @FXML
    private BorderPane containerBorderPane;
    @FXML
    private Label heightLabel;
    @FXML
    private Label widthLabel;
    @FXML
    private Label startingPlantAmountLabel;
    @FXML
    private Label plantGrowingAmountLabel;
    @FXML
    private Label plantEnergyAmountLabel;
    @FXML
    private Label startingAnimalAmountLabel;
    @FXML
    private Label startingEnergyAmountLabel;
    @FXML
    private Label minimumBreedingEnergyLabel;
    @FXML
    private Label breedingEnergyCostLabel;
    @FXML
    private Label minimumMutationAmountLabel;
    @FXML
    private Label maximumMutationAmountLabel;
    @FXML
    private Label animalGenomeLengthLabel;
    @FXML
    private Label fireFrequencyLabel;
    @FXML
    private Label fireLengthLabel;
    @FXML
    private Label seedLabel;
    @FXML
    private LineChart<Number, Number> dataLineChart;
    @FXML
    GridPane mapGrid;

    public void initialize() {
        containerBorderPane.widthProperty().addListener((observable, oldValue, newValue) -> redrawMap());
        containerBorderPane.heightProperty().addListener((observable, oldValue, newValue) -> redrawMap());
    }


    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
        Platform.runLater(() -> {
            heightLabel.setText(String.valueOf(simulation.getHeight()));
            widthLabel.setText(String.valueOf(simulation.getWidth()));
            startingPlantAmountLabel.setText(String.valueOf(simulation.getStartingPlantAmount()));
            plantGrowingAmountLabel.setText(String.valueOf(simulation.getPlantGrowingAmount()));
            plantEnergyAmountLabel.setText(String.valueOf(simulation.getPlantEnergyAmount()));
            startingEnergyAmountLabel.setText(String.valueOf(simulation.getStartingEnergyAmount()));
            startingAnimalAmountLabel.setText(String.valueOf(simulation.getStartingAnimalAmount()));
            minimumBreedingEnergyLabel.setText(String.valueOf(simulation.getMinimumBreedingEnergy()));
            breedingEnergyCostLabel.setText(String.valueOf(simulation.getBreedingEnergyCost()));
            minimumMutationAmountLabel.setText(String.valueOf(simulation.getMinimumMutationAmount()));
            maximumMutationAmountLabel.setText(String.valueOf(simulation.getMaximumMutationAmount()));
            animalGenomeLengthLabel.setText(String.valueOf(simulation.getAnimalGenomeLength()));
            fireFrequencyLabel.setText(String.valueOf(simulation.getFireFrequency()));
            fireLengthLabel.setText(String.valueOf(simulation.getFireLength()));
            seedLabel.setText(String.valueOf(simulation.getSeed()));
            drawMap();
        });
    }
    public double calculateCellSize() {
        int rowCount = simulation.getWidth() + 1;
        int colCount = simulation.getHeight() + 1;
        double width = (containerBorderPane.getWidth() - rowCount) / rowCount;
        double height = (containerBorderPane.getHeight() - colCount) / colCount;
        return Math.min(height, width);
    }

    public void redrawMap() {
        double cellSize = calculateCellSize();
        mapGrid.getColumnConstraints().forEach(x -> {
            x.setPrefWidth(cellSize);
        });
        mapGrid.getRowConstraints().forEach(x -> {
            x.setPrefHeight(cellSize);
        });
        mapGrid.getChildren().forEach(x -> {
            if (x instanceof Label label) {
                label.setFont(new Font(cellSize / 2));
            } else if (x instanceof Rectangle rectangle) {
                rectangle.setWidth(cellSize);
                rectangle.setHeight(cellSize);
            }
        });
    }

    public void drawMap() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
        double cellSize = calculateCellSize();
        mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
        mapGrid.getRowConstraints().add(new RowConstraints(cellSize));
        Label label = new Label("y/x");
        label.setFont(new Font(cellSize / 2));
        mapGrid.add(label, 0, 0);
        GridPane.setHalignment(label, HPos.CENTER);
        for(int i = 0; i < simulation.getWidth(); i++){
            Label colLabel = new Label(Integer.toString(i));
            colLabel.setFont(new Font(cellSize / 2));
            GridPane.setHalignment(colLabel, HPos.CENTER);
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
            mapGrid.add(colLabel, i + 1, 0);
        }
        for(int i = 0; i < simulation.getHeight(); i++){
            Label rowLabel = new Label(Integer.toString(i));
            rowLabel.setFont(new Font(cellSize / 2));
            GridPane.setHalignment(rowLabel, HPos.CENTER);
            mapGrid.getRowConstraints().add(new RowConstraints(cellSize));
            mapGrid.add(rowLabel, 0, i + 1);
        }
        for (int i = 0; i < simulation.getWidth(); i++){
            for (int j = 0; j < simulation.getHeight(); j++){
                Vector2D pos = new Vector2D(i + 1, j + 1);
                int animalAmount = simulation.getAnimalsOnPosition(pos).size();
                if (!simulation.isPlantOnPosition(pos) && animalAmount == 0)
                {
//                    Rectangle cell = new Rectangle(cellSize, cellSize);
//                    cell.setFill(Color.LIGHTGRAY);
//                    mapGrid.add(cell, i + 1, j + 1);
                    continue;
                }
                Rectangle cell = new Rectangle(cellSize, cellSize);
                if (animalAmount == 0) {
                    cell.setFill(Color.GREEN);
                } else if (animalAmount == 1) {
                    cell.setFill(Color.BROWN);
                } else if (animalAmount < 5) {
                    cell.setFill(Color.YELLOW);
                } else if (animalAmount < 10) {
                    cell.setFill(Color.RED);
                } else {
                    cell.setFill(Color.BLACK);
                }
                mapGrid.add(cell, i + 1, j + 1);
            }
        }
    }

    public void onStartStopClicked(ActionEvent actionEvent) {
        if (dataLineChart.getData().isEmpty()) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("Random name");
            series.getData().add(new XYChart.Data<>(0, 15));
            dataLineChart.getData().add(series);
            return;
        }
        XYChart.Series<Number, Number> series = dataLineChart.getData().getFirst();
        Number x = series.getData().getLast().getXValue();
        Random random = new Random();
        series.getData().add(new XYChart.Data<>(x.intValue() + 1, random.nextInt(0, 50)));
        simulation.addStepListener(this);
        simulation.run();
    }

    public void onGridClicked(MouseEvent mouseEvent) {

    }
}
