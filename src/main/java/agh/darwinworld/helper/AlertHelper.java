package agh.darwinworld.helper;

import agh.darwinworld.model.UserFriendlyException;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Window;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * A utility class for displaying alert dialogs to the user.
 */
public class AlertHelper {
    /**
     * Shows an alert dialog for an exception, displaying the error message and the stack trace.
     * @param owner The owner window for the alert, can be {@code null} if no owner window is needed.
     * @param e The exception to be displayed in the alert.
     */
    public static void ShowExceptionAlert(Window owner, Throwable e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(owner);
        alert.setTitle("Exception");
        alert.setHeaderText("Look, an exception has occurred!");
        alert.setContentText(e.getMessage());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();
        Label label = new Label("The exception stacktrace was:");
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane expContent = new GridPane();
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }

    /**
     * Shows a user-friendly alert dialog for a custom exception.
     * This type of alert is generally used for warnings that can be presented to the user in a more friendly manner.
     * @param owner The owner window for the alert, can be {@code null} if no owner window is needed.
     * @param e The user-friendly exception to be displayed in the alert.
     */
    public static void ShowUserFriendlyExceptionAlert(Window owner, UserFriendlyException e) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(owner);
        alert.setTitle("Warning");
        alert.setHeaderText(e.getHeader());
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}
