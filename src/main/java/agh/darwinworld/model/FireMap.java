package agh.darwinworld.model;

import java.util.HashMap;

public class FireMap extends AbstractMap {
    private final HashMap<Vector2D, Integer> fire = new HashMap<>();

    public FireMap(int width, int height) {
        super(width, height);
    }

    public boolean isFireOnPosition(Vector2D position) {
        return fire.containsKey(position);
    }
}
