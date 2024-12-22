package agh.darwinworld;

import agh.darwinworld.model.Animal;
import agh.darwinworld.model.RandomPositionGenerator;
import agh.darwinworld.model.Vector2D;

import java.util.*;

public class Simulation implements Runnable {
    private Random random;
    private final int width;
    private final int height;
    private final int plantGrowingAmount;
    private final int plantEnergyAmount;
    private final int minimumBreedingEnergy;
    private final int breedingEnergyCost;
    private final int minimumMutationAmount;
    private final int maximumMutationAmount;
    private HashMap<Vector2D, ArrayList<Animal>> animals = new HashMap<>();
    private final HashSet<Vector2D> plants = new HashSet<>();
    private boolean isRunning = false;

    public Simulation(int width, int height, int startingPlantAmount,
                      int plantGrowingAmount, int plantEnergyAmount,
                      int animalAmount, int startingEnergyAmount,
                      int minimumBreedingEnergy, int breedingEnergyCost,
                      int minimumMutationAmount, int maximumMutationAmount,
                      int animalGenomeLength, int seed) {
        if (width <= 0 || height <= 0)
            throw new IllegalArgumentException("Map size must be greater than 0!");
        if (startingPlantAmount < 0)
            throw new IllegalArgumentException("Plant amount must be greater than or equal to 0!");
        if (plantGrowingAmount < 0)
            throw new IllegalArgumentException("Amount of growing plants per turn must be greater than or equal to 0!");
        if (plantEnergyAmount < 0)
            throw new IllegalArgumentException("Amount of energy given to animal, when plant has been eaten must be greater than or equal to 0!");
        if (animalAmount < 0)
            throw new IllegalArgumentException("Animal amount must be greater than or equal to 0!");
        if (startingEnergyAmount <= 0)
            throw new IllegalArgumentException("Amount of starting animal energy must be greater than 0!");
        if (minimumBreedingEnergy < 0)
            throw new IllegalArgumentException("Minimum breeding energy must be greater than or equal to 0!");
        if (breedingEnergyCost < 0)
            throw new IllegalArgumentException("Breeding energy cost must be greater than or equal to 0!");
        if (minimumMutationAmount < 0)
            throw new IllegalArgumentException("Minimum mutation amount must be greater than or equal to 0!");
        if (maximumMutationAmount < 0)
            throw new IllegalArgumentException("Maximum mutation amount must be greater than or equal to 0!");
        if (maximumMutationAmount < minimumMutationAmount)
            throw new IllegalArgumentException("Maximum mutation amount must be greater than or equal to minimum mutation amount!");
        if (animalGenomeLength <= 0)
            throw new IllegalArgumentException("Animal genome length must be greater than 0!");
        this.width = width;
        this.height = height;
        this.plantGrowingAmount = plantGrowingAmount;
        this.plantEnergyAmount = plantEnergyAmount;
        this.minimumBreedingEnergy = minimumBreedingEnergy;
        this.breedingEnergyCost = breedingEnergyCost;
        this.minimumMutationAmount = minimumMutationAmount;
        this.maximumMutationAmount = maximumMutationAmount;
        this.random = new Random(seed);
        for (int i = 0; i < animalAmount; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Vector2D v = new Vector2D(x, y);
            Animal animal = new Animal(random, animalGenomeLength, startingEnergyAmount);
            animals.computeIfAbsent(v, k -> new ArrayList<>());
            animals.get(v).add(animal);
        }

        // na razie testowo bez generowania przy równiku
        RandomPositionGenerator grassPosition = new RandomPositionGenerator(width, height, startingPlantAmount);
        while(grassPosition.hasNext()) plants.add(grassPosition.next());
    }

    @Override
    public void run() {
        while(!animals.isEmpty()) {
            removeDeadAnimals();
            moveAnimals();
            feedAnimals();
            breedAnimals();
            // brak randomowego mutowania
            // powinny niby rosnac
        }
    }

    private void removeDeadAnimals() {
        for (Vector2D position : animals.keySet()) {
            for (Animal animal : animals.get(position)) {
                if (animal.isDead()) animals.remove(position);
            }
            if (animals.get(position).isEmpty())
                animals.remove(position);
        }

    }

    private void moveAnimals() {
        HashMap<Vector2D, ArrayList<Animal>> updatedAnimals = new HashMap<>();
        for (Map.Entry<Vector2D, ArrayList<Animal>> entry : animals.entrySet()) {
            Vector2D position = entry.getKey();
            List<Animal> animalList = entry.getValue();

            if (animalList.isEmpty()) continue;

            for (Animal animal : animalList) {
                Vector2D newPosition = animal.move(position, width, height);
                updatedAnimals
                        .computeIfAbsent(newPosition, k -> new ArrayList<>())
                        .add(animal);
            }
        }
        animals = updatedAnimals;
    }

    private void feedAnimals() {
        for (Vector2D position : animals.keySet()) {
            Animal topAnimal = animals.get(position).stream()
                    .max(Comparator.comparingInt(Animal::getEnergy))
                    .orElseThrow(() -> new IllegalArgumentException("List must not be empty"));
            topAnimal.eat(plantEnergyAmount);
            plants.remove(position);
        }
    }

    private void breedAnimals() {
        for (Vector2D position : animals.keySet()) {
            List<Animal> topAnimals = animals.get(position).stream()
                    .sorted(Comparator
                            .comparingInt(Animal::getEnergy).reversed()
                            .thenComparingInt(Animal::getAge).reversed()
                            .thenComparingInt(Animal::getChildrenAmount).reversed()
                            .thenComparing(a -> random.nextInt()))
                    .limit(2)
                    .toList();

            if (topAnimals.size() == 2 && topAnimals.getLast().getEnergy() >= minimumBreedingEnergy) {
                Animal baby = new Animal(topAnimals.getFirst(), topAnimals.getLast(), breedingEnergyCost,
                        minimumBreedingEnergy, minimumMutationAmount, maximumMutationAmount);
                animals.get(position).add(baby);
            }
        }
    }
}
