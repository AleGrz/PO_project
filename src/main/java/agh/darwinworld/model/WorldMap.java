package agh.darwinworld.model;

public class WorldMap extends AbstractMap {
    public void step(int count) {
        super.step(count);
        updateStatistics();
    }
}
