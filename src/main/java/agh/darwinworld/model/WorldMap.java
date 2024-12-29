package agh.darwinworld.model;

import javafx.util.Pair;

public class WorldMap extends AbstractMap implements MovementHandler {
    public void step(int count) {
        super.step(count);
        updateStatistics(count);
    }

    @Override
    public Pair<Vector2D, MapDirection> move(Vector2D position, MapDirection direction) {
        Vector2D newPos = position.add(direction.getValue()).normalize(this.params.width(), null);
        if (newPos.y() < 0 || newPos.y() >= this.params.height()){
            direction = direction.rotate(MoveDirection.BACKWARD);
            return new Pair<>(position, direction);
        }
        return new Pair<>(newPos, direction);
    }
}
