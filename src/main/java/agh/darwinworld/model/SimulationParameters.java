package agh.darwinworld.model;

import java.util.Random;

public record SimulationParameters(
        Integer width,
        Integer height,
        Integer startingPlantAmount,
        Integer plantGrowingAmount,
        Integer plantEnergyAmount,
        Integer startingAnimalAmount,
        Integer startingEnergyAmount,
        Integer minimumBreedingEnergy,
        Integer breedingEnergyCost,
        Integer minimumMutationAmount,
        Integer maximumMutationAmount,
        Integer animalGenomeLength,
        Integer fireFrequency,
        Integer fireLength,
        Integer refreshTime,
        Random random,
        AbstractMap map
) {

    public SimulationParameters  {
        validateInt(width, "Width", 1, 100);
        validateInt(height, "Height", 1, 100);
        validateInt(startingPlantAmount, "Amount of plants spawning at the start", 0, null);
        validateInt(plantGrowingAmount, "Amount of plants growing each day", 0, null);
        validateInt(plantEnergyAmount, "Plant energy profit", 0, null);
        validateInt(startingAnimalAmount, "Amount of animal spawning at the start ", 1, null);
        validateInt(startingEnergyAmount, "Animal start energy", 1, null);
        validateInt(minimumBreedingEnergy, "Minimum breeding energy", 1, null);
        validateInt(breedingEnergyCost, "Breeding energy cost", 1, null);
        validateInt(minimumMutationAmount, "Minimum mutation amount", 0, null);
        validateInt(maximumMutationAmount, "Maximum mutation amount", 1, null);
        validateInt(animalGenomeLength, "Animal genome length", 1, null);
        validateInt(fireFrequency, "Fire frequency", 1, null);
        validateInt(fireLength, "Fire length", 1, null);
        validateInt(refreshTime, "RefreshTime", 10, null);
    }

    private static void validateInt(Integer parameter, String fieldDesc, Integer min, Integer max) {;
        if (parameter == null)
            throw new UserFriendlyException("Missing value!", fieldDesc + " has to be provided.");
        if (min != null && parameter < min)
            throw new UserFriendlyException("Value outside of boundaries!", fieldDesc + " must be greater than or equal to " + min + ".");
        if (max != null && parameter > max)
            throw new UserFriendlyException("Value outside of boundaries!", fieldDesc + " must be less than or equal to " + max + ".");
        }
}
