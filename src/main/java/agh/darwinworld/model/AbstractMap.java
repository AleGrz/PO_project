package agh.darwinworld.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public abstract class AbstractMap {
    private final int width;
    private final int height;
    private HashMap<Vector2D, ArrayList<Animal>> animals = new HashMap<>();
    private final HashSet<Vector2D> plants = new HashSet<>();
    public AbstractMap(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public List<Animal> getAnimalsOnPosition(Vector2D position) {
        return animals.containsKey(position) ? animals.get(position) : new ArrayList<>();
    }

    public boolean isPlantOnPosition(Vector2D position) {
        return plants.contains(position);
    }
}
