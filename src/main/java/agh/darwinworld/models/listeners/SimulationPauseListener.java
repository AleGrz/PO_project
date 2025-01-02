package agh.darwinworld.models.listeners;

/**
 * Interface listening for pausing simulation.
 */
public interface SimulationPauseListener {
    /**
     * Called whenever simulation has been paused.
     */
    void onSimulationPaused();
}
