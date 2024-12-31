package agh.darwinworld.presenters;

import agh.darwinworld.Simulation;
import agh.darwinworld.controls.CellRegion;
import agh.darwinworld.helpers.AlertHelper;
import agh.darwinworld.models.*;
import agh.darwinworld.models.listeners.AnimalListener;
import agh.darwinworld.models.listeners.SimulationPauseListener;
import agh.darwinworld.models.listeners.SimulationStepListener;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.FutureTask;

public class SimulationPresenter implements SimulationStepListener, AnimalListener {
    @FXML
    private Label step;
    @FXML
    private Label animalCount;
    @FXML
    private Label plantCount;
    @FXML
    private Label emptyFieldCount;
    @FXML
    private Label popularGenotype;
    @FXML
    private Label averageLifetime;
    @FXML
    private Label averageDescendantsAmount;
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
    private Label fireIntervalLabel;
    @FXML
    private Label fireLengthLabel;
    @FXML
    private Label seedLabel;
    @FXML
    private Label refreshTimeLabel;
    @FXML
    private LineChart<Number, Number> dataLineChart;
    @FXML
    private GridPane mapGrid;
    @FXML
    private GridPane selectedAnimalGridPane;
    @FXML
    private Label selectedAnimalAgeLabel;
    @FXML
    private Label selectedAnimalEnergyOrDiedAtLabel;
    @FXML
    private Label selectedAnimalEnergyOrDiedAtValueLabel;
    @FXML
    private Label selectedAnimalChildrenAmountLabel;
    @FXML
    private Label selectedAnimalDescendantsAmountLabel;
    @FXML
    private Label selectedAnimalGenomeLabel;
    @FXML
    private Label selectedAnimalPlantsEatenAmountLabel;
    @FXML
    private Label selectedAnimalUuidLabel;
    @FXML
    private Label selectedAnimalCurrentGeneLabel;
    @FXML
    private Label selectedAnimalCurrentDirectionLabel;
    @FXML
    private Button startStopButton;
    @FXML
    private BorderPane rootBorderPane;
    @FXML
    private VBox leftVBox;
    @FXML
    private VBox topVBox;

    private Animal selectedAnimal;
    private Vector2D selectedAnimalPos;
    private double lastCalculatedCellSize;
    private Simulation simulation;
    private Thread simulationThread;
    private final List<SimulationPauseListener> listeners = new ArrayList<>();
    private HashMap<Vector2D, CellRegion> cells = new HashMap<>();

    @FXML
    public void initialize() {
        rootBorderPane.widthProperty().addListener((observable, oldValue, newValue) -> resizeMap());
        rootBorderPane.heightProperty().addListener((observable, oldValue, newValue) -> resizeMap());
        selectedAnimalGridPane.setVisible(false);
        Platform.runLater(() -> {
            Stage stage = (Stage) rootBorderPane.getScene().getWindow();
            stage.setOnCloseRequest(windowEvent -> stopSimulationThread());
        });
        dataLineChart.setCreateSymbols(false);
        String[] seriesNames = new String[]{"Animal count", "Plant count", "Empty field count",
                "Most popular genotype", "Average lifetime",
                "Average descendants amount"};
        for (String seriesName : seriesNames) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(seriesName);
            dataLineChart.getData().add(series);
        }
    }

    public void selectAnimal(Animal animal, Vector2D vector2D) {
        if (selectedAnimal != null)
            selectedAnimal.removeListener(this);
        selectedAnimal = animal;
        selectedAnimal.addListener(this);
        if (selectedAnimalPos != null) {
            CellRegion cell = cells.get(selectedAnimalPos);
            if (cell != null) cell.setIsSelected(false);
        }
        selectedAnimalPos = vector2D;
        CellRegion cell = cells.get(vector2D);
        if (cell != null)
            cell.setIsSelected(true);
        selectedAnimalGridPane.setVisible(true);
        selectedAnimalAgeLabel.setText(Integer.toString(selectedAnimal.getAge()));
        if (selectedAnimal.isDead()) {
            selectedAnimalEnergyOrDiedAtLabel.setText("Died at:");
            selectedAnimalEnergyOrDiedAtValueLabel.setText(Integer.toString(selectedAnimal.diedAt()));
        } else {
            selectedAnimalEnergyOrDiedAtLabel.setText("Energy:");
            selectedAnimalEnergyOrDiedAtValueLabel.setText(Integer.toString(selectedAnimal.getEnergy()));
        }
        selectedAnimalChildrenAmountLabel.setText(Integer.toString(selectedAnimal.getChildrenAmount()));
        selectedAnimalDescendantsAmountLabel.setText(Integer.toString(selectedAnimal.getDescendantsAmount()));
        MoveDirection[] genome = selectedAnimal.getGenome();
        StringBuilder genomeString = new StringBuilder();
        for (MoveDirection gene : genome) {
            genomeString.append(gene.ordinal());
        }
        selectedAnimalGenomeLabel.setText(genomeString.toString());
        selectedAnimalPlantsEatenAmountLabel.setText(Integer.toString(selectedAnimal.getTotalEatenPlants()));
        selectedAnimalUuidLabel.setText(selectedAnimal.getUuid().toString());
        selectedAnimalCurrentGeneLabel.setText(Integer.toString(selectedAnimal.getCurrentGene().ordinal()));
        selectedAnimalCurrentDirectionLabel.setText(selectedAnimal.getDirection().toString());
    }

    private void unselectAnimal() {
        if (selectedAnimal != null)
            selectedAnimal.removeListener(this);
        selectedAnimal = null;
        if (selectedAnimalPos != null) {
            CellRegion cell = cells.get(selectedAnimalPos);
            if (cell != null) cell.setIsSelected(false);
        }
        selectedAnimalPos = null;
        selectedAnimalGridPane.setVisible(false);
    }

    public void setSimulation(Simulation simulation) {
        if (this.simulation != null) {
            this.simulation.removeStepListener(this);
            stopSimulationThread();
        }
        this.simulation = simulation;
        this.simulation.addStepListener(this);
        unselectAnimal();
        Platform.runLater(() -> {
            SimulationParameters p = simulation.getParameters();
            heightLabel.setText(Integer.toString(p.height()));
            widthLabel.setText(Integer.toString(p.width()));
            startingPlantAmountLabel.setText(Integer.toString(p.startingPlantAmount()));
            plantGrowingAmountLabel.setText(Integer.toString(p.plantGrowingAmount()));
            plantEnergyAmountLabel.setText(Integer.toString(p.plantEnergyAmount()));
            startingEnergyAmountLabel.setText(Integer.toString(p.startingEnergyAmount()));
            startingAnimalAmountLabel.setText(Integer.toString(p.startingAnimalAmount()));
            minimumBreedingEnergyLabel.setText(Integer.toString(p.minimumBreedingEnergy()));
            breedingEnergyCostLabel.setText(Integer.toString(p.breedingEnergyCost()));
            minimumMutationAmountLabel.setText(Integer.toString(p.minimumMutationAmount()));
            maximumMutationAmountLabel.setText(Integer.toString(p.maximumMutationAmount()));
            animalGenomeLengthLabel.setText(Integer.toString(p.animalGenomeLength()));
            fireIntervalLabel.setText(Integer.toString(p.fireInterval()));
            fireLengthLabel.setText(Integer.toString(p.fireLength()));
            refreshTimeLabel.setText(Integer.toString(p.refreshTime()));
            seedLabel.setText("NIE DZIALA TBD");
            drawMap();
        });
    }

    private void stopSimulationThread() {
        Stage stage = (Stage) rootBorderPane.getScene().getWindow();
        if (simulationThread != null && simulationThread.isAlive()) {
            try {
                simulationThread.interrupt();
            } catch (SecurityException e) {
                Platform.runLater(() -> AlertHelper.ShowExceptionAlert(stage, e));
            }
        }
    }

    public double calculateCellSize() {
        double gridWidth = rootBorderPane.getWidth() - leftVBox.getWidth() - 20; // 20 - padding
        double gridHeight = rootBorderPane.getHeight() - topVBox.getHeight() - 20; // 20 - padding
        SimulationParameters p = simulation.getParameters();
        int rowCount = p.width() + 1;
        int colCount = p.height() + 1;
        double width = gridWidth / rowCount;
        double height = gridHeight / colCount;
        return Math.min(height, width);
    }

    public void resizeMap() {
        double cellSize = calculateCellSize();
        if (lastCalculatedCellSize == cellSize) return;
        lastCalculatedCellSize = cellSize;
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
        boldFont.set(Font.font("System", FontWeight.BOLD, cellSize / 2f));
        mapGrid.setGridLinesVisible(showGridLines);
    }

    ObjectProperty<Font> defaultFont = new SimpleObjectProperty<>(new Font(12));
    ObjectProperty<Font> boldFont = new SimpleObjectProperty<>(Font.font("System", FontWeight.BOLD, 12));

    private void createLabelCell(String text, int x, int y) {
        SimulationParameters p = simulation.getParameters();
        Label cell = new Label(text);
        cell.fontProperty().bind(p.map().isPrefferedRow(p.height() - y) ? boldFont : defaultFont);
        cell.setAlignment(Pos.CENTER);
        GridPane.setHgrow(cell, Priority.ALWAYS);
        GridPane.setVgrow(cell, Priority.ALWAYS);
        cell.setMaxWidth(Double.MAX_VALUE);
        cell.setMaxHeight(Double.MAX_VALUE);
        mapGrid.add(cell, x, y);
        GridPane.setHalignment(cell, HPos.CENTER);
    }

    public void drawMap() {
        SimulationParameters p = simulation.getParameters();
        cells = new HashMap<>();
        Platform.runLater(() -> {
            mapGrid.setDisable(true);
            if (!mapGrid.getChildren().isEmpty())
                mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
            mapGrid.getColumnConstraints().clear();
            mapGrid.getRowConstraints().clear();
            double cellSize = calculateCellSize();
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize, cellSize, cellSize));
            mapGrid.getRowConstraints().add(new RowConstraints(cellSize, cellSize, cellSize));
            createLabelCell("y/x", 0, 0);
            for (int i = 0; i < p.width(); i++) {
                mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize, cellSize, cellSize));
                createLabelCell(Integer.toString(i), i + 1, 0);
            }
            for (int j = 0; j < p.height(); j++) {
                mapGrid.getRowConstraints().add(new RowConstraints(cellSize, cellSize, cellSize));
                createLabelCell(Integer.toString(p.height() - j - 1), 0, j + 1);
            }
            int maxAnimalAmount = p.map().getMaxAnimalAmount();
            int maxFireLength = p.fireLength();
            for (int i = 0; i < p.width(); i++) {
                for (int j = 0; j < p.height(); j++) {
                    Vector2D pos = new Vector2D(i, p.height() - j - 1);
                    int animalAmount = p.map().getAnimalsOnPosition(pos).size();
                    boolean isPlant = p.map().isPlantOnPosition(pos);
                    CellRegion cell = new CellRegion(isPlant, animalAmount, maxAnimalAmount, 0, maxFireLength);
                    GridPane.setHgrow(cell, Priority.ALWAYS);
                    GridPane.setVgrow(cell, Priority.ALWAYS);
                    mapGrid.add(cell, i + 1, j + 1);
                    cells.put(pos, cell);
                }
            }
            mapGrid.setDisable(false);
        });
    }

    public void onStartStopClicked(ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        try {
            if (simulationThread == null) {
                simulationThread = new Thread(simulation);
                simulationThread.setUncaughtExceptionHandler((thread, throwable) -> {
                    Platform.runLater(() -> {
                        AlertHelper.ShowExceptionAlert(currentStage, throwable);
                        startStopButton.setDisable(true);
                    });
                });
                simulationThread.start();
            }
            if (simulation.isRunning()) {
                simulation.stop();
                startStopButton.setText("Start");
                listeners.forEach(SimulationPauseListener::onSimulationPaused);
            } else {
                simulation.start();
                startStopButton.setText("Stop");
            }
        } catch (Exception e) {
            AlertHelper.ShowExceptionAlert(currentStage, e);
        }
    }

    public void onGridMouseClicked(MouseEvent mouseEvent) {
        SimulationParameters p = simulation.getParameters();
        Stage currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        double cellSize = calculateCellSize();
        double gapH = (mapGrid.getWidth() - cellSize * (p.width() + 1)) / 2;
        double gapV = (mapGrid.getHeight() - cellSize * (p.height() + 1)) / 2;
        int colIndex = (int) ((x - gapH) / cellSize) - 1;
        int rowIndex = p.height() - (int) ((y - gapV) / cellSize);
        Vector2D mouseOverPosition = new Vector2D(colIndex, rowIndex);
        List<Animal> animals = p.map().getAnimalsOnPosition(mouseOverPosition);
        if (animals.size() == 1) {
            selectAnimal(animals.getFirst(), mouseOverPosition);
        } else if (animals.size() > 1) {
            Stage modal = new Stage();
            modal.setX(mouseEvent.getScreenX());
            modal.setY(mouseEvent.getScreenY());
            modal.initModality(Modality.WINDOW_MODAL);
            modal.initOwner(currentStage);
            modal.initStyle(StageStyle.UNDECORATED);
            VBox content = new VBox(10);
            boolean isRunning = simulation.isRunning();
            for (int i = 0; i < animals.size(); i++) {
                Button button = new Button("Animal " + (i + 1));
                button.setFont(new Font(11));
                final int index = i;
                button.setOnAction(event -> {
                    selectAnimal(animals.get(index), mouseOverPosition);
                    modal.close();
                    if (isRunning)
                        simulation.start();
                });
                content.getChildren().add(button);
            }
            content.setPadding(new Insets(5, 5, 5, 5));
            ScrollPane scrollPane = new ScrollPane(content);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            scrollPane.setFitToWidth(true);
            scrollPane.getStylesheets().add("styles.css");
            Scene scene = new Scene(scrollPane);
            modal.setScene(scene);
            modal.setMaxHeight(100);
            simulation.stop();
            modal.show();
        } else {
            unselectAnimal();
        }
    }

    @Override
    public void updateAnimal(Vector2D position, int animalCount, int maxAnimalCount) {
        Platform.runLater(() -> {
            CellRegion cell = cells.get(position);
            if (cell != null) {
                cell.setAnimalAmount(animalCount, maxAnimalCount);
            }
        });
    }

    @Override
    public void updateFire(Vector2D position, int length) {
        Platform.runLater(() -> {
            CellRegion cell = cells.get(position);
            if (cell != null) {
                cell.setCurrentFireStage(length);
            }
        });
    }

    @Override
    public void updateStatistics(int step, int animalCount, int plantCount, int emptyFieldCount, String popularGenotype, int averageLifetime, int averageDescendantsAmount) {
        Platform.runLater(() -> {
            this.step.setText(Integer.toString(step));
            this.animalCount.setText(Integer.toString(animalCount));
            this.plantCount.setText(Integer.toString(plantCount));
            this.emptyFieldCount.setText(Integer.toString(emptyFieldCount));
            this.popularGenotype.setText(popularGenotype);
            this.averageLifetime.setText(Integer.toString(averageLifetime));
            this.averageDescendantsAmount.setText(Integer.toString(averageDescendantsAmount));
            XYChart.Series<Number, Number> animalCountSeries = dataLineChart.getData().get(0);
            XYChart.Series<Number, Number> plantCountSeries = dataLineChart.getData().get(1);
            XYChart.Series<Number, Number> emptyFieldCountSeries = dataLineChart.getData().get(2);
            XYChart.Series<Number, Number> popularGenotypeSeries = dataLineChart.getData().get(3);
            XYChart.Series<Number, Number> averageLifetimeSeries = dataLineChart.getData().get(4);
            XYChart.Series<Number, Number> averageDescendantsAmountSeries = dataLineChart.getData().get(5);
            animalCountSeries.getData().add(new XYChart.Data<>(step, animalCount));
            plantCountSeries.getData().add(new XYChart.Data<>(step, plantCount));
            emptyFieldCountSeries.getData().add(new XYChart.Data<>(step, emptyFieldCount));
            averageLifetimeSeries.getData().add(new XYChart.Data<>(step, averageLifetime));
            averageDescendantsAmountSeries.getData().add(new XYChart.Data<>(step, averageDescendantsAmount));
        });
    }

    @Override
    public void addPlant(Vector2D position) {
        Platform.runLater(() -> {
            CellRegion cell = cells.get(position);
            if (cell != null) {
                cell.setHasPlant(true);
            }
        });
    }

    @Override
    public void removePlant(Vector2D position) {
        Platform.runLater(() -> {
            CellRegion cell = cells.get(position);
            if (cell != null) {
                cell.setHasPlant(false);
            }
        });
    }
  
    @Override
    public void move(Vector2D oldPosition, Vector2D newPosition) {
        Platform.runLater(() -> {
            selectedAnimalPos = newPosition;
            if (oldPosition != null) {
                CellRegion cell = cells.get(oldPosition);
                if (cell != null)
                    cell.setIsSelected(false);
            }
            if (newPosition != null) {
                CellRegion cell = cells.get(newPosition);
                if (cell != null)
                    cell.setIsSelected(true);
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        FutureTask<?> task = switch (evt.getPropertyName()) {
            case "energy" -> new FutureTask<>(() -> {
                if (evt.getNewValue() instanceof Integer i && i < 0) return;
                selectedAnimalEnergyOrDiedAtValueLabel.setText(evt.getNewValue().toString());
            }, null);
            case "diedAt" -> new FutureTask<>(() -> {
                selectedAnimalEnergyOrDiedAtLabel.setText("Died at:");
                selectedAnimalEnergyOrDiedAtValueLabel.setText(evt.getNewValue().toString());
            }, null);
            case "age" -> new FutureTask<>(() -> selectedAnimalAgeLabel.setText(evt.getNewValue().toString()), null);
            case "childrenAmount" -> new FutureTask<>(() -> selectedAnimalChildrenAmountLabel.setText(evt.getNewValue().toString()), null);
            case "descendantsAmount" -> new FutureTask<>(() -> selectedAnimalDescendantsAmountLabel.setText(evt.getNewValue().toString()), null);
            case "totalEatenPlants" -> new FutureTask<>(() -> selectedAnimalPlantsEatenAmountLabel.setText(evt.getNewValue().toString()), null);
            case "currentGene" -> new FutureTask<>(() -> {
                if (!(evt.getNewValue() instanceof MoveDirection moveDirection)) return;
                selectedAnimalCurrentGeneLabel.setText(Integer.toString(moveDirection.ordinal()));
            }, null);
            case "direction" -> new FutureTask<>(() -> selectedAnimalCurrentDirectionLabel.setText(evt.getNewValue().toString()), null);
            default -> null;
        };
        if (task != null)
            Platform.runLater(task);
    }

    public void addPauseListener(SimulationPauseListener listener) {
        this.listeners.add(listener);
    }
}
