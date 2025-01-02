package agh.darwinworld.models.listeners;

import agh.darwinworld.models.MapDirection;
import agh.darwinworld.models.Vector2D;
import javafx.util.Pair;

/**
 * Interface for handling movement logic in a map-based system.
 * This interface defines how a position and direction are transformed
 * during a movement operation.
 */
public interface MovementHandler {
    /**
     * Computes the new position and direction based on the current position
     * and a specified movement direction.
     *
     * @param position the move position of the entity.
     * @param move     the direction in which the entity intends to move.
     * @return a Pair containing the updated position (key) and direction (value)
     * after the movement.
     */
    Pair<Vector2D, MapDirection> move(Vector2D position, MapDirection move);
}
