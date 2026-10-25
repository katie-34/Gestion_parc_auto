package com.example.miageholding;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MiageHoldingApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // 1. Charge la page SplashScreen d'abord
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/miageholding/views/SplashScreen.fxml")
        );
        Scene splashScene = new Scene(loader.load(), 600, 400); // SplashScreen plus petit que login

        // 2. Configure la fenêtre principale
        primaryStage.setTitle("MIAGE Holding - Chargement");
        primaryStage.setScene(splashScene);
        primaryStage.setResizable(false); // Splash screen non redimensionnable
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
