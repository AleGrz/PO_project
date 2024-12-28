package agh.darwinworld.control;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CellRegion extends Region {
    public static final Color NORMAL_BACKGROUND = Color.YELLOWGREEN;
    public static final Color PLANT_BACKGROUND = Color.DARKGREEN;
    public static final Color FIRE_BACKGROUND_LEAST = Color.RED;
    public static final Color FIRE_BACKGROUND_MOST = Color.BLUE;
    public static final Color ANIMAL_COLOR_LEAST = Color.LIGHTBLUE;
    public static final Color ANIMAL_COLOR_MOST = Color.BLUE;

    private final Region content;
    private boolean hasPlant = false;
    private int fireStageAmount;
    private Integer currentFireStage;
    private int currentAnimalAmount;

    public CellRegion(boolean hasPlant, int currentAnimalAmount, Integer currentFireStage, int fireStageAmount) {
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
        this.currentAnimalAmount = currentAnimalAmount;
        updateContent();
        updateBackground();
    }

    public void setIsSelected(boolean isSelected) {
        if (isSelected && !content.getStyleClass().contains("selected")) {
            content.getStyleClass().add("selected");
        } else if (!isSelected) {
            content.getStyleClass().remove("selected");
        }
    }

    public void setCurrentAnimalAmount(int currentAnimalAmount) {
        this.currentAnimalAmount = currentAnimalAmount;
        updateContent();
    }

    public void setHasPlant(boolean hasPlant) {
        this.hasPlant = hasPlant;
        updateBackground();
    }

    public void setFireStageAmount(int amount) {
        this.fireStageAmount = amount;
        updateBackground();
    }

    public void setCurrentFireStage(Integer fireStage) {
        this.currentFireStage = fireStage;
        updateBackground();
    }

    private void updateContent() {
        if (currentAnimalAmount != 0) {
            final Color animalColor = ANIMAL_COLOR_LEAST.interpolate(ANIMAL_COLOR_MOST, (double) currentAnimalAmount / 10);
            content.setBackground(new Background(new BackgroundFill(animalColor, null, null)));
        } else {
            content.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        }
    }

    private void updateBackground() {
        if (this.currentFireStage != null) {
            final Color fireColor = FIRE_BACKGROUND_LEAST.interpolate(FIRE_BACKGROUND_MOST, (double) this.currentFireStage / this.fireStageAmount);
            setBackground(new Background(new BackgroundFill(fireColor, null, null)));
        } else {
            setBackground(new Background(new BackgroundFill(this.hasPlant ? PLANT_BACKGROUND : NORMAL_BACKGROUND, null, null)));
        }
    }
}
