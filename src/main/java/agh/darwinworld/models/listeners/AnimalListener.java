package agh.darwinworld.models.listeners;

import agh.darwinworld.models.Vector2D;

import java.beans.PropertyChangeListener;

/**
 * The {@code AnimalListener} interface defines a set of callback methods
 * to observe and respond to changes in an {@code Animal}'s state or behavior.
 */
public interface AnimalListener extends PropertyChangeListener {
    /**
     * Called whenever an {@code Animal} has moved to a new position on the map.
     * @param oldPosition the previous position of the animal.
     * @param newPosition the new position of the animal.
     */
    default void move(Vector2D oldPosition, Vector2D newPosition) { }
}
