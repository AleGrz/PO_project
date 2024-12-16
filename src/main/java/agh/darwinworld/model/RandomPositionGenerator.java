package agh.darwinworld.model;

import java.util.*;

public class RandomPositionGenerator implements Iterable<Vector2D>, Iterator<Vector2D> {
    private final List<Vector2D> positions;
    private int objectAmount;
    private final Random random;

    public RandomPositionGenerator(Random random, int maxWidth, int maxHeight, int objectAmount) {
        if (maxWidth * maxHeight < objectAmount)
            throw new IllegalArgumentException("There is no enough space for this amount of object in specified boundaries.");
        this.random = random;
        this.objectAmount = objectAmount;
        positions = new ArrayList<>();
        for (int i = 0; i < maxHeight; i++) {
            for (int j = 0; j < maxWidth; j++) {
                positions.add(new Vector2D(i, j));
            }
        }
    }

    public RandomPositionGenerator(int maxWidth, int maxHeight, int objectAmount) {
        this(new Random(), maxWidth, maxHeight, objectAmount);
    }

    @Override
    public boolean hasNext() {
        return objectAmount > 0;
    }

    @Override
    public Vector2D next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more positions available");
        }
        objectAmount--;
        return positions.remove(random.nextInt(positions.size()));
    }

    @Override
    public Iterator<Vector2D> iterator() {
        return this;
    }
}