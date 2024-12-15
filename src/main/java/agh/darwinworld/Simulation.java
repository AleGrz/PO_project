package agh.darwinworld;

import agh.darwinworld.model.Animal;
import agh.darwinworld.model.Grass;
import agh.darwinworld.model.Vector2D;
import agh.darwinworld.model.WorldMap;

import java.util.ArrayList;
import java.util.HashMap;

public class Simulation implements Runnable {
    ArrayList<Animal> animals = new ArrayList<>();
    WorldMap worldMap;

    @Override
    public void run() {
        while(!animals.isEmpty()) {
            animals.removeIf(Animal::isDead);
            for (Animal animal : animals) animal.move();
            for (Animal animal : animals) animal.eat();
        }
    }
}
