package agh.darwinworld.models.animals;

import agh.darwinworld.models.MapDirection;
import agh.darwinworld.models.MoveDirection;
import agh.darwinworld.models.Vector2D;
import agh.darwinworld.models.listeners.AnimalListener;
import agh.darwinworld.models.listeners.MovementHandler;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.util.*;
import java.util.stream.Stream;

// TODO: Animal can die on move -> his energy is < 0, and then eat and be still alive. It's needed to be fixed

/**
 * Represents an animal in the Darwin World simulation.
 */
public class Animal implements AnimalListener {
    protected final Random random;
    protected final UUID uuid = UUID.randomUUID();
    protected final ArrayList<AnimalListener> listeners = new ArrayList<>();
    protected final ArrayList<Animal> subscribedTo = new ArrayList<>();
    protected final HashSet<Animal> descendants = new HashSet<>();

    protected final MoveDirection[] genome;
    protected MapDirection direction;
    protected int energy;
    protected int currentGeneIndex;
    protected int age = 0;
    protected int childrenAmount = 0;
    protected int totalEatenPlants = 0;
    protected Integer diedAt = null;

    /**
     * @return the current direction of the animal.
     */
    public MapDirection getDirection() {
        return this.direction;
    }

    /**
     * @return the genome of the animal.
     */
    public MoveDirection[] getGenome() {
        return this.genome;
    }

    /**
     * @return the current gene the animal will use for the next move.
     */
    public MoveDirection getCurrentGene() {
        return this.genome[this.currentGeneIndex];
    }

    /**
     * @return the age of the animal represented in simulation steps.
     */
    public int getAge() {
        return this.age;
    }

    /**
     * @return the current energy level of the animal. If animal
     * is dead, returns -1;
     */
    public int getEnergy() {
        return this.energy;
    }

    /**
     * @return the number of children this animal has produced.
     */
    public int getChildrenAmount() {
        return this.childrenAmount;
    }

    /**
     * @return all descendants of this animal.
     */
    public int getDescendantsAmount() {
        return descendants.size();
    }

    /**
     * @return true if animal is dead -
     * its energy is less than 0.
     */
    public boolean isDead() {
        return this.energy < 0;
    }

    /**
     * @return time when animal died represented in simulation steps.
     * If animal is still alive, returns -1.
     */
    public int diedAt() {
        return this.diedAt;
    }

    /**
     * @return total amount of plants that this animal has eaten.
     */
    public int getTotalEatenPlants() {
        return this.totalEatenPlants;
    }

    /**
     * @return animal's unique identifier.
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Updates animal's age by increasing by 1 and notifies listeners about the change.
     */
    protected void increaseAge() {
        this.age++;
        PropertyChangeEvent event = new PropertyChangeEvent(this, "age", this.age - 1, this.age);
        listeners.forEach(listener -> listener.propertyChange(event));
    }

    /**
     * Updates animal's energy and notifies listeners about the change.
     */
    protected void updateEnergy(int newEnergy, int step) {
        PropertyChangeEvent event = new PropertyChangeEvent(this, "energy", this.energy, newEnergy);
        this.energy = newEnergy;
        listeners.forEach(listener -> listener.propertyChange(event));
        if (energy < 0) {
            PropertyChangeEvent diedEvent = new PropertyChangeEvent(this, "diedAt", this.diedAt, step);
            this.diedAt = step;
            listeners.forEach(listener -> listener.propertyChange(diedEvent));
            if (listeners.isEmpty()) {
                for (Animal child : subscribedTo) {
                    child.removeListener(this);
                }
                subscribedTo.clear();
            }
        }
    }

    /**
     * Updates animal's current genome index and notifies listeners about the change.
     */
    protected void selectNextGene() {
        MoveDirection oldGene = getCurrentGene();
        this.currentGeneIndex = (this.currentGeneIndex + 1) % this.genome.length;
        PropertyChangeEvent event = new PropertyChangeEvent(this, "currentGene", oldGene, getCurrentGene());
        listeners.forEach(listener -> listener.propertyChange(event));
    }

    /**
     * Updates animal's children amount and notifies listeners about the change.
     */
    protected void increaseChildrenAmount(Animal child) {
        this.childrenAmount++;
        PropertyChangeEvent event = new PropertyChangeEvent(this, "childrenAmount", this.childrenAmount - 1, this.childrenAmount);
        listeners.forEach(listener -> listener.propertyChange(event));
        increaseDescendantsAmount(child);
        child.addListener(this);
        subscribedTo.add(child);
    }

    /**
     * Updates animal's descendants amount and notifies listeners about the change.
     */
    protected void increaseDescendantsAmount(Animal descendant) {
        if (descendants.contains(descendant)) return;
        descendants.add(descendant);
        PropertyChangeEvent event = new PropertyChangeEvent(this, "descendantsAmount", getDescendantsAmount() - 1, getDescendantsAmount());
        listeners.forEach(listener -> listener.propertyChange(event));
        PropertyChangeEvent descendantEvent = new PropertyChangeEvent(this, "descendantAdded", null, descendant);
        listeners.forEach(listener -> listener.propertyChange(descendantEvent));
    }

    /**
     * Updates animal's current direction and notifies listeners about the change.
     */
    protected void updateDirection(MapDirection direction) {
        PropertyChangeEvent event = new PropertyChangeEvent(this, "direction", this.direction, direction);
        this.direction = direction;
        listeners.forEach(listener -> listener.propertyChange(event));
    }

    /**
     * Constructs an animal with the specified genome length and initial energy.
     * @param random the random number generator.
     * @param genomeLength the length of the genome.
     * @param energy the initial energy of the animal/
     * @throws IllegalArgumentException if genomeLength < 0 or energy < 0.
     */
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
        this.currentGeneIndex = random.nextInt(genomeLength);
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
    public Animal(Animal mommy, Animal daddy, int breedingEnergyCost, int minimalBreedingEnergy,
                  int minMutations, int maxMutations, int step) {
        if (mommy == null || daddy == null)
            throw new IllegalArgumentException("Parents cannot be null!");
        if (mommy.isDead() || daddy.isDead())
            throw new IllegalArgumentException("Provided animal cannot be dead!");
        if (mommy.energy < minimalBreedingEnergy || daddy.energy < minimalBreedingEnergy)
            throw new IllegalArgumentException("Provided animal does not have enough energy!");

        mommy.updateEnergy(mommy.getEnergy() - breedingEnergyCost, step);
        daddy.updateEnergy(daddy.getEnergy() - breedingEnergyCost, step);

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
        this.currentGeneIndex = random.nextInt(this.genome.length);

        mommy.increaseChildrenAmount(this);
        daddy.increaseChildrenAmount(this);
    }

    /**
     * Adds a listener to monitor events related to the animal.
     * @param listener the {@link AnimalListener} instance to be added.
     */
    public void addListener(AnimalListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Removes a listener from monitoring events related to the animal.
     * @param listener the {@link AnimalListener} instance to be removed.
     */
    public void removeListener(AnimalListener listener) {
        this.listeners.remove(listener);
        if (listeners.isEmpty()) {
            for (Animal child : subscribedTo) {
                child.removeListener(this);
            }
        }
    }

    /**
     * Moves the animal to a new position based on its current genome and movement logic.
     * @param handler the {@code MovementHandler} responsible for determining the new position and direction.
     * @param position the current position of the animal.
     * @param step the current simulation step at which the movement occurs.
     * @return the new position of the animal after the move.
     * @throws IllegalStateException if the animal is dead or its genome is empty.
     */
    public Vector2D move(MovementHandler handler, Vector2D position, int step) {
        if (isDead())
            throw new IllegalStateException("Cannot move animal that is dead!");
        if (this.genome.length == 0)
            throw new IllegalStateException("Cannot move animal with empty genome!");

        MapDirection direction = this.direction.rotate(this.genome[this.currentGeneIndex]);
        selectNextGene();
        updateEnergy(getEnergy() - 1, step);
        increaseAge();
        Pair<Vector2D, MapDirection> movePair = handler.move(position, direction);
        Vector2D newPos = movePair.getKey();
        updateDirection(movePair.getValue());
        listeners.forEach(listener -> listener.move(position, newPos));
        return newPos;
    }


    /**
     * Increases the animal's energy by the specified amount and updates the count of eaten plants.
     * @param energy the amount of energy to add. Must be greater than or equal to 0.
     * @param step the current simulation step at which this action occurs.
     * @throws IllegalArgumentException if {@code energy} is less than 0.
     */
    public void eat(int energy, int step) {
        if (isDead()) return;
        if (energy < 0)
            throw new IllegalArgumentException("Energy added must be greater than or equal to 0!");
        this.totalEatenPlants++;
        PropertyChangeEvent event = new PropertyChangeEvent(this, "totalEatenPlants", this.totalEatenPlants - 1, this.totalEatenPlants);
        listeners.forEach(listener -> listener.propertyChange(event));
        updateEnergy(getEnergy() + energy, step);
    }

    /**
     * Forces the animal to be marked as dead by reducing its energy below zero.
     * @param step the current simulation step at which this action occurs.
     */
    public void forceKill(int step) {
        updateEnergy(-1, step);
    }

    /**
     * Mutates the genome of the animal by modifying a random number of genes within a specified range.
     * Each mutation replaces a gene with a randomly selected {@code MoveDirection}.
     * @param min the minimum number (inclusive) of genes to mutate. Must be less than {@code max}.
     * @param max the maximum number (exclusive) of genes to mutate. Must be greater than {@code min}.
     * @throws IllegalArgumentException if the animal is dead, or if {@code min} is greater than or equal to {@code max}.
     */
    private void mutate(int min, int max) {
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
    }

    /**
     * Extracts a specified number of genes from the genome, either from the left or right side.
     * @param leftPart a boolean indicating whether to extract from the left part of the genome.
     *                 If {@code true}, genes are extracted from the beginning of the genome.
     *                 If {@code false}, genes are extracted from the end of the genome.
     * @param amount the number of genes to extract. Must be greater than or equal to 0.
     * @return an array of {@code MoveDirection} containing the extracted genes.
     * @throws IllegalArgumentException if {@code amount} is less than 0.
     */
    private MoveDirection[] extractGenomes(boolean leftPart, int amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Amount must be greater than or equal to 0!");

        int start = leftPart ? 0 : genome.length - amount;
        int end = leftPart ? amount : genome.length;

        return Arrays.copyOfRange(genome, start, end);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Animal animal)) return false;
        return uuid.equals(animal.uuid);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!evt.getPropertyName().equals("descendantAdded")) return;
        if (!(evt.getNewValue() instanceof Animal descendant))
            throw new IllegalArgumentException("New value must be an animal!");
        increaseDescendantsAmount(descendant);
    }
}
