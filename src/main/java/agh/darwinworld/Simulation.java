package agh.darwinworld;

import agh.darwinworld.models.*;
import agh.darwinworld.models.listeners.SimulationStepListener;
import agh.darwinworld.models.maps.AbstractMap;
import agh.darwinworld.presenters.SimulationPresenter;

import java.util.*;

public class Simulation implements Runnable {
    private final SimulationParameters params;
    private final AbstractMap map;

    private int step = 1;
    private boolean isRunning = false;

    public Simulation(SimulationParameters params) {
        this.params = params;
        this.map = params.mapType().getMap();
        this.map.setParameters(params);
        this.map.setRandom(new Random(params.seed()));
        map.populateAnimals(params.startingAnimalAmount());
        map.growPlants(params.startingPlantAmount());
    }

    public SimulationParameters getParameters() {
        return this.params;
    }

    @Override
    public void run() {
        while (true) {
            if (isRunning) {
                long deltaTime = System.currentTimeMillis();
                map.step(step);
                long elapsed = System.currentTimeMillis() - deltaTime;
                System.out.println("Step " + step + " took " + elapsed + "ms");
                step++;
            }
            try {
                //noinspection BusyWait
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

    public AbstractMap getMap() {
        return this.map;
    }
}