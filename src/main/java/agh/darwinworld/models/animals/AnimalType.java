package agh.darwinworld.models.animals;

import java.util.Random;

public enum AnimalType {
    ANIMAL,
    AGEING_ANIMAL;

    public Animal createAnimal(Random random, int genomeLength, int energy) {
        return switch (this) {
            case ANIMAL -> new Animal(random, genomeLength, energy);
            case AGEING_ANIMAL -> new AgeingAnimal(random, genomeLength, energy);
        };
    }

    public Animal createAnimal(Animal mommy, Animal daddy, int breedingEnergyCost, int minimalBreedingEnergy,
                            int minMutations, int maxMutations, int step) {
        return switch (this) {
            case ANIMAL ->
                    new Animal(mommy, daddy, breedingEnergyCost, minimalBreedingEnergy, minMutations, maxMutations, step);
            case AGEING_ANIMAL ->
                    new AgeingAnimal(mommy, daddy, breedingEnergyCost, minimalBreedingEnergy, minMutations, maxMutations, step);
        };
    }
}
