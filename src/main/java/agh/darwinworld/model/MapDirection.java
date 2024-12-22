package agh.darwinworld.model;

public enum MapDirection {
    NORTH(new Vector2D(0,1)),
    NORTHEAST(new Vector2D(1,1)),
    EAST(new Vector2D(1,0)),
    SOUTHEAST(new Vector2D(1,-1)),
    SOUTH(new Vector2D(0,-1)),
    SOUTHWEST(new Vector2D(-1,-1)),
    WEST(new Vector2D(-1,0)),
    NORTHWEST(new Vector2D(-1,1));

    private final Vector2D value;

    MapDirection(Vector2D vector) {
        this.value = vector;
    }

    public Vector2D getValue() {
        return value;
    }

    public MapDirection rotate(MoveDirection moveDirection) {
        MapDirection[] directions = MapDirection.values();
        int ind = (ordinal() + moveDirection.ordinal()) % directions.length;
        return directions[ind];
    }
}
