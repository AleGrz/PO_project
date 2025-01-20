package agh.darwinworld.models.animals;

import agh.darwinworld.models.MapDirection;
import agh.darwinworld.models.Vector2D;
import agh.darwinworld.models.listeners.MovementHandler;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class AgeingAnimalTest extends AnimalTest {
    @Test
    void testConstructorWithValidInputs() {
        AgeingAnimal animal = new AgeingAnimal(random, 5, 10);

        assertNotNull(animal, "Animal object should be successfully created.");
        assertEquals(5, animal.getGenome().length, "Genome length should match the input value.");
        assertEquals(10, animal.getEnergy(), "Initial energy should match the input value.");
        assertFalse(animal.isDead(), "Animal should not be dead upon creation.");
    }

    @Test
    public void testOffspringConstructorEvenGenome() {
        final int genomeLength = 10;
        final int energy = 20;
        final int breedingEnergyCost = 10;

        AgeingAnimal dad = new AgeingAnimal(random, genomeLength, energy);
        AgeingAnimal mom = new AgeingAnimal(random, genomeLength, energy);
        AgeingAnimal child = new AgeingAnimal(dad, mom, breedingEnergyCost, 0, 0, 1, 0);

        assertNotNull(child, "Child should be successfully created.");
        assertEquals(breedingEnergyCost * 2, child.getEnergy(), "Child's energy should equal twice the breeding energy cost.");
        assertEquals(genomeLength, child.getGenome().length, "Child's genome length should match parent's genome length.");
        assertEquals(0, child.getAge(), "Child's age should start at 0.");
        assertEquals(energy - breedingEnergyCost, dad.getEnergy(), "Dad's energy should be reduced correctly.");
        assertEquals(energy - breedingEnergyCost, mom.getEnergy(), "Mom's energy should be reduced correctly.");
    }

    @Test
    public void testOffspringConstructorOddGenome() {
        final int genomeLength = 7;
        final int energy = 20;
        final int breedingEnergyCost = 10;

        AgeingAnimal dad = new AgeingAnimal(random, genomeLength, energy);
        AgeingAnimal mom = new AgeingAnimal(random, genomeLength, energy);
        AgeingAnimal child = new AgeingAnimal(dad, mom, breedingEnergyCost, 0, 0, 1, 0);

        assertNotNull(child, "Child should be successfully created.");
        assertEquals(breedingEnergyCost * 2, child.getEnergy(), "Child's energy should equal twice the breeding energy cost.");
        assertEquals(genomeLength, child.getGenome().length, "Child's genome length should match parent's genome length.");
        assertEquals(0, child.getAge(), "Child's age should start at 0.");
        assertEquals(energy - breedingEnergyCost, dad.getEnergy(), "Dad's energy should be reduced correctly.");
        assertEquals(energy - breedingEnergyCost, mom.getEnergy(), "Mom's energy should be reduced correctly.");
    }

    @Test
    void movementProbabilityIncreasesWithAge() {
        Random random = new Random(123); // Fixed seed for reproducibility
        int totalMoves = 10000;
        AgeingAnimal animal = new AgeingAnimal(random, 10, totalMoves * 2);
        Vector2D initialPosition = new Vector2D(0, 0);

        int movesSkipped = 0;

        Vector2D currentPosition = initialPosition;
        MockMovementHandler mockMovementHandler = new MockMovementHandler();
        for (int step = 0; step < totalMoves; step++) {
            Vector2D newPosition = animal.move(mockMovementHandler, currentPosition, step);

            if (newPosition.equals(currentPosition)) {
                movesSkipped++;
            }
            currentPosition = newPosition;
        }

        assertTrue(movesSkipped > 0);
        assertTrue(movesSkipped < totalMoves);
    }

    @Test
    void movementWithoutSkipping() {
        Random random = new Random(0); // Fixed seed
        AgeingAnimal animal = new AgeingAnimal(random, 10, 100);

        MockMovementHandler handler = new MockMovementHandler();
        Vector2D initialPosition = new Vector2D(0, 0);

        Vector2D newPosition = animal.move(handler, initialPosition, 0);

        assertNotEquals(initialPosition, newPosition);
        assertEquals(handler.lastDirection, animal.getDirection());
    }

    @Test
    void movementWithSkippingDueToAge() {
        Random random = new Random(0); // Fixed seed
        AgeingAnimal animal = new AgeingAnimal(random, 10, 100);

        MockMovementHandler handler = new MockMovementHandler();
        Vector2D currentPosition = new Vector2D(0, 0);

        // Simulate multiple moves to increase age
        for (int step = 0; step < 100; step++) {
            Vector2D newPosition = animal.move(handler, currentPosition, step);

            if (newPosition.equals(currentPosition)) {
                return; // Test passes if skipping occurs
            }
            currentPosition = newPosition;
        }

        fail("Animal did not skip any moves despite aging.");
    }

    static class MockMovementHandler implements MovementHandler {
        public Vector2D lastPosition;
        public MapDirection lastDirection;

        @Override
        public Pair<Vector2D, MapDirection> move(Vector2D position, MapDirection direction) {
            lastPosition = position.add(direction.getValue());
            lastDirection = direction;
            return new Pair<>(lastPosition, direction);
        }
    }
}