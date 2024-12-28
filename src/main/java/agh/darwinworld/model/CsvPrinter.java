package agh.darwinworld.model;

import java.io.FileWriter;
import java.io.IOException;

public class CsvPrinter implements SimulationStepListener, SimulationPauseListener {
    private String content = "";
    private final String filename;

    public CsvPrinter(String filename) {
        this.filename = filename;
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
    public void updateStatistics(int step, int animalCount, int plantCount, int emptyFieldCount, String popularGenotype, float averageLifetime, float averageDescendantsAmount) {
        content += step + "," + animalCount + "," + plantCount + "," + emptyFieldCount + "," + popularGenotype + ","
                + averageLifetime + "," + averageDescendantsAmount + "\n";
    }
    @Override
    public void onSimulationPaused() {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("step,animalCount,plantCount,emptyFieldCount,popularGenotype,averageLifetime,averageDescendantsAmount\n");
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("Error while writing to file: " + e.getMessage());
        }
    }
}
