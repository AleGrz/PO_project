package agh.darwinworld.models;

/**
 * Represents the directions on a map, each associated with a vector indicating its direction.
 */
public enum MapDirection {
    /**
     * North direction represented by vector (0, 1).
     */
    NORTH(new Vector2D(0, 1)),

    /**
     * Northeast direction represented by vector (1, 1).
     */
    NORTHEAST(new Vector2D(1, 1)),

    /**
     * East direction represented by vector (1, 0).
     */
    EAST(new Vector2D(1, 0)),

    /**
     * Southeast direction represented by vector (1, -1).
     */
    SOUTHEAST(new Vector2D(1, -1)),

    /**
     * South direction represented by vector (0, -1).
     */
    SOUTH(new Vector2D(0, -1)),

    /**
     * Southwest direction represented by vector (-1, -1).
     */
    SOUTHWEST(new Vector2D(-1, -1)),

    /**
     * West direction represented by vector (-1, 0).
     */
    WEST(new Vector2D(-1, 0)),

    /**
     * Northwest direction represented by vector (-1, 1).
     */
    NORTHWEST(new Vector2D(-1, 1));

    /**
     * The vector associated with the direction.
     */
    private final Vector2D value;

    /**
     * Constructs a MapDirection with a specific vector.
     *
     * @param vector the vector associated with the direction.
     */
    MapDirection(Vector2D vector) {
        this.value = vector;
    }

    /**
     * Gets the vector associated with the direction.
     *
     * @return the vector associated with the direction.
     */
    public Vector2D getValue() {
        return value;
    }

    /**
     * Rotates the current direction based on the given move direction.
     *
     * @param moveDirection the direction to rotate by (e.g. RIGHT).
     * @return the new MapDirection after rotation.
     */
    public MapDirection rotate(MoveDirection moveDirection) {
        MapDirection[] directions = MapDirection.values();
        int ind = (ordinal() + moveDirection.ordinal()) % directions.length;
        return directions[ind];
    }
}
