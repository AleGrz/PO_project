package agh.darwinworld;

import agh.darwinworld.model.Animal;
import agh.darwinworld.model.Vector2D;
import java.util.*;

public class Simulation implements Runnable {
    private final Random random;
    private final int width;
    private final int height;
    private final int startingPlantAmount;
    private final int plantGrowingAmount;
    private final int plantEnergyAmount;
    private final int startingEnergyAmount;
    private final int startingAnimalAmount;
    private final int minimumBreedingEnergy;
    private final int breedingEnergyCost;
    private final int minimumMutationAmount;
    private final int maximumMutationAmount;
    private final int animalGenomeLength;
    private final int fireFrequency;
    private final int fireLength;
    private final int seed;

    private HashMap<Vector2D, ArrayList<Animal>> animals = new HashMap<>();
    private final HashSet<Vector2D> plants = new HashSet<>();
    private HashMap<Vector2D, Integer> fire = new HashMap<Vector2D, Integer>();
    private boolean isRunning = false;

    public Simulation(int width, int height, int startingPlantAmount,
                      int plantGrowingAmount, int plantEnergyAmount,
                      int startingAnimalAmount, int startingEnergyAmount,
                      int minimumBreedingEnergy, int breedingEnergyCost,
                      int minimumMutationAmount, int maximumMutationAmount,
                      int animalGenomeLength, int fireFrequency, int fireLength, int seed) {
        if (width <= 0 || height <= 0)
            throw new IllegalArgumentException("Map size must be greater than 0!");
        if (startingPlantAmount < 0)
            throw new IllegalArgumentException("Plant amount must be greater than or equal to 0!");
        if (plantGrowingAmount < 0)
            throw new IllegalArgumentException("Amount of growing plants per turn must be greater than or equal to 0!");
        if (plantEnergyAmount < 0)
            throw new IllegalArgumentException("Amount of energy given to animal, when plant has been eaten must be greater than or equal to 0!");
        if (startingAnimalAmount < 0)
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
        this.startingPlantAmount = startingPlantAmount;
        this.plantGrowingAmount = plantGrowingAmount;
        this.plantEnergyAmount = plantEnergyAmount;
        this.startingEnergyAmount = startingEnergyAmount;
        this.startingAnimalAmount = startingAnimalAmount;
        this.minimumBreedingEnergy = minimumBreedingEnergy;
        this.breedingEnergyCost = breedingEnergyCost;
        this.minimumMutationAmount = minimumMutationAmount;
        this.maximumMutationAmount = maximumMutationAmount;
        this.animalGenomeLength = animalGenomeLength;
        this.fireFrequency = fireFrequency;
        this.fireLength = fireLength;
        this.seed = seed;
        this.random = new Random(seed);
        for (int i = 0; i < startingAnimalAmount; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Vector2D v = new Vector2D(x, y);
            Animal animal = new Animal(random, animalGenomeLength, startingEnergyAmount);
            animals.computeIfAbsent(v, k -> new ArrayList<>());
            animals.get(v).add(animal);
        }
        growPlants(true);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getStartingPlantAmount() {
        return this.startingPlantAmount;
    }

    public int getPlantGrowingAmount() {
        return this.plantGrowingAmount;
    }

    public int getPlantEnergyAmount() {
        return this.plantEnergyAmount;
    }

    public int getStartingEnergyAmount() {
        return this.startingEnergyAmount;
    }

    public int getStartingAnimalAmount() {
        return this.startingAnimalAmount;
    }

    public int getMinimumBreedingEnergy() {
        return this.minimumBreedingEnergy;
    }

    public int getBreedingEnergyCost() {
        return this.breedingEnergyCost;
    }

    public int getMinimumMutationAmount() {
        return this.minimumMutationAmount;
    }

    public int getMaximumMutationAmount() {
        return this.maximumMutationAmount;
    }

    public int getAnimalGenomeLength() {
        return this.animalGenomeLength;
    }

    public int getFireFrequency() {
        return this.fireFrequency;
    }

    public int getFireLength() {
        return this.fireLength;
    }

    public int getSeed() {
        return this.seed;
    }

    @Override
    public void run() {
        int step = 1;
        while(!animals.isEmpty()) {
            removeDeadAnimals();
            moveAnimals();
            feedAnimals();
            breedAnimals();
            growPlants(false);
            propagateFire();
            if (step % fireFrequency == 0) {
                Vector2D randomPos = plants.toArray(new Vector2D[0])[random.nextInt(0,plants.size())];
                fire.put(randomPos, fireLength);
            }
            step++;
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
                if (!fire.containsKey(animal)) {
                    updatedAnimals
                            .computeIfAbsent(newPosition, k -> new ArrayList<>())
                            .add(animal);
                }
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


    private void growPlants(boolean init) {
        int equatorHeight = Math.round(height*0.2f);
        int barHeight = (int)Math.round((height - equatorHeight)/2f);
        List<Vector2D> plantCandidates = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < barHeight; j++) {
                if (!plants.contains(new Vector2D(i, j))) {
                    plantCandidates.add(new Vector2D(i, j));
                }
            }
            for (int j = barHeight+equatorHeight; j < height; j++) {
                if (!plants.contains(new Vector2D(i, j))) {
                    plantCandidates.add(new Vector2D(i, j));
                }
            }
        }
        plants.addAll(selectRandom(plantCandidates, Math.round((init ? startingPlantAmount : plantGrowingAmount) * 0.2f)));
        plantCandidates = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = barHeight; j < barHeight+equatorHeight; j++) {
                if (!plants.contains(new Vector2D(i, j))) {
                    plantCandidates.add(new Vector2D(i, j));
                }
            }
        }
        plants.addAll(selectRandom(plantCandidates, Math.round((init ? startingPlantAmount : plantGrowingAmount) * 0.8f)));

    }

    public void propagateFire() {
        HashMap<Vector2D, Integer> newFire = new HashMap<>();
        for (Vector2D firePos: fire.keySet()) {
            Vector2D[] directions = new Vector2D[]{
                    firePos.add(new Vector2D(0,1)),
                    firePos.add(new Vector2D(1,0)),
                    firePos.add(new Vector2D(0,-1)),
                    firePos.add(new Vector2D(-1,0)),
                    };
            for (Vector2D direction: directions) {
                if (!fire.containsKey(direction)) {
                    newFire.put(direction, fireLength);
                }
            }
            if (fire.get(firePos) <= 0) {
                fire.remove(firePos);
            }
            else {
                fire.put(firePos, fire.get(firePos)-1);
            }
        }
        fire.putAll(newFire);
    }

    private List<Vector2D> selectRandom(List<Vector2D> positions, int amount) {
        Collections.shuffle(positions);
        return positions.subList(0, amount);
    }

    public List<Animal> getAnimalsOnPosition(Vector2D position) {
        return animals.containsKey(position) ? animals.get(position) : new ArrayList<>();
    }

    public boolean isPlantOnPosition(Vector2D position) {
        return plants.contains(position);
    }
}
