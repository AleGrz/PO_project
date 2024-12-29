package agh.darwinworld.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FireMap extends AbstractMap {
    private final HashMap<Vector2D, Integer> fire = new HashMap<>();

    public boolean isFireOnPosition(Vector2D position) {
        return fire.containsKey(position);
    }

    public void step(int count) {
        super.step(count);
        propagateFire();
        updateStatistics();

    }

    public void propagateFire() {
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
}
