package agh.darwinworld.model;

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
     */
    void updateAnimal(Vector2D position, int animalCount, int maxAnimalCount);
    void updateStatistics(int step, int animalCount, int plantCount, int emptyFieldCount, String popularGenotype,
                          int averageLifetime, int averageDescendantsAmount);

    void updateFire(Vector2D position, int length);
}
