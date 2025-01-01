package agh.darwinworld.models;

import agh.darwinworld.models.animals.Animal;
import agh.darwinworld.models.listeners.MovementHandler;
import javafx.util.Pair;
import org.junit.jupiter.api.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {
    private final Random random = new Random();
    private final SimpleMovementHandler movementHandler = new SimpleMovementHandler();

    private static class SimpleMovementHandler implements MovementHandler {
        @Override
        public Pair<Vector2D, MapDirection> move(Vector2D position, MapDirection move) {
            return new Pair<>(position, move);
        }
    }

    @Test
    public void testConstructorA() {
        final int genomeLength = 10;
        final int energy = 10;
        Animal animal = new Animal(random, genomeLength, energy);
        assertEquals(energy, animal.getEnergy());
        assertEquals(genomeLength, animal.getGenome().length);
        assertEquals(0, animal.getAge());
    }

    @Test
    public void testConstructorBEven() {
        final int genomeLength = 10;
        final int energy = 20;
        final int breedingEnergyCost = 10;
        Animal dad = new Animal(random, genomeLength, energy);
        Animal mom = new Animal(random, genomeLength, energy);
        Animal child = new Animal(dad, mom, breedingEnergyCost, 0, 0, 1, 0);
        assertEquals(breedingEnergyCost * 2, child.getEnergy());
        assertEquals(genomeLength, child.getGenome().length);
        assertEquals(0, child.getAge());
    }
    @Test
    public void testConstructorBOdd() {
        final int genomeLength = 7;
        final int energy = 20;
        final int breedingEnergyCost = 10;
        Animal dad = new Animal(random, genomeLength, energy);
        Animal mom = new Animal(random, genomeLength, energy);
        Animal child = new Animal(dad, mom, breedingEnergyCost, 0, 0, 1, 0);
        assertEquals(breedingEnergyCost * 2, child.getEnergy());
        assertEquals(genomeLength, child.getGenome().length);
        assertEquals(0, child.getAge());
    }

    @Test
    public void testChildrenAmount() {
        final int genomeLength = 10;
        Animal mom = new Animal(random, genomeLength, 0);
        Animal dad = new Animal(random, genomeLength, 0);
        assertEquals(0, mom.getChildrenAmount());
        assertEquals(0, dad.getChildrenAmount());
        Animal child = new Animal(mom, dad, 0, 0, 0, 1, 0);
        assertEquals(1, mom.getChildrenAmount());
        assertEquals(1, dad.getChildrenAmount());
        assertEquals(0, child.getChildrenAmount());
    }

    @Test
    public void testDescendantsChildrenAmount() {
        final int genomeLength = 10;
        Animal mom = new Animal(random, genomeLength, 0);
        Animal dad = new Animal(random, genomeLength, 0);
        assertEquals(0, mom.getDescendantsAmount());
        assertEquals(0, dad.getDescendantsAmount());
        Animal childA = new Animal(mom, dad, 0, 0, 0, 1, 0);
        assertEquals(1, mom.getDescendantsAmount());
        assertEquals(1, dad.getDescendantsAmount());
        assertEquals(0, childA.getDescendantsAmount());
        Animal childB = new Animal(mom, dad, 0, 0, 0, 1, 0);
        assertEquals(2, mom.getDescendantsAmount());
        assertEquals(2, dad.getDescendantsAmount());
        assertEquals(0, childB.getDescendantsAmount());
        Animal childAB = new Animal(childA, childB, 0, 0, 0, 1, 0);
        assertEquals(3, mom.getDescendantsAmount());
        assertEquals(3, dad.getDescendantsAmount());
        assertEquals(1, childA.getDescendantsAmount());
        assertEquals(1, childB.getDescendantsAmount());
        assertEquals(0, childAB.getDescendantsAmount());
        Animal otherChild = new Animal(random, genomeLength, 0);
        Animal childABOther = new Animal(childAB, otherChild, 0, 0, 0, 1, 0);
        assertEquals(4, mom.getDescendantsAmount());
        assertEquals(4, dad.getDescendantsAmount());
        assertEquals(2, childA.getDescendantsAmount());
        assertEquals(2, childB.getDescendantsAmount());
        assertEquals(1, childAB.getDescendantsAmount());
        assertEquals(1, otherChild.getDescendantsAmount());
        assertEquals(0, childABOther.getDescendantsAmount());
    }

    @Test
    public void testIsDeadForceKill() {
        Animal animal = new Animal(random ,1, 0);
        animal.forceKill(0);
        assertTrue(animal.isDead());
    }

    @Test
    public void testMoveAgeChange() {
        Animal animal = new Animal(random ,1, 10);
        assertEquals(0, animal.getAge());
    }

    @Test
    public void testMoveEnergyChange() {
        final int baseEnergy = 10;
        Animal animal = new Animal(random ,1, baseEnergy);
        for (int i = baseEnergy; i > 0; i--) {
            assertEquals(animal.getEnergy(), i);
            animal.move(movementHandler, new Vector2D(0, 0), 0);
        }
        assertEquals(0, animal.getEnergy());
    }

    @Test
    public void testEat() {
        Animal animal = new Animal(random, 1, 10);
        animal.eat(5, 0);
        assertEquals(15, animal.getEnergy());
        assertThrows(IllegalArgumentException.class, () -> animal.eat(-1, 0));
    }
}
