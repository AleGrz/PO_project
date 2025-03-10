package agh.darwinworld;

import agh.darwinworld.helpers.StageHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.util.Objects;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Application.setUserAgentStylesheet("theme.css");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("startMenu.fxml"));
        BorderPane root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Darwin World Project");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icon.png"))));
        StageHelper.bindMinSize(stage, root);
        StageHelper.setDarkMode(stage, true);
        stage.show();
    }
}
