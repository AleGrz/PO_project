package agh.darwinworld.presenter;

import agh.darwinworld.Simulation;
import agh.darwinworld.model.Animal;
import agh.darwinworld.model.MoveDirection;
import agh.darwinworld.model.SimulationStepListener;
import agh.darwinworld.model.Vector2D;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import java.util.List;
import java.util.Random;

public class SimulationPresenter implements SimulationStepListener {
    @FXML private BorderPane containerBorderPane;
    @FXML private Label heightLabel;
    @FXML private Label widthLabel;
    @FXML private Label startingPlantAmountLabel;
    @FXML private Label plantGrowingAmountLabel;
    @FXML private Label plantEnergyAmountLabel;
    @FXML private Label startingAnimalAmountLabel;
    @FXML private Label startingEnergyAmountLabel;
    @FXML private Label minimumBreedingEnergyLabel;
    @FXML private Label breedingEnergyCostLabel;
    @FXML private Label minimumMutationAmountLabel;
    @FXML private Label maximumMutationAmountLabel;
    @FXML private Label animalGenomeLengthLabel;
    @FXML private Label fireFrequencyLabel;
    @FXML private Label fireLengthLabel;
    @FXML private Label seedLabel;
    @FXML private LineChart<Number, Number> dataLineChart;
    @FXML private GridPane mapGrid;
    @FXML private GridPane selectedAnimalGridPane;
    @FXML private Label selectedAnimalAgeLabel;
    @FXML private Label selectedAnimalEnergyLabel;
    @FXML private Label selectedAnimalChildrenAmountLabel;
    @FXML private Label selectedAnimalDescendantsAmountLabel;
    @FXML private Label selectedAnimalGenomeLabel;
    @FXML private Label selectedAnimalPlantsEatenAmountLabel;
    @FXML private Label selectedAnimalDiedAtLabel;
    @FXML private Button startStopButton;

    private Simulation simulation;
    private final SimpleObjectProperty<Animal> selectedAnimal = new SimpleObjectProperty<>(null);

    private Vector2D mouseOverPosition;
    private Label mouseOverCell;

    private int currentCellSize;
    private Thread simulationThread;

    @FXML
    public void initialize() {
        containerBorderPane.widthProperty().addListener((observable, oldValue, newValue) -> resizeMap());
        containerBorderPane.heightProperty().addListener((observable, oldValue, newValue) -> resizeMap());
        selectedAnimalGridPane.setVisible(false);
        selectedAnimal.addListener((observable, oldValue, newValue) -> updateSelectedAnimalData());
    }

    public void updateSelectedAnimalData() {
        Animal animal = selectedAnimal.getValue();
        Platform.runLater(() -> {
            selectedAnimalGridPane.setVisible(animal != null);
            if (animal == null) return;
            selectedAnimalAgeLabel.setText(Integer.toString(animal.getAge()));
            selectedAnimalEnergyLabel.setText(Integer.toString(animal.getEnergy()));
            selectedAnimalChildrenAmountLabel.setText(Integer.toString(animal.getChildrenAmount()));
            selectedAnimalDescendantsAmountLabel.setText("MISSING DATA");
            MoveDirection[] genome = animal.getGenome();
            StringBuilder genomeString = new StringBuilder();
            for (MoveDirection gene : genome) {
                genomeString.append(gene.ordinal());
            }
            selectedAnimalGenomeLabel.setText(genomeString.toString());
            selectedAnimalPlantsEatenAmountLabel.setText("MISSING DATA");
            selectedAnimalDiedAtLabel.setText("MISSING DATA");
        });
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
        simulation.addStepListener(this);
        this.selectedAnimal.set(null);
        Platform.runLater(() -> {
            heightLabel.setText(Integer.toString(simulation.getHeight()));
            widthLabel.setText(Integer.toString(simulation.getWidth()));
            startingPlantAmountLabel.setText(Integer.toString(simulation.getStartingPlantAmount()));
            plantGrowingAmountLabel.setText(Integer.toString(simulation.getPlantGrowingAmount()));
            plantEnergyAmountLabel.setText(Integer.toString(simulation.getPlantEnergyAmount()));
            startingEnergyAmountLabel.setText(Integer.toString(simulation.getStartingEnergyAmount()));
            startingAnimalAmountLabel.setText(Integer.toString(simulation.getStartingAnimalAmount()));
            minimumBreedingEnergyLabel.setText(Integer.toString(simulation.getMinimumBreedingEnergy()));
            breedingEnergyCostLabel.setText(Integer.toString(simulation.getBreedingEnergyCost()));
            minimumMutationAmountLabel.setText(Integer.toString(simulation.getMinimumMutationAmount()));
            maximumMutationAmountLabel.setText(Integer.toString(simulation.getMaximumMutationAmount()));
            animalGenomeLengthLabel.setText(Integer.toString(simulation.getAnimalGenomeLength()));
            fireFrequencyLabel.setText(Integer.toString(simulation.getFireFrequency()));
            fireLengthLabel.setText(Integer.toString(simulation.getFireLength()));
            seedLabel.setText(Integer.toString(simulation.getSeed()));
            drawMap();
        });
    }
    public int calculateCellSize() {
        int rowCount = simulation.getWidth() + 1;
        int colCount = simulation.getHeight() + 1;
        int width = (int) ((containerBorderPane.getWidth() - rowCount) / rowCount);
        int height = (int) ((containerBorderPane.getHeight() - colCount) / colCount);
        return Math.min(height, width);
    }

    public void resizeMap() {
        int cellSize = calculateCellSize();
        if (cellSize == currentCellSize) return;
        currentCellSize = cellSize;
        boolean showGridLines = mapGrid.isGridLinesVisible();
        mapGrid.setGridLinesVisible(false);
        for (ColumnConstraints col : mapGrid.getColumnConstraints()) {
            col.setMinWidth(cellSize);
            col.setPrefWidth(cellSize);
            col.setMaxWidth(cellSize);
        }
        for (RowConstraints row : mapGrid.getRowConstraints()) {
            row.setMinHeight(cellSize);
            row.setPrefHeight(cellSize);
            row.setMaxHeight(cellSize);
        }
        defaultFont.set(new Font(cellSize / 2f));
        mapGrid.setGridLinesVisible(showGridLines);
    }

    ObjectProperty<Font> defaultFont = new SimpleObjectProperty<>(new Font(12));

    public Label createCell(String text, int x, int y) {
        Label label = new Label(text);
        label.fontProperty().bind(defaultFont);
        label.setAlignment(Pos.CENTER);
        GridPane.setHgrow(label, Priority.ALWAYS);
        GridPane.setVgrow(label, Priority.ALWAYS);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMaxHeight(Double.MAX_VALUE);
        mapGrid.add(label, x, y);
        GridPane.setHalignment(label, HPos.CENTER);
        return label;
    }

    private static String computeStyle(int animalAmount, boolean isPlant) {
        String style = "-fx-text-fill: white;";
        if (animalAmount > 15) {
            style += "-fx-background-color: saddlebrown;";
        } else if (animalAmount > 10) {
            style += "-fx-background-color: sienna;";
        } else if (animalAmount > 5) {
            style += "-fx-background-color: chocolate;";
        } else if (animalAmount > 3) {
            style += "-fx-background-color: burlywood;";
        } else if (animalAmount > 0) {
            style += "-fx-background-color: wheat;";
        } else if (isPlant) {
            style += "-fx-background-color: green;";
        } else {
            style += "-fx-background-color: lightgreen;";
        }
        return style;
    }

    public void drawMap() {
        Platform.runLater(() -> {
            if (!mapGrid.getChildren().isEmpty())
                mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
            mapGrid.getColumnConstraints().clear();
            mapGrid.getRowConstraints().clear();
            double cellSize = calculateCellSize();
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize, cellSize, cellSize));
            mapGrid.getRowConstraints().add(new RowConstraints(cellSize, cellSize, cellSize));
            createCell("y/x", 0, 0);
            for (int i = 0; i < simulation.getWidth(); i++) {
                mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize, cellSize, cellSize));
                createCell(Integer.toString(i), i + 1, 0);
            }
            for (int j = 0; j < simulation.getHeight(); j++) {
                mapGrid.getRowConstraints().add(new RowConstraints(cellSize, cellSize, cellSize));
                createCell(Integer.toString(j), 0, j + 1);
            }
            for (int i = 0; i < simulation.getWidth(); i++) {
                for (int j = 0; j < simulation.getHeight(); j++) {
                    Vector2D pos = new Vector2D(i, j);
                    int animalAmount = simulation.getAnimalsOnPosition(pos).size();
                    boolean isPlant = simulation.isPlantOnPosition(pos);
                    Label cell = createCell(isPlant ? "*" : "", i + 1, j + 1);
                    cell.setStyle(computeStyle(animalAmount, isPlant));
                }
            }
        });
    }

    public void onStartStopClicked(ActionEvent actionEvent) {
        //if (dataLineChart.getData().isEmpty()) {
        //    XYChart.Series<Number, Number> series = new XYChart.Series<>();
        //    series.setName("Random name");
        //    series.getData().add(new XYChart.Data<>(0, 15));
        //    dataLineChart.getData().add(series);
        //}
        //XYChart.Series<Number, Number> series = dataLineChart.getData().getFirst();
        //Number x = series.getData().getLast().getXValue();
        //Random random = new Random();
        //series.getData().add(new XYChart.Data<>(x.intValue() + 1, random.nextInt(0, 50)));
        if (simulationThread == null) {
            simulationThread = new Thread(simulation);
            simulationThread.start();
        }
        if (simulation.isRunning()) {
            simulation.stop();
            startStopButton.setText("Start");
        } else {
            simulation.start();
            startStopButton.setText("Stop");
        }
    }

    public void onGridMouseClicked(MouseEvent ignored) {
        if (mouseOverPosition == null) return;
        List<Animal> animals = simulation.getAnimalsOnPosition(mouseOverPosition);
        if (animals.size() == 1)
            selectedAnimal.set(animals.getFirst());
        else
            selectedAnimal.set(null);
    }

    public void onGridMouseEnteredOrMoved(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        double cellSize = calculateCellSize();
        double gapH = (mapGrid.getWidth() - cellSize * (simulation.getWidth() + 1)) / 2;
        double gapV = (mapGrid.getHeight() - cellSize * (simulation.getHeight() + 1)) / 2;
        int colIndex = (int) ((x - gapH) / cellSize);
        int rowIndex = (int) ((y - gapV) / cellSize);
        if (colIndex < 1 || rowIndex < 1) {
            clearMouseOverCell();
            return;
        }
        Label cell = getCellByRowColumn(rowIndex, colIndex);
        if (cell != null) {
            if (this.mouseOverCell == cell) return;
            clearMouseOverCell();
            cell.setStyle(cell.getStyle() + "-fx-border-color: red; -fx-border-width: 1px;");
            this.mouseOverCell = cell;
            this.mouseOverPosition = new Vector2D(colIndex - 1, rowIndex - 1);
        }
    }

    public void onGridMouseExited(MouseEvent ignored) {
        clearMouseOverCell();
    }

    private void clearMouseOverCell() {
        if (mouseOverCell == null) return;
        this.mouseOverCell.setStyle(mouseOverCell.getStyle().replace("-fx-border-color: red; -fx-border-width: 1px;", ""));
        this.mouseOverCell = null;
        this.mouseOverPosition = null;
    }

    private Label getCellByRowColumn(int row, int col) {
        for (Node node : mapGrid.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(node);
            Integer colIndex = GridPane.getColumnIndex(node);
            rowIndex = rowIndex == null ? 0 : rowIndex;
            colIndex = colIndex == null ? 0 : colIndex;

            if (rowIndex == row && colIndex == col && node instanceof Label label) {
                return label;
            }
        }
        return null;
    }

    @Override
    public void moveAnimal(Vector2D oldPosition, Vector2D newPosition) {
        Platform.runLater(() -> {
            int oldAnimalAmount = simulation.getAnimalsOnPosition(oldPosition).size();
            int newAnimalAmount = simulation.getAnimalsOnPosition(newPosition).size();
            Label oldCell = getCellByRowColumn(oldPosition.y() + 1, oldPosition.x() + 1);
            Label newCell = getCellByRowColumn(newPosition.y() + 1, newPosition.x() + 1);
            if (oldCell != null) {
                oldCell.setStyle(computeStyle(oldAnimalAmount, simulation.isPlantOnPosition(oldPosition)));
            }
            if (newCell != null) {
                newCell.setStyle(computeStyle(newAnimalAmount, simulation.isPlantOnPosition(newPosition)));
            }
        });
    }

    @Override
    public void addPlant(Vector2D position) {
        Platform.runLater(() -> {
            Label cell = getCellByRowColumn(position.y() + 1, position.x() + 1);
            if (cell != null) {
                cell.setText("*");
                cell.setStyle(computeStyle(simulation.getAnimalsOnPosition(position).size(), true));
            }
        });
    }

    @Override
    public void addAnimal(Vector2D position) {
        Platform.runLater(() -> {
            Label cell = getCellByRowColumn(position.y() + 1, position.x() + 1);
            if (cell != null) {
                cell.setStyle(computeStyle(simulation.getAnimalsOnPosition(position).size(), false));
            }
        });
    }

    @Override
    public void removePlant(Vector2D position) {
        Platform.runLater(() -> {
            Label cell = getCellByRowColumn(position.y() + 1, position.x() + 1);
            if (cell != null) {
                cell.setText("");
                cell.setStyle(computeStyle(simulation.getAnimalsOnPosition(position).size(), false));
            }
        });
    }

    @Override
    public void removeAnimal(Vector2D position) {
        Platform.runLater(() -> {
            Label cell = getCellByRowColumn(position.y() + 1, position.x() + 1);
            if (cell != null) {
                cell.setStyle(computeStyle(simulation.getAnimalsOnPosition(position).size(),
                                           simulation.isPlantOnPosition(position)));
            }
        });
    }
}
