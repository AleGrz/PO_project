package agh.darwinworld.presenter;

import agh.darwinworld.Simulation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import javafx.scene.input.MouseEvent;

public class SimulationPresenter {
    private Simulation simulation;
    @FXML
    GridPane mapGrid;

    public SimulationPresenter() {

    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public void onStartStopClicked(ActionEvent actionEvent) {

    }

    public void onGridClicked(MouseEvent mouseEvent) {

    }
}
