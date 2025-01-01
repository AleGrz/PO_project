package agh.darwinworld.models.maps;

public enum MapType {
    FIRE("Fire map"),
    WORLD("World map");

    public AbstractMap getMap() {
        return switch (this) {
            case FIRE -> new FireMap();
            case WORLD -> new WorldMap();
        };
    }

    private final String label;

    MapType(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }
}
