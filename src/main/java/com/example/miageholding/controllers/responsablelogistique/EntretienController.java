package com.example.miageholding.controllers.responsablelogistique;

import com.example.miageholding.core.dao.EntretienDAO;
import com.example.miageholding.models.Entretien;
import com.example.miageholding.utils.ToastService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.format.DateTimeFormatter;

public class EntretienController {

    @FXML private TextField textIdEntretien, textImmatriculation, textMotif, textLieu, textCout, textObservation, textRecherche;
    @FXML private DatePicker dateEntree, dateSortie;
    @FXML private TableView<Entretien> tableEntretien;
    @FXML private TableColumn<Entretien, String> colIdEntretien, colVehicule, colMotif, colDateEntree, colDateSortie, colLieu;

    private final ObservableList<Entretien> entretienList = FXCollections.observableArrayList();
    private FilteredList<Entretien> filteredList;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        initTable();
        loadEntretiens();
        initSearch();
    }

    private void initTable() {
        colIdEntretien.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getIdEntretien())));
        colVehicule.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getImmatriculationVehicule()));
        colMotif.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMotif()));
        colDateEntree.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDateEntree() != null ? cellData.getValue().getDateEntree().format(dateFormatter) : ""));
        colDateSortie.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDateSortie() != null ? cellData.getValue().getDateSortie().format(dateFormatter) : ""));
        colLieu.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLieu()));
        tableEntretien.setItems(entretienList);
    }

    private void loadEntretiens() {
        entretienList.clear();
        entretienList.addAll(EntretienDAO.getAllEntretiens());
        filteredList = new FilteredList<>(entretienList, p -> true);
        SortedList<Entretien> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableEntretien.comparatorProperty());
        tableEntretien.setItems(sortedList);
    }

    @FXML
    private void handleAjouterEntretien(ActionEvent event) {
        if (validateInput()) {
            Entretien entretien = buildEntretienFromForm();
            boolean success = EntretienDAO.insertEntretien(entretien);
            if (success) {
                ToastService.showSuccess("Entretien ajouté avec succès !");
                loadEntretiens();
                resetForm();
            } else {
                ToastService.showError("Erreur lors de l'ajout de l'entretien.");
            }
        }
    }

    @FXML
    private void handleModifierEntretien(ActionEvent event) {
        Entretien selected = tableEntretien.getSelectionModel().getSelectedItem();
        if (selected != null && validateInput()) {
            Entretien entretien = buildEntretienFromForm();
            boolean success = EntretienDAO.updateEntretien(entretien);
            if (success) {
                ToastService.showSuccess("Entretien modifié !");
                loadEntretiens();
                resetForm();
            } else {
                ToastService.showError("Erreur lors de la modification de l'entretien.");
            }
        } else {
            ToastService.showWarning("Veuillez sélectionner un entretien à modifier !");
        }
    }

    @FXML
    private void handleSupprimerEntretien(ActionEvent event) {
        Entretien selected = tableEntretien.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer cet entretien ?", ButtonType.YES, ButtonType.NO);
            confirm.setTitle("Confirmation");
            confirm.showAndWait().ifPresent(type -> {
                if (type == ButtonType.YES) {
                    boolean deleted = EntretienDAO.deleteEntretien(selected.getIdEntretien());
                    if (deleted) {
                        ToastService.showSuccess("Entretien supprimé !");
                        loadEntretiens();
                        resetForm();
                    }
                }
            });
        } else {
            ToastService.showWarning("Veuillez sélectionner un entretien à supprimer !");
        }
    }

    @FXML
    private void handleClearForm(ActionEvent event) {
        resetForm();
    }


    private Entretien buildEntretienFromForm() {
        int idEntretien = Integer.parseInt(textIdEntretien.getText());
        String immatriculation = textImmatriculation.getText().trim();
        int cout = textCout.getText().isEmpty() ? 0 : Integer.parseInt(textCout.getText());

        return new Entretien(
                idEntretien,
                immatriculation,
                dateEntree.getValue().atStartOfDay(),
                dateSortie.getValue().atStartOfDay(),
                textMotif.getText(),
                textObservation.getText(),
                cout,
                textLieu.getText()
        );
    }

    private boolean validateInput() {
        if (textIdEntretien.getText().isEmpty() || textImmatriculation.getText().isEmpty()
                || textMotif.getText().isEmpty() || textLieu.getText().isEmpty()
                || dateEntree.getValue() == null || dateSortie.getValue() == null) {
            ToastService.showWarning("Veuillez remplir tous les champs obligatoires !");
            return false;
        }
        try {
            Integer.parseInt(textIdEntretien.getText());
            if (!textCout.getText().isEmpty()) Integer.parseInt(textCout.getText());
        } catch (NumberFormatException e) {
            ToastService.showError("ID Entretien et Coût doivent être numériques !");
            return false;
        }
        if (dateEntree.getValue().isAfter(dateSortie.getValue())) {
            ToastService.showError("La date d'entrée doit être avant la date de sortie !");
            return false;
        }
        return true;
    }

    private void resetForm() {
        textIdEntretien.clear();
        textImmatriculation.clear();
        textMotif.clear();
        textLieu.clear();
        dateEntree.setValue(null);
        dateSortie.setValue(null);
        textCout.clear();
        textObservation.clear();
        tableEntretien.getSelectionModel().clearSelection();
    }

    private void initSearch() {
        textRecherche.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredList.setPredicate(entretien -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String lower = newVal.toLowerCase();
                return (entretien.getMotif() != null && entretien.getMotif().toLowerCase().contains(lower))
                        || (entretien.getImmatriculationVehicule() != null && entretien.getImmatriculationVehicule().toLowerCase().contains(lower))
                        || (entretien.getLieu() != null && entretien.getLieu().toLowerCase().contains(lower));
            });
        });
    }
}
