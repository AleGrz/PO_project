package agh.darwinworld;

import agh.darwinworld.model.Animal;
import agh.darwinworld.model.SimulationStepListener;
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
    private final int refreshTime;
    private final int seed;

    private HashMap<Vector2D, ArrayList<Animal>> animals = new HashMap<>();
    private final HashSet<Vector2D> plants = new HashSet<>();
    private final HashMap<Vector2D, Integer> fire = new HashMap<>();
    private boolean isRunning = false;
    private final List<SimulationStepListener> listeners = new ArrayList<>();

    public static void InputValidator(int width, int height, int startingPlantAmount,
                                      int plantGrowingAmount, int plantEnergyAmount,
                                      int startingAnimalAmount, int startingEnergyAmount,
                                      int minimumBreedingEnergy, int breedingEnergyCost,
                                      int minimumMutationAmount, int maximumMutationAmount,
                                      int animalGenomeLength, int fireFrequency, int fireLength,
                                      int refreshTime, int ignored) {
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
        if (fireFrequency < 0)
            throw new IllegalArgumentException("Fire frequency must be greater than or equal to 0!");
        if (fireLength < 1)
            throw new IllegalArgumentException("Fire length must be greater than or equal to 1!");
        if (refreshTime < 10)
            throw new IllegalArgumentException("Refresh time must be greater than or equal to 10!");
    }

    public Simulation(int width, int height, int startingPlantAmount,
                      int plantGrowingAmount, int plantEnergyAmount,
                      int startingAnimalAmount, int startingEnergyAmount,
                      int minimumBreedingEnergy, int breedingEnergyCost,
                      int minimumMutationAmount, int maximumMutationAmount,
                      int animalGenomeLength, int fireFrequency, int fireLength,
                      int refreshTime, int seed) {
        InputValidator(width, height, startingPlantAmount, plantGrowingAmount, plantEnergyAmount,
                startingAnimalAmount, startingEnergyAmount, minimumBreedingEnergy, breedingEnergyCost,
                minimumMutationAmount, maximumMutationAmount, animalGenomeLength, fireFrequency,
                fireLength, refreshTime, seed);
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
        this.refreshTime = refreshTime;
        this.seed = seed;
        this.random = new Random(seed);
        for (int i = 0; i < startingAnimalAmount; i++) {
            int x = this.random.nextInt(width);
            int y = this.random.nextInt(height);
            Vector2D v = new Vector2D(x, y);
            Animal animal = new Animal(this.random, animalGenomeLength, startingEnergyAmount);
            animals.computeIfAbsent(v, k -> new ArrayList<>());
            animals.get(v).add(animal);
        }
        growPlants(startingPlantAmount);
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

    public int getRefreshTime() {
        return this.refreshTime;
    }

    public int getSeed() {
        return this.seed;
    }

    private int step = 1;

    @Override
    public void run() {
        while (true) {
            if (isRunning) {
                removeDeadAnimals();
                moveAnimals();
                feedAnimals();
                breedAnimals();
                growPlants(plantGrowingAmount);
                propagateFire();
                if (step % fireFrequency == 0) {
                    Vector2D randomPos = plants.toArray(new Vector2D[0])[random.nextInt(plants.size())];
                    fire.put(randomPos, fireLength);
                }
                step++;
            }
            try {
                //noinspection BusyWait
                Thread.sleep(this.refreshTime);
            } catch (InterruptedException e) {
                System.out.println("Stopping simulation loop's sleep!");
                return;
            }
        }
    }

    private void removeDeadAnimals() {
        Map<Vector2D, ArrayList<Animal>> animalsToRemove = new HashMap<>();
        List<Vector2D> emptyPositions = new ArrayList<>();

        for (Vector2D position : animals.keySet()) {
            ArrayList<Animal> toRemove = new ArrayList<>();
            for (Animal animal : animals.get(position)) {
                if (animal.isDead()) {
                    toRemove.add(animal);
                }
            }

            if (!toRemove.isEmpty()) {
                animalsToRemove.put(position, toRemove);
            }

            if (animals.get(position).isEmpty()) {
                emptyPositions.add(position);
            }
        }
        for (Map.Entry<Vector2D, ArrayList<Animal>> entry : animalsToRemove.entrySet()) {
            animals.get(entry.getKey()).removeAll(entry.getValue());
            listeners.forEach(listener -> listener.updateAnimal(entry.getKey(), animals.get(entry.getKey()).size()));
        }
        for (Vector2D position : emptyPositions) {
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
        Set<Vector2D> oldPositions = animals.keySet();
        oldPositions.removeAll(updatedAnimals.keySet());
        for (Vector2D position : oldPositions) {
            listeners.forEach(listener -> listener.updateAnimal(position, 0));
        }
        for (Vector2D position : updatedAnimals.keySet()) {
            listeners.forEach(listener -> listener.updateAnimal(position, updatedAnimals.get(position).size()));
        }
        animals = updatedAnimals;
    }

    private void feedAnimals() {
        List<Vector2D> toRemove = new ArrayList<>();
        for (Vector2D position : plants) {
            if (!animals.containsKey(position)) continue;
            Animal topAnimal = animals.get(position).stream().max(Comparator.comparingInt(Animal::getEnergy)).orElseThrow();
            topAnimal.eat(plantEnergyAmount);
            toRemove.add(position);
            listeners.forEach(listener -> listener.removePlant(position));
        }
        toRemove.forEach(plants::remove);
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
                listeners.forEach(listener -> listener.updateAnimal(position, animals.get(position).size()));
            }
        }
    }


    private void growPlants(int amount) {
        int equatorHeight = Math.round(height * 0.2f);
        int barHeight = Math.round((height - equatorHeight) / 2f);
        List<Vector2D> plantCandidates = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < barHeight; j++) {
                if (!plants.contains(new Vector2D(i, j))) {
                    plantCandidates.add(new Vector2D(i, j));
                }
            }
            for (int j = barHeight + equatorHeight; j < height; j++) {
                if (!plants.contains(new Vector2D(i, j))) {
                    plantCandidates.add(new Vector2D(i, j));
                }
            }
        }
        plants.addAll(selectRandom(plantCandidates, Math.round(amount * 0.2f)));
        plantCandidates = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = barHeight; j < barHeight + equatorHeight; j++) {
                if (!plants.contains(new Vector2D(i, j))) {
                    plantCandidates.add(new Vector2D(i, j));
                }
            }
        }
        plants.addAll(selectRandom(plantCandidates, Math.round((amount) * 0.8f)));
        for (Vector2D position : plants) {
            listeners.forEach(listener -> listener.addPlant(position));
        }

    }

    public void propagateFire() {
        HashMap<Vector2D, Integer> newFire = new HashMap<>();
        Iterator<Map.Entry<Vector2D, Integer>> iterator = fire.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Vector2D, Integer> fireData = iterator.next();
            Vector2D position = fireData.getKey();
            Vector2D[] directions = new Vector2D[]{
                    position.add(new Vector2D(0, 1)),
                    position.add(new Vector2D(1, 0)),
                    position.add(new Vector2D(0, -1)),
                    position.add(new Vector2D(-1, 0)),
            };
            for (Vector2D direction : directions) {
                if (fire.containsKey(direction)) continue;
                if (plants.contains(position)) {
                    newFire.put(direction, fireLength);
                }
            }
            listeners.forEach(listener -> listener.addFire(position));
            listeners.forEach(listener -> listener.updateAnimal(position, 0));
            if (animals.containsKey(position)) {
                animals.get(position).removeAll(animals.get(position));
                animals.remove(position);
            }

            plants.remove(position);

            if (fireData.getValue() <= 0) {
                listeners.forEach(listener -> listener.removeFire(position));
                iterator.remove();
            } else {
                fireData.setValue(fireData.getValue() - 1);
            }
        }
        fire.putAll(newFire);
    }

    private List<Vector2D> selectRandom(List<Vector2D> positions, int amount) {
        Collections.shuffle(positions, this.random);
        return positions.subList(0, Math.min(amount, positions.size()));
    }

    public synchronized List<Animal> getAnimalsOnPosition(Vector2D position) {
        return animals.containsKey(position) ? animals.get(position) : new ArrayList<>();
    }

    public synchronized boolean isPlantOnPosition(Vector2D position) {
        return plants.contains(position);
    }

    public void addStepListener(SimulationStepListener listener) {
        listeners.add(listener);
    }

    public void removeStepListener(SimulationStepListener listener) {
        listeners.remove(listener);
    }

    public void start() {
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }
}