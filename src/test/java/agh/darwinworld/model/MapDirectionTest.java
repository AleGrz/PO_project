package agh.darwinworld.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class MapDirectionTest {
    @Test
    public void testRotateMoveForward() {
        MapDirection[] directions = MapDirection.values();
        for (MapDirection direction : directions) {
            assertEquals(direction, direction.rotate(MoveDirection.FORWARD), "Wrong direction on rotate forward.");
        }
    }

    @Test
    public void testRotateMoveForwardRight() {
        assertEquals(MapDirection.NORTH, MapDirection.NORTHWEST.rotate(MoveDirection.FORWARD_RIGHT),
                "Wrong direction on rotate forward right from NORTHWEST.");
        assertEquals(MapDirection.NORTHEAST, MapDirection.NORTH.rotate(MoveDirection.FORWARD_RIGHT),
                "Wrong direction on rotate forward right from NORTH.");
        assertEquals(MapDirection.EAST, MapDirection.NORTHEAST.rotate(MoveDirection.FORWARD_RIGHT),
                "Wrong direction on rotate forward right from NORTHEAST.");
        assertEquals(MapDirection.SOUTHEAST, MapDirection.EAST.rotate(MoveDirection.FORWARD_RIGHT),
                "Wrong direction on rotate forward right from EAST.");
        assertEquals(MapDirection.SOUTH, MapDirection.SOUTHEAST.rotate(MoveDirection.FORWARD_RIGHT),
                "Wrong direction on rotate forward right from SOUTHEAST.");
        assertEquals(MapDirection.SOUTHWEST, MapDirection.SOUTH.rotate(MoveDirection.FORWARD_RIGHT),
                "Wrong direction on rotate forward right from SOUTH.");
        assertEquals(MapDirection.WEST, MapDirection.SOUTHWEST.rotate(MoveDirection.FORWARD_RIGHT),
                "Wrong direction on rotate forward right from SOUTHWEST.");
        assertEquals(MapDirection.NORTHWEST, MapDirection.WEST.rotate(MoveDirection.FORWARD_RIGHT),
                "Wrong direction on rotate forward right from WEST.");
    }

    @Test
    public void testRotateMoveRight() {
        assertEquals(MapDirection.NORTH, MapDirection.WEST.rotate(MoveDirection.RIGHT),
                "Wrong direction on rotate right from WEST.");
        assertEquals(MapDirection.NORTHEAST, MapDirection.NORTHWEST.rotate(MoveDirection.RIGHT),
                "Wrong direction on rotate right from NORTHWEST.");
        assertEquals(MapDirection.EAST, MapDirection.NORTH.rotate(MoveDirection.RIGHT),
                "Wrong direction on rotate right from NORTH.");
        assertEquals(MapDirection.SOUTHEAST, MapDirection.NORTHEAST.rotate(MoveDirection.RIGHT),
                "Wrong direction on rotate right from NORTHEAST.");
        assertEquals(MapDirection.SOUTH, MapDirection.EAST.rotate(MoveDirection.RIGHT),
                "Wrong direction on rotate right from EAST.");
        assertEquals(MapDirection.SOUTHWEST, MapDirection.SOUTHEAST.rotate(MoveDirection.RIGHT),
                "Wrong direction on rotate right from SOUTHEAST.");
        assertEquals(MapDirection.WEST, MapDirection.SOUTH.rotate(MoveDirection.RIGHT),
                "Wrong direction on rotate right from SOUTH.");
        assertEquals(MapDirection.NORTHWEST, MapDirection.SOUTHWEST.rotate(MoveDirection.RIGHT),
                "Wrong direction on rotate right from SOUTHWEST.");
    }

    @Test
    public void testRotateMoveBackwardRight() {
        assertEquals(MapDirection.NORTH, MapDirection.SOUTHWEST.rotate(MoveDirection.BACKWARD_RIGHT),
                "Wrong direction on rotate backward right from SOUTHWEST.");
        assertEquals(MapDirection.NORTHEAST, MapDirection.WEST.rotate(MoveDirection.BACKWARD_RIGHT),
                "Wrong direction on rotate backward right from WEST.");
        assertEquals(MapDirection.EAST, MapDirection.NORTHWEST.rotate(MoveDirection.BACKWARD_RIGHT),
                "Wrong direction on rotate backward right from NORTHWEST.");
        assertEquals(MapDirection.SOUTHEAST, MapDirection.NORTH.rotate(MoveDirection.BACKWARD_RIGHT),
                "Wrong direction on rotate backward right from NORTH.");
        assertEquals(MapDirection.SOUTH, MapDirection.NORTHEAST.rotate(MoveDirection.BACKWARD_RIGHT),
                "Wrong direction on rotate backward right from NORTHEAST.");
        assertEquals(MapDirection.SOUTHWEST, MapDirection.EAST.rotate(MoveDirection.BACKWARD_RIGHT),
                "Wrong direction on rotate backward right from EAST.");
        assertEquals(MapDirection.WEST, MapDirection.SOUTHEAST.rotate(MoveDirection.BACKWARD_RIGHT),
                "Wrong direction on rotate backward right from SOUTHEAST.");
        assertEquals(MapDirection.NORTHWEST, MapDirection.SOUTH.rotate(MoveDirection.BACKWARD_RIGHT),
                "Wrong direction on rotate backward right from SOUTH.");
    }

    @Test
    public void testRotateMoveBackward() {
        assertEquals(MapDirection.NORTH, MapDirection.SOUTH.rotate(MoveDirection.BACKWARD),
                "Wrong direction on rotate backward from SOUTH.");
        assertEquals(MapDirection.NORTHEAST, MapDirection.SOUTHWEST.rotate(MoveDirection.BACKWARD),
                "Wrong direction on rotate backward from SOUTHWEST.");
        assertEquals(MapDirection.EAST, MapDirection.WEST.rotate(MoveDirection.BACKWARD),
                "Wrong direction on rotate backward from WEST.");
        assertEquals(MapDirection.SOUTHEAST, MapDirection.NORTHWEST.rotate(MoveDirection.BACKWARD),
                "Wrong direction on rotate backward from NORTHWEST.");
        assertEquals(MapDirection.SOUTH, MapDirection.NORTH.rotate(MoveDirection.BACKWARD),
                "Wrong direction on rotate backward from NORTH.");
        assertEquals(MapDirection.SOUTHWEST, MapDirection.NORTHEAST.rotate(MoveDirection.BACKWARD),
                "Wrong direction on rotate backward from NORTHEAST.");
        assertEquals(MapDirection.WEST, MapDirection.EAST.rotate(MoveDirection.BACKWARD),
                "Wrong direction on rotate backward from EAST.");
        assertEquals(MapDirection.NORTHWEST, MapDirection.SOUTHEAST.rotate(MoveDirection.BACKWARD),
                "Wrong direction on rotate backward from SOUTHEAST.");
    }

    @Test
    public void testRotateMoveForwardLeft() {
        assertEquals(MapDirection.NORTH.rotate(MoveDirection.FORWARD_LEFT), MapDirection.NORTHWEST,
                "Wrong direction on rotate forward left from NORTH.");
        assertEquals(MapDirection.NORTHEAST.rotate(MoveDirection.FORWARD_LEFT), MapDirection.NORTH,
                "Wrong direction on rotate forward left from NORTHEAST.");
        assertEquals(MapDirection.EAST.rotate(MoveDirection.FORWARD_LEFT), MapDirection.NORTHEAST,
                "Wrong direction on rotate forward left from EAST.");
        assertEquals(MapDirection.SOUTHEAST.rotate(MoveDirection.FORWARD_LEFT), MapDirection.EAST,
                "Wrong direction on rotate forward left from SOUTHEAST.");
        assertEquals(MapDirection.SOUTH.rotate(MoveDirection.FORWARD_LEFT), MapDirection.SOUTHEAST,
                "Wrong direction on rotate forward left from SOUTH.");
        assertEquals(MapDirection.SOUTHWEST.rotate(MoveDirection.FORWARD_LEFT), MapDirection.SOUTH,
                "Wrong direction on rotate forward left from SOUTHWEST.");
        assertEquals(MapDirection.WEST.rotate(MoveDirection.FORWARD_LEFT), MapDirection.SOUTHWEST,
                "Wrong direction on rotate forward left from WEST.");
        assertEquals(MapDirection.NORTHWEST.rotate(MoveDirection.FORWARD_LEFT), MapDirection.WEST,
                "Wrong direction on rotate forward left from NORTHWEST.");
    }

    @Test
    public void testRotateMoveLeft() {
        assertEquals(MapDirection.NORTH.rotate(MoveDirection.LEFT), MapDirection.WEST,
                "Wrong direction on rotate left from NORTH.");
        assertEquals(MapDirection.NORTHEAST.rotate(MoveDirection.LEFT), MapDirection.NORTHWEST,
                "Wrong direction on rotate left from NORTHEAST.");
        assertEquals(MapDirection.EAST.rotate(MoveDirection.LEFT), MapDirection.NORTH,
                "Wrong direction on rotate left from EAST.");
        assertEquals(MapDirection.SOUTHEAST.rotate(MoveDirection.LEFT), MapDirection.NORTHEAST,
                "Wrong direction on rotate left from SOUTHEAST.");
        assertEquals(MapDirection.SOUTH.rotate(MoveDirection.LEFT), MapDirection.EAST,
                "Wrong direction on rotate left from SOUTH.");
        assertEquals(MapDirection.SOUTHWEST.rotate(MoveDirection.LEFT), MapDirection.SOUTHEAST,
                "Wrong direction on rotate left from SOUTHWEST.");
        assertEquals(MapDirection.WEST.rotate(MoveDirection.LEFT), MapDirection.SOUTH,
                "Wrong direction on rotate left from WEST.");
        assertEquals(MapDirection.NORTHWEST.rotate(MoveDirection.LEFT), MapDirection.SOUTHWEST,
                "Wrong direction on rotate left from NORTHWEST.");
    }

    @Test
    public void testRotateMoveBackwardLeft() {
        assertEquals(MapDirection.NORTH.rotate(MoveDirection.BACKWARD_LEFT), MapDirection.SOUTHWEST,
                "Wrong direction on rotate backward left from NORTH.");
        assertEquals(MapDirection.NORTHEAST.rotate(MoveDirection.BACKWARD_LEFT), MapDirection.WEST,
                "Wrong direction on rotate backward left from NORTHEAST.");
        assertEquals(MapDirection.EAST.rotate(MoveDirection.BACKWARD_LEFT), MapDirection.NORTHWEST,
                "Wrong direction on rotate backward left from EAST.");
        assertEquals(MapDirection.SOUTHEAST.rotate(MoveDirection.BACKWARD_LEFT), MapDirection.NORTH,
                "Wrong direction on rotate backward left from SOUTHEAST.");
        assertEquals(MapDirection.SOUTH.rotate(MoveDirection.BACKWARD_LEFT), MapDirection.NORTHEAST,
                "Wrong direction on rotate backward left from SOUTH.");
        assertEquals(MapDirection.SOUTHWEST.rotate(MoveDirection.BACKWARD_LEFT), MapDirection.EAST,
                "Wrong direction on rotate backward left from SOUTHWEST.");
        assertEquals(MapDirection.WEST.rotate(MoveDirection.BACKWARD_LEFT), MapDirection.SOUTHEAST,
                "Wrong direction on rotate backward left from WEST.");
        assertEquals(MapDirection.NORTHWEST.rotate(MoveDirection.BACKWARD_LEFT), MapDirection.SOUTH,
                "Wrong direction on rotate backward left from NORTHWEST.");
    }

    @Test
    public void testGetValue() {
        assertEquals(MapDirection.NORTH.getValue(), new Vector2D(0, 1));
        assertEquals(MapDirection.NORTHEAST.getValue(), new Vector2D(1, 1));
        assertEquals(MapDirection.EAST.getValue(), new Vector2D(1, 0));
        assertEquals(MapDirection.SOUTHEAST.getValue(), new Vector2D(1, -1));
        assertEquals(MapDirection.SOUTH.getValue(), new Vector2D(0, -1));
        assertEquals(MapDirection.SOUTHWEST.getValue(), new Vector2D(-1, -1));
        assertEquals(MapDirection.WEST.getValue(), new Vector2D(-1, 0));
        assertEquals(MapDirection.NORTHWEST.getValue(), new Vector2D(-1, 1));
    }
}
