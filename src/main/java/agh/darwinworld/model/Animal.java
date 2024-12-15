package agh.darwinworld.model;

import java.util.*;

public class Animal extends WorldElement {
    public final static int BREEDING_ENERGY_COST = 10;
    public final static int EATING_ENERGY_RECEIVED = 10;
    public final static int MAX_STARTING_GENOME_AMOUNT = 10;
    private final Random random = new Random();

    private final List<Direction> genome;
    private Vector2D position;
    private Direction direction;
    private int energy;
    private int age;

    public Animal(Vector2D position, Direction direction) {
        this.position = position;
        this.direction = direction;
        int n = random.nextInt(MAX_STARTING_GENOME_AMOUNT);
        this.genome = new ArrayList<>(n);
        Direction[] directions = Direction.values();
        for (int i = 0; i < n; i++) {
            genome.set(i, directions[random.nextInt(directions.length)]);
        }
    }

    public Animal(Animal mommy, Animal daddy) {
        if (daddy.energy - BREEDING_ENERGY_COST <= 0 || mommy.energy - BREEDING_ENERGY_COST <= 0) {
            throw new IllegalArgumentException("Cannot create child of dead animal!");
        }
        if (!daddy.getPosition().equals(mommy.getPosition())) {
            throw new IllegalArgumentException("Cannot create child of when animals are not at the same position!");
        }
        daddy.energy -= BREEDING_ENERGY_COST;
        mommy.energy -= BREEDING_ENERGY_COST;
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
        mutate();
        this.position = daddy.getPosition();
        this.direction = random.nextBoolean() ? daddy.getDirection() : mommy.getDirection();
    }

    public Vector2D getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    public void move() {
        Direction nextMove = genome.get(age % genome.size());
        // jak to zakodowac????
        age += 1;
    }

    public void mutate() {
        int mutateAmount = random.nextInt(genome.size()) + 1;
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < genome.size(); i++) indexes.add(i);
        Direction[] directions = Direction.values();
        for (int i = 0; i < mutateAmount; i++) {
            genome.set(indexes.get(i), directions[random.nextInt(directions.length)]);
        }
    }

    public void eat() {
        energy += EATING_ENERGY_RECEIVED;
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
