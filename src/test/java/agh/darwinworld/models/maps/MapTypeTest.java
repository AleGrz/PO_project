package agh.darwinworld.models.maps;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapTypeTest {
    @Test
    void testCreateMap() {
        // Test for WORLD
        AbstractMap worldMap = MapType.WORLD.createMap();
        assertNotNull(worldMap, "WORLD map should not be null");
        assertInstanceOf(WorldMap.class, worldMap, "WORLD map should be an instance of WorldMap");

        // Test for FIRE
        AbstractMap fireMap = MapType.FIRE.createMap();
        assertNotNull(fireMap, "FIRE map should not be null");
        assertInstanceOf(FireMap.class, fireMap, "FIRE map should be an instance of FireMap");
    }

    @Test
    void testToString() {
        assertEquals("World map", MapType.WORLD.toString(), "World map label mismatch");
        assertEquals("Fire map", MapType.FIRE.toString(), "Fire map label mismatch");
    }
}