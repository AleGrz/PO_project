package agh.darwinworld.model;

import java.util.ArrayList;
import java.util.Random;

public class Animal extends WorldElement {
    private final ArrayList<Direction> genome;
    private Vector2D position;
    private Direction direction;
    private int energy;

    public Animal(Vector2D position, Direction direction) {
        this.position = position;
        this.direction = direction;
        Random random = new Random();
        int n = random.nextInt();
        this.genome = new ArrayList<>(n);
        Direction[] directions = Direction.values();
        for (int i = 0; i < n; i++) {
            genome.set(i, directions[random.nextInt(directions.length)]);
        }
    }

    public Animal(Animal mommy, Animal daddy) {
        genome = new ArrayList<>();
    }

    public Vector2D getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    public void move() {

    }

    public void mutate() {
        // mutuje losowy gen, down grade mode
    }

    public void eat() {

    }

    public boolean isDead() {
        return energy <= 0;
    }
}
