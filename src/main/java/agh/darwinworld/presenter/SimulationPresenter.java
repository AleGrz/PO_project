package agh.darwinworld.presenter;

import agh.darwinworld.Simulation;
import agh.darwinworld.control.IntField;
import agh.darwinworld.model.Vector2D;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.Random;

public class SimulationPresenter {
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
        containerBorderPane.widthProperty().addListener((observable, oldValue, newValue) -> resizeMap());
        containerBorderPane.heightProperty().addListener((observable, oldValue, newValue) -> resizeMap());
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

    public void resizeMap() {
        double cellSize = calculateCellSize();
        mapGrid.getColumnConstraints().forEach(x -> {
            x.setMinWidth(cellSize);
            x.setPrefWidth(cellSize);
            x.setMaxWidth(cellSize);
        });
        mapGrid.getRowConstraints().forEach(x -> {
            x.setMinHeight(cellSize);
            x.setPrefHeight(cellSize);
            x.setMaxHeight(cellSize);
        });
        mapGrid.getChildren().forEach(x -> {
            if (x instanceof Label cell) {
                cell.setFont(new Font(cellSize / 2));
                cell.setMinSize(cellSize, cellSize);
                cell.setPrefSize(cellSize, cellSize);
                cell.setMaxSize(cellSize, cellSize);
            }
        });
    }

    public void drawMap() {
        if (!mapGrid.getChildren().isEmpty())
            mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
        double cellSize = calculateCellSize();
        mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize, cellSize, cellSize));
        mapGrid.getRowConstraints().add(new RowConstraints(cellSize, cellSize, cellSize));
        Label label = new Label("y/x");
        label.setFont(new Font(cellSize / 2));
        label.setAlignment(Pos.CENTER);
        mapGrid.add(label, 0, 0);
        GridPane.setHalignment(label, HPos.CENTER);
        for(int i = 0; i < simulation.getWidth(); i++){
            Label colLabel = new Label(Integer.toString(i));
            colLabel.setFont(new Font(cellSize / 2));
            GridPane.setHalignment(colLabel, HPos.CENTER);
            colLabel.setAlignment(Pos.CENTER);
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize, cellSize, cellSize));
            mapGrid.add(colLabel, i + 1, 0);
        }
        for(int i = 0; i < simulation.getHeight(); i++){
            Label rowLabel = new Label(Integer.toString(i));
            rowLabel.setFont(new Font(cellSize / 2));
            GridPane.setHalignment(rowLabel, HPos.CENTER);
            rowLabel.setAlignment(Pos.CENTER);
            mapGrid.getRowConstraints().add(new RowConstraints(cellSize, cellSize, cellSize));
            mapGrid.add(rowLabel, 0, i + 1);
        }
        for (int i = 0; i < simulation.getWidth(); i++){
            for (int j = 0; j < simulation.getHeight(); j++){
                Vector2D pos = new Vector2D(i + 1, j + 1);
                int animalAmount = simulation.getAnimalsOnPosition(pos).size();
                Label cell = new Label();
                cell.setMinSize(cellSize, cellSize);
                cell.setPrefSize(cellSize, cellSize);
                cell.setMaxSize(cellSize, cellSize);
                cell.setStyle(cell.getStyle() + "-fx-text-fill: white;");
                cell.setAlignment(Pos.CENTER);
                if (animalAmount > 15) {
                    cell.setStyle(cell.getStyle() + "-fx-background-color: saddlebrown;");
                } else if (animalAmount > 10) {
                    cell.setStyle(cell.getStyle() + "-fx-background-color: sienna;");
                } else if (animalAmount > 5) {
                    cell.setStyle(cell.getStyle() + "-fx-background-color: chocolate;");
                } else if (animalAmount > 3) {
                    cell.setStyle(cell.getStyle() + "-fx-background-color: burlywood;");
                } else if (animalAmount > 0) {
                    cell.setStyle(cell.getStyle() + "-fx-background-color: wheat;");
                } else if (simulation.isPlantOnPosition(pos)) {
                    cell.setStyle(cell.getStyle() + "-fx-background-color: green;");
                } else {
                    cell.setStyle(cell.getStyle() + "-fx-background-color: lightgreen;");
                }
                if (simulation.isPlantOnPosition(pos)) {
                    cell.setText("*");
                    cell.setStyle(cell.getStyle() + "-fx-text-fill: white;");
                    cell.setAlignment(Pos.CENTER);
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
    }

    public void onGridMouseClicked(MouseEvent mouseEvent) {
    }
    Label mouseOverCell = null;

    public void onGridMouseEntered(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        double cellSize = calculateCellSize();
        double gapH = (mapGrid.getWidth() - cellSize * (simulation.getWidth() + 1)) / 2;
        double gapV = (mapGrid.getHeight() - cellSize * (simulation.getHeight() + 1)) / 2;
        int colIndex = (int) ((x - gapH) / cellSize);
        int rowIndex = (int) ((y - gapV) / cellSize);
        if (colIndex < 1 || rowIndex < 1) {
            clearmouseOverCell();
            return;
        }
        Node clickedNode = getNodeByRowColumn(mapGrid, rowIndex, colIndex);
        if (clickedNode instanceof Label cell) {
            if (mouseOverCell == cell) return;
            clearmouseOverCell();
            cell.setStyle(cell.getStyle() + "-fx-border-color: red; -fx-border-width: 1px;");
            mouseOverCell = cell;
        }
    }

    public void onGridMouseExited(MouseEvent mouseEvent) {
        clearmouseOverCell();
    }

    public void onGridMouseMoved(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        double cellSize = calculateCellSize();
        double gapH = (mapGrid.getWidth() - cellSize * (simulation.getWidth() + 1)) / 2;
        double gapV = (mapGrid.getHeight() - cellSize * (simulation.getHeight() + 1)) / 2;
        int colIndex = (int) ((x - gapH) / cellSize);
        int rowIndex = (int) ((y - gapV) / cellSize);
        if (colIndex < 1 || rowIndex < 1) {
            clearmouseOverCell();
            return;
        }
        Node clickedNode = getNodeByRowColumn(mapGrid, rowIndex, colIndex);
        if (clickedNode instanceof Label cell) {
            if (mouseOverCell == cell) return;
            clearmouseOverCell();
            cell.setStyle(cell.getStyle() + "-fx-border-color: red; -fx-border-width: 1px;");
            mouseOverCell = cell;
        }
    }

    private void clearmouseOverCell() {
        if (mouseOverCell == null) return;
        mouseOverCell.setStyle(mouseOverCell.getStyle().replace("-fx-border-color: red; -fx-border-width: 1px;", ""));
        mouseOverCell = null;
    }

    private Node getNodeByRowColumn(GridPane gridPane, int row, int col) {
        for (Node node : gridPane.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(node);
            Integer colIndex = GridPane.getColumnIndex(node);
            rowIndex = rowIndex == null ? 0 : rowIndex;
            colIndex = colIndex == null ? 0 : colIndex;

            if (rowIndex == row && colIndex == col) {
                return node;
            }
        }
        return null;
    }
}
