package agh.darwinworld.models.listeners;

import agh.darwinworld.models.MapDirection;
import agh.darwinworld.models.Vector2D;
import javafx.util.Pair;

/**
 * 
 */
public interface MovementHandler {
    /**
     *
     * @param position
     * @param move
     * @return
     */
    Pair<Vector2D, MapDirection> move(Vector2D position, MapDirection move);
}
