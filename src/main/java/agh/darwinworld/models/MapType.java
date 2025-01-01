package agh.darwinworld.models;

import agh.darwinworld.models.maps.AbstractMap;
import agh.darwinworld.models.maps.FireMap;
import agh.darwinworld.models.maps.WorldMap;

public enum MapType {
    FIRE(new FireMap()),
    WORLD(new WorldMap());

    private final AbstractMap map;

    MapType(AbstractMap map) {
        this.map = map;
    }

    public AbstractMap getMap() {
        return map;
    }
}
