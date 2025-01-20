package agh.darwinworld.models;

import agh.darwinworld.models.listeners.SimulationStepListener;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CsvPrinterTest {
    private CsvPrinter csvPrinter;
    private final String testFilename = "test_output.csv";

    @BeforeEach
    void setUp() {
        csvPrinter = new CsvPrinter(testFilename);
        File file = new File(testFilename);
        if (file.exists()) {
            assertTrue(file.delete(), "Failed to clean up test file before test");
        }
        file.deleteOnExit();
    }

    @Test
    void testFilenameInitialization() {
        assertEquals(testFilename, csvPrinter.getFilename(), "Filename should match the provided value");
    }

    @Test
    void testOnSimulationPaused() throws IOException {
        MoveDirection[] popularGenome = {MoveDirection.FORWARD, MoveDirection.BACKWARD};
        csvPrinter.updateStatistics(1, 10, 5, 20, new Pair<>(popularGenome, 2), 30, 40, 2);
        csvPrinter.onSimulationPaused();

        File file = new File(testFilename);
        assertTrue(file.exists(), "Output file should be created");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            String content = reader.readLine();

            assertEquals("step,animalCount,plantCount,emptyFieldCount,popularGenome,averageLifetime,averageDescendantsAmount,averageEnergy", header, "Header should match");
            assertEquals("1,10,5,20,04,30,40,2", content, "Content should match");
        }
    }

    @Test
    void testIOExceptionDuringFileWriting() {
        CsvPrinter faultyPrinter = new CsvPrinter("/invalid_path/test_output.csv");

        assertThrows(RuntimeException.class, faultyPrinter::onSimulationPaused, "Should throw a RuntimeException for an invalid file path");
    }
}
