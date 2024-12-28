package agh.darwinworld.presenter;

import agh.darwinworld.Simulation;
import agh.darwinworld.helper.AlertHelper;
import agh.darwinworld.model.Animal;
import agh.darwinworld.model.MoveDirection;
import agh.darwinworld.model.SimulationStepListener;
import agh.darwinworld.model.Vector2D;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.util.List;

public class SimulationPresenter implements SimulationStepListener {
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
    private GridPane mapGrid;
    @FXML
    private GridPane selectedAnimalGridPane;
    @FXML
    private Label selectedAnimalAgeLabel;
    @FXML
    private Label selectedAnimalEnergyLabel;
    @FXML
    private Label selectedAnimalChildrenAmountLabel;
    @FXML
    private Label selectedAnimalDescendantsAmountLabel;
    @FXML
    private Label selectedAnimalGenomeLabel;
    @FXML
    private Label selectedAnimalPlantsEatenAmountLabel;
    @FXML
    private Label selectedAnimalDiedAtLabel;
    @FXML
    private Button startStopButton;
    @FXML
    private BorderPane rootBorderPane;
    @FXML
    private VBox leftVBox;
    @FXML
    private VBox topVBox;

    private final SimpleObjectProperty<Pair<Vector2D, Animal>> selectedAnimal = new SimpleObjectProperty<>(null);
    private double lastCalculatedCellSize;
    private Simulation simulation;
    private Thread simulationThread;

    @FXML
    public void initialize() {
        rootBorderPane.widthProperty().addListener((observable, oldValue, newValue) -> resizeMap());
        rootBorderPane.heightProperty().addListener((observable, oldValue, newValue) -> resizeMap());
        selectedAnimalGridPane.setVisible(false);
        selectedAnimal.addListener((observable, oldValue, newValue) -> updateSelectedAnimalData(oldValue, newValue));
        Platform.runLater(() -> {
            Stage stage = (Stage) rootBorderPane.getScene().getWindow();
            stage.setOnCloseRequest(windowEvent -> stopSimulationThread());
        });
    }

    public void updateSelectedAnimalData(Pair<Vector2D, Animal> oldValue, Pair<Vector2D, Animal> newValue) {
        Platform.runLater(() -> {
            Vector2D oldPosition = oldValue == null ? null : oldValue.getKey();
            Vector2D newPosition = newValue == null ? null : newValue.getKey();

            if ((newPosition != null && !newPosition.equals(oldPosition)) ||
                    (oldPosition != null && !oldPosition.equals(newPosition))) {
                if (oldPosition != null) {
                    Region oldCell = getCellByRowColumn(oldPosition, false);
                    if (oldCell != null) oldCell.getStyleClass().remove("selected");
                }
                if (newPosition != null) {
                    Region newCell = getCellByRowColumn(newPosition, false);
                    if (newCell != null) newCell.getStyleClass().add("selected");
                }
            }
            selectedAnimalGridPane.setVisible(newValue != null);
            if (newValue == null) return;
            Animal newAnimal = newValue.getValue();
            selectedAnimalAgeLabel.setText(Integer.toString(newAnimal.getAge()));
            selectedAnimalEnergyLabel.setText(Integer.toString(newAnimal.getEnergy()));
            selectedAnimalChildrenAmountLabel.setText(Integer.toString(newAnimal.getChildrenAmount()));
            selectedAnimalDescendantsAmountLabel.setText("MISSING DATA");
            MoveDirection[] genome = newAnimal.getGenome();
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
        if (this.simulation != null) {
            this.simulation.removeStepListener(this);
            stopSimulationThread();
        }
        this.simulation = simulation;
        this.simulation.addStepListener(this);
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
        int rowCount = simulation.getWidth() + 1;
        int colCount = simulation.getHeight() + 1;
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
        mapGrid.setGridLinesVisible(showGridLines);
    }

    ObjectProperty<Font> defaultFont = new SimpleObjectProperty<>(new Font(12));

    public Label createLabelCell(String text, int x, int y) {
        Label cell = new Label(text);
        cell.fontProperty().bind(defaultFont);
        cell.setAlignment(Pos.CENTER);
        GridPane.setHgrow(cell, Priority.ALWAYS);
        GridPane.setVgrow(cell, Priority.ALWAYS);
        cell.setMaxWidth(Double.MAX_VALUE);
        cell.setMaxHeight(Double.MAX_VALUE);
        mapGrid.add(cell, x, y);
        GridPane.setHalignment(cell, HPos.CENTER);
        return cell;
    }

    public Region createCell(int x, int y, String styleClass) {
        Region cell = new Region();
        GridPane.setHgrow(cell, Priority.ALWAYS);
        GridPane.setVgrow(cell, Priority.ALWAYS);
        cell.setMaxWidth(Double.MAX_VALUE);
        cell.setMaxHeight(Double.MAX_VALUE);
        for (String style : styleClass.split(" ")) {
            cell.getStyleClass().add(style);
        }
        mapGrid.add(cell, x, y);
        GridPane.setHalignment(cell, HPos.CENTER);
        return cell;
    }

    private static String computeStyle(int animalAmount) {
        String style;
        if (animalAmount > 15) {
            style = "-fx-background-color: saddlebrown;";
        } else if (animalAmount > 10) {
            style = "-fx-background-color: sienna;";
        } else if (animalAmount > 5) {
            style = "-fx-background-color: chocolate;";
        } else if (animalAmount > 3) {
            style = "-fx-background-color: burlywood;";
        } else if (animalAmount > 0) {
            style = "-fx-background-color: wheat;";
        } else {
            style = "-fx-background-color: transparent;";
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
            createLabelCell("y/x", 0, 0);
            for (int i = 0; i < simulation.getWidth(); i++) {
                mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize, cellSize, cellSize));
                createLabelCell(Integer.toString(i), i + 1, 0);
            }
            for (int j = 0; j < simulation.getHeight(); j++) {
                mapGrid.getRowConstraints().add(new RowConstraints(cellSize, cellSize, cellSize));
                createLabelCell(Integer.toString(simulation.getHeight() - j - 1), 0, j + 1);
            }
            for (int i = 0; i < simulation.getWidth(); i++) {
                for (int j = 0; j < simulation.getHeight(); j++) {
                    Vector2D pos = new Vector2D(i, simulation.getHeight() - j - 1);
                    int animalAmount = simulation.getAnimalsOnPosition(pos).size();
                    boolean isPlant = simulation.isPlantOnPosition(pos);
                    Region cell = createCell(i + 1, j + 1, "");
                    if (isPlant) {
                        cell.setStyle("-fx-background-color: green;");
                    } else {
                        cell.setStyle("-fx-background-color: lightgreen;");
                    }
                    Region animal = createCell(i + 1, j + 1, "cell animal");
                    animal.setShape(new Circle(1));
                    animal.setStyle(computeStyle(animalAmount));
                }
            }
        });
    }

    public void onStartStopClicked(ActionEvent actionEvent) {
        //if (dataLineChart.getData().isEmpty()) {
        //    XYChart.Series<Number, Number> series = new XYChart.Series<>();
        //    series.setName("Random name");
        //    series.getData().add(new XYChart.Data<>(0, 15));
        //    dataLineChart.getData().add(series);2
        //}
        //XYChart.Series<Number, Number> series = dataLineChart.getData().getFirst();
        //Number x = series.getData().getLast().getXValue();
        //Random random = new Random();
        //series.getData().add(new XYChart.Data<>(x.intValue() + 1, random.nextInt(0, 50)));
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
            } else {
                simulation.start();
                startStopButton.setText("Stop");
            }
        } catch (Exception e) {
            AlertHelper.ShowExceptionAlert(currentStage, e);
        }
    }

    public void onGridMouseClicked(MouseEvent mouseEvent) {
        Stage currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        double cellSize = calculateCellSize();
        double gapH = (mapGrid.getWidth() - cellSize * (simulation.getWidth() + 1)) / 2;
        double gapV = (mapGrid.getHeight() - cellSize * (simulation.getHeight() + 1)) / 2;
        int colIndex = (int) ((x - gapH) / cellSize) - 1;
        int rowIndex = simulation.getHeight() - (int) ((y - gapV) / cellSize);
        Vector2D mouseOverPosition = new Vector2D(colIndex, rowIndex);
        List<Animal> animals = simulation.getAnimalsOnPosition(mouseOverPosition);
        if (animals.size() == 1) {
            selectedAnimal.set(new Pair<>(mouseOverPosition, animals.getFirst()));
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
                    selectedAnimal.set(new Pair<>(mouseOverPosition, animals.get(index)));
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
            selectedAnimal.set(null);
        }
    }


    private Region getCellByRowColumn(Vector2D pos, boolean isBackground) {
        for (Node node : mapGrid.getChildren()) {
            if (node instanceof Label) continue;
            if (node.getStyleClass().stream().noneMatch("animal"::equals) && !isBackground) continue;
            if (node.getStyleClass().stream().anyMatch("animal"::equals) && isBackground) continue;
            int rowIndex = GridPane.getRowIndex(node);
            int colIndex = GridPane.getColumnIndex(node);
            if (pos.equals(new Vector2D(colIndex - 1, simulation.getHeight() - rowIndex))) {
                return (Region) node;
            }
        }
        return null;
    }

    @Override
    public void moveAnimal(Vector2D oldPosition, Vector2D newPosition) {
        Platform.runLater(() -> {
            int oldAnimalAmount = simulation.getAnimalsOnPosition(oldPosition).size();
            List<Animal> newAnimals = simulation.getAnimalsOnPosition(newPosition);
            int newAnimalAmount = newAnimals.size();
            Region oldCell = getCellByRowColumn(oldPosition, false);
            Region newCell = getCellByRowColumn(newPosition, false);
            if (oldCell != null) {
                oldCell.setStyle(computeStyle(oldAnimalAmount));
            }
            if (newCell != null) {
                newCell.setStyle(computeStyle(newAnimalAmount));
            }
            Pair<Vector2D, Animal> animal = selectedAnimal.get();
            if (animal != null && oldPosition.equals(animal.getKey()) && newAnimals.contains(animal.getValue())) {
                selectedAnimal.set(new Pair<>(newPosition, animal.getValue()));
            }
        });
    }

    @Override
    public void updateAnimal(Vector2D position) {
        Platform.runLater(() -> {
            Region cell = getCellByRowColumn(position, false);
            if (cell != null) {
                cell.setStyle(computeStyle(simulation.getAnimalsOnPosition(position).size()));
            }
        });
    }

    @Override
    public void addFire(Vector2D position) {
        Platform.runLater(() -> {
            Region cell = getCellByRowColumn(position, true);
            if (cell != null) {
                cell.setStyle("-fx-background-color: orange;");
            }
        });
    }

    @Override
    public void removeFire(Vector2D position) {
        Platform.runLater(() -> {
            Region cell = getCellByRowColumn(position, true);
            if (cell != null) {
                cell.setStyle("-fx-background-color: lightgreen;");
            }
        });
    }

    @Override
    public void addPlant(Vector2D position) {
        Platform.runLater(() -> {
            Region cell = getCellByRowColumn(position, true);
            if (cell != null) {
                cell.setStyle("-fx-background-color: green;");
            }
        });
    }

    @Override
    public void removePlant(Vector2D position) {
        Platform.runLater(() -> {
            Region cell = getCellByRowColumn(position, true);
            if (cell != null) {
                cell.setStyle("-fx-background-color: lightgreen;");
            }
        });
    }


}
