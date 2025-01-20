package agh.darwinworld.models;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class Vector2DTest {
    @Test
    public void testEqualsPositive() {
        Vector2D v1 = new Vector2D(1, 1);
        Vector2D v2 = new Vector2D(1, 1);
        Vector2D v3 = new Vector2D(2, 1);
        Vector2D v4 = new Vector2D(1, 2);

        assertEquals(v1, v2, "Expected vectors v1 and v2 to be equal since they have the same coordinates.");
        assertNotEquals(v1, v3, "Expected vectors v1 and v3 to be different since their x-coordinates differ.");
        assertNotEquals(v1, v4, "Expected vectors v1 and v4 to be different since their y-coordinates differ.");
    }

    @Test
    public void testEqualsNegative() {
        Vector2D v1 = new Vector2D(-1, -1);
        Vector2D v2 = new Vector2D(-1, -1);
        Vector2D v3 = new Vector2D(-2, -1);
        Vector2D v4 = new Vector2D(-1, -2);

        assertEquals(v1, v2, "Expected vectors v1 and v2 to be equal since they have the same negative coordinates.");
        assertNotEquals(v1, v3, "Expected vectors v1 and v3 to be different since their x-coordinates differ.");
        assertNotEquals(v1, v4, "Expected vectors v1 and v4 to be different since their y-coordinates differ.");
    }

    @Test
    public void testEqualsMixed() {
        Vector2D v1 = new Vector2D(-1, 1);
        Vector2D v2 = new Vector2D(-1, 1);
        Vector2D v3 = new Vector2D(1, -1);
        Vector2D v4 = new Vector2D(1, -1);

        assertEquals(v1, v2, "Expected vectors v1 and v2 to be equal since they have the same mixed coordinates.");
        assertEquals(v3, v4, "Expected vectors v3 and v4 to be equal since they have the same coordinates.");
        assertNotEquals(v1, v3, "Expected vectors v1 and v3 to be different due to opposite x and y signs.");
    }

    @SuppressWarnings("AssertBetweenInconvertibleTypes")
    @Test
    public void testEqualsOther() {
        Vector2D v1 = new Vector2D(-1, 1);
        assertNotEquals(-1, v1, "Expected v1 to not be equal to an integer.");
        assertNotEquals(1, v1, "Expected v1 to not be equal to an integer.");
        assertNotEquals("(-1,1)", v1, "Expected v1 to not be equal to a string representation of itself.");
    }

    @Test
    public void testEqualsNull() {
        Vector2D v1 = new Vector2D(-1, 1);
        Vector2D v2 = new Vector2D(0, 0);
        assertNotEquals(null, v1, "Expected v1 to not be equal to null.");
        assertNotEquals(null, v2, "Expected v2 to not be equal to null.");
    }

    @Test
    public void testToString() {
        Vector2D v1 = new Vector2D(1, 2);
        Vector2D v2 = new Vector2D(-1, -2);
        assertEquals("(1,2)", v1.toString(), "Expected toString to return '(1,2)' for v1.");
        assertEquals("(-1,-2)", v2.toString(), "Expected toString to return '(-1,-2)' for v2.");
    }

    @Test
    public void testPrecedes() {
        Vector2D v1 = new Vector2D(1, 2);
        Vector2D v2 = new Vector2D(1, 2);
        Vector2D v3 = new Vector2D(1, 1);
        Vector2D v4 = new Vector2D(0, 1);

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

        assertTrue(v1PrecedesV1, "Expected v1 to precede itself.");
        assertTrue(v1PrecedesV2, "Expected v1 to precede v2 since they are equal.");
        assertFalse(v1PrecedesV3, "Expected v1 to not precede v3 since v3 has a smaller y-coordinate.");
        assertFalse(v1PrecedesV4, "Expected v1 to not precede v4 since v4 has a smaller x-coordinate.");
        assertTrue(v2PrecedesV1, "Expected v2 to precede v1 since they are equal.");
        assertTrue(v2PrecedesV2, "Expected v2 to precede itself.");
        assertFalse(v2PrecedesV3, "Expected v2 to not precede v3 since v3 has a smaller y-coordinate.");
        assertFalse(v2PrecedesV4, "Expected v2 to not precede v4 since v4 has a smaller x-coordinate.");
        assertTrue(v3PrecedesV1, "Expected v3 to precede v1 since it has smaller coordinates.");
        assertTrue(v3PrecedesV2, "Expected v3 to precede v2 since it has smaller coordinates.");
        assertTrue(v3PrecedesV3, "Expected v3 to precede itself.");
        assertFalse(v3PrecedesV4, "Expected v3 to not precede v4 since v4 has a smaller x-coordinate.");
        assertTrue(v4PrecedesV1, "Expected v4 to precede v1 since it has smaller coordinates.");
        assertTrue(v4PrecedesV2, "Expected v4 to precede v2 since it has smaller coordinates.");
        assertTrue(v4PrecedesV3, "Expected v4 to precede v3 since it has smaller coordinates.");
        assertTrue(v4PrecedesV4, "Expected v4 to precede itself.");
    }

    @Test
    public void testFollows() {
        Vector2D v1 = new Vector2D(1, 2);
        Vector2D v2 = new Vector2D(1, 2);
        Vector2D v3 = new Vector2D(1, 1);
        Vector2D v4 = new Vector2D(0, 1);

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

        assertTrue(v1FollowsV1, "Expected v1 to follow itself.");
        assertTrue(v1FollowsV2, "Expected v1 to follow v2 since they are equal.");
        assertTrue(v1FollowsV3, "Expected v1 to follow v3 since it has greater coordinates.");
        assertTrue(v1FollowsV4, "Expected v1 to follow v4 since it has greater coordinates.");
        assertTrue(v2FollowsV1, "Expected v2 to follow v1 since they are equal.");
        assertTrue(v2FollowsV2, "Expected v2 to follow itself.");
        assertTrue(v2FollowsV3, "Expected v2 to follow v3 since it has greater coordinates.");
        assertTrue(v2FollowsV4, "Expected v2 to follow v4 since it has greater coordinates.");
        assertFalse(v3FollowsV1, "Expected v3 to not follow v1 since it has smaller coordinates.");
        assertFalse(v3FollowsV2, "Expected v3 to not follow v2 since it has smaller coordinates.");
        assertTrue(v3FollowsV3, "Expected v3 to follow itself.");
        assertTrue(v3FollowsV4, "Expected v3 to follow v4 since it has greater coordinates.");
        assertFalse(v4FollowsV1, "Expected v4 to not follow v1 since it has smaller coordinates.");
        assertFalse(v4FollowsV2, "Expected v4 to not follow v2 since it has smaller coordinates.");
        assertFalse(v4FollowsV3, "Expected v4 to not follow v3 since it has smaller coordinates.");
        assertTrue(v4FollowsV4, "Expected v4 to follow itself.");
    }

    @Test
    void testUpperRightLargerOther() {
        Vector2D v1 = new Vector2D(1, 2);
        Vector2D v2 = new Vector2D(3, 4);
        Vector2D result = v1.upperRight(v2);
        Vector2D expected = new Vector2D(3, 4);
        assertEquals(expected, result, "Expected upperRight to return (3,4) when comparing (1,2) and (3,4).");
    }

    @Test
    void TestUpperRightSmallerOther() {
        Vector2D v1 = new Vector2D(5, 6);
        Vector2D v2 = new Vector2D(2, 4);
        Vector2D result = v1.upperRight(v2);
        Vector2D expected = new Vector2D(5, 6);
        assertEquals(expected, result, "Expected upperRight to return (5,6) when comparing (5,6) and (2,4).");
    }

    @Test
    void testLowerLeftSmallerOther() {
        Vector2D v1 = new Vector2D(5, 6);
        Vector2D v2 = new Vector2D(2, 4);
        Vector2D result = v1.lowerLeft(v2);
        Vector2D expected = new Vector2D(2, 4);
        assertEquals(expected, result, "Expected lowerLeft to return (2,4) when comparing (5,6) and (2,4).");
    }

    @Test
    void testLowerLeftLargerOther() {
        Vector2D v1 = new Vector2D(1, 2);
        Vector2D v2 = new Vector2D(3, 4);
        Vector2D result = v1.lowerLeft(v2);
        Vector2D expected = new Vector2D(1, 2);
        assertEquals(expected, result, "Expected lowerLeft to return (1,2) when comparing (1,2) and (3,4).");
    }

    @Test
    void testUpperRightEqual() {
        Vector2D v1 = new Vector2D(5, 5);
        Vector2D v2 = new Vector2D(5, 5);
        Vector2D result = v1.upperRight(v2);
        Vector2D expected = new Vector2D(5, 5);
        assertEquals(expected, result, "Expected upperRight to return (5,5) when both vectors are equal.");
    }

    @Test
    void testLowerLeftEqual() {
        Vector2D v1 = new Vector2D(3, 3);
        Vector2D v2 = new Vector2D(3, 3);
        Vector2D result = v1.lowerLeft(v2);
        Vector2D expected = new Vector2D(3, 3);
        assertEquals(expected, result, "Expected lowerLeft to return (3,3) when both vectors are equal.");
    }

    @Test
    void testUpperRightNegativeCoordinates() {
        Vector2D v1 = new Vector2D(-2, -3);
        Vector2D v2 = new Vector2D(-1, -5);
        Vector2D result = v1.upperRight(v2);
        Vector2D expected = new Vector2D(-1, -3);
        assertEquals(expected, result, "Expected upperRight to return (-1,-3) when comparing (-2,-3) and (-1,-5).");
    }

    @Test
    void testLowerLeftNegative() {
        Vector2D v1 = new Vector2D(-2, -3);
        Vector2D v2 = new Vector2D(-1, -5);
        Vector2D result = v1.lowerLeft(v2);
        Vector2D expected = new Vector2D(-2, -5);
        assertEquals(expected, result, "Expected lowerLeft to return (-2,-5) when comparing (-2,-3) and (-1,-5).");
    }

    @Test
    public void testAdd() {
        Vector2D v1 = new Vector2D(1, 1);
        Vector2D v2 = new Vector2D(1, 1);
        Vector2D resultA = v1.add(v2);
        Vector2D resultB = v2.add(v1);
        assertEquals(2, resultA.x(), "Expected x-coordinate of result to be 2 after addition.");
        assertEquals(2, resultA.y(), "Expected y-coordinate of result to be 2 after addition.");
        assertEquals(2, resultB.x(), "Expected x-coordinate of result to be 2 after addition.");
        assertEquals(2, resultB.y(), "Expected y-coordinate of result to be 2 after addition.");
    }

    @Test
    public void testSubtract() {
        Vector2D v1 = new Vector2D(1, 1);
        Vector2D v2 = new Vector2D(1, 1);
        Vector2D resultA = v1.subtract(v2);
        Vector2D resultB = v2.subtract(v1);
        assertEquals(0, resultA.x(), "Expected x-coordinate of result to be 0 after subtraction.");
        assertEquals(0, resultA.y(), "Expected y-coordinate of result to be 0 after subtraction.");
        assertEquals(0, resultB.x(), "Expected x-coordinate of result to be 0 after subtraction.");
        assertEquals(0, resultB.y(), "Expected y-coordinate of result to be 0 after subtraction.");
    }

    @Test
    public void testOppositePositive() {
        Vector2D vector = new Vector2D(3, 4);
        Vector2D result = vector.opposite();
        assertEquals(-3, result.x(), "Expected x-coordinate of opposite vector to be -3.");
        assertEquals(-4, result.y(), "Expected y-coordinate of opposite vector to be -4.");
    }

    @Test
    public void testOppositeNegative() {
        Vector2D vector = new Vector2D(-3, -4);
        Vector2D result = vector.opposite();
        assertEquals(3, result.x(), "Expected x-coordinate of opposite vector to be 3.");
        assertEquals(4, result.y(), "Expected y-coordinate of opposite vector to be 4.");
    }

    @Test
    public void testOppositeZero() {
        Vector2D vector = new Vector2D(0, 0);
        Vector2D result = vector.opposite();
        assertEquals(0, result.x(), "Expected x-coordinate of opposite vector to be 0.");
        assertEquals(0, result.y(), "Expected y-coordinate of opposite vector to be 0.");
    }

    @Test
    public void testOppositeMixed() {
        Vector2D vectorA = new Vector2D(-1, 1);
        Vector2D vectorB = new Vector2D(1, -1);
        Vector2D resultA = vectorA.opposite();
        Vector2D resultB = vectorB.opposite();
        assertEquals(1, resultA.x(), "Expected x-coordinate of opposite vector to be 1.");
        assertEquals(-1, resultA.y(), "Expected y-coordinate of opposite vector to be -1.");
        assertEquals(-1, resultB.x(), "Expected x-coordinate of opposite vector to be -1.");
        assertEquals(1, resultB.y(), "Expected y-coordinate of opposite vector to be 1.");
    }

    @Test
    public void testNormalizeNull() {
        Vector2D vector = new Vector2D(1, 1);
        assertEquals(vector, vector.normalize(null, null), "Expected normalize to return the same vector when width and height are null.");
    }

    @Test
    public void testNormalizeOnlyWidth() {
        Vector2D vector = new Vector2D(3, 3);
        assertEquals(new Vector2D(1, 3), vector.normalize(2, null), "Expected normalize to wrap the x-coordinate within the width limit.");
    }

    @Test
    public void testNormalizeOnlyHeight() {
        Vector2D vector = new Vector2D(3, 3);
        assertEquals(new Vector2D(3, 1), vector.normalize(null, 2), "Expected normalize to wrap the y-coordinate within the height limit.");
    }

    @Test
    public void testNormalize() {
        Vector2D vector = new Vector2D(3, 3);
        assertEquals(new Vector2D(0, 1), vector.normalize(3, 2), "Expected normalize to wrap both coordinates within their respective limits.");
    }

    @Test
    public void testNormalizeOnlyWidthNegative() {
        Vector2D vector = new Vector2D(-3, -3);
        assertEquals(new Vector2D(1, -3), vector.normalize(2, null), "Expected normalize to wrap the negative x-coordinate within the width limit.");
    }

    @Test
    public void testNormalizeOnlyHeightNegative() {
        Vector2D vector = new Vector2D(-3, -3);
        assertEquals(new Vector2D(-3, 1), vector.normalize(null, 2), "Expected normalize to wrap the negative y-coordinate within the height limit.");
    }

    @Test
    public void testNormalizeNegative() {
        Vector2D vector = new Vector2D(-3, -3);
        assertEquals(new Vector2D(0, 1), vector.normalize(3, 2), "Expected normalize to wrap both negative coordinates within their respective limits.");
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
