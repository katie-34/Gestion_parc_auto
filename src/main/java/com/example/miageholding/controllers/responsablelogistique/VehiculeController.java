package com.example.miageholding.controllers.responsablelogistique;

import com.example.miageholding.core.dao.VehiculeDAO;
import com.example.miageholding.models.Vehicule;
import com.example.miageholding.utils.ToastService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class VehiculeController {

    @FXML
    private ComboBox<String> comboEtatVoiture;
    @FXML
    private TextField textIdVehicule, textNumeroChassi, textImmatriculation, textMarque, textModele, textNbPlaces, textEnergie, textPuissance, textCouleur, textPrixVehicule, textRecherche;
    @FXML
    private DatePicker dateAcquisition, dateAmortissement, dateMiseEnService, dateEtat;
    @FXML
    private TableView<Vehicule> tableVehicule;
    @FXML
    private TableColumn<Vehicule, Integer> colIdVehicule;
    @FXML
    private TableColumn<Vehicule, String> colMarque, colModele, colImmatriculation, colEtat;

    private final ObservableList<Vehicule> vehiculeList = FXCollections.observableArrayList();
    private FilteredList<Vehicule> filteredList;

    @FXML
    public void initialize() {
        initComboBox();
        initTable();
        loadVehicules();
        initSearchFilter();
        VBox.setVgrow(tableVehicule, Priority.ALWAYS);
    }

    private void initComboBox() {
        comboEtatVoiture.setItems(FXCollections.observableArrayList("Disponible", "En mission", "Hors Service", "En entretien", "Attribué"));
    }

    private void initTable() {
        colIdVehicule.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getIdVehicule()));
        colMarque.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMarque()));
        colModele.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getModele()));
        colImmatriculation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getImmatriculation()));
        colEtat.setCellValueFactory(cellData -> {
            int idEtat = cellData.getValue().getIdEtatVoiture();
            String[] etats = {"Disponible", "En mission", "Hors Service", "En entretien", "Attribué"};
            String libelleEtat = (idEtat >= 1 && idEtat <= etats.length) ? etats[idEtat - 1] : "Inconnu";
            return new SimpleStringProperty(libelleEtat);
        });
        tableVehicule.setItems(vehiculeList);
    }

    private void loadVehicules() {
        vehiculeList.clear();
        List<Vehicule> vehicules = VehiculeDAO.getAllVehicules();
        vehiculeList.addAll(vehicules);
    }

    @FXML
    private void handleAjouterVehicule(ActionEvent event) {
        if (validateInput()) {
            try {
                String immatriculation = textImmatriculation.getText().trim();
                if (VehiculeDAO.vehiculeExists(immatriculation)) {
                    ToastService.showError("Un véhicule avec cette immatriculation existe déjà !");
                    return;
                }

                Vehicule vehicule = buildVehiculeFromForm();
                VehiculeDAO.insertVehicule(vehicule);
                ToastService.showSuccess("Véhicule ajouté avec succès !");
                loadVehicules();
                resetForm();
                highlightLastRow();
            } catch (Exception e) {
                ToastService.showError("Erreur lors de l'ajout : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleModifierVehicule(ActionEvent event) {
        Vehicule selected = tableVehicule.getSelectionModel().getSelectedItem();
        if (selected != null && validateInput()) {
            try {
                String nouvelleImmatriculation = textImmatriculation.getText().trim();
                if (!nouvelleImmatriculation.equalsIgnoreCase(selected.getImmatriculation())) {
                    if (VehiculeDAO.vehiculeExists(nouvelleImmatriculation)) {
                        ToastService.showError("Un véhicule avec cette nouvelle immatriculation existe déjà !");
                        return;
                    }
                }

                Vehicule vehicule = buildVehiculeFromForm();
                VehiculeDAO.updateVehicule(vehicule);
                ToastService.showSuccess("Véhicule modifié avec succès !");
                loadVehicules();
                resetForm();
                highlightLastRow();
            } catch (Exception e) {
                ToastService.showError("Erreur lors de la modification : " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            ToastService.showWarning("Veuillez sélectionner un véhicule à modifier !");
        }
    }

    @FXML
    private void handleSupprimerVehicule(ActionEvent event) {
        Vehicule selected = tableVehicule.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce véhicule ?");
            ButtonType result = confirmationAlert.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                VehiculeDAO.deleteVehicule(selected.getIdVehicule());
                ToastService.showSuccess("Véhicule supprimé avec succès !");
                loadVehicules();
                resetForm();
            }
        } else {
            ToastService.showWarning("Veuillez sélectionner un véhicule à supprimer !");
        }
    }

    @FXML
    private void handleVoirDetails(ActionEvent event) {
        Vehicule selected = tableVehicule.getSelectionModel().getSelectedItem();
        if (selected != null) {
            textIdVehicule.setText(String.valueOf(selected.getIdVehicule()));
            comboEtatVoiture.getSelectionModel().select(selected.getIdEtatVoiture() - 1);
            textNumeroChassi.setText(selected.getNumeroChassi());
            textImmatriculation.setText(selected.getImmatriculation());
            textMarque.setText(selected.getMarque());
            textModele.setText(selected.getModele());
            textNbPlaces.setText(String.valueOf(selected.getNbPlaces()));
            textEnergie.setText(selected.getEnergie());
            dateAcquisition.setValue(selected.getDateAcquisition());
            dateAmortissement.setValue(selected.getDateAmortissement());
            dateMiseEnService.setValue(selected.getDateMiseEnService());
            textPuissance.setText(String.valueOf(selected.getPuissance()));
            textCouleur.setText(selected.getCouleur());
            textPrixVehicule.setText(String.valueOf(selected.getPrixVehicule()));
            dateEtat.setValue(selected.getDateEtat());
        } else {
            ToastService.showWarning("Veuillez sélectionner un véhicule !");
        }
    }

    private boolean validateInput() {
        if (textIdVehicule.getText().isEmpty() || comboEtatVoiture.getSelectionModel().isEmpty()
                || textNumeroChassi.getText().isEmpty() || textImmatriculation.getText().isEmpty()
                || textMarque.getText().isEmpty() || textModele.getText().isEmpty()
                || textNbPlaces.getText().isEmpty() || textEnergie.getText().isEmpty()
                || dateAcquisition.getValue() == null || dateAmortissement.getValue() == null
                || dateMiseEnService.getValue() == null || textPuissance.getText().isEmpty()
                || textCouleur.getText().isEmpty() || textPrixVehicule.getText().isEmpty()
                || dateEtat.getValue() == null) {

            ToastService.showWarning("Veuillez remplir tous les champs !");
            return false;
        }

        try {
            Integer.parseInt(textIdVehicule.getText());
            Integer.parseInt(textNbPlaces.getText());
            Integer.parseInt(textPuissance.getText());
            Integer.parseInt(textPrixVehicule.getText());
        } catch (NumberFormatException e) {
            ToastService.showError("ID, Nb Places, Puissance, Prix doivent être numériques !");
            return false;
        }

        return true;
    }

    private Vehicule buildVehiculeFromForm() {
        return new Vehicule(
                Integer.parseInt(textIdVehicule.getText()),
                comboEtatVoiture.getSelectionModel().getSelectedIndex() + 1,
                textNumeroChassi.getText(),
                textImmatriculation.getText().trim(),
                textMarque.getText(),
                textModele.getText(),
                Integer.parseInt(textNbPlaces.getText()),
                textEnergie.getText(),
                dateAcquisition.getValue(),
                dateAmortissement.getValue(),
                dateMiseEnService.getValue(),
                Integer.parseInt(textPuissance.getText()),
                textCouleur.getText(),
                Integer.parseInt(textPrixVehicule.getText()),
                dateEtat.getValue()
        );
    }

    private void resetForm() {
        textIdVehicule.clear();
        comboEtatVoiture.getSelectionModel().clearSelection();
        textNumeroChassi.clear();
        textImmatriculation.clear();
        textMarque.clear();
        textModele.clear();
        textNbPlaces.clear();
        textEnergie.clear();
        textPuissance.clear();
        textCouleur.clear();
        textPrixVehicule.clear();
        dateAcquisition.setValue(null);
        dateAmortissement.setValue(null);
        dateMiseEnService.setValue(null);
        dateEtat.setValue(null);
    }

    private void initSearchFilter() {
        filteredList = new FilteredList<>(vehiculeList, p -> true);

        textRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(vehicule -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String filter = newValue.toLowerCase();
                return vehicule.getMarque().toLowerCase().contains(filter)
                        || vehicule.getModele().toLowerCase().contains(filter)
                        || vehicule.getImmatriculation().toLowerCase().contains(filter);
            });
        });

        SortedList<Vehicule> sortedData = new SortedList<>(filteredList);
        sortedData.comparatorProperty().bind(tableVehicule.comparatorProperty());
        tableVehicule.setItems(sortedData);
    }

    private void highlightLastRow() {
        if (!tableVehicule.getItems().isEmpty()) {
            int lastIndex = tableVehicule.getItems().size() - 1;
            tableVehicule.getSelectionModel().clearSelection();
            tableVehicule.getSelectionModel().select(lastIndex);
            tableVehicule.scrollTo(lastIndex);
        }
    }
}
