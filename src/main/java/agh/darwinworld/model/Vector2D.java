package agh.darwinworld.model;

/**
 * Represents a 2D vector with integer coordinates, providing various vector operations.
 * @param x the x-coordinate of the vector.
 * @param y the y-coordinate of the vector.
 */
public record Vector2D(int x, int y) {
    /**
     * Checks if this vector precedes another vector in both coordinates.
     * @param other the vector to compare with.
     * @return true if this vector precedes the other vector, false otherwise.
     */
    public boolean precedes(Vector2D other) {
        return x <= other.x && y <= other.y;
    }

    /**
     * Checks if this vector follows another vector in both coordinates.
     * @param other the vector to compare with.
     * @return true if this vector follows the other vector, false otherwise.
     */
    public boolean follows(Vector2D other) {
        return x >= other.x && y >= other.y;
    }

    /**
     * Adds another vector coordinates to this vector.
     * @param other the vector to add.
     * @return a new vector representing the sum of the two vectors.
     */
    public Vector2D add(Vector2D other) {
        return new Vector2D(x + other.x, y + other.y);
    }

    /**
     * Subtracts another vector coordinates from this vector.
     *
     * @param other the vector to subtract.
     * @return a new vector representing the difference of the two vectors.
     */
    public Vector2D subtract(Vector2D other) {
        return new Vector2D(x - other.x, y - other.y);
    }

    /**
     * Calculates the upper-right corner of this vector and another vector.
     * @param other the vector to compare with.
     * @return a new vector representing the upper-right corner.
     */
    public Vector2D upperRight(Vector2D other) {
        return new Vector2D(Math.max(x, other.x), Math.max(y, other.y));
    }

    /**
     * Calculates the lower-left corner of this vector and another vector.
     * @param other the vector to compare with.
     * @return a new vector representing the lower-left corner.
     */
    public Vector2D lowerLeft(Vector2D other) {
        return new Vector2D(Math.min(x, other.x), Math.min(y, other.y));
    }

    /**
     * Returns the opposite of this vector.
     * @return a new vector representing the opposite of this vector.
     */
    public Vector2D opposite() {
        return new Vector2D(-x, -y);
    }

    /**
     * Normalizes this vector within the given width and height boundaries.
     * @param width  the width boundary (can be null for no normalization in x-direction).
     * @param height the height boundary (can be null for no normalization in y-direction).
     * @return a new vector normalized within the specified boundaries.
     * @throws IllegalArgumentException if width or height is less than or equal to 0.
     */
    public Vector2D normalize(Integer width, Integer height) {
        if (width != null && width <= 0)
            throw new IllegalArgumentException("Width must be greater than 0.");
        if (height != null && height <= 0)
            throw new IllegalArgumentException("Height must be greater than 0.");
        int normalizedX = (width == null) ? x() : ((x() % width) + width) % width;
        int normalizedY = (height == null) ? y() : ((y() % height) + height) % height;
        return new Vector2D(normalizedX, normalizedY);
    }

    /**
     * Checks if this vector is equal to another object.
     * @param other the object to compare with.
     * @return true if the other object is a Vector2D with the same coordinates, false otherwise.
     */
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