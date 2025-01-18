package agh.darwinworld.presenters;

import agh.darwinworld.Simulation;
import agh.darwinworld.controls.IntField;
import agh.darwinworld.helpers.AlertHelper;
import agh.darwinworld.helpers.StageHelper;
import agh.darwinworld.models.*;
import agh.darwinworld.models.animals.AnimalType;
import agh.darwinworld.models.exceptions.UserFriendlyException;
import agh.darwinworld.models.maps.MapType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class StartMenuPresenter implements Initializable {
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
    private ComboBox<MapType> mapTypeComboBox;
    @FXML
    private ComboBox<AnimalType> behaviourComboBox;
    @FXML
    private Label fireLengthLabel;
    @FXML
    private Label fireIntervalLabel;
    @FXML
    private ListView<String> listView;
    @FXML
    private TextField simulationNameTextField;
    @FXML
    private Button loadButton;
    @FXML
    private Button deleteButton;

    private CsvPrinter csvListener;
    private List<String> saves;
    private final File dir = new File(System.getenv("APPDATA") + "/DarwinWorld");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!dir.exists()) {
            if(!dir.mkdirs()) {
                AlertHelper.showUserFriendlyExceptionAlert(null, new UserFriendlyException("Directory error", "Could not create simulations directory."));
            }
        }

        Optional<File[]> files = Optional.ofNullable(dir.listFiles((dir1, name) -> name.endsWith(".json")));
        saves = Arrays.stream(files.orElse(new File[0]))
                .map(File::getName)
                .map(name -> name.replace(".json", ""))
                .collect(Collectors.toList());

        listView.setItems(FXCollections.observableArrayList(saves));

        generateSeed();
        ObservableList<MapType> mapTypes = FXCollections.observableArrayList(MapType.values());
        mapTypeComboBox.setItems(mapTypes);
        mapTypeComboBox.getSelectionModel().select(MapType.WORLD);
        ObservableList<AnimalType> animalTypes = FXCollections.observableArrayList(AnimalType.values());
        behaviourComboBox.setItems(animalTypes);
        behaviourComboBox.getSelectionModel().select(AnimalType.ANIMAL);
        Platform.runLater(this::updateLayout);
    }

    public void onMapTypeSelectionChanged(ActionEvent ignored) {
        updateLayout();
    }

    public void onSimulationStart(ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        assert currentStage != null;
        try {
            MapType mapType = mapTypeComboBox.getSelectionModel().getSelectedItem();
            AnimalType animalType = behaviourComboBox.getSelectionModel().getSelectedItem();
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
                    mapType,
                    animalType
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
        MapType mapType = mapTypeComboBox.getSelectionModel().getSelectedItem();
        boolean v = mapType.equals(MapType.FIRE);
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

    public void onDelete(ActionEvent ignored) {
        csvButton.setText("Choose");
        csvButton.getStyleClass().remove("success");
        csvListener = null;
    }

    public void onSaveConfig(ActionEvent actionEvent) {
        boolean overwrite = false;
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        try {
            if (simulationNameTextField.getText().isEmpty()) {
                throw new UserFriendlyException("Invalid name", "Simulation name cannot be empty.");
            }
            if(saves.contains(simulationNameTextField.getText())) {
                overwrite = AlertHelper.showConfirmationAlert(currentStage, "Simulation already exists", "Do you want to overwrite the existing simulation?");
                if (!overwrite) {
                    return;
                }
            }
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
                    mapTypeComboBox.getSelectionModel().getSelectedItem(),
                    behaviourComboBox.getSelectionModel().getSelectedItem()
            );
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    throw new UserFriendlyException("Directory error", "Could not create simulations directory.");
                }
            }
            String file = new File(dir, simulationNameTextField.getText() + ".json").getPath();
            params.saveToJson(file);
            if (!overwrite) {
                listView.getItems().add(simulationNameTextField.getText());
                saves.add(simulationNameTextField.getText());
            }
        } catch (UserFriendlyException e) {
            AlertHelper.showUserFriendlyExceptionAlert(currentStage, e);
        }
    }

    public void onItemSelected(MouseEvent _ignored) {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            loadButton.setDisable(false);
            deleteButton.setDisable(false);
        }
    }

    public void onLoad(ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        try {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                throw new UserFriendlyException("Invalid selection", "Please select a simulation to load.");
            }
            SimulationParameters params = SimulationParameters.createFromJson(new File(dir, selected + ".json"));
            widthIntField.setValue(params.width());
            heightIntField.setValue(params.height());
            startingPlantAmountIntField.setValue(params.startingPlantAmount());
            plantGrowingAmountIntField.setValue(params.plantGrowingAmount());
            plantEnergyAmountIntField.setValue(params.plantEnergyAmount());
            startingAnimalAmountIntField.setValue(params.startingAnimalAmount());
            startingEnergyAmountIntField.setValue(params.startingEnergyAmount());
            minimumBreedingEnergyIntField.setValue(params.minimumBreedingEnergy());
            breedingEnergyCostIntField.setValue(params.breedingEnergyCost());
            minimumMutationAmountIntField.setValue(params.minimumMutationAmount());
            maximumMutationAmountIntField.setValue(params.maximumMutationAmount());
            animalGenomeLengthIntField.setValue(params.animalGenomeLength());
            fireIntervalIntField.setValue(params.fireInterval());
            fireLengthIntField.setValue(params.fireLength());
            refreshTimeIntField.setValue(params.refreshTime());
            seedIntField.setValue(params.seed());
            mapTypeComboBox.getSelectionModel().select(params.mapType());
            behaviourComboBox.getSelectionModel().select(params.animalType());
        } catch (UserFriendlyException e) {
            AlertHelper.showUserFriendlyExceptionAlert(currentStage, e);
        }
    }

    public void onDeleteSimulation(ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        try {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                throw new UserFriendlyException("Invalid selection", "Please select a simulation to delete.");
            }
            File file = new File(dir, selected + ".json");
            if (!file.delete()) {
                throw new UserFriendlyException("Delete error", "Could not delete the simulation file.");
            }
            listView.getItems().remove(selected);
            saves.remove(selected);
        } catch (UserFriendlyException e) {
            AlertHelper.showUserFriendlyExceptionAlert(currentStage, e);
        }
    }
}