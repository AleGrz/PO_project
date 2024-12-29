package agh.darwinworld.model;

import javafx.util.Pair;

public interface MovementHandler {
    Pair<Vector2D, MapDirection> move(Vector2D position, MapDirection move);
}
