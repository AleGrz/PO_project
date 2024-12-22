package agh.darwinworld;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.util.Objects;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/simulation.fxml")));
        stage.setScene(new Scene(root));
        stage.setTitle("Darwin World Project");
        stage.show();
    }

    public void onGridClicked(ActionEvent actionEvent) {

    }
}
