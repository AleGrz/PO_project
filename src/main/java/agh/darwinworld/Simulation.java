package agh.darwinworld;

import agh.darwinworld.model.*;
import agh.darwinworld.model.AbstractMap;
import agh.darwinworld.presenter.SimulationPresenter;

import java.util.*;

public class Simulation implements Runnable {
    private final SimulationParameters params;
    private final AbstractMap map;
    private int step;

    private boolean isRunning = false;

    public Simulation(SimulationParameters params) {
        this.params = params;
        this.map = params.map();
        this.map.setParameters(params);
        map.populateAnimals(params.startingAnimalAmount());
        map.growPlants(params.startingPlantAmount());
    }

    public SimulationParameters getParameters() { return this.params; }

    @Override
    public void run() {
        while (true) {
            if (isRunning) {
                map.step(step);
                step++;
            }
            try {
                Thread.sleep(this.params.refreshTime());
            } catch (InterruptedException e) {
                System.out.println("Stopping simulation loop's sleep!");
                return;
            }
        }
    }

    public void start() {
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void addStepListener(SimulationStepListener listener) {
        map.addStepListener(listener);
    }

    public void removeStepListener(SimulationPresenter simulationPresenter) {
        map.removeStepListener(simulationPresenter);
    }
}