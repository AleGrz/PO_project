package agh.darwinworld.model;

import javafx.util.Pair;

import java.util.*;
import java.util.stream.Stream;

public class Animal {
    private final Random random;
    private final MoveDirection[] genome;
    private MapDirection direction;
    private int energy;
    private int currentGene;
    private int age = 0;
    private int childrenAmount = 0;
    private int totalEatenPlants = 0;
    private final ArrayList<AnimalListener> listeners = new ArrayList<>();

    public Animal(Random random, int genomeLength, int energy) {
        if (genomeLength < 0)
            throw new IllegalArgumentException("Genome length must be greater than 0!");
        if (energy < 0)
            throw new IllegalArgumentException("Energy must be greater than or equal to 0!");
        this.random = random;
        this.genome = new MoveDirection[genomeLength];
        this.energy = energy;
        MapDirection[] mapDirections = MapDirection.values();
        this.direction = mapDirections[random.nextInt(mapDirections.length)];
        MoveDirection[] moveDirections = MoveDirection.values();
        for (int i = 0; i < genomeLength; i++)
            genome[i] = moveDirections[random.nextInt(moveDirections.length)];
        this.currentGene = random.nextInt(genomeLength);
    }

    public Animal(Animal mommy, Animal daddy, int breedingEnergyCost, int minimalBreedingEnergy,
                  int minMutations, int maxMutations) {
        if (mommy == null || daddy == null)
            throw new IllegalArgumentException("Parents cannot be null!");
        if (mommy.isDead() || daddy.isDead())
            throw new IllegalArgumentException("Provided animal cannot be dead!");
        if (mommy.energy < minimalBreedingEnergy || daddy.energy < minimalBreedingEnergy)
            throw new IllegalArgumentException("Provided animal does not have enough energy!");

        mommy.energy -= breedingEnergyCost;
        daddy.energy -= breedingEnergyCost;

        this.random = daddy.random;

        int totalEnergy = mommy.energy + daddy.energy;
        int mommyGenomeAmount = Math.round(mommy.genome.length * ((float) mommy.energy / totalEnergy));
        int daddyGenomeAmount = mommy.genome.length - mommyGenomeAmount; // Remaining genes go to daddy

        boolean mommyLeft = random.nextBoolean();
        MoveDirection[] firstGenomes = mommy.extractGenomes(mommyLeft, mommyGenomeAmount);
        MoveDirection[] secondGenomes = daddy.extractGenomes(!mommyLeft, daddyGenomeAmount);

        this.genome = Stream.concat(Arrays.stream(firstGenomes), Arrays.stream(secondGenomes))
                .toArray(MoveDirection[]::new);
        this.energy = breedingEnergyCost * 2;

        mutate(minMutations, maxMutations);

        MapDirection[] directions = MapDirection.values();
        this.direction = directions[random.nextInt(directions.length)];
        this.currentGene = random.nextInt(this.genome.length);

        mommy.childrenAmount++;
        daddy.childrenAmount++;
    }

    public MapDirection getDirection() {
        return this.direction;
    }

    public MoveDirection[] getGenome() {
        return this.genome;
    }

    public MoveDirection getCurrentGene() {
        return this.genome[this.currentGene];
    }

    public int getAge() {
        return this.age;
    }

    public int getEnergy() {
        return this.energy;
    }

    public int getChildrenAmount() {
        return this.childrenAmount;
    }

    public int getDescendantsAmount() {
        return 12;
    }

    public boolean isDead() {
        return energy < 0;
    }

    public int getTotalEatenPlants() {
        return this.totalEatenPlants;
    }

    public void addListener(AnimalListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(AnimalListener listener) {
        this.listeners.remove(listener);
    }

    public Vector2D move(MovementHandler handler, Vector2D position) {
        if (isDead())
            throw new IllegalStateException("Cannot move animal that is dead!");
        if (this.genome.length == 0)
            throw new IllegalStateException("Cannot move animal with empty genome!");

        this.direction = this.direction.rotate(this.genome[this.currentGene]);
        this.currentGene = (this.currentGene + 1) % this.genome.length;
        this.energy--;
        this.age++;
        if (random.nextInt(100)<Math.min(age, 80)) {
            return position;
        }
        Pair<Vector2D, MapDirection> movePair = handler.move(position, this.direction);
        Vector2D newPos = movePair.getKey();
        this.direction = movePair.getValue();

        listeners.forEach(AnimalListener::statsUpdate);
        listeners.forEach(listener -> listener.move(position, newPos));
        return newPos;
    }

    public void mutate(int min, int max) {
        if (isDead())
            throw new IllegalArgumentException("Cannot mutate genome when animal is dead!");
        if (min >= max)
            throw new IllegalArgumentException("Minimum amount of mutations has to be less than maximum amount of mutations!");
        int mutateAmount = random.nextInt(max - min) + min;
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < genome.length; i++) indexes.add(i);
        Collections.shuffle(indexes, random);
        MoveDirection[] directions = MoveDirection.values();
        for (int i = 0; i < mutateAmount; i++) {
            genome[indexes.get(i)] = directions[random.nextInt(directions.length)];
        }
        listeners.forEach(AnimalListener::statsUpdate);
    }

    public void eat(int energy) {
        if (energy < 0)
            throw new IllegalArgumentException("Energy added must be greater than or equal to 0!");
        totalEatenPlants++;
        this.energy += energy;
        listeners.forEach(AnimalListener::statsUpdate);
    }


    private MoveDirection[] extractGenomes(boolean leftPart, int amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Amount must be greater than or equal to 0!");

        int start = leftPart ? 0 : genome.length-amount;
        int end = leftPart ? amount : genome.length;

        return  Arrays.copyOfRange(genome, start, end);
    }
}
