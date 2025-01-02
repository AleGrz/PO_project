package agh.darwinworld.models;

import agh.darwinworld.models.listeners.SimulationPauseListener;
import agh.darwinworld.models.listeners.SimulationStepListener;
import javafx.util.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * A class responsible for printing simulation's data to a CSV file.
 */
public class CsvPrinter implements SimulationStepListener, SimulationPauseListener {
    private String content = "";
    private final String filename;

    /**
     * Constructs a CsvPrinter with the specified filename.
     * @param filename The name of the CSV file to write data to.
     */
    public CsvPrinter(String filename) {
        this.filename = filename;
    }

    /**
     * Retrieves the filename associated with this CsvPrinter.
     * @return The name of the CSV file.
     */
    public String getFilename() {
        return filename;
    }

    @Override
    public void addPlant(Vector2D position) {}

    @Override
    public void removePlant(Vector2D position) {}

    @Override
    public void updateAnimal(Vector2D position, int animalCount, int maxAnimalCount) {}

    @Override
    public void updateFire(Vector2D position, int length) {}

    @Override
    public void updateStatistics(int step, int animalCount, int plantCount, int emptyFieldCount, Pair<MoveDirection[], Integer> popularGenome,
                                 int averageLifetime, int averageDescendantsAmount) {
        String popularGenomeString = popularGenome == null ? "MISSING" :
                Arrays.stream(popularGenome.getKey())
                        .map(x -> Integer.toString(x.ordinal()))
                        .collect(Collectors.joining(""));
        content += step + "," + animalCount + "," + plantCount + "," + emptyFieldCount + "," + popularGenomeString + ","
                + averageLifetime + "," + averageDescendantsAmount + "\n";
    }

    @Override
    public void onSimulationPaused() {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("step,animalCount,plantCount,emptyFieldCount,popularGenome,averageLifetime,averageDescendantsAmount\n");
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("Error while writing to file: " + e.getMessage());
        }
    }
}
