package com.example.miageholding.controllers.responsablelogistique;

import com.example.miageholding.core.dao.AssuranceDAO;
import com.example.miageholding.core.dao.VehiculeDAO;
import com.example.miageholding.models.Assurance;
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
import java.util.List;

public class AssuranceController {

    @FXML
    private TextField textNumCarte, textAgence, textCout, textImmatriculation, textRecherche;

    @FXML
    private DatePicker dateDebut, dateFin;

    @FXML
    private TableView<Assurance> tableAssurance;

    @FXML
    private TableColumn<Assurance, String> colNumCarte, colDateDebut, colDateFin, colAgence, colCout, colImmatriculation;

    @FXML
    private Button btnAjouter, btnModifier, btnSupprimer, btnClear;

    private final ObservableList<Assurance> assuranceList = FXCollections.observableArrayList();
    private FilteredList<Assurance> filteredList;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        initTable();
        loadAssurances();
        initSearch();
    }

    private void initTable() {
        colNumCarte.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumCarteAssurance())));
        colDateDebut.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDateDebut() != null ? cellData.getValue().getDateDebut().format(dateFormatter) : ""));
        colDateFin.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDateFin() != null ? cellData.getValue().getDateFin().format(dateFormatter) : ""));
        colAgence.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAgence()));
        colCout.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCout())));
        colImmatriculation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getImmatriculation()));
    }

    private void loadAssurances() {
        assuranceList.clear();
        List<Assurance> assurances = AssuranceDAO.getAllAssurances();
        assuranceList.addAll(assurances);

        filteredList = new FilteredList<>(assuranceList, p -> true);
        SortedList<Assurance> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableAssurance.comparatorProperty());
        tableAssurance.setItems(sortedList);
    }

    @FXML
    private void handleAjouterAssurance(ActionEvent event) {
        if (validateInput()) {
            int numCarte = Integer.parseInt(textNumCarte.getText());
            String immatriculation = textImmatriculation.getText().trim();

            if (AssuranceDAO.assuranceExists(numCarte)) {
                ToastService.showError("Une assurance avec ce numéro existe déjà !");
                return;
            }

            if (!VehiculeDAO.vehiculeExists(immatriculation)) {
                ToastService.showError("Aucun véhicule enregistré avec cette immatriculation !");
                return;
            }

            if (AssuranceDAO.vehiculeAlreadyInsured(immatriculation)) {
                ToastService.showWarning("Ce véhicule est déjà assuré !");
                return;
            }

            Assurance assurance = buildAssuranceFromForm();
            boolean success = AssuranceDAO.insertAssurance(assurance);

            if (success) {
                ToastService.showSuccess("Assurance ajoutée avec succès !");
                loadAssurances();
                resetForm();
                highlightLastRow();
            } else {
                ToastService.showError("Erreur lors de l'insertion de l'assurance.");
            }
        }
    }

    @FXML
    private void handleModifierAssurance(ActionEvent event) {
        Assurance selected = tableAssurance.getSelectionModel().getSelectedItem();
        if (selected != null && validateInput()) {
            Assurance assurance = buildAssuranceFromForm();

            if (!assurance.getImmatriculation().equalsIgnoreCase(selected.getImmatriculation())) {
                if (AssuranceDAO.vehiculeAlreadyInsured(assurance.getImmatriculation())) {
                    ToastService.showWarning("Ce véhicule est déjà couvert par une autre assurance !");
                    return;
                }
            }

            AssuranceDAO.updateAssurance(assurance);
            ToastService.showSuccess("Assurance modifiée avec succès !");
            loadAssurances();
            resetForm();
        } else {
            ToastService.showWarning("Veuillez sélectionner une assurance à modifier !");
        }
    }

    @FXML
    private void handleSupprimerAssurance(ActionEvent event) {
        Assurance selected = tableAssurance.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette assurance ?");
            ButtonType result = confirmationAlert.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                AssuranceDAO.deleteAssurance(selected.getNumCarteAssurance());
                ToastService.showSuccess("Assurance supprimée avec succès !");
                loadAssurances();
                resetForm();
            }
        } else {
            ToastService.showWarning("Veuillez sélectionner une assurance à supprimer !");
        }
    }

    @FXML
    private void handleClearForm(ActionEvent event) {
        resetForm();
    }

    private boolean validateInput() {
        if (textNumCarte.getText().isEmpty() || textAgence.getText().isEmpty() || textCout.getText().isEmpty()
                || textImmatriculation.getText().isEmpty() || dateDebut.getValue() == null || dateFin.getValue() == null) {
            ToastService.showWarning("Veuillez remplir tous les champs !");
            return false;
        }

        try {
            Integer.parseInt(textNumCarte.getText());
            Integer.parseInt(textCout.getText());
        } catch (NumberFormatException e) {
            ToastService.showError("Numéro de carte et coût doivent être des nombres !");
            return false;
        }

        if (dateDebut.getValue().isAfter(dateFin.getValue())) {
            ToastService.showError("La date de début doit être avant la date de fin !");
            return false;
        }

        return true;
    }

    private void resetForm() {
        textNumCarte.clear();
        textAgence.clear();
        textCout.clear();
        textImmatriculation.clear();
        dateDebut.setValue(null);
        dateFin.setValue(null);
        tableAssurance.getSelectionModel().clearSelection();
    }

    private void initSearch() {
        textRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(assurance -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return (assurance.getAgence() != null && assurance.getAgence().toLowerCase().contains(lowerCaseFilter))
                        || String.valueOf(assurance.getNumCarteAssurance()).contains(lowerCaseFilter)
                        || (assurance.getImmatriculation() != null && assurance.getImmatriculation().toLowerCase().contains(lowerCaseFilter));
            });
        });
    }

    private void highlightLastRow() {
        if (!tableAssurance.getItems().isEmpty()) {
            int lastIndex = tableAssurance.getItems().size() - 1;
            tableAssurance.getSelectionModel().clearSelection();
            tableAssurance.getSelectionModel().select(lastIndex);
            tableAssurance.scrollTo(lastIndex);
        }
    }

    private Assurance buildAssuranceFromForm() {
        return new Assurance(
                Integer.parseInt(textNumCarte.getText()),
                dateDebut.getValue(),
                dateFin.getValue(),
                textAgence.getText(),
                Integer.parseInt(textCout.getText()),
                textImmatriculation.getText()
        );
    }
}
