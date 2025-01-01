package agh.darwinworld.helpers;

import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StageHelper {
    public static Insets getWindowInsets(Stage stage) {
        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();
        double contentWidth = stage.getScene().getWidth();
        double contentHeight = stage.getScene().getHeight();
        double horizontalInsets = (stageWidth - contentWidth) / 2;
        double verticalInsets = stageHeight - contentHeight - horizontalInsets;
        return new Insets(verticalInsets, horizontalInsets, verticalInsets, horizontalInsets);
    }

    public static void bindMinSize(Stage stage, Pane root) {
        stage.setOnShown(e -> {
            Insets windowInsets = StageHelper.getWindowInsets(stage);
            stage.minWidthProperty().bind(root.minWidthProperty().add(windowInsets.getLeft() + windowInsets.getRight()));
            stage.minHeightProperty().bind(root.minHeightProperty().add(windowInsets.getTop() + windowInsets.getBottom()));
            stage.setWidth(stage.getMinWidth());
            stage.setHeight(stage.getMinHeight());
        });
    }
}
