package com.example.miageholding.controllers.administrateur;


import com.example.miageholding.core.dao.ServicesDAO;
import com.example.miageholding.models.Service;
import com.example.miageholding.utils.ToastService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

    public class ServiceController {

        @FXML private TextField textIdService, textLibelle, textLocalisation, textRecherche;
        @FXML private TableView<Service> tableServices;
        @FXML private TableColumn<Service, String> colIdService, colLibelle, colLocalisation;

        private final ObservableList<Service> serviceList = FXCollections.observableArrayList();
        private FilteredList<Service> filteredList;

        @FXML
        public void initialize() {
            initTable();
            loadServices();
            initSearch();
        }

        private void initTable() {
            colIdService.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getIdService())));
            colLibelle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLibelle()));
            colLocalisation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocalisation()));

            tableServices.setItems(serviceList);
        }

        private void loadServices() {
            serviceList.clear();
            List<Service> services = ServicesDAO.getAllServices();
            serviceList.addAll(services);

            filteredList = new FilteredList<>(serviceList, p -> true);
            SortedList<Service> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(tableServices.comparatorProperty());
            tableServices.setItems(sortedList);
        }

        @FXML
        private void handleAjouterService(ActionEvent event) {
            if (validateInput()) {
                Service service = buildServiceFromForm();
                boolean success = ServicesDAO.insertService(service);
                if (success) {
                    ToastService.showSuccess("Service ajouté !");
                    loadServices();
                    resetForm();
                } else {
                    ToastService.showError("Erreur lors de l'ajout !");
                }
            }
        }

        @FXML
        private void handleModifierService(ActionEvent event) {
            Service selected = tableServices.getSelectionModel().getSelectedItem();
            if (selected != null && validateInput()) {
                Service service = buildServiceFromForm();
                boolean success = ServicesDAO.updateService(service);
                if (success) {
                    ToastService.showSuccess("Service modifié !");
                    loadServices();
                    resetForm();
                } else {
                    ToastService.showError("Erreur lors de la modification !");
                }
            } else {
                ToastService.showWarning("Veuillez sélectionner un service !");
            }
        }

        @FXML
        private void handleSupprimerService(ActionEvent event) {
            Service selected = tableServices.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer ce service ?", ButtonType.YES, ButtonType.NO);
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        boolean success = ServicesDAO.deleteService(selected.getIdService());
                        if (success) {
                            ToastService.showSuccess("Service supprimé !");
                            loadServices();
                            resetForm();
                        } else {
                            ToastService.showError("Erreur lors de la suppression !");
                        }
                    }
                });
            } else {
                ToastService.showWarning("Aucun service sélectionné !");
            }
        }

        @FXML
        private void handleClearForm(ActionEvent event) {
            resetForm();
        }

        private Service buildServiceFromForm() {
            return new Service(
                    Integer.parseInt(textIdService.getText()),
                    textLibelle.getText(),
                    textLocalisation.getText()
            );
        }

        private boolean validateInput() {
            if (textIdService.getText().isEmpty() || textLibelle.getText().isEmpty() || textLocalisation.getText().isEmpty()) {
                ToastService.showWarning("Tous les champs sont obligatoires !");
                return false;
            }
            try {
                Integer.parseInt(textIdService.getText());
            } catch (NumberFormatException e) {
                ToastService.showError("L'ID doit être un entier !");
                return false;
            }
            return true;
        }

        private void resetForm() {
            textIdService.clear();
            textLibelle.clear();
            textLocalisation.clear();
            tableServices.getSelectionModel().clearSelection();
        }

        private void initSearch() {
            textRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(service -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lower = newValue.toLowerCase();
                    return service.getLibelle().toLowerCase().contains(lower)
                            || service.getLocalisation().toLowerCase().contains(lower);
                });
            });
        }
    }


