package agh.darwinworld.model;

/**
 * Interface for listening to simulation step events in a simulation.
 * Provides methods for tracking movements and updates to the world elements
 * such as animals and plants.
 */
public interface SimulationStepListener {
    /**
     * Called whenever an animal moves from one position to another.
     * @param oldPosition the previous position of the animal.
     * @param newPosition the new position of the animal.
     */
    void moveAnimal(Vector2D oldPosition, Vector2D newPosition);

    /**
     * Called whenever a new plant is added to the simulation.
     * @param position the position where the plant is added.
     */
    void addPlant(Vector2D position);

    /**
     * Called whenever a new animal is added to the simulation.
     * @param position the position where the animal is added.
     */
    void addAnimal(Vector2D position);

    /**
     * Called whenever a plant is removed from the simulation.
     * @param position the position of the plant that is removed.
     */
    void removePlant(Vector2D position);

    /**
     * Called whenever an animal is removed from the simulation.
     * @param position the position of the animal that is removed.
     */
    void removeAnimal(Vector2D position);
}
