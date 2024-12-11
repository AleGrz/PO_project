package agh.darwinworld;

import agh.darwinworld.model.Animal;
import agh.darwinworld.model.Grass;
import agh.darwinworld.model.WorldMap;

import java.util.ArrayList;

public class Simulation implements Runnable {
    ArrayList<Animal> animals = new ArrayList<>();
    WorldMap worldMap;
    @Override
    public void run() {
        // loop
            // usun zwierzaka
            // skret i przemieszczenie
            // konsumpcja
            // zegz
            // new rosliny
        while(true) {
            for (Animal animal : animals) {
                if(animal.isDead()) {
                    animals.remove(animal);
                }
                animal.move();
                animal.eat();
            }
            worldMap.populateGrass();
        }
    }
}
