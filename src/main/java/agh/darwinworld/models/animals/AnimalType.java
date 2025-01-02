package agh.darwinworld.models.animals;

import java.util.Random;

/**
 * All possible animal behaviours in a simulation.
 */
public enum AnimalType {
    /**
     * Basic animal behaviour.
     */
    ANIMAL("Normal"),

    /**
     * The older the animal, the more often it skips its movements.
     */
    AGEING_ANIMAL("Oldness - sadness");

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

    /**
     * To string label of the enum.
     */
    private final String label;

    /**
     * Constructs a AnimalType with a specific label.
     *
     * @param label the label associated with the animal type.
     */
    AnimalType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
