package agh.darwinworld.models.animals;

import agh.darwinworld.models.MapDirection;
import agh.darwinworld.models.Vector2D;
import agh.darwinworld.models.listeners.AnimalListener;
import agh.darwinworld.models.listeners.MovementHandler;
import javafx.util.Pair;
import org.junit.jupiter.api.*;

import java.beans.PropertyChangeEvent;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {
    private final SimpleMovementHandler movementHandler = new SimpleMovementHandler();

    private static class SimpleMovementHandler implements MovementHandler {
        @Override
        public Pair<Vector2D, MapDirection> move(Vector2D position, MapDirection move) {
            return new Pair<>(position, move);
        }
    }
    private Random random;

    @BeforeEach
    void setup() {
        random = new Random();
    }

    @Test
    void testConstructorWithInvalidGenomeLength() {
        assertThrows(IllegalArgumentException.class, () -> new Animal(random, -1, 10),
                "Constructor should throw an exception for invalid genome length.");
    }

    @Test
    void testConstructorWithInvalidEnergy() {
        assertThrows(IllegalArgumentException.class, () -> new Animal(random, 5, -10),
                "Constructor should throw an exception for invalid energy.");
    }

    @Test
    void testConstructorWithValidInputs() {
        Animal animal = new Animal(random, 5, 10);

        assertNotNull(animal, "Animal object should be successfully created.");
        assertEquals(5, animal.getGenome().length, "Genome length should match the input value.");
        assertEquals(10, animal.getEnergy(), "Initial energy should match the input value.");
        assertFalse(animal.isDead(), "Animal should not be dead upon creation.");
    }

    @Test
    public void testOffspringConstructorEvenGenome() {
        final int genomeLength = 10;
        final int energy = 20;
        final int breedingEnergyCost = 10;

        Animal dad = new Animal(random, genomeLength, energy);
        Animal mom = new Animal(random, genomeLength, energy);
        Animal child = new Animal(dad, mom, breedingEnergyCost, 0, 0, 1, 0);

        assertNotNull(child, "Child should be successfully created.");
        assertEquals(breedingEnergyCost * 2, child.getEnergy(), "Child's energy should equal twice the breeding energy cost.");
        assertEquals(genomeLength, child.getGenome().length, "Child's genome length should match parent's genome length.");
        assertEquals(0, child.getAge(), "Child's age should start at 0.");
        assertEquals(energy - breedingEnergyCost, dad.getEnergy(), "Dad's energy should be reduced correctly.");
        assertEquals(energy - breedingEnergyCost, mom.getEnergy(), "Mom's energy should be reduced correctly.");
    }
    @Test
    public void testOffspringConstructorOddGenome() {
        final int genomeLength = 7;
        final int energy = 20;
        final int breedingEnergyCost = 10;

        Animal dad = new Animal(random, genomeLength, energy);
        Animal mom = new Animal(random, genomeLength, energy);
        Animal child = new Animal(dad, mom, breedingEnergyCost, 0, 0, 1, 0);

        assertNotNull(child, "Child should be successfully created.");
        assertEquals(breedingEnergyCost * 2, child.getEnergy(), "Child's energy should equal twice the breeding energy cost.");
        assertEquals(genomeLength, child.getGenome().length, "Child's genome length should match parent's genome length.");
        assertEquals(0, child.getAge(), "Child's age should start at 0.");
        assertEquals(energy - breedingEnergyCost, dad.getEnergy(), "Dad's energy should be reduced correctly.");
        assertEquals(energy - breedingEnergyCost, mom.getEnergy(), "Mom's energy should be reduced correctly.");
    }

    @Test
    public void testChildrenAmount() {
        final int genomeLength = 10;
        Animal mom = new Animal(random, genomeLength, 0);
        Animal dad = new Animal(random, genomeLength, 0);
        assertEquals(0, mom.getChildrenAmount(), "Mom should initially have no children.");
        assertEquals(0, dad.getChildrenAmount(), "Dad should initially have no children.");
        Animal child = new Animal(mom, dad, 0, 0, 0, 1, 0);
        assertEquals(1, mom.getChildrenAmount(), "Mom should have one child.");
        assertEquals(1, dad.getChildrenAmount(), "Dad should have one child.");
        assertEquals(0, child.getChildrenAmount(), "Child should initially have no children.");
    }

    @Test
    public void testMoveAgeChange() {
        Animal animal = new Animal(random ,1, 10);
        assertEquals(0, animal.getAge(), "Age initially should be zero.");
        animal.move(movementHandler, new Vector2D(0, 0), 1);
        assertEquals(1, animal.getAge(), "Age after move should increase by one.");
    }

    @Test
    public void testDescendantsChildrenAmount() {
        final int genomeLength = 10;
        Animal mom = new Animal(random, genomeLength, 0);
        Animal dad = new Animal(random, genomeLength, 0);
        assertEquals(0, mom.getDescendantsAmount(), "Mom should initially have no descendants.");
        assertEquals(0, dad.getDescendantsAmount(), "Dad should initially have no descendants.");
        Animal childA = new Animal(mom, dad, 0, 0, 0, 1, 0);
        assertEquals(1, mom.getDescendantsAmount(), "Mom should have one descendant.");
        assertEquals(1, dad.getDescendantsAmount(), "Dad should have one descendant.");
        assertEquals(0, childA.getDescendantsAmount(), "Child A should initially have no descendants.");
        Animal childB = new Animal(mom, dad, 0, 0, 0, 1, 0);
        assertEquals(2, mom.getDescendantsAmount(), "Mom should have two descendants.");
        assertEquals(2, dad.getDescendantsAmount(), "Dad should have two descendants.");
        assertEquals(0, childB.getDescendantsAmount(), "Child B should initially have no descendants.");
        Animal childAB = new Animal(childA, childB, 0, 0, 0, 1, 0);
        assertEquals(3, mom.getDescendantsAmount(), "Mom should have three descendants.");
        assertEquals(3, dad.getDescendantsAmount(), "Dad should have three descendants.");
        assertEquals(1, childA.getDescendantsAmount(), "Child A should have one descendant.");
        assertEquals(1, childB.getDescendantsAmount(), "Child B should have one descendant.");
        assertEquals(0, childAB.getDescendantsAmount(), "Child of child A and Child B should have initially no descendants.");
        Animal otherChild = new Animal(random, genomeLength, 0);
        Animal childABOther = new Animal(childAB, otherChild, 0, 0, 0, 1, 0);
        assertEquals(4, mom.getDescendantsAmount(), "Mom should have four descendants.");
        assertEquals(4, dad.getDescendantsAmount(), "Dad should have four descendants.");
        assertEquals(2, childA.getDescendantsAmount(), "Child A should have two descendants.");
        assertEquals(2, childB.getDescendantsAmount(), "Child B should have two descendants.");
        assertEquals(1, childAB.getDescendantsAmount(), "Child of child A and child B should have one descendant.");
        assertEquals(1, otherChild.getDescendantsAmount(), "Other child should have one descendant.");
        assertEquals(0, childABOther.getDescendantsAmount(), "Child of other child and child of child A and child B should have initially no descendants.");
    }

    @Test
    void testIsDeadAndForceKill() {
        Animal animal = new Animal(random, 1, 0);
        animal.forceKill(0);
        assertTrue(animal.isDead(), "Animal should be marked as dead.");
        assertEquals(0, animal.diedAt(), "Animal's death time should be recorded correctly.");
    }

    @Test
    void testMoveUpdatesState() {
        Animal animal = new Animal(random, 1, 10);
        Vector2D initialPosition = new Vector2D(0, 0);
        MovementHandler movementHandler = (position, direction) -> new Pair<>(new Vector2D(1, 1), MapDirection.EAST);

        Vector2D newPosition = animal.move(movementHandler, initialPosition, 0);

        assertEquals(new Vector2D(1, 1), newPosition, "Animal should move to the correct position.");
        assertEquals(MapDirection.EAST, animal.getDirection(), "Animal's direction should update correctly.");
        assertEquals(9, animal.getEnergy(), "Animal's energy should decrease by 1 after moving.");
        assertEquals(1, animal.getAge(), "Animal's age should increase by 1 after moving.");
    }

    @Test
    void testEnergyDepletionOnMove() {
        final int baseEnergy = 10;
        Animal animal = new Animal(random, 1, baseEnergy);
        for (int i = 0; i <= baseEnergy; i++) {
            assertEquals(baseEnergy - i, animal.getEnergy(), "Animal's energy should decrease correctly with each move.");
            animal.move(Pair::new, new Vector2D(0, 0), i);
        }
        assertEquals(-1, animal.getEnergy(), "Animal's energy should reach zero after sufficient moves.");
        assertTrue(animal.isDead(), "Animal should be dead after energy is depleted.");
    }

    @Test
    void testEat() {
        Animal animal = new Animal(random, 1, 10);
        animal.eat(5, 0);
        assertEquals(15, animal.getEnergy(), "Animal's energy should increase correctly after eating.");
        assertEquals(1, animal.getTotalEatenPlants(), "Animal's plant consumption count should update correctly.");
        assertThrows(IllegalArgumentException.class, () -> animal.eat(-1, 0), "Eating negative energy should throw an exception.");
    }

    @Test
    void testListenerNotificationOnPropertyChange() {
        Animal animal = new Animal(random, 5, 10);
        TestAnimalListener listener = new TestAnimalListener();
        animal.addListener(listener);

        animal.eat(5, 1);

        assertTrue(listener.propertyChanged, "Listener should be notified of property changes.");
    }

    static class TestAnimalListener implements AnimalListener {
        boolean propertyChanged = false;

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            propertyChanged = true;
        }
    }
}
