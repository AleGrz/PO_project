package agh.darwinworld.models.maps;

public enum MapType {
    WORLD("World map"),
    FIRE("Fire map");

    public AbstractMap createMap() {
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
