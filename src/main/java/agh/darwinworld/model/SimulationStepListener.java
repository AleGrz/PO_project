package agh.darwinworld.model;

public interface SimulationStepListener {
    void moveAnimal(Vector2D oldPosition, Vector2D newPosition);
    void addPlant(Vector2D position);
    void addAnimal(Vector2D position);
    void removePlant(Vector2D position);
    void removeAnimal(Vector2D position);
}
