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
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FireMapTest {
    private FireMap fireMap;
    private SimulationParameters params;

    @BeforeEach
    void setUp() {
        params = new SimulationParameters(10, 10, 1, 0, 5,
                10, 2, 10, 5, 0,
                1, 7, 5, 5, 10, 5, MapType.WORLD,
                AnimalType.ANIMAL);
        fireMap = new FireMap();
        fireMap.setParameters(params);
    }

    @Test
    void testInitialFireMapState() {
        assertEquals(10, fireMap.animalCount());
        assertEquals(20, fireMap.plantCount());
        assertEquals(0, fireMap.emptyFieldCount());
    }

    @Test
    void testSetParameters() {
        assertNotNull(fireMap.params);
    }

    @Test
    void testGetMaxAnimalAmount() {
        fireMap.populateAnimals(10);
        assertTrue(fireMap.getMaxAnimalAmount() > 0);
    }

    @Test
    void testGetAnimalsOnPosition() {
        fireMap.populateAnimals(1);
        Vector2D position = fireMap.animals.keySet().iterator().next();
        assertEquals(1, fireMap.getAnimalsOnPosition(position).size());
    }

    @Test
    void testIsPlantOnPosition() {
        Vector2D plantPosition = new Vector2D(5, 5);
        fireMap.plants.add(plantPosition);
        assertTrue(fireMap.isPlantOnPosition(plantPosition));
    }

    @Test
    void testAnimalCount() {
        fireMap.populateAnimals(10);
        assertEquals(10, fireMap.animalCount());
    }

    @Test
    void testPlantCount() {
        fireMap.plants.add(new Vector2D(1, 1));
        fireMap.plants.add(new Vector2D(2, 2));
        assertEquals(2, fireMap.plantCount());
    }

    @Test
    void testIsPreferredRow() {
        int height = params.height();
        int equatorHeight = Math.round(height * 0.2f);
        int barHeight = Math.round((height - equatorHeight) / 2f);
        assertTrue(fireMap.isPreferredRow(barHeight));
        assertFalse(fireMap.isPreferredRow(0));
    }

    @Test
    void testEmptyFieldCount() {
        int totalFields = params.width() * params.height();
        fireMap.populateAnimals(10);
        assertTrue(totalFields - 10 <= fireMap.emptyFieldCount());
    }

    @Test
    void testPopularGenome() {
        fireMap.populateAnimals(10);
        Pair<MoveDirection[], Integer> result = fireMap.popularGenome();
        assertNotNull(result.getKey());
        assertTrue(result.getValue() > 0);
    }

    @Test
    void testAverageLifetime() {
        assertEquals(0, fireMap.averageLifetime());
    }

    @Test
    void testAverageDescendantsAmount() {
        fireMap.populateAnimals(10);
        assertEquals(0, fireMap.averageDescendantsAmount());
    }

    @Test
    void testPopulateAnimals() {
        fireMap.populateAnimals(5);
        assertEquals(5, fireMap.animalCount());
    }

    @Test
    void testGrowPlants() {
        fireMap.growPlants(10);
        assertEquals(10, fireMap.plantCount());
    }

    @Test
    void testRemoveDeadAnimals() {
        fireMap.populateAnimals(10);
        fireMap.animals.values().stream().flatMap(List::stream).forEach(animal -> animal.forceKill(1));
        fireMap.removeDeadAnimals();
        assertEquals(0, fireMap.animalCount());
    }

    @Test
    void testMoveAnimals() {
        fireMap.populateAnimals(5);
        fireMap.moveAnimals(1);
        assertEquals(5, fireMap.animalCount());
    }

    @Test
    void testStep() {
        fireMap.populateAnimals(5);
        fireMap.growPlants(10);
        fireMap.step(1);
        assertTrue(fireMap.animalCount() > 0);
        assertTrue(fireMap.plantCount() > 0);
    }

    @Test
    void testFireDestroyingEnvironment() {
        fireMap.populateAnimals(10);
        fireMap.growPlants(1000);
        assertEquals(0, fireMap.emptyFieldCount());
        fireMap.step(fireMap.params.fireInterval());
        fireMap.step(fireMap.params.fireInterval()+1);
        fireMap.step(fireMap.params.fireInterval()+2);
        fireMap.step(fireMap.params.fireInterval()+3);
        assertEquals(63, fireMap.plantCount());
    }

}
