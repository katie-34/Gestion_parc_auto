package com.example.miageholding.controllers.responsablelogistique;

import com.example.miageholding.core.dao.MissionDAO;
import com.example.miageholding.core.dao.VehiculeDAO;
import com.example.miageholding.models.Mission;
import com.example.miageholding.models.Vehicule;
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

public class MissionController {

    @FXML
    private TextField textIdMission, textLibMission, textCoutMission, textCoutCarburant, textCircuit, textObservation, textRecherche;

    @FXML
    private DatePicker dateDebutMission, dateFinMission;

    @FXML
    private ComboBox<String> comboVehicule;

    @FXML
    private TableView<Mission> tableMission;

    @FXML
    private TableColumn<Mission, String> colIdMission, colVehicule, colLibelle, colDateDebut, colDateFin, colCircuit;

    private final ObservableList<Mission> missionList = FXCollections.observableArrayList();
    private FilteredList<Mission> filteredList;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        initTable();
        loadMissions();
        initSearch();
        loadVehiculesCombo();
    }

    private void initTable() {
        colIdMission.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getIdMission())));
        colVehicule.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getImmatriculationVehicule()));
        colLibelle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLibMission()));
        colDateDebut.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDateDebutMission() != null ? cellData.getValue().getDateDebutMission().format(dateFormatter) : ""));
        colDateFin.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDateFinMission() != null ? cellData.getValue().getDateFinMission().format(dateFormatter) : ""));
        colCircuit.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCircuitMission()));

        tableMission.setItems(missionList);
    }

    private void loadMissions() {
        missionList.clear();
        List<Mission> missions = MissionDAO.getAllMissions();
        missionList.addAll(missions);

        filteredList = new FilteredList<>(missionList, p -> true);
        SortedList<Mission> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableMission.comparatorProperty());
        tableMission.setItems(sortedList);
    }

    private void loadVehiculesCombo() {
        List<Vehicule> vehicules = VehiculeDAO.getAllVehicules();
        ObservableList<String> immatriculations = FXCollections.observableArrayList();
        for (Vehicule v : vehicules) {
            immatriculations.add(v.getImmatriculation());
        }
        comboVehicule.setItems(immatriculations);
    }

    @FXML
    private void handleAjouterMission(ActionEvent event) {
        if (validateInput()) {
            Mission mission = buildMissionFromForm();

            // ✅ Vérifier si ce véhicule est déjà affecté à une autre mission
            if (MissionDAO.vehiculeAlreadyAssigned(mission.getImmatriculationVehicule())) {
                ToastService.showError("Ce véhicule est déjà affecté à une autre mission !");
                return;
            }

            boolean success = MissionDAO.insertMission(mission);

            if (success) {
                ToastService.showSuccess("Mission ajoutée avec succès !");
                loadMissions();
                resetForm();
            } else {
                ToastService.showError("Erreur lors de l'ajout de la mission.");
            }
        }
    }

    @FXML
    private void handleModifierMission(ActionEvent event) {
        Mission selected = tableMission.getSelectionModel().getSelectedItem();
        if (selected != null && validateInput()) {
            Mission mission = buildMissionFromForm();

            // ✅ Si on change l'immatriculation, on doit vérifier s'il n'est pas déjà affecté ailleurs
            if (!mission.getImmatriculationVehicule().equals(selected.getImmatriculationVehicule())) {
                if (MissionDAO.vehiculeAlreadyAssigned(mission.getImmatriculationVehicule())) {
                    ToastService.showError("Ce véhicule est déjà affecté à une autre mission !");
                    return;
                }
            }

            boolean success = MissionDAO.updateMission(mission);

            if (success) {
                ToastService.showSuccess("Mission modifiée avec succès !");
                loadMissions();
                resetForm();
            } else {
                ToastService.showError("Erreur lors de la modification de la mission.");
            }
        } else {
            ToastService.showWarning("Veuillez sélectionner une mission à modifier !");
        }
    }

    @FXML
    private void handleSupprimerMission(ActionEvent event) {
        Mission selected = tableMission.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cette mission ?");
            ButtonType result = confirmation.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                boolean success = MissionDAO.deleteMission(selected.getIdMission());

                if (success) {
                    ToastService.showSuccess("Mission supprimée !");
                    loadMissions();
                    resetForm();
                } else {
                    ToastService.showError("Erreur lors de la suppression de la mission.");
                }
            }
        } else {
            ToastService.showWarning("Veuillez sélectionner une mission !");
        }
    }

    @FXML
    private void handleClearForm(ActionEvent event) {
        resetForm();
    }

    private boolean validateInput() {
        if (textIdMission.getText().isEmpty() || comboVehicule.getSelectionModel().isEmpty()
                || textLibMission.getText().isEmpty() || dateDebutMission.getValue() == null
                || dateFinMission.getValue() == null || textCircuit.getText().isEmpty()) {

            ToastService.showWarning("Veuillez remplir tous les champs obligatoires !");
            return false;
        }

        try {
            Integer.parseInt(textIdMission.getText());
            if (!textCoutMission.getText().isEmpty()) Integer.parseInt(textCoutMission.getText());
            if (!textCoutCarburant.getText().isEmpty()) Integer.parseInt(textCoutCarburant.getText());
        } catch (NumberFormatException e) {
            ToastService.showError("ID Mission, Coûts doivent être des nombres !");
            return false;
        }

        if (dateDebutMission.getValue().atStartOfDay().isAfter(dateFinMission.getValue().atStartOfDay())) {
            ToastService.showError("La date de début doit être avant la date de fin !");
            return false;
        }

        return true;
    }

    private void resetForm() {
        textIdMission.clear();
        comboVehicule.getSelectionModel().clearSelection();
        textLibMission.clear();
        dateDebutMission.setValue(null);
        dateFinMission.setValue(null);
        textCoutMission.clear();
        textCoutCarburant.clear();
        textCircuit.clear();
        textObservation.clear();
        tableMission.getSelectionModel().clearSelection();
    }

    private void initSearch() {
        textRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(mission -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return (mission.getLibMission() != null && mission.getLibMission().toLowerCase().contains(lowerCaseFilter))
                        || (mission.getCircuitMission() != null && mission.getCircuitMission().toLowerCase().contains(lowerCaseFilter))
                        || (mission.getImmatriculationVehicule() != null && mission.getImmatriculationVehicule().toLowerCase().contains(lowerCaseFilter));
            });
        });
    }

    private Mission buildMissionFromForm() {
        return new Mission(
                Integer.parseInt(textIdMission.getText()),
                comboVehicule.getSelectionModel().getSelectedItem(),
                textLibMission.getText(),
                dateDebutMission.getValue().atStartOfDay(),
                dateFinMission.getValue().atStartOfDay(),
                textCoutMission.getText().isEmpty() ? 0 : Integer.parseInt(textCoutMission.getText()),
                textCoutCarburant.getText().isEmpty() ? 0 : Integer.parseInt(textCoutCarburant.getText()),
                textObservation.getText(),
                textCircuit.getText()
        );
    }
}
