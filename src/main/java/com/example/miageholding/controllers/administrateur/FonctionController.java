package com.example.miageholding.controllers.administrateur;

import com.example.miageholding.core.dao.FonctionDAO;
import com.example.miageholding.models.Fonction;
import com.example.miageholding.utils.ToastService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class FonctionController {

    @FXML private TextField textIdFonction, textLibelle, textRecherche;
    @FXML private TableView<Fonction> tableFonction;
    @FXML private TableColumn<Fonction, String> colIdFonction, colLibelle;

    private final ObservableList<Fonction> fonctionList = FXCollections.observableArrayList();
    private FilteredList<Fonction> filteredList;

    @FXML
    public void initialize() {
        initTable();
        loadFonctions();
        initSearch();
    }

    private void initTable() {
        colIdFonction.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getIdFonction())));
        colLibelle.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLibFonction()));
        tableFonction.setItems(fonctionList);
    }

    private void loadFonctions() {
        fonctionList.clear();
        List<Fonction> fonctions = FonctionDAO.getAllFonctions();
        fonctionList.addAll(fonctions);

        filteredList = new FilteredList<>(fonctionList, p -> true);
        SortedList<Fonction> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableFonction.comparatorProperty());
        tableFonction.setItems(sortedList);
    }

    @FXML
    private void handleAjouter(ActionEvent event) {
        if (validateInput()) {
            Fonction f = buildFromForm();
            if (FonctionDAO.insertFonction(f)) {
                ToastService.showSuccess("Fonction ajoutée avec succès");
                loadFonctions();
                resetForm();
            } else {
                ToastService.showError("Erreur lors de l'insertion");
            }
        }
    }

    @FXML
    private void handleModifier(ActionEvent event) {
        Fonction selected = tableFonction.getSelectionModel().getSelectedItem();
        if (selected != null && validateInput()) {
            Fonction f = buildFromForm();
            if (FonctionDAO.updateFonction(f)) {
                ToastService.showSuccess("Fonction modifiée avec succès");
                loadFonctions();
                resetForm();
            } else {
                ToastService.showError("Erreur lors de la modification");
            }
        } else {
            ToastService.showWarning("Sélectionnez une fonction à modifier");
        }
    }

    @FXML
    private void handleSupprimer(ActionEvent event) {
        Fonction selected = tableFonction.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer cette fonction ?", ButtonType.YES, ButtonType.NO);
            confirm.setTitle("Confirmation");
            confirm.showAndWait().ifPresent(type -> {
                if (type == ButtonType.YES) {
                    if (FonctionDAO.deleteFonction(selected.getIdFonction())) {
                        ToastService.showSuccess("Fonction supprimée avec succès");
                        loadFonctions();
                        resetForm();
                    }
                }
            });
        } else {
            ToastService.showWarning("Sélectionnez une fonction à supprimer");
        }
    }

    @FXML
    private void handleClearForm(ActionEvent event) {
        resetForm();
    }

    private Fonction buildFromForm() {
        return new Fonction(
                Integer.parseInt(textIdFonction.getText()),
                textLibelle.getText().trim()
        );
    }

    private boolean validateInput() {
        if (textIdFonction.getText().isEmpty() || textLibelle.getText().isEmpty()) {
            ToastService.showWarning("Tous les champs sont requis");
            return false;
        }
        try {
            Integer.parseInt(textIdFonction.getText());
        } catch (NumberFormatException e) {
            ToastService.showError("L'ID doit être un nombre entier");
            return false;
        }
        return true;
    }

    private void resetForm() {
        textIdFonction.clear();
        textLibelle.clear();
        tableFonction.getSelectionModel().clearSelection();
    }

    private void initSearch() {
        textRecherche.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredList.setPredicate(fonction -> {
                String lower = newVal.toLowerCase();
                return fonction.getLibFonction().toLowerCase().contains(lower)
                        || String.valueOf(fonction.getIdFonction()).contains(lower);
            });
        });
    }
}

