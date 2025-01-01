package agh.darwinworld.helpers;

import agh.darwinworld.helpers.natives.Dwmapi;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.lang.reflect.Method;

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
        EventHandler<WindowEvent> oldOnShown = stage.getOnShown();
        stage.setOnShown(e -> {
            if (oldOnShown != null)
                oldOnShown.handle(e);
            Insets windowInsets = StageHelper.getWindowInsets(stage);
            stage.minWidthProperty().bind(root.minWidthProperty().add(windowInsets.getLeft() + windowInsets.getRight()));
            stage.minHeightProperty().bind(root.minHeightProperty().add(windowInsets.getTop() + windowInsets.getBottom()));
            stage.setWidth(stage.getMinWidth());
            stage.setHeight(stage.getMinHeight());
        });
    }

    public static void setDarkMode(Window stage, boolean darkMode) {
        if (stage.isShowing()) {
            setDarkModeInternal(stage, darkMode);
        } else {
            EventHandler<WindowEvent> oldOnShown = stage.getOnShown();
            stage.setOnShown(e -> {
                if (oldOnShown != null)
                    oldOnShown.handle(e);
                setDarkModeInternal(stage, darkMode);
            });
        }
    }

    private static void setDarkModeInternal(Window stage, boolean darkMode) {
        String os = System.getProperty("os.name").toLowerCase();
        if (!os.contains("win")) return;
        WinDef.HWND hwnd = getNativeHandleForWindow(stage);
        Dwmapi dwmapi = Dwmapi.INSTANCE;
        WinDef.BOOLByReference darkModeRef = new WinDef.BOOLByReference(new WinDef.BOOL(darkMode));
        dwmapi.DwmSetWindowAttribute(hwnd, 20, darkModeRef, Native.getNativeSize(WinDef.BOOLByReference.class));
    }


    private static WinDef.HWND getNativeHandleForWindow(Window stage) {
        try {
            final Method getPeer = Window.class.getDeclaredMethod("getPeer");
            getPeer.setAccessible(true);
            final Object tkStage = getPeer.invoke(stage);
            final Method getRawHandle = tkStage.getClass().getMethod("getRawHandle");
            getRawHandle.setAccessible(true);
            final Pointer pointer = new Pointer((Long) getRawHandle.invoke(tkStage));
            return new WinDef.HWND(pointer);
        } catch (Exception ex) {
            System.err.println("Unable to determine native handle for window");
            return null;
        }
    }
}
