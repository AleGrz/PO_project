package agh.darwinworld.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CsvPrinterTest {
    @Test
    public void testGetFileName() {
        CsvPrinter p = new CsvPrinter("test");
        assertEquals("test", p.getFileName());
    }
}
