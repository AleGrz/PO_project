package agh.darwinworld.model;

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
        this.currentGene = random.nextInt(0, genomeLength);
    }

    public Animal(Animal mommy, Animal daddy, int breedingEnergyCost, int minimalBreedingEnergy,
                  int minMutations, int maxMutations) {
        if (daddy.isDead() || mommy.isDead())
            throw new IllegalArgumentException("Provided animal cannot be dead!");
        if (daddy.energy <= minimalBreedingEnergy || mommy.energy < minimalBreedingEnergy)
            throw new IllegalArgumentException("Provided animal has not enough energy!");
        daddy.energy -= breedingEnergyCost;
        mommy.energy -= breedingEnergyCost;
        this.random = daddy.random;
        int total = mommy.energy + daddy.energy;
        int mommyGenomeAmount = Math.round(mommy.genome.length * ((float) mommy.energy / total));
        int daddyGenomeAmount = Math.round(daddy.genome.length * ((float) daddy.energy / total));
        boolean mommyLeft = random.nextBoolean();
        MoveDirection[] firstGenomes = mommy.extractGenomes(mommyLeft, mommyGenomeAmount);
        MoveDirection[] secondGenomes = daddy.extractGenomes(!mommyLeft, daddyGenomeAmount);
        if (mommyLeft) {
            this.genome = Stream.concat(Arrays.stream(firstGenomes), Arrays.stream(secondGenomes))
                    .toArray(MoveDirection[]::new);
        } else {
            this.genome = Stream.concat(Arrays.stream(secondGenomes), Arrays.stream(firstGenomes))
                    .toArray(MoveDirection[]::new);
        }
        mutate(minMutations, maxMutations);
        MapDirection[] directions = MapDirection.values();
        this.energy = breedingEnergyCost * 2;
        this.direction = directions[random.nextInt(0, directions.length)];
        this.currentGene = random.nextInt(0, mommyGenomeAmount + daddyGenomeAmount);
        mommy.childrenAmount += 1;
        daddy.childrenAmount += 1;
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

    public boolean isDead() {
        return energy <= 0;
    }

    public Vector2D move(Vector2D position, int mapWidth, int mapHeight) {
        if (this.energy <= 0)
            throw new IllegalStateException("Cannot move animal that is dead!");
        this.direction = this.direction.rotate(this.genome[this.currentGene]);
        this.currentGene = (this.currentGene + 1) % this.genome.length;
        this.energy -= 1;
        this.age += 1;
        Vector2D newPos = position.add(this.direction.getValue()).clamp(mapWidth, null);
        if (newPos.y() < 0 || newPos.x() >= mapHeight){
            this.direction = this.direction.rotate(MoveDirection.BACKWARD);
            return position;
        }
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
        Collections.shuffle(indexes);
        MoveDirection[] directions = MoveDirection.values();
        for (int i = 0; i < mutateAmount; i++) {
            genome[indexes.get(i)] = directions[random.nextInt(directions.length)];
        }
    }

    public void eat(int energy) {
        if (isDead())
            throw new IllegalArgumentException("Animal cannot eat when is dead!");
        if (energy < 0)
            throw new IllegalArgumentException("Energy added must be greater than or equal to 0!");
        this.energy += energy;
    }

    private MoveDirection[] extractGenomes(boolean leftPart, int amount) {
        if (amount == 0) return new MoveDirection[0];
        if (amount < 0) throw new IllegalArgumentException("Amount must be greater than or equal to 0!");
        int leftIdx = leftPart ? 0 : genome.length / 2;
        int rightIdx = leftPart ? genome.length / 2 : genome.length;
        MoveDirection[] newList = Arrays.copyOfRange(genome, leftIdx, rightIdx);
        Collections.shuffle(Arrays.asList(newList));
        return Arrays.copyOfRange(newList, 0, amount);
    }
}
