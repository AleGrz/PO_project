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

    public int getStartingPlantAmount() {
        return this.params.startingPlantAmount();
    }

    public int getPlantGrowingAmount() {
        return this.params.plantGrowingAmount();
    }

    public int getPlantEnergyAmount() {
        return this.params.plantEnergyAmount();
    }

    public int getStartingEnergyAmount() {
        return this.params.startingEnergyAmount();
    }

    public int getStartingAnimalAmount() {
        return this.params.startingAnimalAmount();
    }

    public int getMinimumBreedingEnergy() {
        return this.params.minimumBreedingEnergy();
    }

    public int getBreedingEnergyCost() {
        return this.params.breedingEnergyCost();
    }

    public int getMinimumMutationAmount() {
        return this.params.minimumMutationAmount();
    }

    public int getMaximumMutationAmount() {
        return this.params.maximumMutationAmount();
    }

    public int getAnimalGenomeLength() {
        return this.params.animalGenomeLength();
    }

    public int getFireFrequency() {
        return this.params.fireFrequency();
    }

    public int getFireLength() {
        return this.params.fireLength();
    }

    public int getRefreshTime() {
        return this.params.refreshTime();
    }

    public int getHeight() {
        return this.params.height();
    }

    public int getWidth() {
        return this.params.width();
    }

    public Random getRandom() {
        return this.params.random();
    }

    public AbstractMap getMap() {
        return this.map;
    }

    @Override
    public void run() {
        while (true) {
            if (isRunning) {
                map.step(step);
                step++;
            }
            try {
                Thread.sleep(getRefreshTime());
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