package agh.darwinworld.models.maps;

import agh.darwinworld.models.Animal;
import agh.darwinworld.models.MapDirection;
import agh.darwinworld.models.listeners.MovementHandler;
import agh.darwinworld.models.Vector2D;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FireMap extends AbstractMap implements MovementHandler {
    private final HashMap<Vector2D, Integer> fire = new HashMap<>();

    public boolean isFireOnPosition(Vector2D position) {
        return fire.containsKey(position);
    }

    @Override
    public void step(int count) {
        super.step(count);
        propagateFire(count);
        if (count % this.params.fireFrequency() == 0 && !plants.isEmpty()) {
            Vector2D randomPos = plants.toArray(new Vector2D[0])[this.params.random().nextInt(plants.size())];
            fire.put(randomPos, this.params.fireLength());
        }
        updateStatistics(count);

    }

    public void propagateFire(int step) {
        HashMap<Vector2D, Integer> newFire = new HashMap<>();
        Iterator<Map.Entry<Vector2D, Integer>> iterator = fire.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Vector2D, Integer> fireData = iterator.next();
            Vector2D position = fireData.getKey();
            Vector2D[] directions = new Vector2D[]{
                    position.add(new Vector2D(0, 1)),
                    position.add(new Vector2D(1, 0)),
                    position.add(new Vector2D(0, -1)),
                    position.add(new Vector2D(-1, 0)),
            };
            for (Vector2D direction : directions) {
                if (fire.containsKey(direction)) continue;
                if (plants.contains(position)) {
                    newFire.put(direction, this.params.fireLength());
                    listeners.forEach(listener -> listener.removePlant(position));
                }
            }
            listeners.forEach(listener -> listener.updateFire(position, fire.get(position)));
            final int max = getMaxAnimalAmount();
            listeners.forEach(listener -> listener.updateAnimal(position, 0, max));
            if (animals.containsKey(position)) {
                for(Animal animal : animals.get(position)) {
                    animal.forceKill(step);
                    totalLifetime += animal.getAge();
                    deadCount++;
                }
                animals.get(position).removeAll(animals.get(position));
                animals.remove(position);
            }

            plants.remove(position);

            if (fireData.getValue() <= 0) {
                listeners.forEach(listener -> listener.updateFire(position, 0));
                iterator.remove();
            } else {
                fireData.setValue(fireData.getValue() - 1);
            }
        }
        fire.putAll(newFire);
    }

    @Override
    public Pair<Vector2D, MapDirection> move(Vector2D position, MapDirection direction) {
        Vector2D newPos = position.add(direction.getValue())
                .lowerLeft(new Vector2D(this.params.width()-1, this.params.height()-1))
                .upperRight(new Vector2D(0, 0));
        return new Pair<>(newPos, direction);
    }
}
