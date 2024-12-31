package agh.darwinworld.models.listeners;

import agh.darwinworld.models.MoveDirection;
import agh.darwinworld.models.Vector2D;
import javafx.util.Pair;

import java.io.IOException;

/**
 * Interface for listening to simulation step events in a simulation.
 * Provides methods for tracking movements and updates to the world elements
 * such as animals and plants.
 */
public interface SimulationStepListener {
    /**
     * Called whenever a plant is added to the simulation.
     * @param position the position where the plant is added to.
     */
    void addPlant(Vector2D position);

    /**
     * Called whenever a plant is removed from the simulation.
     * @param position the position where the plant is removed from.
     */
    void removePlant(Vector2D position);

    /**
     * Called whenever an animal is updated in the simulation.
     * @param position the position of the animal that is updated.
     * @param animalCount amount of animals on provided position.
     * @param maxAnimalCount maximum amount of animals on one tile.
     */
    void updateAnimal(Vector2D position, int animalCount, int maxAnimalCount);

    /**
     * Called whenever simulation statistic have been updated.
     * @param step current simulation step.
     * @param animalCount amount of animals on the map.
     * @param plantCount amount of plants on the map.
     * @param emptyFieldCount amount of empty fields on the map.
     * @param popularGenome the most popular genome of the animals.
     * @param averageLifetime average lifetime of the animals.
     * @param averageDescendantsAmount average descendants amount of the animals.
     */
    void updateStatistics(int step, int animalCount, int plantCount, int emptyFieldCount, Pair<MoveDirection[], Integer> popularGenome,
                          int averageLifetime, int averageDescendantsAmount);

    /**
     * Called whenever a fire is updated in the simulation.
     * @param position the position of the fire that is updated.
     * @param length remaining length of the fire. If length is equal
     *               to or less than 0, it is extinguished.
     */
    void updateFire(Vector2D position, int length);
}
