package com.example.miageholding.controllers.administrateur;

import com.example.miageholding.core.dao.ResponsableDAO;
import com.example.miageholding.models.ResponsableLogistique;
import com.example.miageholding.utils.ToastService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ResponsableLogistiqueController {

    @FXML private TextField textNomUtilisateur;
    @FXML private PasswordField passwordField;
    @FXML private TextField visiblePasswordField;
    @FXML private CheckBox toggleVisibility;

    @FXML private Button btnAjouter, btnModifier, btnSupprimer, btnClear;

    @FXML
    public void initialize() {
        chargerResponsable();
    }

    private void chargerResponsable() {
        ResponsableLogistique rl = ResponsableDAO.getResponsable();
        if (rl != null) {
            textNomUtilisateur.setText(rl.getNomUtilisateur());
            passwordField.setText(rl.getMotDePasse());
            visiblePasswordField.setText(rl.getMotDePasse());
        }
    }

    @FXML
    private void handleAjouter(ActionEvent event) {
        if (ResponsableDAO.getResponsable() != null) {
            ToastService.showWarning("Un responsable logistique existe déjà !");
            return;
        }

        if (validerChamps()) {
            boolean success = ResponsableDAO.insertResponsable(
                    textNomUtilisateur.getText().trim(),
                    getMotDePasse()
            );

            if (success) {
                ToastService.showSuccess("Responsable ajouté !");
                chargerResponsable();
                resetForm();
            } else {
                ToastService.showError("Erreur lors de l'ajout !");
            }
        }
    }

    @FXML
    private void handleModifier(ActionEvent event) {
        if (ResponsableDAO.getResponsable() == null) {
            ToastService.showWarning("Aucun responsable logistique trouvé !");
            return;
        }

        if (validerChamps()) {
            boolean success = ResponsableDAO.updateResponsable(
                    textNomUtilisateur.getText().trim(),
                    getMotDePasse()
            );

            if (success) {
                ToastService.showSuccess("Responsable modifié !");
                chargerResponsable();
            } else {
                ToastService.showError("Erreur lors de la modification !");
            }
        }
    }

    @FXML
    private void handleSupprimer(ActionEvent event) {
        if (ResponsableDAO.getResponsable() == null) {
            ToastService.showWarning("Aucun responsable à supprimer !");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Confirmer la suppression ?", ButtonType.YES, ButtonType.NO);
        confirmation.setTitle("Suppression");
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                boolean deleted = ResponsableDAO.deleteResponsable();
                if (deleted) {
                    ToastService.showSuccess("Responsable supprimé !");
                    resetForm();
                } else {
                    ToastService.showError("Erreur lors de la suppression !");
                }
            }
        });
    }

    @FXML
    private void handleClearForm(ActionEvent event) {
        resetForm();
    }

    private void resetForm() {
        textNomUtilisateur.clear();
        passwordField.clear();
        visiblePasswordField.clear();
        toggleVisibility.setSelected(false);
        togglePasswordVisibility();  // Assure la réinitialisation d’affichage
    }

    private boolean validerChamps() {
        if (textNomUtilisateur.getText().isEmpty() || getMotDePasse().isEmpty()) {
            ToastService.showWarning("Tous les champs sont obligatoires !");
            return false;
        }
        return true;
    }

    private String getMotDePasse() {
        return toggleVisibility.isSelected() ? visiblePasswordField.getText() : passwordField.getText();
    }

    @FXML
    private void togglePasswordVisibility() {
        boolean isVisible = toggleVisibility.isSelected();

        if (isVisible) {
            visiblePasswordField.setText(passwordField.getText());
            visiblePasswordField.setVisible(true);
            visiblePasswordField.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
        } else {
            passwordField.setText(visiblePasswordField.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            visiblePasswordField.setVisible(false);
            visiblePasswordField.setManaged(false);
        }
    }
}
