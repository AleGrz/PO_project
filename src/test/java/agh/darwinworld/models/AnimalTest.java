package agh.darwinworld.models;

import org.junit.jupiter.api.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {
    private final Random random = new Random();
    @Test
    public void testChildrenAmount() {
        final int genomeLength = 10;
        Animal mom = new Animal(random, genomeLength, 0);
        Animal dad = new Animal(random, genomeLength, 0);
        assertEquals(mom.getChildrenAmount(), 0);
        assertEquals(dad.getChildrenAmount(), 0);
        Animal child = new Animal(mom, dad, 0, 0, 0, 1, 0);
        assertEquals(mom.getChildrenAmount(), 1);
        assertEquals(dad.getChildrenAmount(), 1);
        assertEquals(child.getChildrenAmount(), 0);
    }

    @Test
    public void testDescendantsChildrenAmount() {
        final int genomeLength = 10;
        Animal mom = new Animal(random, genomeLength, 0);
        Animal dad = new Animal(random, genomeLength, 0);
        assertEquals(mom.getDescendantsAmount(), 0);
        assertEquals(dad.getDescendantsAmount(), 0);
        Animal childA = new Animal(mom, dad, 0, 0, 0, 1, 0);
        assertEquals(mom.getDescendantsAmount(), 1);
        assertEquals(dad.getDescendantsAmount(), 1);
        assertEquals(childA.getDescendantsAmount(), 0);
        Animal childB = new Animal(mom, dad, 0, 0, 0, 1, 0);
        assertEquals(mom.getDescendantsAmount(), 2);
        assertEquals(dad.getDescendantsAmount(), 2);
        assertEquals(childB.getDescendantsAmount(), 0);
        Animal childAB = new Animal(childA, childB, 0, 0, 0, 1, 0);
        assertEquals(mom.getDescendantsAmount(), 3);
        assertEquals(dad.getDescendantsAmount(), 3);
        assertEquals(childA.getDescendantsAmount(), 1);
        assertEquals(childB.getDescendantsAmount(), 1);
        assertEquals(childAB.getDescendantsAmount(), 0);
        Animal otherChild = new Animal(random, genomeLength, 0);
        Animal childABOther = new Animal(childAB, otherChild, 0, 0, 0, 1, 0);
        assertEquals(mom.getDescendantsAmount(), 4);
        assertEquals(dad.getDescendantsAmount(), 4);
        assertEquals(childA.getDescendantsAmount(), 2);
        assertEquals(childB.getDescendantsAmount(), 2);
        assertEquals(childAB.getDescendantsAmount(), 1);
        assertEquals(otherChild.getDescendantsAmount(), 1);
        assertEquals(childABOther.getDescendantsAmount(), 0);
    }
    /*
    @Test
    public void testConstructorA() {
        final int genomeLength = 10;
        final int energy = 10;
        Animal animal = new Animal(random, genomeLength, energy);
        assertEquals(animal.getEnergy(), energy);
        assertEquals(animal.getGenome().length, genomeLength);
        assertEquals(animal.getAge(), 0);
    }

    @Test
    public void testConstructorB() {
        final int genomeLength = 10;
        final int energy = 20;
        final int breedingEnergyCost = 10;
        Animal dad = new Animal(random, genomeLength, energy);
        Animal mom = new Animal(random, genomeLength, energy);
        Animal child = new Animal(dad, mom, breedingEnergyCost, 0, 0, 1);
        assertEquals(child.getEnergy(), breedingEnergyCost * 2);
        assertEquals(child.getGenome().length, genomeLength);
        assertEquals(child.getAge(), 0);
    }

    @Test
    public void testIsDead() {
        Animal animal = new Animal(random ,1, 0);
        assertTrue(animal.isDead());
    }

    @Test
    public void testMoveAgeChange() {
        Animal animal = new Animal(random ,1, 10);
        assertEquals(animal.getAge(), 0);
    }

    @Test
    public void testMoveEnergyChange() {
        final int baseEnergy = 10;
        Animal animal = new Animal(random ,1, baseEnergy);
        for (int i = baseEnergy; i > 0; i--) {
            assertEquals(animal.getEnergy(), i);
            animal.move(new Vector2D(0, 0), 1, 1);
        }
        assertEquals(animal.getEnergy(), 0);
        assertThrows(IllegalStateException.class, () -> animal.move(new Vector2D(0, 0), 1, 1));
    }

    @Test
    public void testMoveWidthLoop() {
        final Random seededRandom = new Random(4098);
        Animal animal = new Animal(seededRandom ,1, 10);
        assertEquals(animal.getCurrentGene(), MoveDirection.RIGHT);
        assertEquals(animal.getDirection(), MapDirection.NORTH);
        assertEquals(new Vector2D(0, 0), animal.move(new Vector2D(1, 0), 2, 1));
    }

    // add more move tests

    @Test
    public void testMutateNoChange() {
        final int genomeLength = 100;
        Animal animal = new Animal(random ,genomeLength, 10);
        MoveDirection[] copiedGenome = Arrays.copyOf(animal.getGenome(), genomeLength);
        animal.mutate(0, 1);
        assertArrayEquals(copiedGenome, animal.getGenome());
    }

    @Test
    public void testMutateInMaxRange() {
        final int genomeLength = 100;
        Animal animal = new Animal(random ,genomeLength, 10);
        MoveDirection[] copiedGenome = Arrays.copyOf(animal.getGenome(), genomeLength);
        animal.mutate(0, 5);
        int visibleChanges = 0;
        MoveDirection[] currentGenome = animal.getGenome();
        for (int i = 0; i < currentGenome.length; i++) {
            if (copiedGenome[i] != currentGenome[i])
                visibleChanges++;
        }
        assertTrue(visibleChanges < 5);
    }

    @Test
    public void testEat() {
        Animal animal = new Animal(random, 1, 10);
        animal.eat(5);
        assertEquals(15, animal.getEnergy());
        assertThrows(IllegalArgumentException.class, () -> animal.eat(-1));
    }

    @Test
    public void testChildrenAmount() {
        final int genomeLength = 10;
        final int energy = 20;
        final int breedingEnergyCost = 10;
        Animal dad = new Animal(random, genomeLength, energy);
        Animal mom = new Animal(random, genomeLength, energy);
        new Animal(dad, mom, breedingEnergyCost, 0, 0, 1);
        assertEquals(dad.getChildrenAmount(), 1);
        assertEquals(mom.getChildrenAmount(), 1);
    }*/
}
