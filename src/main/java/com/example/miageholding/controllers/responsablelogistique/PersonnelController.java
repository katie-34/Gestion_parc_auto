package com.example.miageholding.controllers.responsablelogistique;

import com.example.miageholding.core.dao.FonctionDAO;
import com.example.miageholding.core.dao.ServicesDAO;
import com.example.miageholding.core.dao.PersonnelDAO;
import com.example.miageholding.core.dao.VehiculeDAO;
import com.example.miageholding.models.Personnel;
import com.example.miageholding.utils.ToastService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PersonnelController {

    @FXML private TextField textId, textNom, textPrenom, textContact, textRecherche;
    @FXML private DatePicker dateAttribution;
    @FXML private ComboBox<String> comboVehicule, comboService, comboFonction;
    @FXML private TableView<Personnel> tablePersonnel;
    @FXML private TableColumn<Personnel, String> colId, colNom, colPrenom, colContact, colService, colFonction, colVehicule, colDate;

    private final ObservableList<Personnel> personnelList = FXCollections.observableArrayList();
    private FilteredList<Personnel> filteredList;

    @FXML
    public void initialize() {
        chargerCombos();
        chargerTable();
        initSearch();
    }

    private void chargerCombos() {
        comboVehicule.setItems(FXCollections.observableArrayList(VehiculeDAO.getAllImmatriculations()));
        comboService.setItems(FXCollections.observableArrayList(ServicesDAO.getAllLibelles()));
        comboFonction.setItems(FXCollections.observableArrayList(FonctionDAO.getAllLibelles()));
        comboVehicule.setItems(FXCollections.observableArrayList(VehiculeDAO.getImmatriculationsDisponibles()));

    }

    private void chargerTable() {
        personnelList.setAll(PersonnelDAO.getAllPersonnel());

        colId.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getIdPersonnel())));
        colNom.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNom()));
        colPrenom.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPrenom()));
        colContact.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getContact()));
        colVehicule.setCellValueFactory(cell -> new SimpleStringProperty(
                VehiculeDAO.getImmatriculationById(cell.getValue().getIdVehicule())));
        colService.setCellValueFactory(cell -> new SimpleStringProperty(
                ServicesDAO.getLibelleById(cell.getValue().getIdService())));
        colFonction.setCellValueFactory(cell -> new SimpleStringProperty(
                FonctionDAO.getLibelleById(cell.getValue().getIdFonction())));
        colDate.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getDateAttribution() != null ?
                        cell.getValue().getDateAttribution().toString() : ""));

        SortedList<Personnel> sorted = new SortedList<>(personnelList);
        sorted.comparatorProperty().bind(tablePersonnel.comparatorProperty());
        tablePersonnel.setItems(sorted);
    }

    private void initSearch() {
        filteredList = new FilteredList<>(personnelList, p -> true);
        textRecherche.textProperty().addListener((obs, oldVal, newVal) -> {
            String lower = newVal.toLowerCase();
            filteredList.setPredicate(p ->
                    p.getNom().toLowerCase().contains(lower) ||
                            p.getPrenom().toLowerCase().contains(lower) ||
                            p.getContact().toLowerCase().contains(lower));
        });
        SortedList<Personnel> sorted = new SortedList<>(filteredList);
        sorted.comparatorProperty().bind(tablePersonnel.comparatorProperty());
        tablePersonnel.setItems(sorted);
    }

    @FXML
    private void handleAjouter(ActionEvent event) {
        if (validateForm()) {
            Personnel p = buildFromForm();
            int idVehicule = p.getIdVehicule();

            if (PersonnelDAO.isVehiculeAttribue(idVehicule, null)) {
                ToastService.showWarning("Ce véhicule est déjà attribué à un autre personnel !");
                return;
            }

            if (PersonnelDAO.insertPersonnel(p)) {
                ToastService.showSuccess("Personnel ajouté !");
                chargerTable();
                clearForm();
            } else {
                ToastService.showError("Échec de l'ajout.");
            }
        }
    }


    @FXML
    private void handleModifier(ActionEvent event) {
        Personnel selected = tablePersonnel.getSelectionModel().getSelectedItem();
        if (selected != null && validateForm()) {
            Personnel p = buildFromForm();
            int idVehicule = p.getIdVehicule();

            // Ne pas bloquer si le véhicule est toujours pour ce personnel
            if (PersonnelDAO.isVehiculeAttribue(idVehicule, selected.getIdPersonnel())) {
                ToastService.showWarning("Ce véhicule est déjà utilisé par un autre personnel !");
                return;
            }

            if (PersonnelDAO.updatePersonnel(p)) {
                ToastService.showSuccess("Modifié avec succès.");
                chargerTable();
                clearForm();
            } else {
                ToastService.showError("Erreur de modification.");
            }
        }
    }


    @FXML
    private void handleSupprimer(ActionEvent event) {
        Personnel selected = tablePersonnel.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (PersonnelDAO.deletePersonnel(selected.getIdPersonnel())) {
                ToastService.showSuccess("Supprimé !");
                chargerTable();
                clearForm();
            } else {
                ToastService.showError("Erreur lors de la suppression.");
            }
        }
    }

    @FXML
    private void handleClear(ActionEvent event) {
        clearForm();
    }

    private boolean validateForm() {
        return !(textId.getText().isEmpty() || textNom.getText().isEmpty() || textPrenom.getText().isEmpty()
                || textContact.getText().isEmpty() || comboVehicule.getValue() == null
                || comboService.getValue() == null || comboFonction.getValue() == null
                || dateAttribution.getValue() == null);
    }

    private Personnel buildFromForm() {
        return new Personnel(
                Integer.parseInt(textId.getText()),
                VehiculeDAO.getIdByImmatriculation(comboVehicule.getValue()),
                FonctionDAO.getIdByLibelle(comboFonction.getValue()),
                ServicesDAO.getIdByLibelle(comboService.getValue()),
                textNom.getText(),
                textPrenom.getText(),
                textContact.getText(),
                dateAttribution.getValue().atStartOfDay()
        );
    }

    private void clearForm() {
        textId.clear();
        textNom.clear();
        textPrenom.clear();
        textContact.clear();
        comboVehicule.getSelectionModel().clearSelection();
        comboService.getSelectionModel().clearSelection();
        comboFonction.getSelectionModel().clearSelection();
        dateAttribution.setValue(null);
        tablePersonnel.getSelectionModel().clearSelection();
    }
}
