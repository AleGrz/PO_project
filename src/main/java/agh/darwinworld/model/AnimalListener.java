package agh.darwinworld.model;

/**
 * The {@code AnimalListener} interface defines a set of callback methods
 * to observe and respond to changes in an {@code Animal}'s state or behavior.
 * It is typically implemented by classes that need to track animal movements
 * and update their stats in a simulation or game environment.
 */
public interface AnimalListener {
    /**
     * Called whenever an {@code Animal} has moved to a new position on the map.
     * @param oldPosition the previous position of the animal.
     * @param newPosition the new position of the animal.
     */
    void move(Vector2D oldPosition, Vector2D newPosition);

    /**
     * Called whenever the stats of an {@code Animal} have been updated.
     * This may include changes in attributes like energy, health, or any
     * other properties that define the state of the animal.
     */
    void statsUpdate();
}
