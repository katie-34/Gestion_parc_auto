// ✅ Controller refactorisé pour AccueilRL
package com.example.miageholding.controllers.responsablelogistique;

import com.example.miageholding.core.dao.AssuranceDAO;
import com.example.miageholding.core.dao.MissionDAO;
import com.example.miageholding.core.dao.VehiculeDAO;
import com.example.miageholding.models.Assurance;
import com.example.miageholding.models.Mission;
import com.example.miageholding.models.Vehicule;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class AccueilRLController {

    @FXML private Label labelDisponibles, labelAssures, labelAffectes;
    @FXML private TableView<Assurance> assuranceTable;
    @FXML private TableColumn<Assurance, String> colAgence, colImmatriculationAssurance, colDateFin;
    @FXML private TableView<Vehicule> vehiculeTable;
    @FXML private TableColumn<Vehicule, String> colMarque, colModele, colImmatriculationRecent;
    @FXML private TableView<Mission> vehiculeAffecteTable;
    @FXML private TableColumn<Mission, String> colMissionVehicule, colMissionLibelle, colMissionDebut, colMissionFin;
    @FXML private Text titleExpiringAssurances, titleRecentVehicules;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        chargerKPIs();
        chargerAssurances();
        chargerVehiculesRecents();
        chargerMissionsVehicules();
    }

    private void chargerKPIs() {
        labelDisponibles.setText(VehiculeDAO.countByEtat("Disponible") + " disponibles");
        labelAssures.setText(AssuranceDAO.countValides() + " assurés");
        labelAffectes.setText(VehiculeDAO.countAttribues() + " attribués");
    }

    private void chargerAssurances() {
        ObservableList<Assurance> assurances = FXCollections.observableArrayList(AssuranceDAO.getAssurancesExpirantSous7Jours());
        colAgence.setCellValueFactory(new PropertyValueFactory<>("agence"));
        colImmatriculationAssurance.setCellValueFactory(new PropertyValueFactory<>("immatriculation"));
        colDateFin.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getDateFin() != null ? dateFormatter.format(cell.getValue().getDateFin()) : ""));
        assuranceTable.setItems(assurances);
    }

    private void chargerVehiculesRecents() {
        List<Vehicule> vehicules = VehiculeDAO.getVehiculesRecents(3);
        ObservableList<Vehicule> list = FXCollections.observableArrayList(vehicules);
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colImmatriculationRecent.setCellValueFactory(new PropertyValueFactory<>("immatriculation"));
        vehiculeTable.setItems(list);
    }

    private void chargerMissionsVehicules() {
        ObservableList<Mission> missions = FXCollections.observableArrayList(MissionDAO.getRecentes());
        colMissionVehicule.setCellValueFactory(new PropertyValueFactory<>("immatriculationVehicule"));
        colMissionLibelle.setCellValueFactory(new PropertyValueFactory<>("libMission"));
        colMissionDebut.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getDateDebutMission() != null ? dateFormatter.format(cell.getValue().getDateDebutMission().toLocalDate()) : ""));
        colMissionFin.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getDateFinMission() != null ? dateFormatter.format(cell.getValue().getDateFinMission().toLocalDate()) : ""));
        vehiculeAffecteTable.setItems(missions);
    }
}
