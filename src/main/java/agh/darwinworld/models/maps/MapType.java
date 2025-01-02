package agh.darwinworld.models.maps;

/**
 * All possible map types in a simulation.
 */
public enum MapType {
    /**
     * A globe-shaped map where objects can move.
     * The horizontal edges wrap around, while the vertical edges act as poles.
     */
    WORLD("World map"),

    /**
     * Basic rectangular map with additional fires.
     */
    FIRE("Fire map");

    /**
     * Creates new instance of the map.
     *
     * @return new map.
     */
    public AbstractMap createMap() {
        return switch (this) {
            case FIRE -> new FireMap();
            case WORLD -> new WorldMap();
        };
    }

    /**
     * To string label of the enum.
     */
    private final String label;

    /**
     * Constructs a MapType with a specific label.
     *
     * @param label the label associated with the map.
     */
    MapType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
