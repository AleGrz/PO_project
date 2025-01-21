package agh.darwinworld.models.maps;

import agh.darwinworld.models.MapDirection;
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
        fireMap.populateAnimals(10);
        fireMap.growPlants(20);
        assertEquals(10, fireMap.animalCount());
        assertEquals(20, fireMap.plantCount());
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

    @Test
    void testFireKillsAnimals() {
        fireMap.populateAnimals(10);
        fireMap.growPlants(1000);
        fireMap.step(fireMap.params.fireInterval());
        fireMap.step(fireMap.params.fireInterval()+1);
        fireMap.step(fireMap.params.fireInterval()+2);
        fireMap.step(fireMap.params.fireInterval()+3);
        fireMap.step(fireMap.params.fireInterval()+4);
        fireMap.step(fireMap.params.fireInterval()+5);
        fireMap.step(fireMap.params.fireInterval()+6);
        fireMap.step(fireMap.params.fireInterval()+7);
        fireMap.step(fireMap.params.fireInterval()+8);
        fireMap.step(fireMap.params.fireInterval()+9);
        fireMap.step(fireMap.params.fireInterval()+10);
        fireMap.step(fireMap.params.fireInterval()+11);
        fireMap.step(fireMap.params.fireInterval()+12);
        fireMap.step(fireMap.params.fireInterval()+13);
        fireMap.step(fireMap.params.fireInterval()+14);
        assertEquals(0, fireMap.animalCount());
    }

    @Test
    void testMove() {
        Vector2D position = new Vector2D(5, 5);
        Pair<Vector2D, MapDirection> newPosition = fireMap.move(position, MapDirection.NORTH);
        assertEquals(new Vector2D(5, 6), newPosition.getKey());
        assertEquals(MapDirection.NORTH, newPosition.getValue());

        Vector2D position2 = new Vector2D(0, 0);
        Pair<Vector2D, MapDirection> newPosition2 = fireMap.move(position2, MapDirection.SOUTHEAST);
        assertEquals(new Vector2D(1, 0), newPosition2.getKey());
        assertEquals(MapDirection.SOUTHEAST, newPosition2.getValue());

        Vector2D position3 = new Vector2D(9, 9);
        Pair<Vector2D, MapDirection> newPosition3 = fireMap.move(position3, MapDirection.NORTHWEST);
        assertEquals(new Vector2D(8, 9), newPosition3.getKey());
        assertEquals(MapDirection.NORTHWEST, newPosition3.getValue());

        Vector2D position4 = new Vector2D(9, 5);
        Pair<Vector2D, MapDirection> newPosition4 = fireMap.move(position4, MapDirection.EAST);
        assertEquals(new Vector2D(9, 5), newPosition4.getKey());
        assertEquals(MapDirection.EAST, newPosition4.getValue());
    }

}
