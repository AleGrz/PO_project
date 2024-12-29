package agh.darwinworld.model;

import java.util.*;

public abstract class AbstractMap {
    private final int width;
    private final int height;
    private final HashMap<Vector2D, ArrayList<Animal>> animals = new HashMap<>();
    private final HashSet<Vector2D> plants = new HashSet<>();
    public AbstractMap(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getMaxAnimalAmount() {
        return animals.values().stream().map(ArrayList::size).max(Integer::compareTo).orElse(0);
    }

    public List<Animal> getAnimalsOnPosition(Vector2D position) {
        return animals.containsKey(position) ? animals.get(position) : new ArrayList<>();
    }

    public List<Animal> getAllAnimals(Vector2D position) {
        return animals.containsKey(position) ? animals.get(position) : new ArrayList<>();
    }

    public boolean isPlantOnPosition(Vector2D position) {
        return plants.contains(position);
    }

    public int animalCount() {
        return animals.values().stream().mapToInt(List::size).sum();
    }

    public int plantCount() {
        return plants.size();
    }

    public int emptyFieldCount() {
        HashSet<Vector2D> allFields = new HashSet<>(plants);
        allFields.addAll(animals.keySet());
        return width * height - allFields.size();
    }

    public String popularGenotype() {
        HashMap<String, Integer> genotypeCount = new HashMap<>();
        for (List<Animal> animalList : animals.values()) {
            for (Animal animal : animalList) {
                String genotype = Arrays.stream(animal.getGenome()).map(Enum::ordinal).map(String::valueOf).reduce("", String::concat);
                genotypeCount.put(genotype, genotypeCount.getOrDefault(genotype, 0) + 1);
            }
        }
        return genotypeCount.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey)
                .orElse("Missing");
    }

    public void placeAnimal(Vector2D position, Animal animal) {

    }

    public void placePlant(Vector2D position) {
        plants.add(position);
    }

    public void moveAnimal(Animal animal) {
    }

    public void breedAnimals(Animal mother, Animal father) {

    }

    public void moveAnimal(Animal animal) {

    }
}
