package agh.darwinworld.models;

import agh.darwinworld.controls.IntField;
import agh.darwinworld.models.exceptions.UserFriendlyException;
import agh.darwinworld.models.maps.AbstractMap;

import java.util.Random;

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
        Random random,
        AbstractMap map
) {
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
            Random random,
            AbstractMap map
    ) throws UserFriendlyException {
        return new SimulationParameters(
                getValidatedIntField(width, "Width", 1, 100),
                getValidatedIntField(height, "Height", 1, 100),
                getValidatedIntField(startingPlantAmount, "Amount of plants spawning at the start", 0, null),
                getValidatedIntField(plantGrowingAmount, "Amount of plants growing each day", 0, null),
                getValidatedIntField(plantEnergyAmount, "Plant energy profit", 0, null),
                getValidatedIntField(startingAnimalAmount, "Amount of animal spawning at the start", 1, null),
                getValidatedIntField(startingEnergyAmount, "Animal start energy", 1, null),
                getValidatedIntField(minimumBreedingEnergy, "Minimum breeding energy", 1, null),
                getValidatedIntField(breedingEnergyCost, "Breeding energy cost", 1, null),
                getValidatedIntField(minimumMutationAmount, "Minimum mutation amount", 0, null),
                getValidatedIntField(maximumMutationAmount, "Maximum mutation amount", 1, null),
                getValidatedIntField(animalGenomeLength, "Animal genome length", 1, null),
                getValidatedIntField(fireInterval, "Fire interval", 1, null),
                getValidatedIntField(fireLength, "Fire length", 1, null),
                getValidatedIntField(refreshTime, "Refresh time", 10, null),
                random,
                map
        );
    }

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
            throw new UserFriendlyException("Value outside of boundaries!", parameter + " must be less than or equal to " + max + ".");
        return value;
    }
}
