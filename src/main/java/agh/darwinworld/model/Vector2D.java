package agh.darwinworld.model;

public record Vector2D(int x, int y) {
    public boolean precedes(Vector2D other) {
        return x <= other.x && y <= other.y;
    }

    public boolean follows(Vector2D other) {
        return x >= other.x && y >= other.y;
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(x + other.x, y + other.y);
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(x - other.x, y - other.y);
    }

    public Vector2D upperRight(Vector2D other) {
        return new Vector2D(Math.max(x, other.x), Math.max(y, other.y));
    }

    public Vector2D lowerLeft(Vector2D other) {
        return new Vector2D(Math.min(x, other.x), Math.min(y, other.y));
    }

    public Vector2D opposite() {
        return new Vector2D(-x, -y);
    }

    public Vector2D normalize(Integer width, Integer height) {
        if (width != null && width <= 0)
            throw new IllegalArgumentException("Width must be greater than 0.");
        if (height != null && height <= 0)
            throw new IllegalArgumentException("Height must be greater than 0.");
        int normalizedX = (width == null) ? x() : ((x() % width) + width) % width;
        int normalizedY = (height == null) ? y() : ((y() % height) + height) % height;
        return new Vector2D(normalizedX, normalizedY);
    }

    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Vector2D v)) return false;
        return x == v.x && y == v.y;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(x) + Integer.hashCode(y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}