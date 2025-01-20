package agh.darwinworld.models.animals;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTypeTest {

    @Test
    void testCreateAnimalWithRandom() {
        Random random = new Random();
        int genomeLength = 10;
        int energy = 100;

        // Test ANIMAL
        Animal normalAnimal = AnimalType.ANIMAL.createAnimal(random, genomeLength, energy);
        assertNotNull(normalAnimal);
        assertEquals(Animal.class, normalAnimal.getClass());
        assertEquals(energy, normalAnimal.getEnergy());

        // Test AGEING_ANIMAL
        Animal ageingAnimal = AnimalType.AGEING_ANIMAL.createAnimal(random, genomeLength, energy);
        assertNotNull(ageingAnimal);
        assertEquals(AgeingAnimal.class, ageingAnimal.getClass());
        assertEquals(energy, ageingAnimal.getEnergy());
    }

    @Test
    void testCreateAnimalWithParents() {
        Random random = new Random();
        int genomeLength = 10;
        int energy = 100;

        Animal mommy = new Animal(random, genomeLength, energy);
        Animal daddy = new Animal(random, genomeLength, energy);

        int breedingEnergyCost = 20;
        int minimalBreedingEnergy = 50;
        int minMutations = 1;
        int maxMutations = 3;
        int step = 2;

        // Test ANIMAL
        Animal normalAnimal = AnimalType.ANIMAL.createAnimal(mommy, daddy, breedingEnergyCost, minimalBreedingEnergy, minMutations, maxMutations, step);
        assertNotNull(normalAnimal);
        assertEquals(Animal.class, normalAnimal.getClass());
        assertEquals(breedingEnergyCost * 2, normalAnimal.getEnergy());

        // Test AGEING_ANIMAL
        Animal ageingAnimal = AnimalType.AGEING_ANIMAL.createAnimal(mommy, daddy, breedingEnergyCost, minimalBreedingEnergy, minMutations, maxMutations, step);
        assertNotNull(ageingAnimal);
        assertEquals(AgeingAnimal.class, ageingAnimal.getClass());
        assertEquals(breedingEnergyCost * 2, ageingAnimal.getEnergy());
    }

    @Test
    void testToString() {
        assertEquals("Normal", AnimalType.ANIMAL.toString());
        assertEquals("Oldness - sadness", AnimalType.AGEING_ANIMAL.toString());
    }
}
