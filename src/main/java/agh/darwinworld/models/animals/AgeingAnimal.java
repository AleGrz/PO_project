package agh.darwinworld.models.animals;

import agh.darwinworld.models.MapDirection;
import agh.darwinworld.models.Vector2D;
import agh.darwinworld.models.listeners.MovementHandler;
import javafx.util.Pair;

import java.util.Random;

/**
 * Represents an animal in the Darwin World simulation.
 * When animal is ageing it has higher probability that it
 * skip its move.
 */
public class AgeingAnimal extends Animal {
    /**
     * Constructs an animal with the specified genome length and initial energy.
     * @param random the random number generator.
     * @param genomeLength the length of the genome.
     * @param energy the initial energy of the animal/
     * @throws IllegalArgumentException if genomeLength < 0 or energy < 0.
     */
    public AgeingAnimal(Random random, int genomeLength, int energy) {
        super(random, genomeLength, energy);
    }

    /**
     * Constructs an offspring animal from two parent animals.
     * @param mommy the first parent animal.
     * @param daddy the second parent animal.
     * @param breedingEnergyCost the energy cost for breeding.
     * @param minimalBreedingEnergy the minimal energy required for breeding.
     * @param minMutations the minimum number of genome mutations.
     * @param maxMutations the maximum number of genome mutations.
     * @throws IllegalArgumentException if any parent is null, dead, or lacks energy.
     */
    public AgeingAnimal(Animal mommy, Animal daddy, int breedingEnergyCost, int minimalBreedingEnergy,
                  int minMutations, int maxMutations, int step) {
        super(mommy, daddy, breedingEnergyCost, minimalBreedingEnergy, minMutations, maxMutations, step);
    }

    @Override
    public Vector2D move(MovementHandler handler, Vector2D position, int step) {
        if (isDead())
            throw new IllegalStateException("Cannot move animal that is dead!");
        if (this.genome.length == 0)
            throw new IllegalStateException("Cannot move animal with empty genome!");

        MapDirection direction = this.direction.rotate(this.genome[this.currentGeneIndex]);
        selectNextGene();
        updateEnergy(getEnergy() - 1, step);
        increaseAge();
        if (random.nextInt(100) < Math.min(age, 80)) {
            return position;
        }
        Pair<Vector2D, MapDirection> movePair = handler.move(position, direction);
        Vector2D newPos = movePair.getKey();
        updateDirection(movePair.getValue());
        listeners.forEach(listener -> listener.move(position, newPos));
        return newPos;
    }
}
