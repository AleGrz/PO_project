package agh.darwinworld.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoveDirectionTest {
    @Test
    public void testEnumValues() {
        MoveDirection[] directions = MoveDirection.values();
        assertEquals(8, directions.length, "The number of MoveDirection enum values is incorrect.");
        assertEquals(MoveDirection.FORWARD, directions[0], "FORWARD should be the first value.");
        assertEquals(MoveDirection.FORWARD_RIGHT, directions[1], "FORWARD_RIGHT should be the second value.");
        assertEquals(MoveDirection.RIGHT, directions[2], "RIGHT should be the third value.");
        assertEquals(MoveDirection.BACKWARD_RIGHT, directions[3], "BACKWARD_RIGHT should be the fourth value.");
        assertEquals(MoveDirection.BACKWARD, directions[4], "BACKWARD should be the fifth value.");
        assertEquals(MoveDirection.BACKWARD_LEFT, directions[5], "BACKWARD_LEFT should be the sixth value.");
        assertEquals(MoveDirection.LEFT, directions[6], "LEFT should be the seventh value.");
        assertEquals(MoveDirection.FORWARD_LEFT, directions[7], "FORWARD_LEFT should be the last value.");
    }

    @Test
    public void testToString() {
        for (MoveDirection direction : MoveDirection.values()) {
            assertEquals(
                    Integer.toString(direction.ordinal()),
                    direction.toString(),
                    "toString() does not return the expected ordinal for " + direction.name()
            );
        }
    }
}
