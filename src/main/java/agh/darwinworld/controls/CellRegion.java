package agh.darwinworld.controls;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

/**
 * One cell for grid representing one position in the simulation.
 */
public class CellRegion extends Region {
    /**
     * Background color of the cell when there is no plant and no fire at this position.
     */
    public static final Color NORMAL_BACKGROUND = Color.web("0x9ece6a");

    /**
     * Background color of the cell where there is a plant and no fire at this position.
     */
    public static final Color PLANT_BACKGROUND = Color.web("0x5e7b3f");

    /**
     * Background color of the cell where there is a fire, and it is in its last stage.
     * Values between first and last stage has color interpolated between
     * {@code FIRE_BACKGROUND_LEAST} and {@code FIRE_BACKGROUND_MOST}.
     */
    public static final Color FIRE_BACKGROUND_LEAST = Color.web("0x190f0a");

    /**
     * Background color of the cell where there is a fire, and it is in its first stage.
     * Values between first and last stage has color interpolated between
     * {@code FIRE_BACKGROUND_LEAST} and {@code FIRE_BACKGROUND_MOST}.
     */
    public static final Color FIRE_BACKGROUND_MOST = Color.web("0xff9e64");

    /**
     * Background color of the circle inside the cell where there is an animal.
     * This color will be applied for cells where there is the lowest amount of animals.
     * Values between min and max amount of animals has color interpolated between
     * {@code ANIMAL_COLOR_LEAST} and {@code ANIMAL_COLOR_MOST}.
     */
    public static final Color ANIMAL_COLOR_LEAST = Color.web("0xc8aef8");

    /**
     * Background color of the circle inside the cell where there is an animal.
     * This color will be applied for cells where there is the highest amount of animals.
     * Values between min and max amount of animals has color interpolated between
     * {@code ANIMAL_COLOR_LEAST} and {@code ANIMAL_COLOR_MOST}.
     */
    public static final Color ANIMAL_COLOR_MOST = Color.web("0x414868");

    public static final Color POPULAR_GENOME_COLOR = Color.web("0x2ac3de");

    /**
     * Cached transparent background for better performance.
     */
    private static final Background transparentBackground = new Background(new BackgroundFill(Color.TRANSPARENT, null, null));

    /**
     * Cached plant background for better performance.
     */
    private static final Background plantBackground = new Background(new BackgroundFill(PLANT_BACKGROUND, null, null));

    /**
     * Cached normal background for better performance.
     */
    private static final Background normalBackground = new Background(new BackgroundFill(NORMAL_BACKGROUND, null, null));

    private final Region content;
    private final Region indicator;
    private final Label energyLabel;
    private boolean hasPlant;
    private int fireStageAmount;
    private int currentFireStage;

    /**
     * Constructs a CellRegion with the given parameters.
     *
     * @param hasPlant            whether the cell has a plant.
     * @param currentAnimalAmount current number of animals in the cell.
     * @param maxAnimalAmount     maximum number of animals in the cell.
     * @param currentFireStage    current stage of fire in the cell.
     * @param fireStageAmount     total number of fire stages in the cell.
     */
    public CellRegion(boolean hasPlant, int currentAnimalAmount, int maxAnimalAmount, int currentFireStage, int fireStageAmount, int energy) {
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

        indicator = new Region();
        indicator.setShape(new Circle(1));
        indicator.minWidthProperty().bind(widthProperty().multiply(0.5));
        indicator.minHeightProperty().bind(heightProperty().multiply(0.5));
        indicator.layoutXProperty().bind(widthProperty().subtract(indicator.widthProperty()).divide(2));
        indicator.layoutYProperty().bind(heightProperty().subtract(indicator.heightProperty()).divide(2));
        getChildren().add(indicator);

        energyLabel = new Label();
        energyLabel.fontProperty().bind(fontProperty);
        energyLabel.getStyleClass().add("energy");
        energyLabel.layoutXProperty().bind(widthProperty().subtract(energyLabel.widthProperty()).divide(2));
        energyLabel.layoutYProperty().bind(heightProperty().subtract(energyLabel.heightProperty()).divide(2));
        widthProperty().addListener((observable, oldValue, newValue) -> updateFont());
        heightProperty().addListener((observable, oldValue, newValue) -> updateFont());
        getChildren().add(energyLabel);

        this.hasPlant = hasPlant;
        this.currentFireStage = currentFireStage;
        this.fireStageAmount = fireStageAmount;
        updateContent(currentAnimalAmount, maxAnimalAmount, energy);
        updateBackground();
    }

    /**
     * Updates cell font size.
     */
    private void updateFont() {
        double size = getWidth() / 3.0;
        if (size == fontProperty.get().getSize()) return;
        fontProperty.setValue(new Font(size));
    }

    /**
     * Stores font of cell.
     */
    SimpleObjectProperty<Font> fontProperty = new SimpleObjectProperty<>(new Font(getWidth() / 3.0));

    /**
     * Sets whether the cell is selected.
     *
     * @param isSelected true if the cell is selected, false otherwise.
     */
    public void setIsSelected(boolean isSelected) {
        if (isSelected && !content.getStyleClass().contains("selected")) {
            content.getStyleClass().add("selected");
        } else if (!isSelected) {
            content.getStyleClass().remove("selected");
        }
    }

    /**
     * Updates the number of animals in the cell.
     *
     * @param currentAnimalAmount current number of animals in the cell.
     * @param maxAnimalAmount     maximum number of animals in the cell.
     * @param energy     average energy of animals in the cell.
     */
    public void setAnimalAmount(int currentAnimalAmount, int maxAnimalAmount, int energy) {
        updateContent(currentAnimalAmount, maxAnimalAmount, energy);
    }

    /**
     * Sets whether the cell has a plant.
     *
     * @param hasPlant true if the cell has a plant, false otherwise.
     */
    public void setHasPlant(boolean hasPlant) {
        this.hasPlant = hasPlant;
        updateBackground();
    }

    /**
     * Sets the total number of fire stages for the cell.
     *
     * @param amount total number of fire stages.
     */
    public void setFireStageAmount(int amount) {
        this.fireStageAmount = amount;
        updateBackground();
    }

    /**
     * Sets the current fire stage of the cell.
     *
     * @param fireStage current fire stage.
     */
    public void setCurrentFireStage(int fireStage) {
        this.currentFireStage = fireStage;
        updateBackground();
    }

    public void updateIndicator(boolean isPopularGenome) {
        if (isPopularGenome) {
            indicator.setBackground(new Background(new BackgroundFill(POPULAR_GENOME_COLOR, null, null)));
        } else {
            indicator.setBackground(transparentBackground);
        }
    }

    private void updateContent(int currentAnimalAmount, int maxAnimalAmount, int energy) {
        if (currentAnimalAmount != 0) {
            final Color animalColor = ANIMAL_COLOR_LEAST.interpolate(ANIMAL_COLOR_MOST, (double) currentAnimalAmount / Math.max(maxAnimalAmount, 5));
            content.setBackground(new Background(new BackgroundFill(animalColor, null, null)));
            energyLabel.setText(Integer.toString(energy));
        } else {
            content.setBackground(transparentBackground);
            energyLabel.setText("");
        }
    }

    private void updateBackground() {
        if (this.currentFireStage > 0) {
            final Color fireColor = FIRE_BACKGROUND_LEAST.interpolate(FIRE_BACKGROUND_MOST, (double) (this.currentFireStage - 1) / this.fireStageAmount);
            setBackground(new Background(new BackgroundFill(fireColor, null, null)));
        } else {
            setBackground(this.hasPlant ? plantBackground : normalBackground);
        }
    }
}