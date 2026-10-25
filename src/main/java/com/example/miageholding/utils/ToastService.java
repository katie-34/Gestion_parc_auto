package com.example.miageholding.utils;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Service global pour afficher des Toasts dans toute l'application.
 */
public class ToastService {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 50;

    public static void showSuccess(String message) {
        showToast(message, "#4CAF50"); // Vert
    }

    public static void showError(String message) {
        showToast(message, "#f44336"); // Rouge
    }

    public static void showWarning(String message) {
        showToast(message, "#ff9800"); // Orange
    }

    private static void showToast(String message, String backgroundColor) {
        Stage toastStage = new Stage();
        toastStage.initStyle(StageStyle.TRANSPARENT);

        Label label = new Label(message);
        label.setStyle("-fx-background-color: " + backgroundColor + "; -fx-text-fill: white; "
                + "-fx-padding: 10px 20px; -fx-background-radius: 15px; -fx-font-size: 14px;");

        StackPane root = new StackPane(label);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.BOTTOM_CENTER);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(Color.TRANSPARENT);

        toastStage.setScene(scene);
        toastStage.setAlwaysOnTop(true);

        // Centrage sur l'écran principal
        javafx.stage.Screen screen = javafx.stage.Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
        toastStage.setX(bounds.getMinX() + (bounds.getWidth() - WIDTH) / 2);
        toastStage.setY(bounds.getMinY() + bounds.getHeight() - HEIGHT - 50);

        toastStage.show();

        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), root);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setDelay(Duration.seconds(2.5));

        fadeIn.play();
        fadeIn.setOnFinished(e -> fadeOut.play());
        fadeOut.setOnFinished(e -> toastStage.close());
    }
}
