package com.example.miageholding.controllers.auth;

import com.example.miageholding.core.bd.connexionBD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private TextField identifiant;

    @FXML
    private PasswordField motdepasse;

    @FXML
    private Label Erreur;

    @FXML
    private void Seconnecter(ActionEvent event) {
        String user = identifiant.getText().trim();
        String pass = motdepasse.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            afficherErreur("Veuillez remplir tous les champs !");
            return;
        }

        String query = "SELECT ROLE FROM utilisateurs WHERE NOM_UTILISATEUR = ? AND MOT_DE_PASSE = ?";

        try (Connection conn = connexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user);
            stmt.setString(2, pass);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("ROLE");

                if ("administrateur".equalsIgnoreCase(role)) {
                    chargerDashboard(event, "/com/example/miageholding/views/admin/AdminDashboard.fxml");
                } else if ("responsable_logistique".equalsIgnoreCase(role)) {
                    chargerDashboard(event, "/com/example/miageholding/views/responsablelogistique/dashboardRL.fxml");
                } else {
                    afficherErreur("Rôle utilisateur inconnu !");
                }
            } else {
                afficherErreur("Identifiant ou mot de passe incorrect !");
            }

        } catch (Exception e) {
            e.printStackTrace();
            afficherErreur("Erreur lors de la connexion à la base de données !");
        }
    }

    private void afficherErreur(String message) {
        Erreur.setTextFill(Color.RED);
        Erreur.setText(message);
        Erreur.setVisible(true);
    }

    private void chargerDashboard(ActionEvent event, String cheminFXML) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(cheminFXML));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Dashboard");
            stage.centerOnScreen(); // Bien centrer la fenêtre
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            afficherErreur("Erreur lors du chargement du dashboard !");
        }
    }
}
