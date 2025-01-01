package agh.darwinworld.models;

import agh.darwinworld.models.maps.AbstractMap;
import agh.darwinworld.models.maps.FireMap;
import agh.darwinworld.models.maps.WorldMap;

public enum MapType {
    FIRE,
    WORLD;

    public AbstractMap createMap() {
        return switch (this) {
            case FIRE -> new FireMap();
            case WORLD -> new WorldMap();
        };
    }
}
