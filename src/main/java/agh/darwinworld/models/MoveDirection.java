package agh.darwinworld.models;

/**
 * Represents the possible movement directions
 * relative to the object, including rotation.
 */
public enum MoveDirection {
    /**
     * Movement forward in the current direction.
     */
    FORWARD,

    /**
     * Movement forward and to the right.
     */
    FORWARD_RIGHT,

    /**
     * Movement to the right.
     */
    RIGHT,

    /**
     * Movement backward and to the right.
     */
    BACKWARD_RIGHT,

    /**
     * Movement backward in the opposite direction.
     */
    BACKWARD,

    /**
     * Movement backward and to the left.
     */
    BACKWARD_LEFT,

    /**
     * Movement to the left.
     */
    LEFT,

    /**
     * Movement forward and to the left.
     */
    FORWARD_LEFT;

    @Override
    public String toString() {
        return Integer.toString(this.ordinal());
    }
}
