package com.example.miageholding.controllers.administrateur;

import com.example.miageholding.core.dao.FonctionDAO;
import com.example.miageholding.core.dao.PersonnelDAO;
import com.example.miageholding.core.dao.ServicesDAO;
import com.example.miageholding.models.dto.PersonnelAffectationDTO;
import com.example.miageholding.models.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AccueilAdminController {

    @FXML private Label labelServices;
    @FXML private Label labelPersonnels;
    @FXML private Label labelFonctionsParService;

    @FXML private TableView<Service> tableServices;
    @FXML private TableColumn<Service, Integer> colIdService;
    @FXML private TableColumn<Service, String> colLibelleService;
    @FXML private TableColumn<Service, String> colLocalisationService;

    @FXML private TableView<PersonnelAffectationDTO> tablePersonnelsFonctions;
    @FXML private TableColumn<PersonnelAffectationDTO, String> colNomPersonnel;
    @FXML private TableColumn<PersonnelAffectationDTO, String> colPrenomPersonnel;
    @FXML private TableColumn<PersonnelAffectationDTO, String> colFonctionPersonnel;
    @FXML private TableColumn<PersonnelAffectationDTO, String> colServicePersonnel;

    @FXML
    public void initialize() {
        chargerStatistiques();
        initialiserTableServices();
        initialiserTableAffectations();
    }

    private void chargerStatistiques() {
        int nbServices = ServicesDAO.countServices();
        int nbPersonnels = PersonnelDAO.countPersonnels();
        int nbFonctionsParService = FonctionDAO.countFonctionsByService();

        labelServices.setText(String.valueOf(nbServices));
        labelPersonnels.setText(String.valueOf(nbPersonnels));
        labelFonctionsParService.setText(String.valueOf(nbFonctionsParService));
    }

    private void initialiserTableServices() {
        colIdService.setCellValueFactory(new PropertyValueFactory<>("idService"));
        colLibelleService.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        colLocalisationService.setCellValueFactory(new PropertyValueFactory<>("localisation"));

        List<Service> services = ServicesDAO.getAllServices();
        ObservableList<Service> observableServices = FXCollections.observableArrayList(services);
        tableServices.setItems(observableServices);
    }

    private void initialiserTableAffectations() {
        colNomPersonnel.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenomPersonnel.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colFonctionPersonnel.setCellValueFactory(new PropertyValueFactory<>("libelleFonction"));
        colServicePersonnel.setCellValueFactory(new PropertyValueFactory<>("libelleService"));

        List<PersonnelAffectationDTO> affectations = PersonnelDAO.getPersonnelAvecFonctionEtService();
        ObservableList<PersonnelAffectationDTO> observableAffectations = FXCollections.observableArrayList(affectations);
        tablePersonnelsFonctions.setItems(observableAffectations);
    }
}
