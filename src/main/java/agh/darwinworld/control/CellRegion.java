package agh.darwinworld.control;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CellRegion extends Region {
    public static final Color NORMAL_BACKGROUND = Color.web("0xa6da95");
    public static final Color PLANT_BACKGROUND = Color.web("0x40a02b");
    public static final Color FIRE_BACKGROUND_LEAST = Color.BLACK;
    public static final Color FIRE_BACKGROUND_MOST = Color.ORANGE;
    public static final Color ANIMAL_COLOR_LEAST = Color.web("0xc6d0f5");
    public static final Color ANIMAL_COLOR_MOST = Color.web("0x303446");

    private final Region content;
    private boolean hasPlant = false;
    private int fireStageAmount;
    private int currentFireStage;

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

    public void setIsSelected(boolean isSelected) {
        if (isSelected && !content.getStyleClass().contains("selected")) {
            content.getStyleClass().add("selected");
        } else if (!isSelected) {
            content.getStyleClass().remove("selected");
        }
    }

    public void setAnimalAmount(int currentAnimalAmount, int maxAnimalAmount) {
        updateContent(currentAnimalAmount, maxAnimalAmount);
    }

    public void setHasPlant(boolean hasPlant) {
        this.hasPlant = hasPlant;
        updateBackground();
    }

    public void setFireStageAmount(int amount) {
        this.fireStageAmount = amount;
        updateBackground();
    }

    public void setCurrentFireStage(int fireStage) {
        if (fireStage > 0) this.hasPlant = false;
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
