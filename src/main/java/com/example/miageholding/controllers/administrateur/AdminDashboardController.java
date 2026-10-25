package com.example.miageholding.controllers.administrateur;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class AdminDashboardController {

    @FXML
    private AnchorPane contentArea;

    @FXML
    public void initialize() {
        // Charger la page d'accueil après que tous les éléments @FXML aient été injectés
        Platform.runLater(() -> {
            if (contentArea != null) {
                loadPage("/com/example/miageholding/views/admin/AccueilAdmin.fxml");
            } else {
                System.err.println("❌ contentArea est null. Vérifie le fx:id dans le fichier FXML.");
            }
        });
    }

    private void loadPage(String fxmlPath) {
        if (contentArea == null) {
            System.err.println("⚠️ contentArea est null lors du chargement de : " + fxmlPath);
            return;
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);

            // Ancrer automatiquement
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);

            // Animation de fondu
            FadeTransition ft = new FadeTransition(Duration.millis(400), root);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();

        } catch (IOException e) {
            System.err.println("❌ Erreur de chargement FXML : " + fxmlPath);
            e.printStackTrace();
        }
    }

    // ---------------- Navigation ----------------
    @FXML
    private void goToAccueilAdmin() {
        loadPage("/com/example/miageholding/views/admin/AccueilAdmin.fxml");
    }

    @FXML
    private void goToRL() {
        loadPage("/com/example/miageholding/views/admin/ResponsableLogistique.fxml");
    }

    @FXML
    private void goToFonction() {
        loadPage("/com/example/miageholding/views/admin/Fonction.fxml");
    }

    @FXML
    private void goToServices() {
        loadPage("/com/example/miageholding/views/admin/Service.fxml");
    }

    @FXML
    private void goToListe() {
        loadPage("/com/example/miageholding/views/admin/ListeVehicules.fxml");
    }

    // ---------------- Déconnexion ----------------
    @FXML
    private void logout() {
        try {
            Stage stage = (Stage) contentArea.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/miageholding/views/auth/login.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Connexion - MIAGE Holding");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Erreur lors du retour à la page de connexion !");
            e.printStackTrace();
        }
    }
}
