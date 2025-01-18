package agh.darwinworld.models;

import agh.darwinworld.controls.IntField;
import agh.darwinworld.models.animals.AnimalType;
import agh.darwinworld.models.exceptions.UserFriendlyException;
import agh.darwinworld.models.maps.MapType;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import org.json.JSONObject;

/**
 * A record that encapsulates the parameters for running a simulation.
 */
public record SimulationParameters(
        int width,
        int height,
        int startingPlantAmount,
        int plantGrowingAmount,
        int plantEnergyAmount,
        int startingAnimalAmount,
        int startingEnergyAmount,
        int minimumBreedingEnergy,
        int breedingEnergyCost,
        int minimumMutationAmount,
        int maximumMutationAmount,
        int animalGenomeLength,
        int fireInterval,
        int fireLength,
        int refreshTime,
        int seed,
        MapType mapType,
        AnimalType animalType
) {
    /**
     * Creates an instance of {@code SimulationParameters}.
     *
     * @param width                 the width of the map.
     * @param height                the height of the map.
     * @param startingPlantAmount   the initial number of plants on the map.
     * @param plantGrowingAmount    the number of plants that grow each step.
     * @param plantEnergyAmount     the energy gained by consuming a plant.
     * @param startingAnimalAmount  the initial number of animals on the map.
     * @param startingEnergyAmount  the starting energy of animals.
     * @param minimumBreedingEnergy the minimum energy required for breeding.
     * @param breedingEnergyCost    the energy cost for breeding.
     * @param minimumMutationAmount the minimum number of mutations an animal can have.
     * @param maximumMutationAmount the maximum number of mutations an animal can have.
     * @param animalGenomeLength    the length of the animal genome.
     * @param fireInterval          the interval at which fires occur.
     * @param fireLength            the duration of a fire.
     * @param refreshTime           the refresh time for simulation updates.
     * @param seed                  the seed for randomization.
     * @param mapType               the type of the map.
     * @param animalType            the type of animals in the simulation.
     * @throws IllegalArgumentException if parameters are invalid.
     */
    public SimulationParameters {
        validateInt(width, "Width", 1, 100);
        validateInt(height, "Height", 1, 100);
        validateInt(startingPlantAmount, "Amount of plants spawning at the start", 0, null);
        validateInt(plantGrowingAmount, "Amount of plants growing each step", 0, null);
        validateInt(plantEnergyAmount, "Plant energy profit", 0, null);
        validateInt(startingAnimalAmount, "Amount of animal spawning at the start", 1, null);
        validateInt(startingEnergyAmount, "Animal start energy", 1, null);
        validateInt(minimumBreedingEnergy, "Minimum breeding energy", 1, null);
        validateInt(breedingEnergyCost, "Breeding energy cost", 1, null);
        validateInt(minimumMutationAmount, "Minimum mutation amount", 0, null);
        validateInt(maximumMutationAmount, "Maximum mutation amount", 1, null);
        validateInt(animalGenomeLength, "Animal genome length", 1, null);
        validateInt(fireInterval, "Fire interval", 1, null);
        validateInt(fireLength, "Fire length", 1, null);
        validateInt(refreshTime, "Refresh time", 10, null);
        if (minimumMutationAmount > maximumMutationAmount) {
            throw new IllegalArgumentException("Minimum mutation amount must be less than or equal to maximum mutation amount.");
        }
        if (minimumMutationAmount > animalGenomeLength) {
            throw new IllegalArgumentException("Minimum mutation amount must be less than or equal to animal genome length.");
        }
        if (maximumMutationAmount > animalGenomeLength) {
            throw new IllegalArgumentException("Maximum mutation amount must be less than or equal to animal genome length.");
        }
    }

    /**
     * Creates an instance of {@code SimulationParameters}.
     *
     * @param width                 the width of the map.
     * @param height                the height of the map.
     * @param startingPlantAmount   the initial number of plants on the map.
     * @param plantGrowingAmount    the number of plants that grow each step.
     * @param plantEnergyAmount     the energy gained by consuming a plant.
     * @param startingAnimalAmount  the initial number of animals on the map.
     * @param startingEnergyAmount  the starting energy of animals.
     * @param minimumBreedingEnergy the minimum energy required for breeding.
     * @param breedingEnergyCost    the energy cost for breeding.
     * @param minimumMutationAmount the minimum number of mutations an animal can have.
     * @param maximumMutationAmount the maximum number of mutations an animal can have.
     * @param animalGenomeLength    the length of the animal genome.
     * @param fireInterval          the interval at which fires occur.
     * @param fireLength            the duration of a fire.
     * @param refreshTime           the refresh time for simulation updates.
     * @param seed                  the seed for randomization.
     * @param mapType               the type of the map.
     * @param animalType            the type of animals in the simulation.
     * @return a {@code SimulationParameters} instance initialized with the provided values.
     * @throws UserFriendlyException if the input values are invalid.
     */
    public static SimulationParameters createFromIntField(
            IntField width,
            IntField height,
            IntField startingPlantAmount,
            IntField plantGrowingAmount,
            IntField plantEnergyAmount,
            IntField startingAnimalAmount,
            IntField startingEnergyAmount,
            IntField minimumBreedingEnergy,
            IntField breedingEnergyCost,
            IntField minimumMutationAmount,
            IntField maximumMutationAmount,
            IntField animalGenomeLength,
            IntField fireInterval,
            IntField fireLength,
            IntField refreshTime,
            IntField seed,
            MapType mapType,
            AnimalType animalType
    ) throws UserFriendlyException {
        int minMutation = getValidatedIntField(minimumMutationAmount, "Minimum mutation amount", 0, null);
        int maxMutation = getValidatedIntField(maximumMutationAmount, "Maximum mutation amount", 1, null);
        int genomeLength = getValidatedIntField(animalGenomeLength, "Animal genome length", 1, null);
        if (minMutation > maxMutation) {
            throw new UserFriendlyException("Incorrect values!", "Minimum mutation amount must be less than or equal to maximum mutation amount.");
        }
        if (minMutation > genomeLength) {
            throw new UserFriendlyException("Incorrect values!", "Minimum mutation amount must be less than or equal to animal genome length.");
        }
        if (maxMutation > genomeLength) {
            throw new UserFriendlyException("Incorrect values!", "Maximum mutation amount must be less than or equal to animal genome length.");
        }
        return new SimulationParameters(
                getValidatedIntField(width, "Width", 1, 100),
                getValidatedIntField(height, "Height", 1, 100),
                getValidatedIntField(startingPlantAmount, "Amount of plants spawning at the start", 0, null),
                getValidatedIntField(plantGrowingAmount, "Amount of plants growing each step", 0, null),
                getValidatedIntField(plantEnergyAmount, "Plant energy profit", 0, null),
                getValidatedIntField(startingAnimalAmount, "Amount of animal spawning at the start", 1, null),
                getValidatedIntField(startingEnergyAmount, "Animal start energy", 1, null),
                getValidatedIntField(minimumBreedingEnergy, "Minimum breeding energy", 1, null),
                getValidatedIntField(breedingEnergyCost, "Breeding energy cost", 1, null),
                minMutation,
                maxMutation,
                genomeLength,
                getValidatedIntField(fireInterval, "Fire interval", 1, null),
                getValidatedIntField(fireLength, "Fire length", 1, null),
                getValidatedIntField(refreshTime, "Refresh time", 10, null),
                getValidatedIntField(seed, "Seed", null, null),
                mapType,
                animalType
        );
    }

    public static SimulationParameters createFromJson(File file) {
        try (Scanner scanner = new Scanner(file)) {
            scanner.useDelimiter("\\Z");
            JSONObject json = new JSONObject(scanner.next());
            return new SimulationParameters(
                    json.getInt("width"),
                    json.getInt("height"),
                    json.getInt("startingPlantAmount"),
                    json.getInt("plantGrowingAmount"),
                    json.getInt("plantEnergyAmount"),
                    json.getInt("startingAnimalAmount"),
                    json.getInt("startingEnergyAmount"),
                    json.getInt("minimumBreedingEnergy"),
                    json.getInt("breedingEnergyCost"),
                    json.getInt("minimumMutationAmount"),
                    json.getInt("maximumMutationAmount"),
                    json.getInt("animalGenomeLength"),
                    json.getInt("fireInterval"),
                    json.getInt("fireLength"),
                    json.getInt("refreshTime"),
                    json.getInt("seed"),
                    MapType.values()[json.getInt("mapType")],
                    AnimalType.values()[json.getInt("animalType")]
            );
        } catch (Exception e) {
            throw new UserFriendlyException("Failed to load simulation parameters from file: "+file, e.toString());
        }

    }

    public void saveToJson(String path) {
        JSONObject json = new JSONObject();
        json.put("width", width);
        json.put("height", height);
        json.put("startingPlantAmount", startingPlantAmount);
        json.put("plantGrowingAmount", plantGrowingAmount);
        json.put("plantEnergyAmount", plantEnergyAmount);
        json.put("startingAnimalAmount", startingAnimalAmount);
        json.put("startingEnergyAmount", startingEnergyAmount);
        json.put("minimumBreedingEnergy", minimumBreedingEnergy);
        json.put("breedingEnergyCost", breedingEnergyCost);
        json.put("minimumMutationAmount", minimumMutationAmount);
        json.put("maximumMutationAmount", maximumMutationAmount);
        json.put("animalGenomeLength", animalGenomeLength);
        json.put("fireInterval", fireInterval);
        json.put("fireLength", fireLength);
        json.put("refreshTime", refreshTime);
        json.put("seed", seed);
        json.put("mapType", mapType.ordinal());
        json.put("animalType", animalType.ordinal());
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(json.toString());
        } catch (Exception e) {
            throw new UserFriendlyException("Failed to save simulation parameters to file: "+path, e.toString());
        }
    }

    /**
     * Validates the value of an {@link IntField} parameter using {@link UserFriendlyException}.
     *
     * @param parameter the {@code IntField} to validate.
     * @param fieldDesc a description of the field for error messages.
     * @param min       the minimum allowable value, or {@code null} for no minimum.
     * @param max       the maximum allowable value, or {@code null} for no maximum.
     * @return the validated integer value.
     * @throws UserFriendlyException if the value is invalid or outside the specified bounds.
     */
    private static int getValidatedIntField(IntField parameter, String fieldDesc, Integer min, Integer max) throws UserFriendlyException {
        Integer value;
        try {
            value = parameter.getValue();
        } catch (NumberFormatException e) {
            int minBounds = min == null ? Integer.MIN_VALUE : min;
            int maxBounds = max == null ? Integer.MAX_VALUE : max;
            throw new UserFriendlyException("Value too large!", fieldDesc + " must be between " + minBounds + " and " + maxBounds + ".");
        }
        if (value == null)
            throw new UserFriendlyException("Missing value!", fieldDesc + " has to be provided.");
        if (min != null && value < min)
            throw new UserFriendlyException("Value outside of boundaries!", fieldDesc + " must be greater than or equal to " + min + ".");
        if (max != null && value > max)
            throw new UserFriendlyException("Value outside of boundaries!", fieldDesc + " must be less than or equal to " + max + ".");
        return value;
    }

    private static void validateInt(int parameter, String fieldDesc, Integer min, Integer max) {
        if (min != null && parameter < min)
            throw new IllegalArgumentException(fieldDesc + " must be greater than or equal to " + min + ".");
        if (max != null && parameter > max)
            throw new IllegalArgumentException(fieldDesc + " must be less than or equal to " + max + ".");
    }
}
