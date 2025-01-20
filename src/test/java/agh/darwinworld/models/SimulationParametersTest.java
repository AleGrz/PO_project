package agh.darwinworld.models;

import agh.darwinworld.controls.IntField;
import agh.darwinworld.models.animals.AnimalType;
import agh.darwinworld.models.exceptions.UserFriendlyException;
import agh.darwinworld.models.maps.MapType;
import javafx.application.Platform;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class SimulationParametersTest {
    @Test
    public void testConstructorValidParameters() {
        assertDoesNotThrow(() -> new SimulationParameters(
                50, 50, 10,
                5, 10, 20,
                100, 50, 20,
                0, 8, 8,
                5, 3, 30,
                12345, MapType.WORLD, AnimalType.ANIMAL
        ));
    }

    @Test
    public void testConstructorInvalidWidth() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new SimulationParameters(
                0, 50, 10,
                5, 10, 20,
                100, 50, 20,
                0, 10, 8,
                5, 3, 30,
                12345, MapType.FIRE, AnimalType.AGEING_ANIMAL
        ));
        assertTrue(exception.getMessage().contains("Width must be greater than or equal to 1."));
    }

    @Test
    public void testConstructorInvalidMutationAmounts() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new SimulationParameters(
                50, 50, 10,
                5, 10, 20,
                100, 50, 20,
                5, 3, 8,
                5, 3, 30,
                12345, MapType.WORLD, AnimalType.ANIMAL
        ));
        assertTrue(exception.getMessage().contains("Minimum mutation amount must be less than or equal to maximum mutation amount."));
    }

    @Test
    public void testCreateFromIntFieldValidParameters() {
        Platform.startup(() -> {});
        assertDoesNotThrow(() -> {
            SimulationParameters params = SimulationParameters.createFromIntField(
                    createIntField(50), createIntField(50), createIntField(10),
                    createIntField(5), createIntField(10), createIntField(20),
                    createIntField(100), createIntField(50), createIntField(20),
                    createIntField(0), createIntField(8), createIntField(8),
                    createIntField(5), createIntField(3), createIntField(30),
                    createIntField(12345),
                    MapType.FIRE, AnimalType.AGEING_ANIMAL
            );

            assertEquals(50, params.width());
            assertEquals(20, params.startingAnimalAmount());
            assertEquals(MapType.FIRE, params.mapType());
            assertEquals(AnimalType.AGEING_ANIMAL, params.animalType());
        });
    }

    @Test
    public void testCreateFromJsonValidFile() {
        try {
            File tempFile = File.createTempFile("params", ".json");
            try (FileWriter writer = new FileWriter(tempFile)) {
                writer.write(new JSONObject()
                        .put("width", 50)
                        .put("height", 50)
                        .put("startingPlantAmount", 10)
                        .put("plantGrowingAmount", 5)
                        .put("plantEnergyAmount", 10)
                        .put("startingAnimalAmount", 20)
                        .put("startingEnergyAmount", 100)
                        .put("minimumBreedingEnergy", 50)
                        .put("breedingEnergyCost", 20)
                        .put("minimumMutationAmount", 0)
                        .put("maximumMutationAmount", 8)
                        .put("animalGenomeLength", 8)
                        .put("fireInterval", 5)
                        .put("fireLength", 3)
                        .put("refreshTime", 30)
                        .put("seed", 12345)
                        .put("mapType", 1)
                        .put("animalType", 1)
                        .toString());
            }

            SimulationParameters params = SimulationParameters.createFromJson(tempFile);
            assertEquals(50, params.width());
            assertEquals(MapType.FIRE, params.mapType());
            assertEquals(AnimalType.AGEING_ANIMAL, params.animalType());
            tempFile.deleteOnExit();
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        } catch (UserFriendlyException e) {
            fail("UserFriendlyException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testSaveToJsonAndReload() {
        File tempFile;
        try {
            SimulationParameters params = new SimulationParameters(
                    50, 50, 10,
                    5, 10, 20,
                    100, 50, 20,
                    0, 8, 8,
                    5, 3, 30,
                    12345, MapType.WORLD, AnimalType.ANIMAL
            );
            tempFile = File.createTempFile("params", ".json");
            params.saveToJson(tempFile.getAbsolutePath());
            SimulationParameters reloadedParams = SimulationParameters.createFromJson(tempFile);
            assertEquals(params.width(), reloadedParams.width());
            assertEquals(params.mapType(), reloadedParams.mapType());
            assertEquals(params.animalType(), reloadedParams.animalType());
            tempFile.deleteOnExit();
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        } catch (UserFriendlyException e) {
            fail("UserFriendlyException occurred: " + e.getMessage());
        }
    }

    private IntField createIntField(int value) {
        IntField field = new IntField();
        field.setValue(value);
        return field;
    }
}
