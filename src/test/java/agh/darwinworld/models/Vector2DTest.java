package agh.darwinworld.models;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class Vector2DTest {
    @Test
    public void testEqualsPositive() {
        Vector2D v1 = new Vector2D(1,1);
        Vector2D v2 = new Vector2D(1,1);
        Vector2D v3 = new Vector2D(2,1);
        Vector2D v4 = new Vector2D(1,2);
        assertEquals(v1, v2);
        assertNotEquals(v1, v3);
        assertNotEquals(v1, v4);
    }

    @Test
    public void testEqualsNegative() {
        Vector2D v1 = new Vector2D(-1,-1);
        Vector2D v2 = new Vector2D(-1,-1);
        Vector2D v3 = new Vector2D(-2,-1);
        Vector2D v4 = new Vector2D(-1,-2);
        assertEquals(v1, v2);
        assertNotEquals(v1, v3);
        assertNotEquals(v1, v4);
    }

    @Test
    public void testEqualsMixed() {
        Vector2D v1 = new Vector2D(-1,1);
        Vector2D v2 = new Vector2D(-1,1);
        Vector2D v3 = new Vector2D(1,-1);
        Vector2D v4 = new Vector2D(1,-1);
        assertEquals(v1, v2);
        assertEquals(v3, v4);
        assertNotEquals(v1, v3);
    }

    @SuppressWarnings("AssertBetweenInconvertibleTypes")
    @Test
    public void testEqualsOther() {
        Vector2D v1 = new Vector2D(-1,1);
        assertNotEquals(-1, v1);
        assertNotEquals(1, v1);
        assertNotEquals("(-1,1)", v1);
    }

    @Test
    public void testEqualsNull() {
        Vector2D v1 = new Vector2D(-1,1);
        Vector2D v2 = new Vector2D(0,0);
        assertNotEquals(null, v1);
        assertNotEquals(null, v2);
    }

    @Test
    public void testToString() {
        Vector2D v1 = new Vector2D(1,2);
        Vector2D v2 = new Vector2D(-1,-2);
        assertEquals("(1,2)", v1.toString());
        assertEquals("(-1,-2)", v2.toString());
    }

    @Test
    public void testPrecedes() {
        Vector2D v1 = new Vector2D(1,2);
        Vector2D v2 = new Vector2D(1,2);
        Vector2D v3 = new Vector2D(1,1);
        Vector2D v4 = new Vector2D(0,1);

        boolean v1PrecedesV1 = v1.precedes(v1);
        boolean v1PrecedesV2 = v1.precedes(v2);
        boolean v1PrecedesV3 = v1.precedes(v3);
        boolean v1PrecedesV4 = v1.precedes(v4);
        boolean v2PrecedesV1 = v2.precedes(v1);
        boolean v2PrecedesV2 = v2.precedes(v2);
        boolean v2PrecedesV3 = v2.precedes(v3);
        boolean v2PrecedesV4 = v2.precedes(v4);
        boolean v3PrecedesV1 = v3.precedes(v1);
        boolean v3PrecedesV2 = v3.precedes(v2);
        boolean v3PrecedesV3 = v3.precedes(v3);
        boolean v3PrecedesV4 = v3.precedes(v4);
        boolean v4PrecedesV1 = v4.precedes(v1);
        boolean v4PrecedesV2 = v4.precedes(v2);
        boolean v4PrecedesV3 = v4.precedes(v3);
        boolean v4PrecedesV4 = v4.precedes(v4);

        assertTrue(v1PrecedesV1);
        assertTrue(v1PrecedesV2);
        assertFalse(v1PrecedesV3);
        assertFalse(v1PrecedesV4);
        assertTrue(v2PrecedesV1);
        assertTrue(v2PrecedesV2);
        assertFalse(v2PrecedesV3);
        assertFalse(v2PrecedesV4);
        assertTrue(v3PrecedesV1);
        assertTrue(v3PrecedesV2);
        assertTrue(v3PrecedesV3);
        assertFalse(v3PrecedesV4);
        assertTrue(v4PrecedesV1);
        assertTrue(v4PrecedesV2);
        assertTrue(v4PrecedesV3);
        assertTrue(v4PrecedesV4);
    }

    @Test
    public void testFollows() {
        Vector2D v1 = new Vector2D(1,2);
        Vector2D v2 = new Vector2D(1,2);
        Vector2D v3 = new Vector2D(1,1);
        Vector2D v4 = new Vector2D(0,1);

        boolean v1FollowsV1 = v1.follows(v1);
        boolean v1FollowsV2 = v1.follows(v2);
        boolean v1FollowsV3 = v1.follows(v3);
        boolean v1FollowsV4 = v1.follows(v4);
        boolean v2FollowsV1 = v2.follows(v1);
        boolean v2FollowsV2 = v2.follows(v2);
        boolean v2FollowsV3 = v2.follows(v3);
        boolean v2FollowsV4 = v2.follows(v4);
        boolean v3FollowsV1 = v3.follows(v1);
        boolean v3FollowsV2 = v3.follows(v2);
        boolean v3FollowsV3 = v3.follows(v3);
        boolean v3FollowsV4 = v3.follows(v4);
        boolean v4FollowsV1 = v4.follows(v1);
        boolean v4FollowsV2 = v4.follows(v2);
        boolean v4FollowsV3 = v4.follows(v3);
        boolean v4FollowsV4 = v4.follows(v4);

        assertTrue(v1FollowsV1);
        assertTrue(v1FollowsV2);
        assertTrue(v1FollowsV3);
        assertTrue(v1FollowsV4);
        assertTrue(v2FollowsV1);
        assertTrue(v2FollowsV2);
        assertTrue(v2FollowsV3);
        assertTrue(v2FollowsV4);
        assertFalse(v3FollowsV1);
        assertFalse(v3FollowsV2);
        assertTrue(v3FollowsV3);
        assertTrue(v3FollowsV4);
        assertFalse(v4FollowsV1);
        assertFalse(v4FollowsV2);
        assertFalse(v4FollowsV3);
        assertTrue(v4FollowsV4);
    }

    @Test
    void testUpperRightLargerOther() {
        Vector2D v1 = new Vector2D(1, 2);
        Vector2D v2 = new Vector2D(3, 4);
        Vector2D result = v1.upperRight(v2);
        Vector2D expected = new Vector2D(3, 4);
        assertEquals(expected, result);
    }

    @Test
    void TestUpperRightSmallerOther() {
        Vector2D v1 = new Vector2D(5, 6);
        Vector2D v2 = new Vector2D(2, 4);
        Vector2D result = v1.upperRight(v2);
        Vector2D expected = new Vector2D(5, 6);
        assertEquals(expected, result);
    }

    @Test
    void testLowerLeftSmallerOther() {
        Vector2D v1 = new Vector2D(5, 6);
        Vector2D v2 = new Vector2D(2, 4);
        Vector2D result = v1.lowerLeft(v2);
        Vector2D expected = new Vector2D(2, 4);
        assertEquals(expected, result);
    }

    @Test
    void testLowerLeftLargerOther() {
        Vector2D v1 = new Vector2D(1, 2);
        Vector2D v2 = new Vector2D(3, 4);
        Vector2D result = v1.lowerLeft(v2);
        Vector2D expected = new Vector2D(1, 2);
        assertEquals(expected, result);
    }

    @Test
    void testUpperRightEqual() {
        Vector2D v1 = new Vector2D(5, 5);
        Vector2D v2 = new Vector2D(5, 5);
        Vector2D result = v1.upperRight(v2);
        Vector2D expected = new Vector2D(5, 5);
        assertEquals(expected, result);
    }

    @Test
    void testLowerLeftEqual() {
        Vector2D v1 = new Vector2D(3, 3);
        Vector2D v2 = new Vector2D(3, 3);
        Vector2D result = v1.lowerLeft(v2);
        Vector2D expected = new Vector2D(3, 3);
        assertEquals(expected, result);
    }

    @Test
    void testUpperRightNegativeCoordinates() {
        Vector2D v1 = new Vector2D(-2, -3);
        Vector2D v2 = new Vector2D(-1, -5);
        Vector2D result = v1.upperRight(v2);
        Vector2D expected = new Vector2D(-1, -3);
        assertEquals(expected, result);
    }

    @Test
    void testLowerLeftNegative() {
        Vector2D v1 = new Vector2D(-2, -3);
        Vector2D v2 = new Vector2D(-1, -5);
        Vector2D result = v1.lowerLeft(v2);
        Vector2D expected = new Vector2D(-2, -5);
        assertEquals(expected, result);
    }

    @Test
    public void testAdd() {
        Vector2D v1 = new Vector2D(1,1);
        Vector2D v2 = new Vector2D(1,1);
        Vector2D resultA = v1.add(v2);
        Vector2D resultB = v2.add(v1);
        assertEquals(2, resultA.x());
        assertEquals(2, resultA.y());
        assertEquals(2, resultB.x());
        assertEquals(2, resultB.y());
    }

    @Test
    public void testSubtract() {
        Vector2D v1 = new Vector2D(1,1);
        Vector2D v2 = new Vector2D(1,1);
        Vector2D resultA = v1.subtract(v2);
        Vector2D resultB = v2.subtract(v1);
        assertEquals(0, resultA.x());
        assertEquals(0, resultA.y());
        assertEquals(0, resultB.x());
        assertEquals(0, resultB.y());
    }

    @Test
    public void testOppositePositive() {
        Vector2D vector = new Vector2D(3, 4);
        Vector2D result = vector.opposite();
        assertEquals(-3, result.x());
        assertEquals(-4, result.y());
    }

    @Test
    public void testOppositeNegative() {
        Vector2D vector = new Vector2D(-3, -4);
        Vector2D result = vector.opposite();
        assertEquals(3, result.x());
        assertEquals(4, result.y());
    }

    @Test
    public void testOppositeZero() {
        Vector2D vector = new Vector2D(0, 0);
        Vector2D result = vector.opposite();
        assertEquals(0, result.x());
        assertEquals(0, result.y());
    }

    @Test
    public void testOppositeMixed() {
        Vector2D vectorA = new Vector2D(-1, 1);
        Vector2D vectorB = new Vector2D(1, -1);
        Vector2D resultA = vectorA.opposite();
        Vector2D resultB = vectorB.opposite();
        assertEquals(1, resultA.x());
        assertEquals(-1, resultA.y());
        assertEquals(-1, resultB.x());
        assertEquals(1, resultB.y());
    }

    @Test
    public void testNormalizeNull() {
        Vector2D vector = new Vector2D(1, 1);
        assertEquals(vector, vector.normalize(null, null));
    }

    @Test
    public void testNormalizeOnlyWidth() {
        Vector2D vector = new Vector2D(3, 3);
        assertEquals(new Vector2D(1, 3), vector.normalize(2, null));
    }

    @Test
    public void testNormalizeOnlyHeight() {
        Vector2D vector = new Vector2D(3, 3);
        assertEquals(new Vector2D(3, 1), vector.normalize(null, 2));
    }

    @Test
    public void testNormalize() {
        Vector2D vector = new Vector2D(3, 3);
        assertEquals(new Vector2D(0, 1), vector.normalize(3, 2));
    }

    @Test
    public void testNormalizeIllegalArguments() {
        Vector2D vector = new Vector2D(3, 3);
        assertThrows(IllegalArgumentException.class, () -> vector.normalize(0, null),
                "IllegalArgumentException should be thrown when provided width is equal to 0.");
        assertThrows(IllegalArgumentException.class, () -> vector.normalize(-1, null),
                "IllegalArgumentException should be thrown when provided width is a negative number.");
        assertThrows(IllegalArgumentException.class, () -> vector.normalize(null, 0),
                "IllegalArgumentException should be thrown when provided height is equal to 0.");
        assertThrows(IllegalArgumentException.class, () -> vector.normalize(null, -1),
                "IllegalArgumentException should be thrown when provided height is a negative number.");
        assertThrows(IllegalArgumentException.class, () -> vector.normalize(0, 1),
                "IllegalArgumentException should be thrown when provided width is equal to 0.");
        assertThrows(IllegalArgumentException.class, () -> vector.normalize(-1, 1),
                "IllegalArgumentException should be thrown when provided width is a negative number.");
        assertThrows(IllegalArgumentException.class, () -> vector.normalize(0, 0),
                "IllegalArgumentException should be thrown when provided width and height are equal to 0.");
        assertThrows(IllegalArgumentException.class, () -> vector.normalize(-1, -1),
                "IllegalArgumentException should be thrown when provided width and height are negative numbers.");
        assertThrows(IllegalArgumentException.class, () -> vector.normalize(0, -1),
                "IllegalArgumentException should be thrown when provided width is equal to 0 and height is a negative number.");
        assertThrows(IllegalArgumentException.class, () -> vector.normalize(-1, 0),
                "IllegalArgumentException should be thrown when provided height is equal to 0 and width is a negative number.");
        assertThrows(IllegalArgumentException.class, () -> vector.normalize(1, 0),
                "IllegalArgumentException should be thrown when provided height is equal to 0.");
        assertThrows(IllegalArgumentException.class, () -> vector.normalize(1, -1),
                "IllegalArgumentException should be thrown when provided height is a negative number.");
    }
}
