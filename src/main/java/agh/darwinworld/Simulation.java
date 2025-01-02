package agh.darwinworld;

import agh.darwinworld.models.*;
import agh.darwinworld.models.listeners.SimulationStepListener;
import agh.darwinworld.models.maps.AbstractMap;
import agh.darwinworld.presenters.SimulationPresenter;

/**
 * The {@code Simulation} class represents a runnable simulation of a Darwinian world.
 * It manages the lifecycle of the simulation and interacts with the map and its elements.
 */
public class Simulation implements Runnable {
    private final SimulationParameters params;
    private final AbstractMap map;

    private int step = 1;
    private boolean isRunning = false;

    /**
     * Constructs a {@code Simulation} with the specified parameters.
     *
     * @param params the parameters for the simulation
     */
    public Simulation(SimulationParameters params) {
        this.params = params;
        this.map = params.mapType().createMap();
        this.map.setParameters(params);
        map.populateAnimals(params.startingAnimalAmount());
        map.growPlants(params.startingPlantAmount());
    }

    /**
     * Returns the parameters of the simulation.
     *
     * @return the simulation parameters
     */
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

    /**
     * Starts the simulation.
     */
    public void start() {
        isRunning = true;
    }

    /**
     * Stops the simulation.
     */
    public void stop() {
        isRunning = false;
    }

    /**
     * Checks if the simulation is running.
     *
     * @return {@code true} if the simulation is running, otherwise {@code false}.
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Adds a step listener to the simulation.
     *
     * @param listener the listener to be added
     */
    public void addStepListener(SimulationStepListener listener) {
        map.addStepListener(listener);
    }

    /**
     * Removes a step listener from the simulation.
     *
     * @param simulationPresenter the listener to be removed
     */
    public void removeStepListener(SimulationPresenter simulationPresenter) {
        map.removeStepListener(simulationPresenter);
    }

    /**
     * Returns the map used in the simulation.
     *
     * @return the simulation map
     */
    public AbstractMap getMap() {
        return this.map;
    }
}