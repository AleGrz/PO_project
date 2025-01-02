package agh.darwinworld.models.maps;

import agh.darwinworld.models.*;
import agh.darwinworld.models.animals.Animal;
import agh.darwinworld.models.listeners.MovementHandler;
import agh.darwinworld.models.listeners.SimulationStepListener;
import javafx.util.Pair;

import java.util.*;

public abstract class AbstractMap implements MovementHandler {
    protected int deadCount = 0;
    protected int totalLifetime = 0;
    protected HashMap<Vector2D, ArrayList<Animal>> animals = new HashMap<>();
    protected final HashSet<Vector2D> plants = new HashSet<>();
    protected SimulationParameters params;
    protected final List<SimulationStepListener> listeners = new ArrayList<>();
    protected Random random;

    public void setParameters(SimulationParameters params) {
        this.params = params;
        random = new Random(params.seed());
    }

    public int getMaxAnimalAmount() {
        return animals.values().stream().map(ArrayList::size).max(Integer::compareTo).orElse(0);
    }

    public List<Animal> getAnimalsOnPosition(Vector2D position) {
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

    public boolean isPreferredRow(int row) {
        int equatorHeight = Math.round(params.height() * 0.2f);
        int barHeight = Math.round((params.height() - equatorHeight) / 2f);
        return row >= barHeight && row < barHeight + equatorHeight;
    }

    public int emptyFieldCount() {
        HashSet<Vector2D> allFields = new HashSet<>(plants);
        allFields.addAll(animals.keySet());
        return params.width() * params.height() - allFields.size();
    }

    public Pair<MoveDirection[], Integer> popularGenome() {
        HashMap<List<MoveDirection>, Integer> genomeCount = new HashMap<>();
        for (List<Animal> animalList : animals.values()) {
            for (Animal animal : animalList) {
                MoveDirection[] genome = animal.getGenome();
                genomeCount.put(Arrays.asList(genome), genomeCount.getOrDefault(Arrays.asList(genome), 0) + 1);
            }
        }
        Map.Entry<List<MoveDirection>, Integer> maxEntry = genomeCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        int count = (maxEntry != null) ? maxEntry.getValue() : 0;
        MoveDirection[] genome = (maxEntry != null) ? maxEntry.getKey().toArray(new MoveDirection[0]) : null;
        return new Pair<>(genome, count);
    }

    public int averageLifetime() {
        if (deadCount == 0) return 0;
        return totalLifetime / deadCount;
    }

    public int averageDescendantsAmount() {
        if (animalCount() == 0) return 0;
        int childrenAmount = 0;
        for (List<Animal> animalList : animals.values()) {
            for (Animal animal : animalList) {
                childrenAmount += animal.getDescendantsAmount();
            }
        }
        return childrenAmount / animalCount();
    }


    public void populateAnimals(int amount) {
        for (int i = 0; i < amount; i++) {
            int x = this.random.nextInt(params.width());
            int y = this.random.nextInt(params.height());
            Vector2D v = new Vector2D(x, y);
            Animal animal = new Animal(this.random, params.animalGenomeLength(), params.startingEnergyAmount());
            animals.computeIfAbsent(v, k -> new ArrayList<>());
            animals.get(v).add(animal);
        }
    }

    protected void breedAnimals(int step) {
        for (Vector2D position : animals.keySet()) {
            List<Animal> topAnimals = animals.get(position).stream()
                    .sorted(Comparator
                            .comparingInt(Animal::getEnergy).reversed()
                            .thenComparingInt(Animal::getAge).reversed()
                            .thenComparingInt(Animal::getChildrenAmount).reversed()
                            .thenComparing(a -> random.nextInt()))
                    .limit(2)
                    .toList();

            if (topAnimals.size() == 2 && topAnimals.getLast().getEnergy() >= params.minimumBreedingEnergy()) {
                Animal baby = new Animal(topAnimals.getFirst(), topAnimals.getLast(), params.breedingEnergyCost(),
                        params.minimumBreedingEnergy(), params.minimumMutationAmount(), params.maximumMutationAmount(), step);
                animals.get(position).add(baby);
                final int max = getMaxAnimalAmount();
                listeners.forEach(listener -> listener.updateAnimal(position, animals.get(position).size(), max));
            }
        }
    }

    protected void feedAnimals(int step) {
        List<Vector2D> toRemove = new ArrayList<>();
        for (Vector2D position : plants) {
            if (!animals.containsKey(position)) continue;
            Animal topAnimal = animals.get(position).stream().max(Comparator.comparingInt(Animal::getEnergy)).orElseThrow();
            topAnimal.eat(params.plantEnergyAmount(), step);
            toRemove.add(position);
            listeners.forEach(listener -> listener.removePlant(position));
        }
        toRemove.forEach(plants::remove);
    }


    public void growPlants(int amount) {
        int equatorHeight = Math.round(params.height() * 0.2f);
        int barHeight = Math.round((params.height() - equatorHeight) / 2f);
        List<Vector2D> plantCandidates = new ArrayList<>();
        for (int i = 0; i < params.width(); i++) {
            for (int j = 0; j < barHeight; j++) {
                if (!plants.contains(new Vector2D(i, j))) {
                    plantCandidates.add(new Vector2D(i, j));
                }
            }
            for (int j = barHeight + equatorHeight; j < params.height(); j++) {
                if (!plants.contains(new Vector2D(i, j))) {
                    plantCandidates.add(new Vector2D(i, j));
                }
            }
        }
        plants.addAll(selectRandom(plantCandidates, Math.round(amount * 0.2f)));
        plantCandidates = new ArrayList<>();
        for (int i = 0; i < params.width(); i++) {
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

    protected void removeDeadAnimals() {
        Map<Vector2D, ArrayList<Animal>> animalsToRemove = new HashMap<>();
        List<Vector2D> emptyPositions = new ArrayList<>();

        for (Vector2D position : animals.keySet()) {
            ArrayList<Animal> toRemove = new ArrayList<>();
            for (Animal animal : animals.get(position)) {
                if (animal.isDead()) {
                    totalLifetime += animal.getAge();
                    deadCount++;
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
        final int max = getMaxAnimalAmount();
        for (Map.Entry<Vector2D, ArrayList<Animal>> entry : animalsToRemove.entrySet()) {
            animals.get(entry.getKey()).removeAll(entry.getValue());
            listeners.forEach(listener -> listener.updateAnimal(entry.getKey(), animals.get(entry.getKey()).size(), max));
        }
        for (Vector2D position : emptyPositions) {
            animals.remove(position);
        }
    }

    private void moveAnimals(int step) {
        HashMap<Vector2D, ArrayList<Animal>> updatedAnimals = new HashMap<>();
        for (Map.Entry<Vector2D, ArrayList<Animal>> entry : animals.entrySet()) {
            Vector2D position = entry.getKey();
            List<Animal> animalList = entry.getValue();

            if (animalList.isEmpty()) continue;
            for (Animal animal : animalList) {
                Vector2D newPosition = animal.move(this, position, step);
                updatedAnimals
                        .computeIfAbsent(newPosition, k -> new ArrayList<>())
                        .add(animal);
            }
        }
        Set<Vector2D> oldPositions = animals.keySet();
        oldPositions.removeAll(updatedAnimals.keySet());
        final int max = getMaxAnimalAmount();
        for (Vector2D position : oldPositions) {
            listeners.forEach(listener -> listener.updateAnimal(position, 0, max));
        }
        for (Vector2D position : updatedAnimals.keySet()) {
            listeners.forEach(listener -> listener.updateAnimal(position, updatedAnimals.get(position).size(), max));
        }
        animals = updatedAnimals;
    }

    public void step(int stepNumber) {
        removeDeadAnimals();
        moveAnimals(stepNumber);
        feedAnimals(stepNumber);
        breedAnimals(stepNumber);
        growPlants(this.params.plantGrowingAmount());
    }

    private List<Vector2D> selectRandom(List<Vector2D> positions, int amount) {
        Collections.shuffle(positions, this.random);
        return positions.subList(0, Math.min(amount, positions.size()));
    }

    protected void updateStatistics(int step) {
        listeners.forEach(l -> l.updateStatistics(
                step,
                animalCount(),
                plantCount(),
                emptyFieldCount(),
                popularGenome(),
                averageLifetime(),
                averageDescendantsAmount()
        ));
    }

    public void addStepListener(SimulationStepListener listener) {
        listeners.add(listener);
        listeners.forEach(l -> l.updateStatistics(
                0,
                animalCount(),
                plantCount(),
                emptyFieldCount(),
                popularGenome(),
                averageLifetime(),
                averageDescendantsAmount()
        ));
    }

    public void removeStepListener(SimulationStepListener listener) {
        listeners.remove(listener);
    }
}
