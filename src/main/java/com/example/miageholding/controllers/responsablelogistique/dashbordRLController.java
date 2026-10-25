package com.example.miageholding.controllers.responsablelogistique;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class dashbordRLController {

    @FXML
    private AnchorPane contentArea;

    @FXML
    public void initialize() {
        // Charge Accueil par défaut au démarrage
        loadPage("/com/example/miageholding/views/responsablelogistique/AccueilRL.fxml");
    }

    private void loadPage(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));

            // Efface le contenu actuel
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);

            // Ancrage automatique aux bords
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);

            // Ajoute une animation de fondu doux
            FadeTransition ft = new FadeTransition(Duration.millis(500), root);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();

        } catch (IOException e) {
            System.err.println("Erreur de chargement : " + fxmlPath);
            e.printStackTrace();
        }
    }

    // --- Méthodes de navigation ---
    @FXML
    private void goToAccueilRL() {
        loadPage("/com/example/miageholding/views/responsablelogistique/AccueilRL.fxml");
    }

    @FXML
    private void goToVehicule() {
        loadPage("/com/example/miageholding/views/responsablelogistique/Vehicules.fxml");
    }

    @FXML
    private void goToAssurancesVehicules() {
        loadPage("/com/example/miageholding/views/responsablelogistique/AssurancesVehicules.fxml");
    }

    @FXML
    private void goToPersonnel() {
        loadPage("/com/example/miageholding/views/responsablelogistique/AffectationMission.fxml");
    }

    @FXML
    private void goToEntretien() {
        loadPage("/com/example/miageholding/views/responsablelogistique/entretien.fxml");
    }

    @FXML
    private void goToMission() {
        loadPage("/com/example/miageholding/views/responsablelogistique/mission.fxml");
    }

    @FXML
    private void goToAttributionVehicule() {
        loadPage("/com/example/miageholding/views/responsablelogistique/Personnel.fxml");
    }


    // --- Déconnexion ---
    @FXML
    private void logout() {
        try {
            Stage stage = (Stage) contentArea.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/miageholding/views/auth/login.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Connexion - MIAGE Holding");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du retour à la page de connexion !");
        }
    }
}
