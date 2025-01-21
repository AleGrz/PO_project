package agh.darwinworld.models.maps;

import agh.darwinworld.models.MoveDirection;
import agh.darwinworld.models.SimulationParameters;
import agh.darwinworld.models.Vector2D;
import agh.darwinworld.models.animals.AnimalType;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WorldMapTest {
    private WorldMap worldMap;
    private SimulationParameters params;

    @BeforeEach
    void setUp() {
        params = new SimulationParameters(10, 10, 1, 100, 5,
                10, 2, 1, 5, 1,
                2, 8, 5, 5, 10, 5, MapType.WORLD,
                AnimalType.ANIMAL);
        worldMap = new WorldMap();
        worldMap.setParameters(params);
    }

    @Test
    void testSetParameters() {
        assertNotNull(worldMap.params);
    }

    @Test
    void testGetMaxAnimalAmount() {
        worldMap.populateAnimals(10);
        assertTrue(worldMap.getMaxAnimalAmount() > 0);
    }

    @Test
    void testGetAnimalsOnPosition() {
        worldMap.populateAnimals(1);
        Vector2D position = worldMap.animals.keySet().iterator().next();
        assertEquals(1, worldMap.getAnimalsOnPosition(position).size());
    }

    @Test
    void testIsPlantOnPosition() {
        Vector2D plantPosition = new Vector2D(5, 5);
        worldMap.plants.add(plantPosition);
        assertTrue(worldMap.isPlantOnPosition(plantPosition));
    }

    @Test
    void testAnimalCount() {
        worldMap.populateAnimals(10);
        assertEquals(10, worldMap.animalCount());
    }

    @Test
    void testPlantCount() {
        worldMap.plants.add(new Vector2D(1, 1));
        worldMap.plants.add(new Vector2D(2, 2));
        assertEquals(2, worldMap.plantCount());
    }

    @Test
    void testIsPreferredRow() {
        int height = params.height();
        int equatorHeight = Math.round(height * 0.2f);
        int barHeight = Math.round((height - equatorHeight) / 2f);
        assertTrue(worldMap.isPreferredRow(barHeight));
        assertFalse(worldMap.isPreferredRow(0));
    }

    @Test
    void testEmptyFieldCount() {
        int totalFields = params.width() * params.height();
        worldMap.populateAnimals(10);
        assertTrue(totalFields - 10 <= worldMap.emptyFieldCount());
    }

    @Test
    void testPopularGenome() {
        worldMap.populateAnimals(10);
        Pair<MoveDirection[], Integer> result = worldMap.popularGenome();
        assertNotNull(result.getKey());
        assertTrue(result.getValue() > 0);
    }

    @Test
    void testAverageLifetime() {
        assertEquals(0, worldMap.averageLifetime());
    }

    @Test
    void testAverageDescendantsAmount() {
        worldMap.populateAnimals(10);
        assertEquals(0, worldMap.averageDescendantsAmount());
    }

    @Test
    void testPopulateAnimals() {
        worldMap.populateAnimals(5);
        assertEquals(5, worldMap.animalCount());
    }

    @Test
    void testGrowPlants() {
        worldMap.growPlants(10);
        assertEquals(10, worldMap.plantCount());
    }

    @Test
    void testRemoveDeadAnimals() {
        worldMap.populateAnimals(10);
        worldMap.animals.values().stream().flatMap(List::stream).forEach(animal -> animal.forceKill(1));
        worldMap.removeDeadAnimals();
        assertEquals(0, worldMap.animalCount());
    }

    @Test
    void testMoveAnimals() {
        worldMap.populateAnimals(5);
        worldMap.moveAnimals(1);
        assertEquals(5, worldMap.animalCount());
    }

    @Test
    void testStep() {
        worldMap.populateAnimals(5);
        worldMap.growPlants(10);
        worldMap.step(1);
        assertTrue(worldMap.animalCount() > 0);
        assertTrue(worldMap.plantCount() > 0);
    }
}
