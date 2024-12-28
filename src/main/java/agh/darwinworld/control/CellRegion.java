package agh.darwinworld.control;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * One cell for grid representing one position in the simulation.
 */
public class CellRegion extends Region {
    /**
     * Background color of the cell when there is no plant and no fire at this position.
     */
    public static final Color NORMAL_BACKGROUND = Color.web("0xa6da95");

    /**
     * Background color of the cell where there is a plant and no fire at this position.
     */
    public static final Color PLANT_BACKGROUND = Color.web("0x40a02b");

    /**
     * Background color of the cell where there is a fire, and it is in its last stage.
     * Values between first and last stage has color interpolated between
     * {@code FIRE_BACKGROUND_LEAST} and {@code FIRE_BACKGROUND_MOST}.
     */
    public static final Color FIRE_BACKGROUND_LEAST = Color.BLACK;

    /**
     * Background color of the cell where there is a fire, and it is in its first stage.
     * Values between first and last stage has color interpolated between
     * {@code FIRE_BACKGROUND_LEAST} and {@code FIRE_BACKGROUND_MOST}.
     */
    public static final Color FIRE_BACKGROUND_MOST = Color.ORANGE;

    /**
     * Background color of the circle inside the cell where there is an animal.
     * This color will be applied for cells where there is the lowest amount of animals.
     * Values between min and max amount of animals has color interpolated between
     * {@code ANIMAL_COLOR_LEAST} and {@code ANIMAL_COLOR_MOST}.
     */
    public static final Color ANIMAL_COLOR_LEAST = Color.web("0xc6d0f5");

    /**
     * Background color of the circle inside the cell where there is an animal.
     * This color will be applied for cells where there is the highest amount of animals.
     * Values between min and max amount of animals has color interpolated between
     * {@code ANIMAL_COLOR_LEAST} and {@code ANIMAL_COLOR_MOST}.
     */
    public static final Color ANIMAL_COLOR_MOST = Color.web("0x303446");

    private final Region content;
    private boolean hasPlant = false;
    private int fireStageAmount;
    private int currentFireStage;

    /**
     * @param hasPlant
     * @param currentAnimalAmount
     * @param maxAnimalAmount
     * @param currentFireStage
     * @param fireStageAmount
     */
    public CellRegion(boolean hasPlant, int currentAnimalAmount, int maxAnimalAmount, int currentFireStage, int fireStageAmount) {
        super();
        this.getStyleClass().add("cell");
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);
        content = new Region();
        content.setShape(new Circle(1));
        content.getStyleClass().add("animal");
        content.minWidthProperty().bind(widthProperty());
        content.minHeightProperty().bind(heightProperty());
        getChildren().add(content);
        this.hasPlant = hasPlant;
        this.currentFireStage = currentFireStage;
        this.fireStageAmount = fireStageAmount;
        updateContent(currentAnimalAmount, maxAnimalAmount);
        updateBackground();
    }

    /**
     * @param isSelected
     */
    public void setIsSelected(boolean isSelected) {
        if (isSelected && !content.getStyleClass().contains("selected")) {
            content.getStyleClass().add("selected");
        } else if (!isSelected) {
            content.getStyleClass().remove("selected");
        }
    }

    /**
     * @param currentAnimalAmount
     * @param maxAnimalAmount
     */
    public void setAnimalAmount(int currentAnimalAmount, int maxAnimalAmount) {
        updateContent(currentAnimalAmount, maxAnimalAmount);
    }

    /**
     * @param hasPlant
     */
    public void setHasPlant(boolean hasPlant) {
        this.hasPlant = hasPlant;
        updateBackground();
    }

    /**
     * 
     * @param amount
     */
    public void setFireStageAmount(int amount) {
        this.fireStageAmount = amount;
        updateBackground();
    }

    /**
     * @param fireStage
     */
    public void setCurrentFireStage(int fireStage) {
        this.currentFireStage = fireStage;
        updateBackground();
    }

    private void updateContent(int currentAnimalAmount, int maxAnimalAmount) {
        if (currentAnimalAmount != 0) {
            final Color animalColor = ANIMAL_COLOR_LEAST.interpolate(ANIMAL_COLOR_MOST, (double) currentAnimalAmount / Math.max(maxAnimalAmount, 5));
            content.setBackground(new Background(new BackgroundFill(animalColor, null, null)));
        } else {
            content.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        }
    }

    private void updateBackground() {
        if (this.currentFireStage > 0) {
            final Color fireColor = FIRE_BACKGROUND_LEAST.interpolate(FIRE_BACKGROUND_MOST, (double) (this.currentFireStage - 1) / this.fireStageAmount);
            setBackground(new Background(new BackgroundFill(fireColor, null, null)));
        } else {
            setBackground(new Background(new BackgroundFill(this.hasPlant ? PLANT_BACKGROUND : NORMAL_BACKGROUND, null, null)));
        }
    }
}