package com.example.miageholding.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class SplashScreenController {

    @FXML
    private ProgressIndicator loader;

    @FXML
    private Label loadingText;

    @FXML
    public void initialize() {
        // Pause de 3 secondes avant de passer au Login
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            try {
                Stage stage = (Stage) loader.getScene().getWindow();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/miageholding/views/auth/login.fxml")));
                stage.setScene(new Scene(root));
                stage.setTitle("Connexion - Gestion Parc Auto");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        pause.play();
    }
}
