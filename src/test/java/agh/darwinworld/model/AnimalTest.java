package agh.darwinworld.model;

import org.junit.jupiter.api.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnimalTest {
    private static final int GENOME_LENGTH = 10;
    private static final int ENERGY = 20;
    private static final int BREEDING_ENERGY_COST = 10;
    private Random random = new Random();
    private Animal animalA;
    private Animal animalB;
    private Animal animalC;

    @BeforeEach
    void setUp() {
        animalA = new Animal(random, GENOME_LENGTH, ENERGY + BREEDING_ENERGY_COST);
        animalB = new Animal(random, GENOME_LENGTH, ENERGY + BREEDING_ENERGY_COST);
        animalC = new Animal(animalA, animalB, BREEDING_ENERGY_COST, 0, 0, 1);
    }

    @Test
    void testConstructorA() {
        assertEquals(animalA.getEnergy(), ENERGY);
        assertEquals(animalA.getGenome().length, GENOME_LENGTH);
        assertEquals(animalA.getAge(), 0);
        assertEquals(animalB.getEnergy(), ENERGY);
        assertEquals(animalB.getGenome().length, GENOME_LENGTH);
        assertEquals(animalB.getAge(), 0);
    }

    @Test
    void testConstructorB() {
        assertEquals(animalC.getEnergy(), BREEDING_ENERGY_COST * 2);
        assertEquals(animalC.getGenome().length, GENOME_LENGTH);
        assertEquals(animalC.getAge(), 0);
    }
    // to rework up

    @Test
    void testIsDead() {
        Animal animal = new Animal(random ,1, 0);
        assertTrue(animal.isDead());
    }
}
