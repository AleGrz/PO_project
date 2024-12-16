package agh.darwinworld.model;

import java.util.*;

public class Animal {
    private final Random random = new Random();

    private final List<Direction> genome;
    private Direction direction;
    private int energy;
    private int age = 0;
    private int activatedGenome;

    public Animal(int genomeLength, int energy) {
        this.genome = new ArrayList<>(genomeLength);
        this.energy = energy;
        Direction[] directions = Direction.values();
        this.direction = directions[random.nextInt(directions.length)];
        for (int i = 0; i < genomeLength; i++)
            genome.set(i, directions[random.nextInt(directions.length)]);
        this.activatedGenome = random.nextInt(0, genomeLength);
    }

    public Animal(Animal mommy, Animal daddy, int breedingEnergyCost, int minimalBreedingEnergy,
                  int minMutations, int maxMutations) {
        if (daddy.energy >= minimalBreedingEnergy || mommy.energy >= minimalBreedingEnergy)
            throw new IllegalArgumentException("Cannot create child of dead animal!");
        daddy.energy -= breedingEnergyCost;
        mommy.energy -= breedingEnergyCost;
        int total = mommy.energy + daddy.energy;
        int mommyGenomeAmount = Math.round(mommy.genome.size() * ((float) mommy.energy / total));
        int daddyGenomeAmount = Math.round(daddy.genome.size() * ((float) daddy.energy / total));
        boolean mommyLeft = random.nextBoolean();
        List<Direction> firstGenomes = mommy.extractGenomes(mommyLeft, mommyGenomeAmount);
        List<Direction> secondGenomes = daddy.extractGenomes(!mommyLeft, daddyGenomeAmount);
        if (mommyLeft) {
            this.genome = firstGenomes;
            genome.addAll(secondGenomes);
        } else {
            this.genome = secondGenomes;
            this.genome.addAll(firstGenomes);
        }
        mutate(minMutations, maxMutations);
        Direction[] directions = Direction.values();
        this.energy = breedingEnergyCost * 2;
        this.direction = directions[random.nextInt(0, directions.length)];
        this.activatedGenome = random.nextInt(0, mommyGenomeAmount + daddyGenomeAmount);
    }

    public Direction getDirection() {
        return direction;
    }

    public Vector2D move(Vector2D pos) {

        // jak to zakodowac????
        age += 1;
        return pos;
    }

    public void mutate(int min, int max) {
        int mutateAmount = random.nextInt(max - min) + min;
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < genome.size(); i++) indexes.add(i);
        Direction[] directions = Direction.values();
        for (int i = 0; i < mutateAmount; i++) {
            genome.set(indexes.get(i), directions[random.nextInt(directions.length)]);
        }
    }

    public int getEnergy() {
        return energy;
    }

    public void eat(int energy) {
        this.energy += energy;
    }

    public boolean isDead() {
        return energy <= 0;
    }

    private List<Direction> extractGenomes(boolean leftPart, int amount) {
        int leftIdx = leftPart ? 0 : genome.size() / 2;
        int rightIdx = leftPart ? genome.size() / 2 : genome.size();
        ArrayList<Direction> newList = new ArrayList<>(genome.subList(leftIdx, rightIdx));
        Collections.shuffle(newList);
        return newList.subList(0, amount);
    }
}
